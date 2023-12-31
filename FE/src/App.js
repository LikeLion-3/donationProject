import logo from "./logo.svg";
import "./App.css";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { Routes, Route } from "react-router-dom";
import { useLocation } from "react-router-dom";
import { useSelector } from "react-redux";
import Preloader from "./component/preloader/Preloader";
import Nav from "./component/nav/Nav";
import Home from "./component/main/home/Home";
import Footer from "./component/footer/Footer";
import ArticleList from "./component/article/list/ArticleList";
import ArticleDetails from "./component/article/detail/ArticleDetails";
import User from "./component/user/user";
import Login from "./component/user/signup&login/login";
import PostButton from "./component/article/post/PostButton";
import PostForm from "./component/article/post/PostForm";
import PatchForm from "./component/article/post/PatchForm"; // PatchForm을 import 합니다.
import Signup from "./component/user/signup&login/signup";
import Individual_signup from "./component/user/signup&login/individual_signup";
import Institution_signup from "./component/user/signup&login/institution_signup";
import MyPage from "./component/user/mypage/mypage";
import CommunityList from "./component/community/list/CommunityList";
import CommunityDetails from "./component/community/detail/CommunityDetails";
import Comment from "./component/comment/Comment";
import CommunityWrite from "./component/community/write/CommunityWrite";
import CommunityUpdate from "./component/community/write/CommunityUpdate";
import ChatMain from "./component/chat/ChatMain";

function App() {
  const location = useLocation();
  const loginState = useSelector((state) => state.loginSlice);

  return (
    <div>
      <Preloader />
      <ToastContainer />

      <Routes>
        <Route path="/" element={<Nav />}>
          <Route index element={<Home />} />
          <Route path="/donate" element={<ArticleList />} />
          <Route path="/donate/:articleId" element={<ArticleDetails />} />
          <Route path="/write" element={<PostForm />} /> {/* PostForm을 /write 경로에 연결합니다. */}
          <Route path="/update/:articleId" element={<PatchForm />} />
          <Route path="/user" element={<User />} />
          <Route path="/user/me" element={<MyPage />} />
          <Route path="/community" element={<CommunityList />} />
          <Route path="/community/:communityId" element={<CommunityDetails />} />
          <Route path="/comment/:communityId" element={<Comment />} />
          <Route path="/community/write" element={<CommunityWrite />} />
          <Route path="/community/update/:communityId" element={<CommunityUpdate />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/chat" element={<ChatMain />} />
        </Route>

        <Route path="/signup/individual" element={<Individual_signup />} />
        <Route path="/signup/institution" element={<Institution_signup />} />
        <Route path="/user/login" element={<Login />} />
      </Routes>
      {location.pathname !== "/signup/individual" && location.pathname !== "/signup/institution" && location.pathname !== "/user/login" && <Footer />}
      {loginState.id && <PostButton />}
    </div>
  );
}

export default App;
