import KakaoMap from "../../kakaomap/KakaoMap";
import Inst from "../institution/Inst";
import InstDefault from "../institution/InstDefault";
import { useEffect, useState } from "react";
import Axios from "axios";

function Middle() {
    const [hasData, setHasData] = useState(false);
    const [favoriteInst, setFavoriteInst] = useState(null);

    const fetchData = async () => {
        try {
            const response = await Axios.get('/api/users/adjacent', {
                headers: {
                    // 여기는 토큰 어떻게 받아와야 할지 생각하고 수정해야할듯합니다.
                    // 'Authorization' : 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImJvZHkiOnsiZW1haWwiOiJ0ZXN0MDRAZ21haWwuY29tIn0sImV4cCI6MTcwMTcwNDI1NX0.HUoJuRZPgwA1SAYy-5kpM0_Vz_LSFEwg9lWsS6ecB13BYzcj1RrQcBkB_hDJR6I3PHusamA3LDalos_KWdjYCA'
                }
            });

            const fetchedData = response.data;

            if (fetchedData.result != 'FAIL') {
                // 데이터가 있다면 Inst 컴포넌트를 표시
                setHasData(true);
                setFavoriteInst(fetchedData);
            } else {
                // 데이터가 없다면 InstDefault 컴포넌트를 표시
                setHasData(false);
            }
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

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
                            {hasData ? <Inst data={favoriteInst}/> : <InstDefault/>}
                        </div>
                    </div>
                    <div className="col-lg-4">
                        <div className="side-bar-map">
                            <div className="row">
                                <div className="col-lg-12">
                                    <div id="map">
                                        <KakaoMap data={favoriteInst}/>
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