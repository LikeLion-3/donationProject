import './styles.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useState, useEffect } from 'react';
import axios from 'axios';

function ArticleDetails() {
  const [article, setArticle] = useState(null);

  useEffect(() => {
    // 백엔드 API 엔드포인트로부터 데이터를 가져옵니다.
    axios.get('http://localhost:8080/api/article/8')
      .then(response => {
        // 가져온 데이터를 상태에 설정합니다.
        setArticle(response.data);
      })
      .catch(error => {
        console.error('Error fetching article:', error);
      });
  }, []); // 빈 배열은 한 번만 실행되도록 보장합니다.

  // createdAt이 유효하면 해당 날짜를 포맷팅합니다.
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
        // 다른 아티클 타입에 대한 처리를 추가할 수 있습니다.
        default:
          return type; // 기본적으로는 아티클 타입 그대로 반환
      }
    };

  return (
    <section className="py-5">
      <div className="container px-4 px-lg-5 my-5">
        <div className="row gx-4 gx-lg-5 align-items-start">
          <div className="col-md-6">
            {article?.images && article.images.length > 0 ? (
              // 이미지가 존재하는 경우에만 이미지를 표시합니다.
              article.images.map(image => (
                <img
                  key={image.id}
                  className="card-img-top mb-5 mb-md-0"
                  src="/Users/wook/Desktop/project/donationProject/src/main/upload/article-images/"
                  alt={image.imgName}
                  style={{ maxHeight: '400px', width: '100%', objectFit: 'cover' }}
                />
              ))
            ) : (
              // 이미지가 없는 경우 대체 이미지를 표시합니다.
              <img
                className="card-img-top mb-5 mb-md-0"
                src="https://dummyimage.com/600x700/dee2e6/6c757d.jpg"
                alt="No Image"
                style={{ maxHeight: '400px', width: '100%', objectFit: 'cover' }}
              />
            )}
          </div>
          <div className="col-md-6">
            <div className="d-flex align-items-start mb-1" style={{ flexDirection: 'column' }}>
              <div className="badge bg-warning text-white me-2" style={{ top: '0.5rem', right: '0.5rem', padding: '0.5rem', fontSize: '1.5rem' }}>
                {getArticleTypeText(article?.articleType)}
              </div>
              <p></p>
              <h1 className="fw-bolder display-5 ms-0.5 text-left" style={{ fontSize: '2rem' }}>{article?.title || '로딩 중...'}</h1>
            </div>
            <div className="fs-5 mb-3 d-flex justify-content-between">
              <div className="d-flex">
                <span className="btn btn-outline-dark" disabled style={{ fontSize: '16px' }}>상품태그</span>
              </div>
              <div>
                <span className="me-3" style={{ fontSize: '18px' }}>조회수: 100</span>
                <span style={{ fontSize: '18px' }}>게시날짜: {formattedDate}</span>
              </div>
            </div>
            <p className="lead">
              {/* article이 로드되면 내용을 표시합니다. */}
              {article?.content || '로딩 중...'}
            </p>
            
            <div className="d-flex justify-content-between align-items-center mb-3">
              <p className="mb-4" style={{ fontSize: '20px' }}>
                <span className="fw-bolder me-2" style={{ fontSize: '20px' }}>기부지역:</span> 서울시 강남구
              </p>
              <button className="btn btn-outline-dark" type="button">
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
}

export default ArticleDetails;