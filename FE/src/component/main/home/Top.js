import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import MiddleBar from "./MiddleBar";

function Top() {
    const [bannerTimer, setBannerTimer] = useState(null);

    let current = 0;
    const bannerSwitcher = () => {
        current++;
        console.log(current);

        const nextInput = document.querySelector(`#banner${current + 1}`);
        if (nextInput) {
            nextInput.checked = true;
        } else {
            window.document.querySelector('#banner1').checked = true;
            current=0;
        }
    };

    useEffect(() => {
        const timer = setInterval(bannerSwitcher, 5000);

        return () => clearInterval(timer);
    }, []);

    const handleControlClick = (bannerNumber) => {
        
        current = bannerNumber;
        console.log("**", current, bannerNumber);
        clearInterval(bannerTimer);
        const newBannerTimer = setInterval(bannerSwitcher, 5000);
        setBannerTimer(newBannerTimer);
    };



    return (
        < section id="section-1" >
            <div className="content-slider">
                <input type="radio" id="banner1" className="sec-1-input" name="banner" defaultChecked />
                <input type="radio" id="banner2" className="sec-1-input" name="banner" />
                <input type="radio" id="banner3" className="sec-1-input" name="banner" />
                <input type="radio" id="banner4" className="sec-1-input" name="banner" />
                <div className="slider">
                    <div id="top-banner-1" className="banner">
                        <div className="banner-inner-wrapper header-text">
                            <div className="main-caption">
                                <h2>누군가에게 도움을 주고 싶으신가요?</h2>
                                <h1>WarmWave에서 기부 한번 어떠세요?</h1>
                                <div className="border-button"><Link to="/donate">기부하러 가기</Link></div>
                            </div>
                            {/* 이 부분 어떻게 처리할지 생각 -> 통계 자료 보여주는 용도? 일단은 킵 */}
                            <MiddleBar/>
                        </div>
                    </div>
                    <div id="top-banner-2" className="banner">
                        <div className="banner-inner-wrapper header-text">
                            <div className="main-caption">
                                <h2>기부, 그렇게 어렵지 않아요.</h2>
                                <h1>쉽고 빠르게 할 수 있는 기부, 해보시겠어요?</h1>
                                <div className="border-button"><Link to="/donate">기부하러 가기</Link></div>
                            </div>
                            <MiddleBar/>
                        </div>
                    </div>
                    <div id="top-banner-3" className="banner">
                        <div className="banner-inner-wrapper header-text">
                            <div className="main-caption">
                                <h2>사소한 물건이라도 좋아요.</h2>
                                <h1>그게 누군가에게는 꼭 필요한 물건일지도 모르니까요.</h1>
                                <div className="border-button"><Link to="/donate">기부하러 가기</Link></div>
                            </div>
                            <MiddleBar/>
                        </div>
                    </div>
                    <div id="top-banner-4" className="banner">
                        <div className="banner-inner-wrapper header-text">
                            <div className="main-caption">
                                <h2>혹여 기부한 물품이 제대로 쓰일까 망설이시나요?</h2>
                                <h1>안심하세요. 인증된 기관만이 우리와 함께하니까요.</h1>
                                <div className="border-button"><Link to="/donate">기부하러 가기</Link></div>
                            </div>
                            <MiddleBar/>
                        </div>
                    </div>
                </div>
                <nav>
                    <div className="controls">
                        {[1, 2, 3, 4].map((bannerNumber) => (
                            <label key={bannerNumber} htmlFor={`banner${bannerNumber}`}>
                                <span
                                    className={`progressbar ${bannerNumber === current ? 'active' : ''}`}>
                                    <span className="progressbar-fill" />
                                </span>
                                <span className="text" onClick={() => handleControlClick(bannerNumber)}>{bannerNumber}</span>
                            </label>
                        ))}
                    </div>
                </nav>
            </div>
        </section >

    )
}

export default Top;