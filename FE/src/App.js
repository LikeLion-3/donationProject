import logo from './logo.svg';
import './App.css';
import { Routes, Route } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import { useSelector } from 'react-redux';
import Preloader from './component/preloader/Preloader';
import Nav from './component/nav/Nav';
import Home from './component/main/home/Home';
import Footer from './component/footer/Footer';
import ArticleList from './component/article/list/ArticleList';
import ArticleDetails from './component/article/detail/ArticleDetails';
import User from "./component/user/user";
import Login from './component/user/login';
import PostButton from './component/article/post/PostButton';
import PostForm from './component/article/post/PostForm'; // PostForm을 import 합니다.
import SignUp from './component/user/signup';
import MyPage from './component/user/mypage/mypage';
import CommunityList from './component/community/list/CommunityList';
import CommunityDetails from './component/community/detail/CommunityDetails';

function App() {

    const location = useLocation();
    const loginState = useSelector(state => state.loginSlice);

    return (
        <div>
            <Preloader />

            <Routes>
                <Route path="/" element={<Nav />}>
                    <Route index element={<Home />} />
                    <Route path="/donate" element={<ArticleList />} />
                    <Route path="/donate/:articleId" element={<ArticleDetails />} />
                    <Route path="/write" element={<PostForm />} /> {/* PostForm을 /write 경로에 연결합니다. */}
                    <Route path="/user" element={<User />} />
                    <Route path='/user/me' element={<MyPage />} />
                    <Route path="/community" element={<CommunityList />} />
                    <Route path="/community/:communityId" element={<CommunityDetails />} />
                </Route>
                <Route path="/signup" element={<SignUp />} />
                <Route path="/user/login" element={<Login />} />
            </Routes>
            {location.pathname !== '/signup' && location.pathname !== '/user/login' && <Footer />}
            {loginState.id && <PostButton />}
        </div>
    );
}

export default App;
