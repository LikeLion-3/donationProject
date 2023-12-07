import { Link } from "react-router-dom";
import '../../assets/css/mypage.css';
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import MyInfo from "./myInfo";
import MyArticleList from "./myArticleList";
import FavoriteInstList from "./favoriteInstList";
import MyChatRoom from "./myChatRoom";

function MyPage() {
  const [currentPage, setCurrentPage] = useState('myInfo');

  const myInfoOnclick = e => {
    e.stopPropagation();
    e.nativeEvent.stopImmediatePropagation();
    getSiblings(e.target).forEach(e => {
      e.style = '#FFFFFF';
    });
    e.target.style.backgroundColor = "#FAAC58";
    setCurrentPage('myInfo');
  }
  const myArticleListOnClick = e => {
    e.stopPropagation();
    getSiblings(e.target).forEach(e => {
      e.style = '#FFFFFF';
    });
    e.target.style.backgroundColor = "#FAAC58";
    setCurrentPage('myArticleList');
  }
  const myChatRoomOnclick = e => {
    e.stopPropagation();
    getSiblings(e.target).forEach(e => {
      e.style = '#FFFFFF';
    });
    e.target.style.backgroundColor = "#FAAC58";
    setCurrentPage('myChatRoom');
  }
  const favoriteInstListOnclick = e => {
    e.stopPropagation();
    getSiblings(e.target).forEach(e => {
      e.style = '#FFFFFF';
    });
    e.target.style.backgroundColor = "#FAAC58";
    setCurrentPage('favoriteInstList');
  }

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
        return <MyInfo />
      case 'myArticleList':
        return <MyArticleList />;
      case 'favoriteInstList':
        return <FavoriteInstList />;
      case 'myChatRoom':
        return <MyChatRoom />
      default:
        return <MyInfo />
    }
  };

  return (
    <div className="container mt-5">
      <div className="main-body">
        {/* Breadcrumb */}
        {/* <nav aria-label="breadcrumb" className="main-breadcrumb">
          <ol className="breadcrumb">
            <li className="breadcrumb-item"><a href="index.html">Home</a></li>
            <li className="breadcrumb-item"><a href="javascript:void(0)">User</a></li>
            <li className="breadcrumb-item active" aria-current="page">User Profile</li>
          </ol>
        </nav> */}
        {/* /Breadcrumb */}
        <div className="row gutters-sm mb-4">
          <div className="col-md-4 mt-5">
            <div className="card">
              <div className="card-body">
                <div className="d-flex flex-column align-items-center text-center">
                  <Link to="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Admin" className="rounded-circle" width={150} />
                  <div className="mt-3">
                    <h4>사용자 닉네임</h4>
                    <p className="text-secondary mb-1">기타 사용자 설명</p>
                    <p className="text-muted font-size-sm">사용자 주소</p>
                  </div>
                </div>
              </div>
            </div>
            <div className="card mt-4">
              <ul className="list-group list-group-flush">
                <li className="list-group-item d-flex justify-content-center align-items-center flex-wrap" onClick={myInfoOnclick}>
                  <span onClick={(e) => { e.stopPropagation(); }}>내정보</span>
                  {/* <h6 className="mb-0"><svg xmlns="http://www.w3.org/2000/svg" width={24} height={24} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round" className="feather feather-globe mr-2 icon-inline"><circle cx={12} cy={12} r={10} /><line x1={2} y1={12} x2={22} y2={12} /><path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z" /></svg>Website</h6>
                  <span className="text-secondary">https://bootdey.com</span> */}
                </li>
                <li className="list-group-item d-flex justify-content-center align-items-center flex-wrap" onClick={myArticleListOnClick}>
                  <span onClick={(e) => { e.stopPropagation(); }}>내 게시글 조회</span>
                  {/* <h6 className="mb-0"><svg xmlns="http://www.w3.org/2000/svg" width={24} height={24} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round" className="feather feather-github mr-2 icon-inline"><path d="M9 19c-5 1.5-5-2.5-7-3m14 6v-3.87a3.37 3.37 0 0 0-.94-2.61c3.14-.35 6.44-1.54 6.44-7A5.44 5.44 0 0 0 20 4.77 5.07 5.07 0 0 0 19.91 1S18.73.65 16 2.48a13.38 13.38 0 0 0-7 0C6.27.65 5.09 1 5.09 1A5.07 5.07 0 0 0 5 4.77a5.44 5.44 0 0 0-1.5 3.78c0 5.42 3.3 6.61 6.44 7A3.37 3.37 0 0 0 9 18.13V22" /></svg>Github</h6>
                  <span className="text-secondary">bootdey</span> */}
                </li>
                <li className="list-group-item d-flex justify-content-center align-items-center flex-wrap" onClick={myChatRoomOnclick}>
                  <span onClick={(e) => { e.stopPropagation(); }}>내 채팅방 조회</span>
                  {/* <h6 className="mb-0"><svg xmlns="http://www.w3.org/2000/svg" width={24} height={24} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round" className="feather feather-twitter mr-2 icon-inline text-info"><path d="M23 3a10.9 10.9 0 0 1-3.14 1.53 4.48 4.48 0 0 0-7.86 3v1A10.66 10.66 0 0 1 3 4s-4 9 5 13a11.64 11.64 0 0 1-7 2c9 5 20 0 20-11.5a4.5 4.5 0 0 0-.08-.83A7.72 7.72 0 0 0 23 3z" /></svg>Twitter</h6>
                  <span className="text-secondary">@bootdey</span> */}
                </li>
                <li className="list-group-item d-flex justify-content-center align-items-center flex-wrap" onClick={favoriteInstListOnclick}>
                  <sapn onClick={(e) => { e.stopPropagation(); }}>즐겨찾기 기관 목록 조회</sapn>
                  {/* <h6 className="mb-0"><svg xmlns="http://www.w3.org/2000/svg" width={24} height={24} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round" className="feather feather-instagram mr-2 icon-inline text-danger"><rect x={2} y={2} width={20} height={20} rx={5} ry={5} /><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z" /><line x1="17.5" y1="6.5" x2="17.51" y2="6.5" /></svg>Instagram</h6>
                  <span className="text-secondary">bootdey</span> */}
                </li>
              </ul>
            </div>
          </div>
          {renderPage()}
          {/* <div className="row gutters-sm">
              <div className="col-sm-6 mb-3">
                <div className="card h-100">
                  <div className="card-body">
                    <h6 className="d-flex align-items-center mb-3"><i className="material-icons text-info mr-2">assignment</i>Project Status</h6>
                    <small>Web Design</small>
                    <div className="progress mb-3" style={{ height: 5 }}>
                      <div className="progress-bar bg-primary" role="progressbar" style={{ width: '80%' }} aria-valuenow={80} aria-valuemin={0} aria-valuemax={100} />
                    </div>
                    <small>Website Markup</small>
                    <div className="progress mb-3" style={{ height: 5 }}>
                      <div className="progress-bar bg-primary" role="progressbar" style={{ width: '72%' }} aria-valuenow={72} aria-valuemin={0} aria-valuemax={100} />
                    </div>
                    <small>One Page</small>
                    <div className="progress mb-3" style={{ height: 5 }}>
                      <div className="progress-bar bg-primary" role="progressbar" style={{ width: '89%' }} aria-valuenow={89} aria-valuemin={0} aria-valuemax={100} />
                    </div>
                    <small>Mobile Template</small>
                    <div className="progress mb-3" style={{ height: 5 }}>
                      <div className="progress-bar bg-primary" role="progressbar" style={{ width: '55%' }} aria-valuenow={55} aria-valuemin={0} aria-valuemax={100} />
                    </div>
                    <small>Backend API</small>
                    <div className="progress mb-3" style={{ height: 5 }}>
                      <div className="progress-bar bg-primary" role="progressbar" style={{ width: '66%' }} aria-valuenow={66} aria-valuemin={0} aria-valuemax={100} />
                    </div>
                  </div>
                </div>
              </div>
            </div> */}
        </div>
      </div>
    </div >
  )
}

export default MyPage;