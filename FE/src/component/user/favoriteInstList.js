function FavoriteInstList() {
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
                <th scope="col" style={{ textAlign: "center" }}>기관이름</th>
                <td></td>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th style={{ textAlign: "center", verticalAlign: "middle" }} scope="row">1</th>
                <td style={{ textAlign: "center", verticalAlign: "middle" }}>멋쟁이사자노인회관</td>
                <td style={{ textAlign: "center" }}>
                  <button className='btn btn-danger'>취소</button>
                </td>
              </tr>
              <tr>
                <th style={{ textAlign: "center", verticalAlign: "middle" }} scope="row">2</th>
                <td style={{ textAlign: "center", verticalAlign: "middle" }}>멋쟁이사자청소년쉼터</td>
                <td style={{ textAlign: "center" }}>
                  <button className='btn btn-danger'>취소</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default FavoriteInstList;