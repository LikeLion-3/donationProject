import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './PostButton.css'; // 스타일 파일을 import 합니다.

const PostButton = () => {
  const location = useLocation();

  // 현재 경로가 메인페이지 또는 "/donate" 페이지일 때만 버튼 렌더링
  const shouldRenderButton = location.pathname === '/' || location.pathname === '/donate';

  return shouldRenderButton ? (
    <Link to="/write" className="write-button" style={{ backgroundColor: '#ffa500', borderColor: '#ffa500' }}>
      게시글 작성
    </Link>
  ) : null;
};

export default PostButton;