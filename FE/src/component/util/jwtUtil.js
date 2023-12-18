import axios from "axios";
import { getCookie, setCookie } from "./cookieUtil";

const jwtAxios = axios.create() // axios 인스턴스를 생성

// accessToken과 refreshToken을 이용하여 새로운 JWT를 받아오는 함수, JWT가 만료되었을 때 새로운 토큰을 받아옵니다.
const refreshJWT = async () => {
    const host = API_SERVER_HOST

    const res = await axios.get(`${host}/api/user/refresh`, { withCredentials: true })

    return res.data
}

//before request 모든 요청 전에 실행되는 인터셉터
const beforeReq = (config) => {
    console.log("before request.............")

    const userInfo = getCookie("user")

    if (!userInfo) {
        console.log("User NOT FOUND")
        return Promise.reject(
            {
                response:
                {
                    data:
                        { error: "REQUIRE_LOGIN" }
                }
            }
        )
    }

    config.withCredentials = true

    return config
}

//fail request 요청이 실패했을 때 실행되는 인터셉터
const requestFail = (err) => {
    console.log("request error............")

    return Promise.reject(err)
}

//before return response 모든 응답 전에 실행되는 인터셉터
const beforeRes = async (res) => {
    console.log("before return response...........")

    return res
}


//fail response 응답이 실패했을 때 실행되는 인터셉터
const responseFail = async (err) => {
    console.log("response fail error.............")
    if (err.response.status === 401) {
        const userCookieValue = getCookie("user")
        const result = await refreshJWT(userCookieValue.accessToken, userCookieValue.refreshToken)
        console.log("refreshJWT RESULT", result)

        userCookieValue.accessToken = result.accessToken
        userCookieValue.refreshToken = result.refreshToken

        setCookie("user", JSON.stringify(userCookieValue), 7)

        //원래의 호출
        const originalRequest = err.config

        originalRequest.config.withCredentials = true

        return await axios(originalRequest)
    }
    return Promise.reject(err);
}

jwtAxios.interceptors.request.use(beforeReq, requestFail) // 요청 인터셉터를 설정

jwtAxios.interceptors.response.use(beforeRes, responseFail) // 응답 인터셉터를 설정

export default jwtAxios