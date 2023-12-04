import { Link } from "react-router-dom"
import KakaoMap from "./KakaoMap";
import Inst from "./Inst";

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
                            <Inst/>
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