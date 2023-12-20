import axios from "axios";
import {useDaumPostcodePopup} from "react-daum-postcode";
import {postcodeScriptUrl} from "react-daum-postcode/lib/loadPostcode";
import styled from 'styled-components';

function DaumPost(props) {
    //클릭 시 수행될 팝업 생성 함수
    const open = useDaumPostcodePopup(postcodeScriptUrl);
    //주소 선택 시 처리될 함수
    const handleComplete = (data) => {
        let fullAddress = data.address;
        let extraAddress = '';
        let sidoAddress = data.sido;
        let sigunguAddress = data.sigungu;
        if (data.addressType === 'R') {
            if (data.bname !== '') {
                extraAddress += data.bname;
            }
            if (data.buildingName !== '') {
                extraAddress += (extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName);
            }
            //지역주소 제외 전체주소 치환
            fullAddress = fullAddress.replace(sidoAddress+ ' ' + sigunguAddress, '');
            //조건 판단 완료 후 지역 주소 및 상세주소 state 수정
            props.callFunction({
                provinceAddress: sidoAddress,
                cityAddress: sigunguAddress,
                townAddress: fullAddress += (extraAddress !== '' ? `(${extraAddress})` : '')
            });
            //주소 검색이 완료된 후 결과를 매개변수로 전달
            if (props.setLocationObj) {
                //getLocation(data.address);
            }
        }
    }
    //클릭 시 발생할 이벤트
    const handleClick = () => {
        open({onComplete: handleComplete});
    }
    return (
        <StButton
            type="button"
            btName="주소찾기"
            onClick={handleClick}>
            주소찾기
        </StButton>
    );
}

const StButton = styled.button`
  width: 40%;
  height: 20px;
  min-height: 40px;
  border-radius: 20px;
  border: none;
  cursor: default;
  // 글자
  font-size: 16px;
  font-weight: 600;
  text-align: center;
  letter-spacing: 0.0056em;
  line-height: 24px;
  cursor: pointer;
  background-color: #FABA96;
  color: white;
  `;

export default DaumPost;