import {Link, NavLink, Outlet} from "react-router-dom";
import {useSelector, useDispatch} from "react-redux";
import { logout } from "../slices/loginSlice";
import useToast from '../hooks/useToast';

function Nav() {
    const { showToast } = useToast();
    
    const loginState = useSelector(state => state.loginSlice);
    console.log( loginState );
    const dispatch = useDispatch();

    const handleLogout = (e) => {
        e.preventDefault();
        dispatch(logout());
        showToast("로그아웃되었습니다.");
    };

    return (
        <div>
            <header className="header-area header-sticky">
                <div className="container">
                    <div className="row">
                        <div className="col-12">
                            <nav className="main-nav">
                                {/* ***** Logo Start ***** */}
                                <Link to="/" className="logo">
                                    <img src="logo.png" style={{width:"110px"}} alt="true"/>
                                </Link>
                                {/* ***** Logo End ***** */}
                                {/* ***** Menu Start ***** */}
                                <ul className="nav">
                                    <li><NavLink to="/">Home</NavLink></li>
                                    <li><NavLink to="/donate">Donate</NavLink></li>
                                    <li><NavLink to="/community">Community</NavLink></li>
                                    {!loginState.id ?
                                        <>
                                            <li><NavLink to="/signup">SignUp</NavLink></li>
                                            <li><NavLink to="/user/login">LogIn</NavLink></li>
                                        </>
                                        :
                                        <>
                                            <li><NavLink to="/chat">Chat</NavLink></li>
                                            <li><NavLink to="/" onClick={handleLogout}>Logout</NavLink></li>
                                        </>
                                    }
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