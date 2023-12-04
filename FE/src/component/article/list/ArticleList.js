import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';

const ArticleList = () => {
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('ko-KR', options);
  };

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

  const ProductCard = ({ articleId, title, articleType, images, writer, category, postDate }) => {
    const imageUrl = images.length > 0 ? `/images/${images[0].imgName}` : 'https://dummyimage.com/450x300/dee2e6/6c757d.jpg';

    return (
      <div className="col mb-4">
        <div className="card h-100" style={{ width: "110%" }}>
            <img
            className="card-img-top"
            src={imageUrl}
            alt="Product"
            style={{ maxWidth: '450px', height: '250px', objectFit: 'cover' }}
            />    
            <div className="card-body p-4 mb-2">
            <div className="text-left">
              <div className="badge bg-warning text-white position-absolute" style={{ top: '0.3rem', right: '0.3rem', padding: '0.5rem' }}>
                {getArticleTypeText(articleType)}
              </div>
              <h5 className="fw-bolder mb-1 text-center" style={{ color: '#212529', marginBottom: '10px' }}>
                <Link to={`/donate/${articleId}`}>{title}</Link>
              </h5>
              <hr className="my-1" />
              <p className="mb-1" style={{ color: '#212529', marginBottom: '10px' }}><strong>작성자 :</strong> {writer}</p>
              <strong>상품카테고리 :</strong> <span className="badge bg-secondary mx-1">{category}</span>
              <p className="mb-1" style={{ color: '#212529', marginBottom: '1px' }}><strong>게시일 :</strong> {formatDate(postDate)}</p>
              <p className="mb-1" style={{ color: '#212529', marginBottom: '10px' }}><strong>기부지역 :</strong> 서울시 송파구 백제고분로 7777-77</p>
            </div>
            <br />
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
  };

  const ArticleList = () => {
    const [products, setProducts] = useState([]);

    useEffect(() => {
      const fetchData = async () => {
        try {
          const response = await fetch('http://localhost:8080/api/articles?page=1&size=10');
          const data = await response.json();
          setProducts(data.content);
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      };

      fetchData();
    }, []);

    return (
      <section className="py-5">
        <div className="container-fluid px-4 px-lg-5 mt-5">
          <div className="row gx-4 gx-lg-5 row-cols-4" style={{ display: 'flex', flexWrap: 'wrap' }}>
            {products.map(product => (
              <ProductCard
                key={product.articleId}
                articleId={product.articleId}
                articleType={product.articleType}
                title={product.title}
                category={product.prodCategory}
                images={product.images}
                writer={product.writer}
                tags={product.articleStatus}
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