function MyArticleList() {
  return (
    <div className="col-md-8 mt-5">
      <div className="card mb-3">
        <div className="card-body">
          <table class="table table-hover">
            <colgroup>
              <col width="*" />
              <col width="80%" />
              <col width="11%" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col" style={{ textAlign: "center" }}>번호</th>
                <th scope="col" style={{ textAlign: "center" }}>게시글 제목</th>
                <td></td>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th style={{ textAlign: "center", verticalAlign: "middle" }} scope="row">1</th>
                <td style={{ verticalAlign: "middle" }}>첫번째 게시글</td>
                <td style={{ textAlign: "center" }}>
                  <button className='btn btn-danger'>삭제</button>
                </td>
              </tr>
              <tr>
                <th style={{ textAlign: "center", verticalAlign: "middle" }} scope="row">2</th>
                <td style={{ verticalAlign: "middle" }}>두번째 게시글</td>
                <td style={{ textAlign: "center" }}>
                  <button className='btn btn-danger'>삭제</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default MyArticleList;