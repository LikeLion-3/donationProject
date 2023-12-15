import React, { useState, useEffect } from 'react';
import axios from 'axios';
import InfiniteScroll from "react-infinite-scroll-component";

function Comment({ communityId }) {
  const [comments, setComments] = useState([]);
  const [hasMore, setHasMore] = useState(true);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const limit = 10;

  // 날짜 포맷팅 함수
  const formatCreatedAt = (createdAt) => {
    return createdAt ? new Date(createdAt).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    }).replace(/ /g, '') : '로딩 중...';
  };

  const fetchComments = async () => {
    if (loading) return; // 이미 로딩 중이면 추가 호출 방지
    setLoading(true);

    try {
      const token = process.env.REACT_APP_TOKEN;
      const response = await axios.get(`http://localhost:8080/api/communities/${communityId}/comments?page=${page}&size=${limit}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      setComments(prev => [...prev, ...response.data.content]);
      setHasMore(!response.data.last);
      setPage(prevPage => prevPage + 1);
    } catch (error) {
      console.error('Error fetching comments:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    setComments([]);
    setPage(0);
    //fetchComments();
  }, [communityId]);

  return (
    <div className="container" style={{ maxWidth: '900px' }}>
      <InfiniteScroll
        dataLength={comments.length}
        next={fetchComments}
        hasMore={hasMore}
        loader={comments.length === 0 ? <p>댓글 없음</p> : <h4>Loading...</h4>} // 댓글이 없으면 로더를 표시하지 않음
        endMessage={
          <p style={{ textAlign: 'center' }}>
            <p>마지막 댓글</p>
          </p>
        }
      >
        {comments.map((comment, index) => (
          <div key={index} style={{ marginBottom: '15px' }}>
            <div style={{ display: 'flex', alignItems: 'center', marginBottom: '5px' }}>
              <p style={{ fontWeight: 'bold', margin: '0', marginRight: '10px', color: 'black' }}>{comment.writer}</p>
              <p style={{ margin: '0', color: 'black' }}>{comment.contents}</p>
            </div>
            <p style={{ fontSize: '0.8rem', color: 'grey', margin: '0' }}>
              {formatCreatedAt(comment.createdAt)}
            </p>
          </div>
        ))}
      </InfiniteScroll>
    </div>
  )
}

export default Comment;
