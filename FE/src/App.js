import logo from './logo.svg';
import './App.css';
import { Routes, Route } from 'react-router-dom';
import Preloader from './component/preloader/Preloader';
import Nav from './component/nav/Nav';
import Home from './component/main/home/Home';
import Footer from './component/footer/Footer';
import ArticleList from './component/article/list/ArticleList';
import ArticleDetails from './component/article/detail/ArticleDetails';
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
        </Route>
        <Route path="/signin" element={<SignIn />}>
        </Route>
        <Route path="/signup" element={<SignUp />}>
        </Route>
      </Routes>

      <Footer />
    </div>
  );
}

export default App;
