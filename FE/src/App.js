import logo from './logo.svg';
import './App.css';
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Preloader from './component/preloader/Preloader';
import Nav from './component/nav/Nav';
import Home from './component/main/home/Home';
import Footer from './component/footer/Footer';
import ArticleList from './component/article/list/ArticleList';
import ArticleDetails from './component/article/detail/ArticleDetails';
import PostButton from './component/article/post/PostButton';
import PostForm from './component/article/post/PostForm'; // PostForm을 import 합니다.
import SignIn from './component/user/signin';
import SignUp from './component/user/signup';

function App() {
  return (
    <div>
      <Preloader />

      <Routes>
        <Route path="/" element={<Nav />}>
          <Route index element={<Home />} />
          <Route path="/donate" element={<ArticleList />} />
          <Route path="/donate/:articleId" element={<ArticleDetails />} />
          <Route path="/write" element={<PostForm />} /> {/* PostForm을 /write 경로에 연결합니다. */}
        </Route>
        <Route path="/signin" element={<SignIn />} />
        <Route path="/signup" element={<SignUp />} />
      </Routes>

      <Footer />

      <PostButton />
    </div>
  );
}

export default App;