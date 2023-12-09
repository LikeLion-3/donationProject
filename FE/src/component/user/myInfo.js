import React, { useState, useEffect } from 'react';
import axios from 'axios';
import EditMyInfo from './editIndivInfo';
import ShowIndivInfo from './showIndivInfo';
import ShowInstInfo from './showInstInfo';
import EditInstInfo from './editInstInfo';
import EditIndivInfo from './editIndivInfo';

function MyInfo(props) {
  const userType = props.userType;
  const [info, setInfo] = useState([]);
  //status: 게시글이 정보 보여주기 상태인지 수정 가능 상태인지
  const [status, setStatus] = useState("show");

  const switchPage = status => {
    setStatus(status);
  }

  if (userType == 'institution' && status == 'show') {
    return <ShowInstInfo sendDataToParent={switchPage} />
  } else if (userType == 'individual' && status == 'show') {
    return <ShowIndivInfo sendDataToParent={switchPage} />
  } else if (userType == 'institution' && status == 'edit') {
    return <EditInstInfo sendDataToParent={switchPage} />
  } else {
    return <EditIndivInfo sendDataToParent={switchPage} />
  }
}

export default MyInfo;