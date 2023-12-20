import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { loginPost } from "../user/signup&login/userApi";
import { getCookie, setCookie, removeCookie } from "../util/cookieUtil";

const initState = {
    email: '' // email 값이 있는 경우 로그인 상태로 간주, 없으면 로그인되지 않은 상태
}

// 비동기 통신 호출
export const loginPostAsync = createAsyncThunk('loginPostAsync', (param) => {
    return loginPost(param)

})

const loadUserCookie = () => {  //쿠키에서 로그인 정보 로딩
    const userInfo = getCookie("user")

    return userInfo
}

// 리듀서 : 스토어에 있는 애플리케이션의 상태 가공
const loginSlice = createSlice({    // 로그인 상태
    name: 'LoginSlice',
    initialState: loadUserCookie() || initState, //쿠키가 없다면 초깃값사용
    reducers: { // 액션의 페이로드값을 처리해서 보관해야 할 애플리케이션 상태 데이터를 반환
        login: (state, action) => {
            console.log("login.....")

            //{email, pw로 구성}
            const data = action.payload

            //새로운 상태
            return { email: data.email }

        },
        logout: (state, action) => {
            console.log("logout....")

            removeCookie("user")
            return { ...initState }
        }
    },
    extraReducers: (builder) => {   // 로그인 시에 전송되는 데이터들을 상태 데이터로 보관

        builder.addCase(loginPostAsync.fulfilled, (state, action) => {
            console.log("fulfilled")

            const payload = action.payload

            //정상적인 로그인시에만 저장
            if (!payload.error) {
                setCookie("user", JSON.stringify(payload), 7) //7일
            }

            return payload
        })

            .addCase(loginPostAsync.pending, (state, action) => {
                console.log("pending")
            })
            .addCase(loginPostAsync.rejected, (state, action) => {
                console.log("rejected")
            })
    }
})

export const { login, logout } = loginSlice.actions
export default loginSlice.reducer