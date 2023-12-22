import React, {useState, useEffect} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import styled from 'styled-components';
import {useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
import * as yup from 'yup';
import useAuthAPI from './authApi';
import useToast from '../../hooks/useToast';
import DaumPost from '../daumPost';
import TermsModal from './terms';

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
    nickname: yup
        .string()
        .min(2, '닉네임은 최소 2자리입니다')
        .max(12, '닉네임은 최대 12자리입니다')
        .matches(
            /^(?=.*[가-힣])[^\s]*$/,
            "공백을 제외한 한글만 입력해주세요"
        ),
    agreeTerms: yup.bool().oneOf([true], '서비스 이용약관에 동의해주세요').required()
});

const Individual_signup = () => {
    const {showToast} = useToast();
    // 이메일 유효 여부
    const [emailValid, setEmailValid] = useState(false);
    const [nicknameValid, setNicknameValid] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const navigate = useNavigate();

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

    const checkNicknameDuplicated = async () => {   // 닉네임 중복체크
        const data = getValues('nickname');

        // 이메일칸이 비어 있는지 확인
        if (!data || data.trim() === '') {
            showToast('닉네임을 입력해주세요.', 'warning');
            return;
        }

        try {
            // 서버에 닉네임 중복 확인 요청 보내기
            const res = await authAPI.nicknameValidCheck(data);

            if (res.data) {
                showToast('유효한 닉네임입니다.', 'success');
                // 이메일 중복 여부를 상태로 저장
                setNicknameValid(true);
            } else {
                showToast('이미 사용중인 닉네임입니다.', 'warning');
                // 닉네임 중복 여부를 상태로 저장
                setNicknameValid(false);
            }
        } catch (error) {
            showToast('에러가 발생했습니다.', 'error');
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

    const onSubmit = async (data) => {

        const {email, password, nickname, agreeTerms} = data;

        const auth = {
            email: data.email,
            password: data.password,
            nickname: data.nickname,
            fullAddr: `${address1} ${address2} ${address3}`,
            sdName: address1,
            sggName: address2,
            details: address3,
        };

        // 유효성 검사
        if (!emailValid) {
            showToast('이메일 중복체크를 해주세요.', 'error');
            return;
        }
        if (!nicknameValid) {
            showToast('닉네임 중복체크를 해주세요.', 'error');
            return;
        }
        if (!address1 && !address2 && !address3) {
            showToast('주소를 등록해주세요', 'warning');
            return;
        }
        if (!getValues('agreeTerms')) {
            showToast('서비스 이용약관에 동의해주세요.', 'error');
            return;
        }

        try {
            const res = await authAPI.SignUpIndividual(auth);
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
        const nickname = getValues('nickname');
        if (!nickname || nickname.trim() === '') {
            return;
        }
        if (!nicknameValid) {
            setError('nickname', {type: 'custom', message: '사용중인 닉네임입니다.'});
        } else {
            clearErrors('nickname', {type: 'custom'});
        }

    }, [nicknameValid]);

    return (
        <StLayout>
            <StContainer>
                <StRegister>
                    <Top>
                        <StLogo>
                            <img src="logo.png" style={{width:"110px"}} alt="true"/>
                        </StLogo>
                    </Top>
                    <Main>
                        <form onSubmit={handleSubmit(onSubmit, onError)}>
                            <Title>회원가입</Title>
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
                                <label>닉네임</label>
                            </StLabel>
                            <InputBox>
                                <StInput
                                    id="nickname"
                                    name="nickname"
                                    placeholder="한글 2~12자로 입력해주세요."
                                    className={errors.nickname ? 'error' : ''}
                                    {...register('nickname', {required: true})}
                                />
                                <StButton
                                    type="button"
                                    disabled={!getValues('nickname') || errors.nickname}
                                    className="nicknameBtn"
                                    onClick={checkNicknameDuplicated}
                                >
                                    중복확인
                                </StButton>
                            </InputBox>
                            <Typography>{errors.nickname?.message}</Typography>
                            <AddressGroup>
                                <StAddressLabel>
                                    <label>시/도</label>
                                </StAddressLabel>
                                <AddressBox>
                                    <StAddress
                                        name="address1"
                                        id="address1"
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
  max-width: 450px;
  min-height: 992px;
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
  
  &.nicknameBtn {
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

export default Individual_signup;