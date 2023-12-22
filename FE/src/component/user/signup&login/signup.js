import React from 'react';
import {Link, useNavigate} from 'react-router-dom';
import styled from 'styled-components';
import { ReactComponent as Google } from "../../../assets/images/social/google.svg"
import {API_SERVER_HOST} from "../../util/jwtUtil"

const NaverImg = styled.img.attrs({
    src: "naver.png",
})`
  height: 40px;
  width: 40px;
  border: 1px solid black;
  border-radius: 100%;
  cursor: pointer;
`;

const KakaoImg = styled.img.attrs({
    src: "kakao.png",
})`
  height: 40px;
  width: 40px;
  padding: 8px 6px 6px 7px;
  border: 1px solid black;
  border-radius: 100%;
  cursor: pointer;
`;

const GoogleSVG = styled(Google)`
  height: 40px;
  width: 40px;
  border: 1px solid black;
  border-radius: 100%;
  cursor: pointer;
`;

const SSection = styled.section`
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
  row-gap: 10px;
  margin: 25px;
`;

const SH3 = styled.h3`
  font-size: 20px;
  color: black;
`;

const SSocialBox = styled.div`
  display: flex;
  flex-flow: row nowrap;
  justify-content: space-between;
  width: 80%;
`;

const Signup = () => {
    const navigate = useNavigate();

    const handleIndividualButtonClick = () => {
        navigate("/signup/individual");
    };
    const handleInstitutionButtonClick = () => {
        navigate("/signup/institution");
    };

    return (
        <StLayout>
            <StContainer>
                <StSignup>
                    <Main>
                        <Title>일반 회원가입</Title>
                        <StLabel>
                            <label>이메일,패스워드 등록으로 회원가입 하실 수 있습니다.</label>
                        </StLabel>
                        <StButton
                            className="IndividualSignup" onClick={handleIndividualButtonClick}
                        >개인회원가입
                        </StButton>
                        <StButton
                            className="InstitutionSignup" onClick={handleInstitutionButtonClick}
                        >기관회원가입
                        </StButton>
                    </Main>
                </StSignup>
            </StContainer>


    <SSection>
        <SH3>SNS로 간편하게 시작하기</SH3>
        <SSocialBox>
            <NaverImg/>
            <Link to={`${API_SERVER_HOST}/oauth2/authorization/kakao`}><KakaoImg /></Link>
            <GoogleSVG
                viewBox="4 4 38 38"
            />
        </SSocialBox>
    </SSection>
        </StLayout>
)
};

const StLayout = styled.div`
  display: flex;
  flex-flow: column wrap;
  justify-content: center;
  align-items: center;
  height: 1000px;
  padding: 7rem 0;
`;

const StContainer = styled.div`
  max-width: 500px;
  min-height: 300px;
  width: 100%;
  margin: 0 auto;
  display: flex;
`;

const StSignup = styled.div`
  border-radius: 5px;
  width: 100%;
  max-height: 300px;
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

const StLogo = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
`;

const Main = styled.div`
  overflow: auto;
  position: relative;
  padding: 20px;
  min-height: 200px;
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

const StButton = styled.button`
  width: 100%;
  height: 50px;
  min-height: 50px;
  border-radius: 25px;
  border: none;
  cursor: default;
  background-color: #FABA96;
  // 글자
  font-size: 16px;
  font-weight: 600;
  text-align: center;
  letter-spacing: 0.0056em;
  line-height: 24px;
  cursor: pointer;
  margin-top: 10px;
  margin-bottom: 10px;
  color: #ffffff;
  `

export default Signup;