import { Link } from "react-router-dom";

function Top5Articles({ data }) {
    const getArticleTypeText = (type) => {
        switch (type) {
            case 'DONATION':
                return '기부해요';
            case 'BENEFICIARY':
                return '필요해요';
            case 'CERTIFICATION':
                return '인증해요';
            default:
                return type;
        }
    };

    if (data.content) {
        const article = data.content;
        const pageable = data.pageable;

        return (
            <div className="row">
                <div className="col-lg-12">
                    <div className="item">
                        {article.map((a, idx) => {
                            return (
                                <div className="row" key={idx}>
                                    <div className="col-lg-8 col-sm-7">
                                        <div className="right-content">
                                            <div className="badge bg-warning text-white position-absolute" style={{ top: '0.3rem', right: '0.3rem', padding: '0.5rem' }}>
                                                {getArticleTypeText(a.type)}
                                            </div>
                                            <h4><Link to={`/donate/${a.articleId}`}>{a.title}</Link></h4>
                                            <p>{a.writer}</p>
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

export default Top5Articles;