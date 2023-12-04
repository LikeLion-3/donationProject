const { kakao } = window;

function KakaoMap({ data }) {
    kakao.maps.load(() => {
        const container = document.getElementById('map'); // 지도를 담을 영역의 DOM 레퍼런스
        const options = {
            center: new kakao.maps.LatLng(37.5069494959122, 127.055596615858), // 지도 중심 좌표
            level: 3
        };
        const map = new kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴

        // data가 array 로 들어올 것.
        data.forEach((el) => {
            // 주소-좌표 변환 객체를 생성합니다
            var geocoder = new kakao.maps.services.Geocoder();

            // 주소로 좌표를 검색합니다
            geocoder.addressSearch(`${el.fullAddr}`, function (result, status) {

                // 정상적으로 검색이 완료됐으면 
                if (status === kakao.maps.services.Status.OK) {

                    var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

                    // 결과값으로 받은 위치를 마커로 표시합니다
                    var marker = new kakao.maps.Marker({
                        map: map,
                        position: coords,
                        title: el.details
                    });

                    // 인포윈도우로 장소에 대한 설명을 표시합니다
                    var infowindow = new kakao.maps.InfoWindow({
                        content: '<div style="width:150px;text-align:center;padding:6px 0;">우리회사</div>'
                    });
                    infowindow.open(map, marker);

                    // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
                    map.setCenter(coords);
                } else {
                    coords = new kakao.maps.LatLng(
                        37.5069494959122, 127.055596615858
                    );

                    marker = new kakao.maps.Marker({
                        position: coords
                    });
                }

                marker.setMap(map);

                // // api 불러온 결과 - List 형태의 결과를 받아와서 forEach 로 출어준다.
                // data.forEach((el) => {
                //     // 마커를 생성합니다
                //     new kakao.maps.Marker({
                //       //마커가 표시 될 지도
                //       map: map,
                //       //마커가 표시 될 위치
                //       position: new kakao.maps.LatLng(el.lat, el.lng),
                //       //마커에 hover시 나타날 title
                //       title: el.title,
                //     });
                //   });
            })
        })

        return (
            <div id="map" style={{
                width: '100%',
                height: '550px',
                border: 0,
                borderRadius: 23
            }}>

            </div>
        )
    })
}

export default KakaoMap;