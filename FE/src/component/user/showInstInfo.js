function ShowInstInfo({ sendDataToParent }) {
  const goToEditPage = () => {
    sendDataToParent('edit');
  }

  return (
    <div className="col-md-8 mt-5">
      <div className="card mb-3">
        <div className="card-body">
          <div className="row">
            <div className="col-sm-3">
              <h6 className="mb-0">기관이름</h6>
            </div>
            <div className="col-sm-9 text-secondary">
              Kenneth Valdez
            </div>
          </div>
          <hr />
          <div className="row">
            <div className="col-sm-3">
              <h6 className="mb-0">이메일</h6>
            </div>
            <div className="col-sm-9 text-secondary">
              fip@jukmuh.al
            </div>
          </div>
          <hr />
          <div className="row">
            <div className="col-sm-3">
              <h6 className="mb-0">사업자번호</h6>
            </div>
            <div className="col-sm-9 text-secondary">
              (239) 816-9029
            </div>
          </div>
          <hr />
          <div className="row">
            <div className="col-sm-3">
              <h6 className="mb-0">기관주소</h6>
            </div>
            <div className="col-sm-9 text-secondary">
              Bay Area, San Francisco, CA
            </div>
          </div>
          <hr />
          <div className="row">
            <div className="col-sm-12">
              <a className="btn btn-info" onClick={goToEditPage}>정보수정</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ShowInstInfo;