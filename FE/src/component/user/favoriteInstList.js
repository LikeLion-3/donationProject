function FavoriteInstList() {
  return (
    <div className="col-md-8 mt-5">
      <div className="card mb-3">
        <div className="card-body">
          <table class="table table-hover">
            <colgroup>
              <col width="*" />
              <col width="80%" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col">번호</th>
                <th scope="col" style={{ textAlign: "center" }}>기관이름</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th style={{ verticalAlign: "middle" }} scope="row">1</th>
                <td style={{ verticalAlign: "middle" }}>멋쟁이사자노인회관</td>

              </tr>
              <tr>
                <th style={{ verticalAlign: "middle" }} scope="row">2</th>
                <td style={{ verticalAlign: "middle" }}>멋쟁이사자청소년쉼터</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default FavoriteInstList;