function ShowIndivInfo({ sendDataToParent }) {
  const goToEditPage = () => {
    sendDataToParent('edit');
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
              <h6 className="mb-0">주소</h6>
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
  )
}

export default ShowIndivInfo;