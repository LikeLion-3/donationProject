import { useState, useEffect } from "react";
const { kakao } = window;

function KakaoMap({ data }) {
    const [isMapLoaded, setIsMapLoaded] = useState(false);

    useEffect(() => {
        kakao.maps.load(() => {
            setIsMapLoaded(true);
        })
    }, []);

    useEffect(() => {
        if (isMapLoaded) {
            const container = document.getElementById('map'); // 지도를 담을 영역의 DOM 레퍼런스
            const options = {
                center: new kakao.maps.LatLng(37.5069494959122, 127.055596615858), // 지도 중심 좌표
                level: 3
            };
            const map = new kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴

            // 주소-좌표 변환 객체를 생성합니다
            var geocoder = new kakao.maps.services.Geocoder();

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
                    }
                    map.setCenter(coords);

                    marker.setMap(map);
                })
            })
        }
    }, [isMapLoaded, data.content])

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