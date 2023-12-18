import axios from "axios"

const host = `/api/users` // API 요청의 기본 URL설정
// 아래 코드는 로컬 테스트에 필요합니다. 
// 테스트 시에 아래 코드를 주석 해제 하시고 위 코드를 주석처리하여 테스트 진행하시면 됩니다.
// 커밋하실 때는 코드 원상복귀 해주셔야합니다 !!!
// const host = `http://localhost:8080/api/users`

export const loginPost = async (loginParam) => {   // 사용자가 로그인을 시도할 때 호출
    // loginParam은 로그인 정보를 담고 있는 객체로, 'email'과 'password'를 포함

    const loginData = {
        email: loginParam.email,
        password: loginParam.password
    }

    const res = await axios.post(`${host}/login`, loginData, { withCredentials: true }) //  'axios.post'를 사용하여 서버에 로그인 요청 전송, 첫 번째 인자는 요청 URL, 두 번째 인자는 요청 본문, 세 번째 인자는 요청 config

    return res.data // 서버로부터 받은 응답의 데이터를 반환
}
