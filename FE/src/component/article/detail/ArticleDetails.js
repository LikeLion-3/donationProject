import './styles.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

const ArticleDetails = () => {
  const [article, setArticle] = useState(null);
  const params = useParams();

  useEffect(() => {
    axios.get(`http://localhost:8080/api/articles/${params.articleId}`)
      .then(response => {
        setArticle(response.data);
      })
      .catch(error => {
        console.error('Error fetching article:', error);
      });
  }, [params]);

  const formattedDate = article?.createdAt
    ? new Date(article.createdAt).toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      }).replace(/ /g, '')
    : '로딩 중...';

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

  return (
    <section className="py-5">
      <div className="container px-4 px-lg-5 my-5">
        <div className="row gx-4 gx-lg-5 align-items-start">
          <div className="col-md-6" style={{ backgroundColor: '#f8f9fa', padding: '15px' }}>
            {article?.images && article.images.length > 0 ? (
              article.images.map(image => (
                <img
                  key={image.id}
                  className="card-img-top mb-5 mb-md-0"
                  src={`/images/${image.imgName}`}
                  alt={image.imgName}
                  style={{ maxHeight: '400px', width: '100%', objectFit: 'contain' }}
                />
              ))
            ) : (
              <img
                className="card-img-top mb-5 mb-md-0"
                src="https://dummyimage.com/600x700/dee2e6/6c757d.jpg"
                alt="true"
                style={{ maxHeight: '400px', width: '100%', objectFit: 'contain' }}
              />
            )}
          </div>
          <div className="col-md-6">
            <div className="d-flex align-items-start mb-1" style={{ flexDirection: 'column' }}>
              <div className="d-flex align-items-center">
                <div className="badge bg-warning text-white me-2" style={{ padding: '0.5rem', fontSize: '1.5rem' }}>
                  {getArticleTypeText(article?.articleType)}
                </div>
                <h1 className="fw-bolder display-5 ms-0.5 text-left" style={{ fontSize: '2rem' }}>
                  {article?.title || '로딩 중...'}
                </h1>
              </div>
            </div>
            <br />
            <div className="fs-5 mb-3 d-flex justify-content-between">
              <div className="d-flex flex-wrap">
                <p style={{ fontSize: '16px', marginRight: '8px', color: '#212529' }}>물품종류 :</p>
                {article?.prodCategories && article.prodCategories.map((category, index) => (
                  <span
                    key={index}
                    className="btn btn-outline-dark me-2 mb-2"
                    disabled
                    style={{
                      fontSize: '16px',
                      padding: '0.25rem 0.5rem',
                      paddingTop: '0.1rem',
                      paddingBottom: '0rem',
                      color: '#212529',
                      backgroundColor: '#D3D3D3',
                      borderColor: '#D3D3D3',
                    }}
                  >
                    {category}
                  </span>
                ))}
              </div>
              <div>
                <span className="me-3" style={{ fontSize: '18px', color: '#212529' }}>조회수: 100</span>
                <span style={{ fontSize: '18px', color: '#212529' }}>게시날짜: {formattedDate}</span>
              </div>
            </div>
            <hr style={{ borderColor: '#212529', marginTop: '1rem', marginBottom: '1rem' }} />
            <p className="lead" style={{ color: '#212529' }}>
              {article?.content || '로딩 중...'}
            </p>
            <hr style={{ borderColor: '#212529', marginTop: '1rem', marginBottom: '1rem' }} />
            <div className="d-flex justify-content-between align-items-center mb-3">
              <p className="mb-4" style={{ fontSize: '20px', color: '#212529' }}>
                <span className="me-2" style={{ fontSize: '18px', color: '#212529', fontWeight: 'normal' }}>기부지역:</span> 서울시 송파구 백제고분로 777-7777
              </p>
              <button
                className="btn"
                type="button"
                style={{
                  backgroundColor: '#87CEEB',
                  borderColor: '#87CEEB',
                  color: '#ffffff',
                }}
              >
                <i className="bi-cart-fill me-1"></i>
                채팅하기
              </button>
            </div>
            <hr className="my-4" />
          </div>
        </div>
      </div>
    </section>
  );
};

export default ArticleDetails;