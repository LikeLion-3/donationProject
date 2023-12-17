// CommunityDetails.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import Comment from './../../comment/Comment';
function CommunityDetails() {
  const [community, setCommunity] = useState(null);
  const params = useParams();

  useEffect(() => {
    const token = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImJvZHkiOnsiZW1haWwiOiJ0ZXN0QHRlc3QuY29tIn0sImV4cCI6MTcwMjk2Mzc3MH0.y93O_SP_YMaJhx7h0obQotaLIK0qSD-k0_bm1iagSeTT88tn1y89QQXiHUx_uOdSnWAp0klMxgHpMbLCAgA9kw';
    axios.get(`/api/communities/${params.communityId}`, {
      headers: {
        Authorization: `Bearer ${token}` // 토큰을 Authorization 헤더에 추가
      }

    })
      .then(response => {
        setCommunity(response.data);
      })
      .catch(error => {
        console.error('Error fetching community:', error);
      });
  }, [params.communityId]);

  const formattedDate = community?.createdAt
    ? new Date(community.createdAt).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    }).replace(/ /g, '')
    : '로딩 중...';

  return (
    <section className="community-list-page-section" id="contact">
      {/* 페이지 사이즈 */}
      <div className="container" style={{ maxWidth: '900px' }}>
        {/* 게시글 */}
        <div className="row gx-4 gx-lg-5 align-items-center" style={{ border: '2px solid #E2E2E2' }}>
          {/* 제목, 작성자 */}
          <div className='community-header' style={{ border: '0px solid #E2E2E2', borderBottom: '2px solid #E2E2E2' }}>
            <div className="col-md-12" style={{ marginTop: '10px' }}>
              <h1 className="fw-bolder">{community?.title || '로딩 중...'}</h1>
              <div className="d-flex justify-content-between" style={{ marginBottom: '10px' }}>
                <p className="lead mb-0" style={{ color: 'black' }}>
                  {community?.writer || '로딩 중...'}
                </p>
                <p className="lead mb-0">
                  조회수 {community?.hit || 0} |
                  {community?.category || '로딩 중...'} |
                  {formattedDate}
                </p>
              </div>
            </div>
          </div>
          {community?.images && community.images.map((image, index) => {
            const imgName = image.split('/').pop();
            return (
              <div className="col-md-12" key={index} style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '402px', marginTop: '30px', marginBottom: '30px' }}>
                <img
                  src={`/images/${imgName}`} // 이미지 경로
                  alt={`${index}`}
                  style={{
                    maxWidth: '600px',
                    maxHeight: '402px',
                    width: 'auto',
                    height: 'auto',
                    objectFit: community.images.length === 1 && new Image().src === image ? 'contain' : 'cover'
                  }}
                />
              </div>
            );
          })}
          <div className="col-md-12">
            <p className="lead" style={{ color: 'black', marginBottom:'20px' }}>{community?.contents || '로딩 중...'}</p>
          </div>
        </div>
        <div style={{ marginTop: '20px' }}>
          <Comment communityId={params.communityId} />
        </div>
      </div>
    </section>
  );
}

export default CommunityDetails;
