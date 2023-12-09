// 컴포넌트가 함께 사용하는 메모리 공간 (애플리케이션 내의 공유되는 상태 데이터)
import { configureStore } from '@reduxjs/toolkit'   // Store 객체를 생성하기 위한 함수
import loginSlice from './component/slices/loginSlice'

export default configureStore({
    reducer: {
        "loginSlice": loginSlice
    }
})