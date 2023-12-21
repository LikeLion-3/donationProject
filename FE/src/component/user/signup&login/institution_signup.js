import React, {useState, useEffect, useRef} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import styled from 'styled-components';
import Logo from './logo.png';
import {useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
import * as yup from 'yup';
import useAuthAPI from './authApi';
import useToast from '../../hooks/useToast';
import DaumPost from '../daumPost';
import TermsModal from './terms';
import BusinessValidate from './business_validate'

const schema = yup.object().shape({
    email: yup
        .string()
        .email('올바른 이메일을 입력해주세요.')
        .required('이메일을 인증해주세요'),
    password: yup
        .string()
        .min(8, '비밀번호는 최소 8자리입니다')
        .max(16, '비밀번호는 최대 16자리입니다')
        .matches(
            /^(?=.*[a-zA-Z])(?=.*[0-9])[^\s]*$/,
            '공백을 제외한 영문, 숫자를 포함하여 입력해주세요',
        )
        .required('비밀번호를 입력해주세요'),
    registerNum: yup
        .string()
        .matches(/^\d{10}$/,'공백을 제외한 숫자만 입력해주세요')
        .required('사업자등록번호를 입력해주세요'),
    agreeTerms: yup.bool().oneOf([true], '서비스 이용약관에 동의해주세요').required()
});

const Institution_signup = () => {
    const {showToast} = useToast();
    // 이메일 유효 여부
    const [emailValid, setEmailValid] = useState(false);
    // 사업자등록번호 유효 여부
    const [registerNumValid, setRegisterNumValid] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const navigate = useNavigate();
    const uploadRef = useRef();

    const {
        register,
        handleSubmit,
        getValues,
        setError,
        clearErrors,
        formState: {errors, isValid},
    } = useForm({
        resolver: yupResolver(schema),
        mode: 'onChange',
    });

    const authAPI = useAuthAPI();

    const checkEmailDuplicated = async () => {   // 이메일 중복확인
        const data = getValues('email');

        // 이메일칸이 비어 있는지 확인
        if (!data || data.trim() === '') {
            showToast('이메일을 입력해주세요.', 'warning');
            return;
        }

        try {
            // 서버에 이메일 중복 확인 요청 보내기
            const res = await authAPI.emailValidCheck(data);

            if (res.data) {
                showToast('유효한 이메일입니다.', 'success');
                // 이메일 중복 여부를 상태로 저장
                setEmailValid(true);
            } else {
                showToast('이미 사용중인 이메일입니다.', 'warning');
                // 이메일 중복 여부를 상태로 저장
                setEmailValid(false);
            }
        } catch (error) {
            showToast('에러가 발생했습니다.', 'error');
        }
    };

    const handleBusinessValidation = async () => {
        const data = getValues('registerNum');

        // 사업자등록번호 칸이 비어 있는지 확인
        if (!data || data.trim() === '') {
            showToast('사업자등록번호를 입력해주세요.', 'warning');
            return;
        }

        try {
            // 국세청API로 사업자등록번호 진위확인 요청 보내기
            const res = await BusinessValidate(data);
            // 납세자상태(명칭) 01: 계속사업자,02: 휴업자, 03: 폐업자
            if (res.data && res.data.length > 0 && res.data[0].b_stt_cd === '01') {
                showToast('사업자등록번호가 확인되었습니다.', 'success')
                setRegisterNumValid(true);
            } else if (res.data && res.data.length > 0) {
                showToast('유효하지 않은 사업자입니다.', 'warning');
                setRegisterNumValid(false);
            } else {
                showToast('사업자등록번호 유효성 검사에 실패했습니다.', 'error');
                setRegisterNumValid(false);
            }
        } catch (error) {
            showToast('에러가 발생했습니다.', 'error')
        }
    }

    const [isDaumPostOpen, setDaumPostOpen] = useState(false);
    const [addressObj, setAddressObj] = useState({});

    const [address1, setAddress1] = useState("");
    const [address2, setAddress2] = useState("");
    const [address3, setAddress3] = useState("");
    const handleOpenDaumPost = () => {
        alert(true);
        setDaumPostOpen(true);
    };

    const handleCloseDaumPost = (data) => {
        console.log(data);
        setDaumPostOpen(false);
    };

    const callFunction = (obj) => {
        setAddress1(obj.provinceAddress);
        setAddress2(obj.cityAddress);
        setAddress3(obj.townAddress);
    }

    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    const onSubmit = async (e) => {

        // FormData 객체 생성
        const formData = new FormData();

        // 'dto' 파트 - JSON 데이터를 문자열로 변환하여 추가
        const {email, password, institutionName, registerNum, agreeTerms} = e;
        const dtoData = JSON.stringify({
            email: e.email,
            password: e.password,
            institutionName: e.institutionName,
            registerNum: e.registerNum,
            fullAddr: `${address1} ${address2} ${address3}`,
            sdName: address1,
            sggName: address2,
            details: address3
        });

        formData.append('dto', new Blob([dtoData], {type: "application/json"}));

        // 'file' 파트 - 이미지 파일
        const file = uploadRef.current.files[0];
        formData.append("file", file);

        // 유효성 검사
        if (!emailValid) {
            showToast('이메일 중복체크를 해주세요.', 'error');
            return;
        }
        if (!registerNumValid) {
            showToast('사업자등록번호를 확인해주세요.', 'error');
            return;
        }
        if (!uploadRef.current?.files[0]) {
            showToast('사업자등록증 이미지를 업로드해주세요.', 'error');
            return;
        }
        if (!getValues('agreeTerms')) {
            showToast('서비스 이용약관에 동의해주세요.', 'error');
            return;
        }

        try {
            const res = await authAPI.SignUpInstitution(formData);
            if (res.status === 200 || res.status === 201) {
                showToast(
                    '이메일로 인증링크가 발송되었습니다. 링크로 인증시 회원가입이 완료됩니다.',
                    'success'
                );
                navigate('/user/login');
            }
        } catch (error) {
            showToast('회원가입에 실패했습니다. 관리자에게 문의해주세요', 'error');
        }
    };

    const onError = (errors, e) => {
        // 폼 제출 실패 시
        showToast('모든 항목을 입력하고 중복체크, 조회, 사진등록, 약관동의해야 회원가입이 가능합니다.', 'error');
    };

    useEffect(() => {
        const email = getValues('email');
        // 이메일이 비어 있는지 확인
        if (!email || email.trim() === '') {
            return;
        }
        if (!emailValid) {
            setError('email', {type: 'custom', message: '사용중인 이메일입니다.'});
        } else {
            clearErrors('email', {type: 'custom'});
        }
    }, [emailValid]);

    useEffect(() => {
        const registerNum = getValues('registerNum');
        if (!registerNum || registerNum.trim() === '') {
            return;
        }
        if (!registerNumValid) {
            setError('registerNum', {type: 'custom', message: '유효하지 않은 사업자입니다.'});
        } else {
            clearErrors('registerNum', {type: 'custom'});
        }
    }, [registerNumValid]);

    return (
        <StLayout>
            <StContainer>
                <StRegister>
                    <Top>
                        <StLogo>
                            <img src={Logo} alt="logo"/>
                        </StLogo>
                    </Top>
                    <br/><br/><br/>
                    <Main>
                        <form onSubmit={handleSubmit(onSubmit, onError)}>
                            <Title>복지기관 회원가입</Title>
                            <StLabel>
                                <label>이메일</label>
                            </StLabel>
                            <InputBox>
                                <StInput
                                    id="email"
                                    name="email"
                                    placeholder="이메일을 입력해주세요."
                                    className={errors.email ? 'error' : ''}
                                    {...register('email', {required: true})}
                                />
                                <StButton
                                    type="button"
                                    disabled={!getValues('email') || errors.email}
                                    className="emailBtn"
                                    onClick={checkEmailDuplicated}
                                >
                                    중복확인
                                </StButton>
                            </InputBox>
                            <Typography>
                                {errors.emailCheck?.message || errors.email?.message}
                            </Typography>
                            <StLabel>
                                <label>비밀번호</label>
                            </StLabel>
                            <StInput
                                type="password"
                                id="password"
                                name="password"
                                placeholder="영문,숫자를 포함한 8~16자로 입력해주세요."
                                className={errors.password ? 'error' : ''}
                                {...register('password', {required: true})}
                            />
                            <Typography>{errors.password?.message}</Typography>
                            <StLabel>
                                <label>기관명</label>
                            </StLabel>
                            <InputBox>
                                <StInput
                                    id="institutionName"
                                    name="institutionName"
                                    placeholder="기관이름을 입력해주세요."
                                    className={errors.institutionName ? 'error' : ''}
                                    {...register('institutionName', {required: true})}
                                />
                            </InputBox>
                            <StLabel>
                                <label>사업자등록번호</label>
                            </StLabel>
                            <InputBox>
                                <StInput
                                    id="registerNum"
                                    name="registerNum"
                                    placeholder="숫자로만 10자리를 입력해주세요."
                                    className={errors.registerNum ? 'error' : ''}
                                    {...register('registerNum', {required: true})}
                                />
                                <StButton
                                    type="button"
                                    disabled={!getValues('registerNum') || errors.registerNum}
                                    className="registerNumBtn"
                                    onClick={handleBusinessValidation}
                                >
                                    조회
                                </StButton>
                            </InputBox>
                            <Typography>{errors.registerNum?.message}</Typography>
                            <StLabel>
                                <label htmlFor="uploadImage">사업자등록증 이미지 추가</label>
                            </StLabel>
                            <InputBox>
                                <StInput
                                    ref={uploadRef}
                                    name="uploadImage"
                                    className="uploadImage"
                                    type={'file'}
                                    multiple={false}
                                />
                            </InputBox>
                            <AddressGroup>
                                <StAddressLabel>
                                    <label>시/도</label>
                                </StAddressLabel>
                                <AddressBox>
                                    <StAddress
                                        name="address1"
                                        id="address1"
                                        placeholder="기관 주소를 등록해주세요."
                                        value={address1}
                                        {...register('address1', {required: true})}
                                        disabled  // 입력 상자 비활성화
                                    />
                                    <DaumPost close={handleCloseDaumPost} callFunction={callFunction}/>
                                </AddressBox>
                                <StAddressLabel>
                                    <label>시/군/구</label>
                                </StAddressLabel>
                                <AddressBox>
                                    <StAddress
                                        name="address2"
                                        id="address2"
                                        value={address2}
                                        {...register('address2', {required: true})}
                                        disabled  // 입력 상자 비활성화
                                    />
                                </AddressBox>
                                <StAddressLabel>
                                    <label>상세주소</label>
                                </StAddressLabel>
                                <AddressBox>
                                    <StAddress
                                        name="address3"
                                        id="address3"
                                        value={address3}
                                        {...register('address3', {required: true})}
                                        disabled  // 입력 상자 비활성화
                                    />
                                </AddressBox>
                            </AddressGroup>

                            <StButton
                                disabled={!isValid}
                                type="submit"
                                className="registerBtn"
                            >
                                회원가입
                            </StButton>
                            <input
                                className="form-check-input me-2"
                                type="checkbox"
                                id="agreeTerms"
                                name="agreeTerms"
                                value="1"
                                {...register('agreeTerms', {required: true})} />
                            <label className="form-check-label" htmlFor="agreeTerms">
                                <a href="#none" className="text-body" onClick={openModal}>
                                    <u>서비스이용약관 및 개인정보처리방침</u></a>에 동의합니다
                            </label>
                            {isModalOpen && <TermsModal closeModal={closeModal} />}
                            <Link to="/user/login">
                                <Caption>이미 회원이신가요? 로그인</Caption>
                            </Link>
                        </form>
                    </Main>
                </StRegister>
            </StContainer>
        </StLayout>
    );
};

const StLayout = styled.div`
  width: 100%;
  height: 100%;
  background-color: #ffffff;
`;

const StContainer = styled.div`
  max-width: 500px;
  height: 100%;
  width: 100%;
  margin: 0 auto;
  display: flex;
`;

const StRegister = styled.div`
  border-radius: 5px;
  width: 100%;
  max-height: 992px;
  overflow: auto;
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
  // #FFF2CC;
  padding: 10px;
  justify-content: space-between;

  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }
`;

const Top = styled.div`
  padding: 10px;
  height: 10px;
  width: 60%;
`;

const StLogo = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
`;

const Main = styled.div`
  overflow: auto;
  position: relative;
  padding: 20px;
  min-height: 850px;
`;

const Title = styled.h2`
  font-weight: 700;
  text-align: center;
  line-height: 38px;
`;

const StLabel = styled.div`
  margin: 15px 0px 7px;

  label {
    color: #888;
    font-weight: 600;
    text-align: left;
    letter-spacing: 0.0145em;
    line-height: 20px;
  }
`;

const InputBox = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: baseline;
`;

const StInput = styled.input`
  width: 100%;
  height: 50px;
  min-height: 50px;
  padding: 0px 5px;
  margin-bottom: 5px;
  outline: none;
  background-color: transparent;
  border-radius: 5px;
  font-size: 16px;
  font-weight: 400;

  &:focus {
    border: 1px solid #36f;
  }

  &.error {
    border: 1px solid #fe415c;
  }
`;

const AddressGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: 5px;  // 필드 사이의 간격 조정
`;

const AddressBox = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: baseline;
`;

const StAddressLabel = styled.div`
  margin: 5px 0px 3px;

  label {
    color: #888;
    font-weight: 600;
    text-align: left;
    letter-spacing: 0.0145em;
    line-height: 20px;
  }
`;

const StAddress = styled.input`
  width: 100%;
  height: 50px;
  min-height: 50px;
  padding: 0px 5px;
  outline: none;
  background-color: transparent;
  border-radius: 5px;
  font-size: 16px;
  font-weight: 400;
`;

const StButton = styled.button`
  width: 100%;
  height: 50px;
  min-height: 50px;
  border-radius: 25px;
  border: none;
  cursor: default;
  // 글자
  font-size: 16px;
  font-weight: 600;
  text-align: center;
  letter-spacing: 0.0056em;
  line-height: 24px;
  cursor: pointer;

  &:disabled {
    background-color: #FABA96;
    color: white;
  }

  &.emailBtn {
    max-width: 80px;
    margin-left: 10px;
  }
  
  &.registerNumBtn {
    max-width: 80px;
    margin-left: 10px;
  }
  
  &.addressBtn {
    max-width: 80px;
    margin-left: 10px;
  }

  &.registerBtn {
    margin-bottom: 10px;
    margin-top: 30px;
  }
`;

const Typography = styled.p`
  font-size: 13px;
  font-weight: 400;
  line-height: 18px;
  text-align: left;
  margin-bottom: 8px;
  margin-top: 0px;
  color: #fe415c;
`;

const Caption = styled.p`
  color: #939393;
  font-weight: 600;
  text-align: center;
  font-size: 15px;
  line-height: 16px;
  margin-top: 15px;
  margin-bottom: 15px;
  text-decoration: underline;
`;

export default Institution_signup;