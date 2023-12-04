import { Link } from "react-router-dom";

function Inst({ data }) {
    return (
        <div className="row">
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
                                <h4>{data.institutionName}</h4>
                                <p>{data.fullAddr}</p>
                                <ul className="info">
                                    <li><i className="fa fa-user" /> {data.donationCount}</li>
                                    <li><i className="fa fa-globe" /> 미정</li>
                                    <li><i className="fa fa-home" /> 미정</li>
                                </ul>
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
    )
}

export default Inst;