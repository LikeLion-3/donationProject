import { useEffect } from "react";
const { kakao } = window;

function KakaoMap({ data }) {
    useEffect(() => {
        kakao.maps.load(() => {
            const container = document.getElementById('map'); // 지도를 담을 영역의 DOM 레퍼런스
            const options = {
                center: new kakao.maps.LatLng(37.5069494959122, 127.055596615858), // 지도 중심 좌표
                level: 3
            };
            const map = new kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴

            if (data != null) {
                // 주소-좌표 변환 객체를 생성합니다
                var geocoder = new kakao.maps.services.Geocoder();

                // 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
                var bounds = new kakao.maps.LatLngBounds();

                data.content.forEach((res) => {
                    // 주소로 좌표를 검색합니다
                    geocoder.addressSearch(`${res.fullAddr}`, function (result, status) {

                        // 정상적으로 검색이 완료됐으면 
                        if (status === kakao.maps.services.Status.OK) {

                            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

                            // 결과값으로 받은 위치를 마커로 표시합니다
                            var marker = new kakao.maps.Marker({
                                map: map,
                                position: coords,
                                title: res.institutionName
                            });

                            // 인포윈도우로 장소에 대한 설명을 표시합니다
                            var infowindow = new kakao.maps.InfoWindow({
                                content: `<div style="width:150px;text-align:center;padding:6px 0;color:black;">${res.institutionName}</div>`
                            });

                            infowindow.open(map, marker);

                            // 마커 지정
                            marker.setMap(map);

                            // 범위 객체에 위치 입력
                            bounds.extend(coords);

                            // 지도 범위 재조정
                            map.setBounds(bounds);
                        }
                    })
                })
            } else {
                let markerPosition = new kakao.maps.LatLng(37.5069494959122, 127.055596615858);

                let marker = new kakao.maps.Marker({
                    position: markerPosition,
                    title: '애플트리타워'
                });

                var infowindow = new kakao.maps.InfoWindow({
                    content: '<div style="width:150px;text-align:center;padding:6px 0;color:black;">애플트리타워</div>'
                });

                infowindow.open(map, marker);

                map.setCenter(markerPosition);

                marker.setMap(map);
            }
        })
    }, [data]);

    return (
        <div id="map" style={{
            width: '100%',
            height: '550px',
            border: 0,
            borderRadius: 23
        }}>

        </div>
    )
}

export default KakaoMap;