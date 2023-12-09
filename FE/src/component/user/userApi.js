import axios from "axios"
import jwtAxios from "../util/jwtUtil"

export const API_SERVER_HOST = 'http://localhost:8080'  // 로컬 서버의 주소 정의

const host = `${API_SERVER_HOST}/api/users` // API 요청의 기본 URL설정

export const loginPost = async (loginParam) => {   // 사용자가 로그인을 시도할 때 호출
    // loginParam은 로그인 정보를 담고 있는 객체로, 'email'과 'password'를 포함

    const header = {headers: {"Content-Type": "application/json"}} // 요청 헤더 설정, Content-Type은 요청 본문의 미디어 타입을 지정하는데 사용

    const form = new FormData() // FormData 객체생성
    form.append('email', loginParam.email)
    form.append('password', loginParam.password)

    const res = await axios.post(`${host}/login`, form, header) //  'axios.post'를 사용하여 서버에 로그인 요청 전송, 첫 번째 인자는 요청 URL, 두 번째 인자는 요청 본문, 세 번째 인자는 요청 헤더

    return res.data // 서버로부터 받은 응답 데이터를 반환
}
