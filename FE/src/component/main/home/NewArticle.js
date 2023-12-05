import { useEffect, useState } from "react";
import Axios from "axios";
import Top5Articles from "./articles/Top5Articles";
import Top5ArticlesDefault from "./articles/Top5ArticlesDefault";

function MiddleArticle() {
    const [hasData, setHasData] = useState(false);
    const [articleList, setArticleList] = useState(null);

    const fetchData = async () => {
        try {
            const response = await Axios.get('http://localhost:8080/api/articles/today');
            const fetchedData = response.data;

            if (fetchedData.content.length > 0) {
                // 데이터가 있다면 Inst 컴포넌트를 표시
                setHasData(true);
                setArticleList(fetchedData);
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
                            <h2>최신 기부글</h2>
                            <p>가장 최근에 등록된 기부글이에요. 해당 기관에게 기부해 보는게 어떨까요?</p>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-lg-8">
                        <div className="items">
                            {hasData ? <Top5Articles data={articleList}/> : <Top5ArticlesDefault/>}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default MiddleArticle;