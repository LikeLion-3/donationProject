import { Link } from "react-router-dom";

function Top5ArticlesDefault() {
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
                                <h4>기부글이 존재하지 않습니다.</h4>
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

export default Top5ArticlesDefault;