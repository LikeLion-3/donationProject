import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link, useParams, useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import jwtAxios from '../../util/jwtUtil';
import { getCookie } from '../../util/cookieUtil';


const ArticleDetails = () => {
  const [article, setArticle] = useState(null);
  const params = useParams();
  const navigate = useNavigate();

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
    return type || '기본값';
  };

  const getArticleTypeStyle = (type) => {
    switch (type) {
      case '기부해요':
        return { backgroundColor: '#ffc107', borderColor: '#ffc107', color: '#ffffff' };
      case '필요해요':
        return { backgroundColor: '#007bff', borderColor: '#007bff', color: '#ffffff' };
      case '인증해요':
        return { backgroundColor: '#28a745', borderColor: '#28a745', color: '#ffffff' };
      default:
        return { backgroundColor: '#000000', borderColor: '#000000', color: '#ffffff' };
    }
  };

  const renderBadges = () => {
    if (!article?.articleType) {
      return null;
    }

    const badgeStyle = getArticleTypeStyle(article.articleType);
    const badgeText = getArticleTypeText(article.articleType);

    return (
      <div className="badge" style={{ ...badgeStyle, padding: '0.5rem', fontSize: '1.2rem', marginRight: '0.5rem' }}>
        {badgeText}
      </div>
    );
  };

  const handleDelete = async () => {
    const userToken = Cookies.get('user');
    const parsedToken = userToken ? JSON.parse(decodeURIComponent(userToken)) : null;

    try {
      await axios.delete(`http://localhost:8080/api/articles/${params.articleId}`, {
        headers: {
          'Authorization': `Bearer ${parsedToken.accessToken}`,
        },
      });

      // 삭제 후 목록 페이지로 이동
      navigate('/donate');
    } catch (error) {
      console.error('Error deleting article:', error);
    }
  };

  const handleUpdate = () => {
    // '수정' 버튼 클릭 시, PatchForm 페이지로 이동하고 현재 글의 정보를 전달
    navigate(`/update/${params.articleId}`, { state: { article } });
  };

  const loggedInUserId = getCookie('user')?.id; // 토큰이 없을 때를 대비해 ?. 연산자 추가
  const articleWriterId = article?.userId;

  const showEditButtons = loggedInUserId && loggedInUserId === articleWriterId;

  return (
    <section className="py-5">
      <div className="container px-4 px-lg-5 my-5">
        <div className="row gx-4 gx-lg-5 align-items-start">
          <div className="col-md-6" style={{ padding: '15px' }}>
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
                {renderBadges()}
                <h1 className="fw-bolder display-5 ms-0.5 text-left" style={{ fontSize: '2rem' }}>
                  {article?.title || '로딩 중...'}
                </h1>
              </div>
            </div>
            <br />
            <div className="d-flex flex-wrap">
              <p style={{ fontSize: '19px', marginRight: '8px', color: '#212529' }}>물품종류 :</p>
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
                    color: '#ffffff',
                    backgroundColor: '#6c757d',
                    borderColor: '#6c757d',
                  }}
                >
                  {category}
                </span>
              ))}
            </div>
            <div className="fs-5 mb-3 d-flex justify-content-between">
              <p style={{ fontSize: '19px', color: '#212529' }}>작성자 : {article?.writer || '로딩 중...'}</p>

              <div>
                <span className="me-3" style={{ fontSize: '19px', color: '#212529' }}>조회수👀 :   100</span>
                <span style={{ fontSize: '19px', color: '#212529' }}>게시날짜 : {formattedDate}</span>
              </div>
            </div>
            <hr style={{ borderColor: '#212529', marginTop: '1rem', marginBottom: '1rem' }} />
            <div className="lead" style={{ color: '#666666', alignItems: 'flex-start', flexDirection: 'column', display: 'flex' }}>
              <div style={{ color: '#666666', fontWeight: '500' }}>
                {article?.content || '로딩 중...'}
              </div>
            </div>
            <div className="mt-3 d-flex justify-content-end">
              {showEditButtons && (
                <>
                  <button
                    className="btn btn-secondary me-2"
                    type="button"
                    onClick={handleUpdate}
                    style={{
                      backgroundColor: '#ffffff',
                      borderColor: '#999999',
                      color: '#999999',
                    }}
                  >
                    수정
                  </button>

                  <button
                    className="btn btn-danger"
                    type="button"
                    onClick={handleDelete}
                    style={{
                      backgroundColor: '#ffffff',
                      borderColor: '#999999',
                      color: '#999999',
                    }}
                  >
                    삭제
                  </button>
                </>
              )}
            </div>
            <hr style={{ borderColor: '#212529', marginTop: '1rem', marginBottom: '1rem' }} />
            <div className="d-flex justify-content-between align-items-center mb-3">
              <p className="mb-4" style={{ fontSize: '20px', color: '#212529' }}>
                <span className="me-2" style={{ fontSize: '19px', color: '#212529', fontWeight: 'normal' }}>기부지역 :</span> 서울시 송파구 백제고분로 777-7777
              </p>
              <button
                className="btn"
                type="button"
                style={{
                  backgroundColor: '#87CEEB',
                  borderColor: '#87CEEB',
                  color: '#ffffff',
                  padding: '6px',
                }}
              >
                <i className="bi-cart-fill me-1"></i>
                <span style={{ fontSize: '18px', display: 'inline-block', lineHeight: '1' }}>채팅하기</span>
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