import logo from "./logo.svg";
import "./App.css";
import { Routes, Route } from "react-router-dom";
import Preloader from "./component/preloader/Preloader";
import Nav from "./component/nav/Nav";
import Home from "./component/main/home/Home";
import Footer from "./component/footer/Footer";
import ChatMain from "./pages/chatMain";

function App() {
  return (
    <div>
      <Preloader />

      <Routes>
        <Route path="/" element={<Nav />}>
          <Route index element={<Home />} />
          <Route path="/chat" element={<ChatMain />} />
        </Route>
      </Routes>

      <Footer />
    </div>
  );
}

export default App;
