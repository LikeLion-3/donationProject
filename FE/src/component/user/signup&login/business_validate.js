import axios from 'axios';
import useToast from '../../hooks/useToast';

const NTS_KEY = process.env.REACT_APP_NTS_API;

const BusinessValidate = async (registerNum) => {
    const { showToast } = useToast();

    try {
        const response = await axios.post(
            `${NTS_KEY}`,
            {
                b_no: [registerNum]
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                    Accept: 'application/json'
                }
            }
        );

        return response.data;
    } catch (error) {
        showToast('사업자등록번호 유효성 검사에 실패했습니다.');
        throw new Error(error);
    }
};

export default BusinessValidate;