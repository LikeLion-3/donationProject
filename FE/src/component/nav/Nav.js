import { Link, NavLink, Outlet } from "react-router-dom";

function Nav() {
    return (
        <div>
            <header className="header-area header-sticky">
                <div className="container">
                    <div className="row">
                        <div className="col-12">
                            <nav className="main-nav">
                                {/* ***** Logo Start ***** */}
                                <Link to="/" className="logo">
                                    <img src="logo.png" alt="true" />
                                </Link>
                                {/* ***** Logo End ***** */}
                                {/* ***** Menu Start ***** */}
                                <ul className="nav">
                                    <li><NavLink to="/">Home</NavLink></li>
                                    <li><NavLink to="/donate">Donate</NavLink></li>
                                    <li><NavLink to="/chat">Chat</NavLink></li>
                                    {/* 로그인 여부에 따라 회원가입 / 로그인 창으로 변경하기 */}
                                    <li><NavLink to="#">SignUp / SignIn</NavLink></li>
                                </ul>
                                <NavLink className="menu-trigger">
                                    <span>Menu</span>
                                </NavLink>
                                {/* ***** Menu End ***** */}
                            </nav>
                        </div>
                    </div>
                </div>
            </header>

            <Outlet/>
        </div>
    )
}

export default Nav;