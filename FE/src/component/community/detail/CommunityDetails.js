import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import Comment from './../../comment/Comment';

function CommunityDetails() {
  const [article, setArticle] = useState(null);
  const params = useParams();

  useEffect(() => {
    console.log("communityId : " + params.communityId);
    axios.get(`/api/communities/${params.communityId}`)
      .then(response => {
        setArticle(response.data);
      })
      .catch(error => {
        console.error('Error fetching article:', error);
      });
  }, [params.communityId]);

  const formattedDate = article?.createdAt
    ? new Date(article.createdAt).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    }).replace(/ /g, '')
    : '로딩 중...';

  return (
    <section className="py-5">
      <div className="container px-4 px-lg-5 my-5">
        <div className="row gx-4 gx-lg-5 align-items-center">
          <div className="col-md-12">
            <h1 className="fw-bolder">{article?.title || '로딩 중...'}</h1>
            <p className="lead">
              작성자: {article?.writer || '로딩 중...'} |
              작성 날짜: {formattedDate} |
              조회수: {article?.hit || 0} |
              카테고리: {article?.category || '로딩 중...'}
            </p>
          </div>
          {article?.images && article.images.map((image, index) => (
            <div className="col-md-12" key={index}>
              <img
                src={`/images/${image}`}
                alt={`Image ${index}`}
                style={{ maxHeight: '400px', width: '100%', objectFit: 'cover' }}
              />
            </div>
          ))}
          <div className="col-md-12">
            <p className="lead">{article?.contents || '로딩 중...'}</p>
          </div>
        </div>
      </div>
      <Comment communityId={params.communityId} />
    </section>
  );
}

export default CommunityDetails;