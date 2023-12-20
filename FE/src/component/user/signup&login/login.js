import React, { useState } from "react";
import {Link} from "react-router-dom";
import useCustomLogin from "../../hooks/useCustomLogin"
// import KakaoLogin from "./KakaoLogin"
import useToast from "../../hooks/useToast";
// nodejs library that concatenates classes
import classnames from "classnames";

// reactstrap components
import {
    Button,
    Card,
    CardHeader,
    CardBody,
    FormGroup,
    Form,
    Input,
    InputGroupText,
    InputGroup,
    Container,
    Row,
    Col,
} from "reactstrap";

function Login() {
    const { showToast } = useToast();

    const [focused, setFocused] = useState({"passwordFocused": false, "emailFocused": false})

    const initState = {
        email: '',
        password: ''
    }

    const [loginParam, setLoginParam] = useState({...initState})
    
    const {doLogin, moveToPath} = useCustomLogin()

    const handleChange = (e) => {
        loginParam[e.target.name] = e.target.value
        setLoginParam({...loginParam})
    }

    const handleClickLogin = (e) => {
        doLogin(loginParam) // loginSlice의 비동기 호출
            .then(data => {
                console.log(data)
                
                if (data.error) {
                    showToast("이메일과 패스워드를 다시 확인하세요", 'warning')
                } else {
                    showToast("로그인 성공", 'success')
                    moveToPath("/") // 뒤로 가기 했을 때 로그인 화면을 볼 수 없도록
                }
            })
    }

    return (
        <div>
            <section className="section section-lg section-shaped mt-5">
                <div className="shape shape-style-1 shape-default">
                    <span/>
                    <span/>
                    <span/>
                    <span/>
                    <span/>
                    <span/>
                    <span/>
                    <span/>
                </div>
                <Container className="py-md">
                    <Row className="row-grid justify-content-center align-items-center">
                        <Col className="mb-lg-auto" lg="5">
                            <div className="transform-perspective-right">
                                <Card className="bg-secondary shadow border-0">
                                    <CardHeader className="bg-white pb-5">
                                        <div className="text-muted text-center mb-3">
                                            <small>SNS 로그인</small>
                                        </div>
                                        {/*<KakaoLogin/>*/}
                                    </CardHeader>
                                    <CardBody className="px-lg-5 py-lg-5">
                                        <div className="text-center text-muted mb-4">
                                            <small>WarmWave 아이디로 로그인</small>
                                        </div>
                                        <Form role="form">
                                            <FormGroup
                                                className={classnames("mb-3", {
                                                    focused: focused.mailFocused,
                                                })}
                                            >
                                                <InputGroup className="input-group-alternative">
                                                    <InputGroupText>
                                                        <i className="ni ni-email-83"/>
                                                    </InputGroupText>
                                                    <Input
                                                        name="email"
                                                        placeholder="Email"
                                                        type="email"
                                                        value={loginParam.email}
                                                        onChange={handleChange}
                                                        onFocus={(e) =>
                                                            setFocused({...focused, emailFocused: true})
                                                        }
                                                        onBlur={(e) =>
                                                            setFocused({...focused, emailFocused: false})
                                                        }
                                                    />
                                                </InputGroup>
                                            </FormGroup>
                                            <FormGroup
                                                className={classnames({
                                                    focused: focused.asswordFocused,
                                                })}
                                            >
                                                <InputGroup className="input-group-alternative">
                                                    <InputGroupText>
                                                        <i className="ni ni-lock-circle-open"/>
                                                    </InputGroupText>
                                                    <Input
                                                        name="password"
                                                        placeholder="Password"
                                                        type="password"
                                                        value={loginParam.password}
                                                        onChange={handleChange}
                                                        autoComplete="off"
                                                        onFocus={(e) =>
                                                            setFocused({...focused, passwordFocused: true})
                                                        }
                                                        onBlur={(e) =>
                                                            setFocused({...focused, passwordFocused: false})
                                                        }
                                                    />
                                                </InputGroup>
                                            </FormGroup>
                                            <div className="custom-control custom-control-alternative custom-checkbox">
                                                <input
                                                    className="custom-control-input"
                                                    id="customCheckLogin2"
                                                    type="checkbox"
                                                />
                                                <label
                                                    className="custom-control-label"
                                                    htmlFor="customCheckLogin2"
                                                >
                                                    <span>Remember me</span>
                                                </label>
                                            </div>
                                            <div className="text-center">
                                                <Button
                                                    className="my-4"
                                                    color="primary"
                                                    style={{backgroundColor: '#FABA96', border: 'none'}}
                                                    type="button"
                                                    onClick={handleClickLogin}
                                                >
                                                    Log in
                                                </Button>
                                            </div>
                                            <div className="text-center">
                                                <span>아직 회원이 아니신가요? </span>
                                                <Link to="/signup">
                                                    <Button
                                                        className="my-4"
                                                        style={{backgroundColor: '#FABA96', border: 'none'}}
                                                        type="button"
                                                    >
                                                        회원가입
                                                    </Button>
                                                </Link>
                                            </div>
                                        </Form>
                                    </CardBody>
                                </Card>
                            </div>
                        </Col>
                    </Row>
                </Container>
                {/* SVG separator */}
                <div className="separator separator-bottom separator-skew">
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        preserveAspectRatio="none"
                        version="1.1"
                        viewBox="0 0 2560 100"
                        x="0"
                        y="0"
                    >
                        <polygon className="fill-white" points="2560 0 2560 100 0 100"/>
                    </svg>
                </div>
            </section>
        </div>
    );
}

export default Login;
