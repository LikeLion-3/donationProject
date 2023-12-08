import './styles.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

function CommunityDetails() {
  const [article, setArticle] = useState(null);
  const params = useParams();


  useEffect(() => {
    // 백엔드 API 엔드포인트로부터 데이터를 가져옵니다.
    // axios.get(`http://localhost:8080/api/articles/${params.articleId}`)
    axios.get(`http://localhost:8080/api/articles/1`)
      .then(response => {
        // 가져온 데이터를 상태에 설정합니다.
        setArticle(response.data);
      })
      .catch(error => {
        console.error('Error fetching article:', error);
      });
  }, [params]); // 빈 배열은 한 번만 실행되도록 보장합니다.

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
              // 각 이미지에 대한 정보를 사용하도록 수정
              <img
                key={image.id}
                className="card-img-top mb-5 mb-md-0"
                src={`/images/${image.imgName}`} // 이미지의 URL을 사용
                alt={image.imgName}
                style={{ maxHeight: '400px', width: '100%', objectFit: 'cover' }}
              />
            ))
          ) : (
            // 이미지가 없는 경우 대체 이미지를 표시합니다.
            <img
              className="card-img-top mb-5 mb-md-0"
              src="https://dummyimage.com/600x700/dee2e6/6c757d.jpg"
              alt="true"
              style={{ maxHeight: '400px', width: '100%', objectFit: 'cover' }}
            />
          )}
        </div>
          <div className="col-md-6">
          <div className="d-flex align-items-start mb-1" style={{ flexDirection: 'column' }}>
            <div className="d-flex align-items-center"> {/* 새로운 부모 요소 */}
              <div className="badge bg-warning text-white me-2" style={{ padding: '0.5rem', fontSize: '1.5rem' }}>
                {getArticleTypeText(article?.articleType)}
              </div>
              <h1 className="fw-bolder display-5 ms-0.5 text-left" style={{ fontSize: '2rem' }}>
                {article?.title || '로딩 중...'}
              </h1>
            </div>
          </div>
            <br/>
            <div className="fs-5 mb-3 d-flex justify-content-between">
              <div className="d-flex">
                <p style={{ fontSize: '16px', marginRight: '8px', color: '#212529' }}>상품카테고리 :</p>
                <span
                  className="btn btn-outline-dark"
                  disabled
                  style={{
                    fontSize: '16px',
                    padding: '0.25rem 0.5rem',
                    paddingTop: '0.1rem',
                    paddingBottom: '0rem',
                    color: '#212529',
                    backgroundColor: '#D3D3D3', // 연한 회색
                    borderColor: '#D3D3D3', // 테두리 색상도 설정
                  }}
                >
                  {article?.prodCategory}
                </span>              </div>
              <div>
                <span className="me-3" style={{ fontSize: '18px', color: '#212529' }}>조회수: 100</span>
                <span style={{ fontSize: '18px', color: '#212529' }}>게시날짜: {formattedDate}</span>
              </div>
            </div>
            <hr style={{borderColor: '#212529', marginTop: '1rem', marginBottom: '1rem'}} />

<p className="lead" style={{color: '#212529'}}>
  {/* article이 로드되면 내용을 표시합니다. */}
  {article?.content || '로딩 중...'}
</p>


<hr style={{borderColor: '#212529', marginTop: '1rem', marginBottom: '1rem'}} />

<div className="d-flex justify-content-between align-items-center mb-3">
  <p className="mb-4" style={{ fontSize: '20px', color: '#212529' }}>
  <span className="me-2" style={{ fontSize: '18px', color: '#212529', fontWeight: 'normal' }}>기부지역:</span> 서울시 송파구 백제고분로 777-7777
  </p>
  <button
  className="btn"
  type="button"
  style={{
    backgroundColor: '#87CEEB', // 내부 색상
    borderColor: '#87CEEB', // 테두리 색상
    color: '#ffffff', // 글자 색상
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
}

export default CommunityDetails;