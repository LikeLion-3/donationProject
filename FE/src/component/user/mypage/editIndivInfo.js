import React, { useState, useEffect } from 'react';
import axios from 'axios';

function EditIndivInfo({ sendDataToParent }) {
  const save = () => {

  }
  const goToInfoPage = () => {
    sendDataToParent('show');
  }

  return (
    <div className="col-md-8 mt-5">
      <div className="card mb-3">
        <div className="card-body">
          <div className="row">
            <div className="col-sm-3">
              <h6 className="mb-0">닉네임</h6>
            </div>
            <div className="col-sm-9 text-secondary">
              <input type='text' value="기존값 불러오기"></input>
            </div>
          </div>
          <hr />
          <div className="row">
            <div className="col-sm-3">
              <h6 className="mb-0">이메일</h6>
            </div>
            <div className="col-sm-9 text-secondary">
              <input type='text' value="기존값 불러오기"></input>
            </div>
          </div>
          <hr />
          <div className="row">
            <div className="col-sm-3">
              <h6 className="mb-0">주소</h6>
            </div>
            <div className="col-sm-9 text-secondary">
              <input type='text' value="기존값 불러오기"></input>
            </div>
          </div>
          <hr />
          <div className="row">
            <div className="col-sm-12">
              <a className="btn btn-info" onClick={save}>저장</a>
              <a className="btn btn-secondary" onClick={goToInfoPage}>취소</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default EditIndivInfo;