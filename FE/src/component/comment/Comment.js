import React, { useState, useEffect } from 'react';
import jwtAxios from '../util/jwtUtil';
import InfiniteScroll from "react-infinite-scroll-component";
import { getCookie } from '../util/cookieUtil';
import { API_SERVER_HOST } from "../util/jwtUtil"

function Comment({ communityId }) {
  const [comments, setComments] = useState([]);
  const [hasMore, setHasMore] = useState(true);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const limit = 10;
  const [editingCommentId, setEditingCommentId] = useState(null);
  const [editedContent, setEditedContent] = useState("");

  const loggedInUserId = getCookie('user')?.id;
  console.log("login User :" + loggedInUserId); // 1

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
    console.log("fetch Comments Page : " + page);
    try {

      const response = await jwtAxios.get(`${API_SERVER_HOST}/api/communities/${communityId}/comments?page=${page}&size=${limit}`);
      console.log('Server response:', response);

      // setComments(prev => [...prev, ...response.data.content]);
      // 디버깅 용
      setComments(prev => {
        const updatedComments = [...prev, ...response.data.content];
        console.log('Updated comments:', updatedComments); // Debugging log
        return updatedComments;
      });
      setHasMore(!response.data.last);
      setPage(prevPage => prevPage + 1);
    } catch (error) {
      console.error('Error fetching comments:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    setPage(0);
    setComments([]);
    fetchComments();
  }, [communityId]);

  const handleEditClick = (comment) => {
    setEditingCommentId(comment.id);
    setEditedContent(comment.contents);
  };

  const handleUpdate = async (communityId, commentId) => {
    if (editedContent.trim() === "") {
      alert("댓글이 비어 있습니다.");
      return;
    }
    if (window.confirm('변경 사항을 저장하시겠습니까?')) {
      try {
        const response = await jwtAxios.put(`${API_SERVER_HOST}/api/communities/${communityId}/comments/${commentId}`, {
          contents: editedContent
        });
        console.log(response);

        // 로컬 상태 업데이트
        setComments(comments => comments.map(comment => {
          if (comment.id === commentId) {
            return { ...comment, contents: editedContent };
          } else {
            return comment;
          }
        }));

        // 수정 모드 종료
        setEditingCommentId(null);
      } catch (error) {
        console.error('Error deleting community:', error);
      }
    }
  };

  const handleDelete = async (communityId, commentId) => {
    if (window.confirm('정말로 삭제하시겠습니까?')) {
      try {
        const response = await jwtAxios.delete(`${API_SERVER_HOST}/api/communities/${communityId}/comments/${commentId}`);
        console.log(response);
        setComments(comments => comments.filter(comment => comment.id !== commentId));
      } catch (error) {
        console.error('Error deleting community:', error);
      }
    }
  };

  return (
    <div className="container" style={{ maxWidth: '900px', overflow: 'hidden' }}>
      <style>
        {`
          .comment-container {
            position: relative;
            margin-bottom: 15px;
            padding-bottom: 5px;
            transition: border-bottom 0.3s;
          }
          .comment-container:hover {
            border-bottom: 1px solid #E2E2E2;
          }
          .date-and-buttons {
            display: flex;
            justify-content: space-between;
            align-items: center;
          }
          .comment-buttons {
            display: none;
            color: grey; /* Same color as the date */
          }
          .comment-container:hover .comment-buttons {
            display: block;
            border-bottom: 1px solid #E2E2E2;
          }
        `}
      </style>
      <InfiniteScroll
        dataLength={comments.length}
        next={fetchComments}
        hasMore={hasMore}
        loader={<h4>Loading...</h4>} // 댓글이 없으면 로더를 표시하지 않음
        endMessage={
          <p style={{ textAlign: 'center' }}>
            <p>마지막 댓글</p>
          </p>
        }
      >
        {comments.length === 0 ? (
          <p>댓글 없음</p>
        ) : (
          comments.map((comment, index) => (
            <div className="comment-container" key={index} style={{ marginBottom: '15px' }}>
              {/* 여기 className 적용하면 작성자, 내용 다른 줄 됨 */}
              <div style={{ display: 'flex', alignItems: 'center', marginBottom: '5px' }}>
                <p style={{ fontWeight: 'bold', margin: '0', marginRight: '10px', color: 'black' }}>{comment.writer}</p>
                {editingCommentId === comment.id ? (
                  <input
                    type="text"
                    value={editedContent}
                    onChange={(e) => setEditedContent(e.target.value)}
                    style={{ flex: 1, border: '1px solid #E2E2E2', outline: 'none' }}
                  />
                ) : (
                  <p style={{ margin: '0', color: 'black' }}>{comment.contents}</p>
                )}
              </div>
              <div className="date-and-buttons">
                <p style={{ fontSize: '0.8rem', color: 'grey', margin: '0' }}>
                  {formatCreatedAt(comment.createdAt)}
                </p>
                {loggedInUserId === comment.userId && (
                  <div className="comment-buttons">
                    {editingCommentId === comment.id ? (
                      <>
                        <button className="btn " onClick={() => setEditingCommentId(null)} style={{ backgroundColor: '#FFFFFF', borderColor: '#999999', color: "#999999", marginRight: '10px' }}>취소</button>
                        <button className="btn " onClick={() => handleUpdate(comment.communityId, comment.id)} style={{ backgroundColor: '#FFFFFF', borderColor: '#999999', color: "#999999" }}>저장</button>
                      </>) : (
                      <>
                        <button className="btn " onClick={() => handleEditClick(comment)} style={{ backgroundColor: '#FFFFFF', borderColor: '#999999', color: "#999999", marginRight: '10px' }}>수정</button>
                        <button className="btn " onClick={() => handleDelete(comment.communityId, comment.id)} style={{ backgroundColor: '#FFFFFF', borderColor: '#999999', color: "#999999" }}>삭제</button>
                      </>
                    )}
                  </div>
                )}
              </div>
            </div>
          ))
        )}
      </InfiniteScroll>
    </div>
  );
}
export default Comment;
