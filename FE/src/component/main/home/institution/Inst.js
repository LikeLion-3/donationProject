import { Link } from "react-router-dom";

function Inst({ data }) {
    if (data.content) {
        const instList = data.content;
        const pageable = data.pageable;

        return (
            <div className="row">
                <div className="col-lg-12">
                    <div className="item">
                        {instList.map((i, idx) => {
                            return (
                                <div className="row" style={{marginBottom: "30px"}} key={idx}>
                                    <div className="col-lg-8 col-sm-7">
                                        <div className="right-content">
                                            <h4>{i.institutionName}</h4>
                                            <p>{i.fullAddr}</p>
                                            <ul className="info">
                                                <li><i className="fa fa-user" /> 기부 받은 횟수: {i.donationCount}</li>
                                                {/* <li><i className="fa fa-globe" /> 미정</li> */}
                                                {/* <li><i className="fa fa-home" /> 미정</li> */}
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            )
                        })}
                    </div>
                </div>
                <div className="col-lg-12">
                    <ul className="page-numbers">
                        {pageable.pageNumber > 0 && (
                            <li><Link to={`/${pageable.pageNumber - 1}`}><i className="fa fa-arrow-left" /></Link></li>
                        )}

                        {[...Array(pageable.totalPages)].map((_, index) => (
                            <li key={index} className={pageable.pageNumber === index ? 'active' : ''}><Link to={`/${index}`}>{index + 1}</Link></li>
                        ))}

                        {pageable.pageNumber < pageable.totalPages - 1 && (
                            <li><Link to={`/${pageable.pageNumber + 1}`}><i className="fa fa-arrow-right" /></Link></li>
                        )}
                    </ul>
                </div>
            </div>
        )
    }

}

export default Inst;