import axios from "axios"

const host = process.env.REACT_APP_HOST

export const loginPost = async (loginParam) => {   // 사용자가 로그인을 시도할 때 호출
    // loginParam은 로그인 정보를 담고 있는 객체로, 'email'과 'password'를 포함

    const loginData = {
        email: loginParam.email,
        password: loginParam.password
    }

    const res = await axios.post(`${host}/api/users/login`, loginData, { withCredentials: true }) //  'axios.post'를 사용하여 서버에 로그인 요청 전송, 첫 번째 인자는 요청 URL, 두 번째 인자는 요청 본문, 세 번째 인자는 요청 config

    return res.data // 서버로부터 받은 응답의 데이터를 반환
}
