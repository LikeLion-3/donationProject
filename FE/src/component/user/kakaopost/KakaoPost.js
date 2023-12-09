import React from 'react';
import {useDaumPostcodePopup} from "react-daum-postcode"
interface Props {
    scriptUrl?: string;
    setpSignForms: TYPE_setpSignForms;
}
function KakaoPost({ scriptUrl,callFunction }) {
    const open = useDaumPostcodePopup(scriptUrl);

    const handelComplete = (data) => {
        let fullAddress = data.address;
        let extraAddress = '';
        let localAddress = data.sido + ' ' + data.sigungu;

        if (data.addressType === 'R'){
            if (data.bname !== ''){
                extraAddress += data.bname;
            }
            if (data.buildingName !== ''){
                extraAddress += (extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName);
            }

            fullAddress = fullAddress.replace(localAddress, '');

            // setAddressObj({ //사용자가 선택한 주소 데이터를 SignUp 컴포넌트의 상태에 저장하는 역할
            //     areaAddresss: localAddress,
            //     townAddress: fullAddress += (extraAddress !== '' ? `($extraAddress)` : '')
            // });
            callFunction(localAddress, fullAddress += (extraAddress !== '' ? `(extraAddress)` : ''));
        }

    }

    const handleClick = () => {
        open({onComplete: handelComplete});
    }

    return <button type="button" onClick={handleClick}>주소찾기</button>
}

export default KakaoPost;