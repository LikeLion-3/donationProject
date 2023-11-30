import { Link } from "react-router-dom"
import KakaoMap from "./KakaoMap";

function Middle() {
    return (
        <div className="visit-country">
            <div className="container">
                <div className="row">
                    <div className="col-lg-5">
                        <div className="section-heading">
                            <h2>주위에 도움이 필요한 기관이에요.</h2>
                            <p>어디에 기부를 해야할 지 모르는 여러분을 위해 지역 근처 기관을 알려드려요.</p>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-lg-8">
                        <div className="items">
                            <div className="row">
                                {/* 이거를 포맷으로 데이터 반환해서 수정하기 */}
                                <div className="col-lg-12">
                                    <div className="item">
                                        <div className="row">
                                            <div className="col-lg-4 col-sm-5">
                                                <div className="image">
                                                    <img src="assets/images/country-01.jpg" alt="true" />
                                                </div>
                                            </div>
                                            <div className="col-lg-8 col-sm-7">
                                                <div className="right-content">
                                                    <h4>SWITZERLAND</h4>
                                                    <span>Europe</span>
                                                    <div className="main-button">
                                                        <Link to="about.html">Explore More</Link>
                                                    </div>
                                                    <p>Woox Travel is a professional Bootstrap 5 theme HTML CSS layout for your website. You can use this layout for your commercial work.</p>
                                                    <ul className="info">
                                                        <li><i className="fa fa-user" /> 8.66 Mil People</li>
                                                        <li><i className="fa fa-globe" /> 41.290 km2</li>
                                                        <li><i className="fa fa-home" /> $1.100.200</li>
                                                    </ul>
                                                    <div className="text-button">
                                                        <Link to="about.html">Need Directions ? <i className="fa fa-arrow-right" /></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-lg-12">
                                    <ul className="page-numbers">
                                        <li><Link to="#"><i className="fa fa-arrow-left" /></Link></li>
                                        <li className="active"><Link to="#">1</Link></li>
                                        <li><Link to="#">2</Link></li>
                                        <li><Link to="#">3</Link></li>
                                        <li><Link to="#"><i className="fa fa-arrow-right" /></Link></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-lg-4">
                        <div className="side-bar-map">
                            <div className="row">
                                <div className="col-lg-12">
                                    <div id="map">
                                        <KakaoMap/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Middle;