import { Suspense, lazy } from "react"; // 필요한 순간까지 컴포넌트를 메모리상으로 올리지 않도록 지연로딩을 위해서 사용
const Loading = <div>Loading....</div>  // 컴포넌트의 처리가 끝나지 않은 경우 화면에 'Loading....'메시지
const Login = lazy(() => import("./login"))
// const KakaoRedirect = lazy(() => import("./KakaoRedirect"))

const user = () => {
    return [
        {
            path: "login",
            element: <Suspense fallback={Loading}><Login/></Suspense>
        },
        // {
        //     path: "kakao",
        //     element: <Suspense fallback={Loading}><KakaoRedirect/></Suspense>
        // }
    ]

}

export default user;