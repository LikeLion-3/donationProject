import { Link } from "react-router-dom";
import './mypage.css';
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import MyInfo from "./myInfo";
import MyArticleList from "./myArticleList";
import FavoriteInstList from "./favoriteInstList";
import MyChatRoom from "../myChatRoom";
import EditMyInfo from "./editIndivInfo";

function MyPage() {
  const [currentPage, setCurrentPage] = useState('myInfo');
  //TODO: 현재 로그인한 user type 가져와서 주입해야 함
  const [userType, setUserType] = useState('individual');

  const changeSelectedItem = (e, selectedItem) => {
    const siblings = getSiblings(e.target);
    siblings.forEach(sibling => {
      sibling.style.backgroundColor = '#FFFFFF';
    });
    e.target.style.backgroundColor = "#FAAC58";
    setCurrentPage(selectedItem);
  };

  //현제 노드의 형제 요소들을 추출하는 메소드
  function getSiblings(currentNode) {
    const slblings = [];

    // 부모 노드가 없는 경우 현재 노드를 반환
    if (!currentNode.parentNode) {
      return currentNode;
    }

    // 1. 부모 노드를 접근합니다.
    let parentNode = currentNode.parentNode;

    // 2. 부모 노드의 첫 번째 자식 노드를 가져옵니다.
    let silblingNode = parentNode.firstChild;

    while (silblingNode) {
      // 기존 노드가 아닌 경우 배열에 추가합니다.
      if (silblingNode.nodeType === 1 && silblingNode !== currentNode) {
        slblings.push(silblingNode);
      }
      // 다음 노드를 접근합니다.
      silblingNode = silblingNode.nextElementSibling;
    }

    // 형제 노드가 담긴 배열을 반환합니다.
    return slblings;
  }
  // 출처: https://developer-talk.tistory.com/855 [DevStory:티스토리]

  const renderPage = () => {
    switch (currentPage) {
      case 'myInfo':
        return <MyInfo userType={userType} />
      case 'myArticleList':
        return <MyArticleList />;
      case 'favoriteInstList':
        return <FavoriteInstList />;
      case 'myChatRoom':
        return <MyChatRoom />
      case 'editMyInfo':
        return <EditMyInfo />
      default:
        return <MyInfo />
    }
  };

  return (
    <div className="container mt-5">
      <div className="main-body">
        <div className="row gutters-sm mb-4">
          <div className="col-md-4 mt-5">
            <div className="card">
              <div className="card-body">
                <div className="d-flex flex-column align-items-center text-center">
                  <div className="mt-3">
                    <h4>사용자 닉네임</h4>
                    <p className="text-secondary mb-1">기타 사용자 설명</p>
                    <p className="text-muted font-size-sm">사용자 주소</p>
                    {/* 테스트용 버튼입니다.  */}
                    <button className="btn btn-primary m-1" onClick={() => { setUserType('individual'); }}>개인</button>
                    <button className="btn btn-primary" onClick={() => { setUserType('institution'); }}>기관</button>
                  </div>
                </div>
              </div>
            </div>
            <div className="card mt-4">
              <ul className="list-group list-group-flush">
                <li className="list-group-item d-flex justify-content-center align-items-center flex-wrap" style={{ backgroundColor: '#FAAC58' }} onClick={e => { changeSelectedItem(e, 'myInfo') }}>
                  <span onClick={(e) => { e.stopPropagation(); }}>내정보</span>
                </li>
                <li className="list-group-item d-flex justify-content-center align-items-center flex-wrap" onClick={e => { changeSelectedItem(e, 'myArticleList') }}>
                  <span onClick={(e) => { e.stopPropagation(); }}>내 게시글 조회</span>
                </li>
                <li className="list-group-item d-flex justify-content-center align-items-center flex-wrap" onClick={e => { changeSelectedItem(e, 'myChatRoom') }}>
                  <span onClick={(e) => { e.stopPropagation(); }}>내 채팅방 조회</span>
                </li>
                <li className="list-group-item d-flex justify-content-center align-items-center flex-wrap" onClick={e => { changeSelectedItem(e, 'favoriteInstList') }}>
                  <sapn onClick={(e) => { e.stopPropagation(); }}>즐겨찾기 기관 목록 조회</sapn>
                </li>
              </ul>
            </div>
          </div>
          {renderPage()}
        </div>
      </div>
    </div >
  )
}

export default MyPage;