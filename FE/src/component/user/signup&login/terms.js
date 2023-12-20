import React, { useState, useEffect } from 'react';
import { Modal, Button } from 'react-bootstrap';

const TermsModal = () => {
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        openModal();
    }, []);

    const openModal = () => {
        setShowModal(true);
    };

    const closeModal = () => {
        setShowModal(false);
    };

    return (
        <>
            <Modal show={showModal} onHide={closeModal}>
                <Modal.Header closeButton>
                    <Modal.Title>이용약관 및 개인정보 처리방침</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <h4>이용약관</h4>
                    <p>제 1 조 (목적)</p>
                    <p>이용약관은 warmwave가 이용자에게 제공하는 인터넷 관련 서비스 (이하 “서비스”)를 이용함에 있어 warmwave과 회원 간의 권리, 의무 및 이용에 관한 제반 사항 등을 규정함을 목적으로 합니다.</p>
                    <p>제 2 조 (정의)</p>
                    <p>이 약관에서 사용하는 용어의 정의는 다음과 같습니다.</p>
                    <p>“이용자”라 함은 warmwave의 서비스에 접속하여 본 약관에 따라 warmwave이 제공하는 콘텐츠를 이용하는 회원 및 비회원을 말합니다.</p>
                    <p>“회원”이라 함은 warmwave이 제공하는 서비스를 받기 위하여 warmwave과 이용계약을 체결하고 이용자 아이디(ID)를 부여 받은 자를 말합니다.</p>
                    <p>“비회원”이라 함은 회원에 가입하지 않고 서비스를 이용하는 자를 말합니다.</p>
                    <p>“간편로그인 회원” 이라 함은 warmwave의 홈페이지에 네이버, 카카오, 구글 등에 등록된 정보로 로그인, 회원가입을 하여 서비스를 이용하는 자를 말합니다.</p>
                    <p>“아이디(ID)”라 함은 “회원”의 식별과 “서비스” 이용을 위하여 “회원”이 정하고 “warmwave”이 승인하는 문자나 숫자 조합을 의미합니다.</p>
                    <p>“비밀번호”라 함은 “warmwave”이 부여 받은 아이디와 일치되는 “회원”임을 확인하고 개인정보 등 의 보호를 위해 “회원” 자신이 정한 문자 또는 숫자의 조합을 의미합니다.</p>
                    <p>“게시물”이라 함은 “이용자”가 “서비스”를 이용함에 있어 “서비스상”에 게시한 부호 · 문자 · 음성 · 음향 · 화상 · 동영상 등의 정보 형태의 글, 사진, 동영상 및 각종 파일과 링크 등을 의미합니다.</p>
                    <p>“회원탈퇴”라 함은 warmwave 또는 회원이 이용계약을 탈퇴하는 것을 말합니다. </p>
                    <h4>개인정보 처리방침</h4>
                    <p>
                        1. 수집 · 이용 항목

                        아이디(ID), 비밀번호, 이름, 생년월일, 성별, 휴대전화번호, 이메일

                        2. 수집 · 이용 목적

                        개인식별, 본인확인, 기부 등 참여내역 및 회원정보 관리, 불만처리 등 민원처리, 고지사항 전달, 부정이용방지, 중복가입확인, 자원봉사자 등록 등

                        3. 보유 · 이용 기간

                        온라인 서비스 이용 회원탈퇴 시까지 또는 법정 의무 보유기간까지

                        4. 동의 거부에 대한 안내

                        귀하는 동의를 거부할 권리가 있으나, 거부하시는 경우 온라인 서비스에 대한 회원가입이 불가능합니다.
                    </p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        닫기
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default TermsModal;