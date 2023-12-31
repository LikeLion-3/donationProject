import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getCookie } from '../../util/cookieUtil';
import Comment from './../../comment/Comment';
import jwtAxios from "../../util/jwtUtil"
import axios from "axios";
import { API_SERVER_HOST } from "../../util/jwtUtil"
import { CommunityCategoryStyles } from '../CommunityCategoryStyles';


function CommunityDetails() {
  const [community, setCommunity] = useState(null);
  const params = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    console.log("API 호출");

    if (loading) return;

    setLoading(true);
    axios.get(`${API_SERVER_HOST}/api/communities/${params.communityId}`)
      .then(response => {
        setCommunity(response.data);
      })
      .catch(error => {
        console.error('Error fetching community:', error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [params.communityId]);

  const handleUpdate = () => {
    navigate(`/community/update/${params.communityId}`, { state: { community } });
  };

  const handleDelete = async () => {
    if (window.confirm('정말로 삭제하시겠습니까?')) {
      try {
        await jwtAxios.delete(`${API_SERVER_HOST}/api/communities/${params.communityId}`);
        navigate('/community');
      } catch (error) {
        console.error('Error deleting community:', error);
      }
    }
  };

  const loggedInUserId = getCookie('user')?.id;
  const communityWriterId = community?.userId;

  const isWriter = loggedInUserId && loggedInUserId === communityWriterId;

  const formattedDate = community?.createdAt
    ? new Date(community.createdAt).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    }).replace(/ /g, '')
    : '로딩 중...';

  return (
    <section style={{ padding: '0rem 0' }} id="contact">
    {/* <section className="community-list-page-section" id="contact"> */}
      {/* 페이지 사이즈 */}
      <div className="container" style={{ maxWidth: '900px', border: '1px solid #E2E2E2', padding: '23px' }}>
        {/* 게시글 테두리 씹힘*/}
        <div className="row gx-4 gx-lg-5 align-items-center" style={{ borderBottom: '1px solid #E2E2E2' }}>
          <div>
            <div className='community-header1' style={{ borderBottom: '0px solid #E2E2E2', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              {/* 제목, 카테고리 */}
              <h1 className="fw-bolder">{community?.title || '로딩 중...'}</h1>
              <span
                style={{
                  ...CommunityCategoryStyles[community?.category],
                  padding: '0.5em 0.8em',
                  fontSize: '1rem',
                  borderRadius: '0.25rem',
                  border: `1px solid ${CommunityCategoryStyles[community?.category]?.backgroundColor}`,
                  color: 'white',
                  backgroundColor: CommunityCategoryStyles[community?.category]?.backgroundColor,
                  margin: '0 0.5em'
                }}
              >
                {community?.category || '로딩 중...'}
              </span>
            </div>
          </div>
          {/* 작성자, 조회수 */}
          <div className="d-flex justify-content-between" style={{ marginTop: '10px', marginBottom: '10px' }}>
            <p className="lead mb-0" style={{ color: 'black' }}>
              {community?.writer || '로딩 중...'}
            </p>
            <p className="lead">
              조회수 {community?.hit || 0} | {formattedDate}
            </p>
          </div>
        </div>
        {community?.images && community.images.map((image, index) => {
          return (
            <div className="col-md-12" key={index} style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '402px', marginTop: '30px' }}>
              <img
                src={image}
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
          <p className="lead" style={{ color: 'black', marginBottom: '20px', marginTop: '30px' }}>{community?.contents || '로딩 중...'}</p>
        </div>
        {/* 수정, 삭제 버튼 */}
        {isWriter && (
          <>
            <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '0px' }}>
              <button className="btn " onClick={handleUpdate} style={{ backgroundColor: '#FFFFFF', borderColor: '#999999', color: "#999999", marginRight: '10px' }}>수정</button>
              <button className="btn " onClick={handleDelete} style={{ backgroundColor: '#FFFFFF', borderColor: '#999999', color: "#999999" }}>삭제</button>
            </div>
          </>
        )}
      </div>
      <div style={{ marginTop: '20px' }}>
        <Comment communityId={params.communityId} />
      </div>
    </section >
  );
}

export default CommunityDetails;
