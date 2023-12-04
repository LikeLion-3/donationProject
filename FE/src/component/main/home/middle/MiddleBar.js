import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import Axios from "axios";

function MiddleBar() {
    const [indivCount, setIndivCount] = useState(null);
    const [instCount, setInstCount] = useState(null);
    const [totalDonationCount, setTotalDonationCount] = useState(null);

    useEffect(() => {
        // 서버에서 데이터를 받아오는 API 엔드포인트에 대한 URL을 적절히 변경
        Axios.get('http://localhost:8080/api/main/count')
            .then(response => {
                const data = response.data;
                setIndivCount(data.indivCount);
                setInstCount(data.instCount);
                setTotalDonationCount(data.totalDonationCount);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }, []);

    return (
        <div className="container">
            <div className="row">
                <div className="col-lg-12">
                    <div className="more-info">
                        <div className="row" style={{ justifyContent: "space-around"}}>
                            <div className="col-lg-3 col-sm-6 col-6">
                                <i className="fa fa-user" />
                                <h4><span>개인 가입자 수:</span><br />{indivCount}</h4>
                            </div>
                            <div className="col-lg-3 col-sm-6 col-6">
                                <i className="fa fa-user" />
                                <h4><span>기관 가입자 수:</span><br />{instCount}</h4>
                            </div>
                            <div className="col-lg-3 col-sm-6 col-6">
                                <i className="fa fa-home" />
                                <h4><span>총 기부 진행:</span><br />{totalDonationCount}</h4>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default MiddleBar;