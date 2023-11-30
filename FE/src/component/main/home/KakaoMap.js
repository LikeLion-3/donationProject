import { useEffect } from "react";

const { kakao } = window;

function KakaoMap() {
    kakao.maps.load(() => {
        const container = document.getElementById('map'); // 지도를 담을 영역의 DOM 레퍼런스
        const options = {
            center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도 중심 좌표
            level: 3
        };
        const map = new kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴

        let markerPosition = new kakao.maps.LatLng(
            33.450701, 126.570667
        );

        let marker = new kakao.maps.Marker({
            position: markerPosition
        });

        marker.setMap(map);

        // api 불러온 결과 - List 형태의 결과를 받아와서 forEach 로 출어준다.
        // markerdata.forEach((el) => {
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

    return(
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