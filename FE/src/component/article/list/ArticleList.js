import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

const ArticleList = () => {
    // 게시일을 형식에 맞게 변환하는 함수
    const formatDate = (dateString) => {
        const options = { year: 'numeric', month: 'long', day: 'numeric' };
        const formattedDate = new Date(dateString).toLocaleDateString('ko-KR', options);
        return formattedDate;
    };
    
    // 아티클 타입에 따른 게시글 상태 변환표시하는 함수
    const getArticleTypeText = (type) => {
        switch (type) {
        case 'DONATION':
            return '기부해요';
        case 'BENEFICIARY':
            return '필요해요';
        case 'CERTIFICATION':
            return '인증해요';
        // 다른 아티클 타입에 대한 처리를 추가할 수 있습니다.
        default:
            return type; // 기본적으로는 아티클 타입 그대로 반환
        }
    };
    
    const ProductCard = ({ title, articleType, imageUrl, location, category = [], postDate }) => (
        <div className="col mb-4">
            <div className="card h-100">
                <img className="card-img-top" src={imageUrl} alt="Product" />
                <div className="card-body p-4 mb-2">
                    <div className="text-left">
                        <div className="badge bg-warning text-white position-absolute" style={{ top: '0.3rem', right: '0.3rem', padding: '0.5rem' }}>
                            {getArticleTypeText(articleType)}
                        </div>
                        <h5 className="fw-bolder mb-1 text-center"> {/* text-center 클래스 추가 */}
                            {title}
                        </h5>
                        <hr className="my-1" />
                        <p className="mb-1"><strong>Location:</strong> {location}</p>
                        <strong>Category:</strong>
                        {Array.isArray(category) && category.map((category, index) => (
                            <span key={index} className="badge bg-secondary mx-1">{category}</span>
                        ))}
                    </div>
                    <p className="mb-1"><strong>Post Date:</strong> {formatDate(postDate)}</p>
                </div>
            </div>
            <div className="card-footer p-4 border-top-0 bg-transparent">
                <div className="text-center">
                    <div className="mb-1">
                    </div>
                </div>
            </div>
        </div>
    );
    
    const ArticleList = () => {
        const [products, setProducts] = useState([]);
    
        useEffect(() => {
            // 백엔드에서 데이터를 가져오는 비동기 함수 호출
            const fetchData = async () => {
                try {
                    const response = await fetch('http://localhost:8080/api/article?page=1&size=10');
                    const data = await response.json();
                    setProducts(data.content);
                } catch (error) {
                    console.error('Error fetching data:', error);
                }
            };
    
            fetchData(); // 함수 호출
        }, []); // 빈 배열을 전달하여 최초 한 번만 호출되도록 함
  
        return (
            <section className="py-5">
                <div className="container-fluid px-4 px-lg-5 mt-5">
                    <div className="row gx-4 gx-lg-5 row-cols-4" style={{ display: 'flex', flexWrap: 'wrap' }}>
                        {products.map(product => (
                            <ProductCard
                                key={product.id}
                                articleType={product.articleType}
                                title={product.title}
                                category={product.prodCategory}
                                imageUrl={`https://dummyimage.com/450x300/dee2e6/6c757d.jpg`}  
                                location={product.location}
                                tags={product.tags}
                                postDate={product.createdAt}
                            />
                        ))}
                    </div>
                </div>
            </section>
        );
    };
    
    return <ArticleList />;
};
  
export default ArticleList;