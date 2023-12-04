const { kakao } = window;

function KakaoMapDefault() {
    kakao.maps.load(() => {
        const container = document.getElementById('map'); // 지도를 담을 영역의 DOM 레퍼런스
        const options = {
            center: new kakao.maps.LatLng(37.5069494959122, 127.055596615858), // 지도 중심 좌표
            level: 3
        };
        const map = new kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴

        let markerPosition = new kakao.maps.LatLng(37.5069494959122, 127.055596615858);

        let marker = new kakao.maps.Marker({
            position: markerPosition
        });

        marker.setMap(map);
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
}

export default KakaoMapDefault;