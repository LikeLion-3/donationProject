import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import jwtAxios, { API_SERVER_HOST } from "../../../util/jwtUtil";
import useToast from '../../../hooks/useToast'

function ChangePasswordModal(props) {
  const { showToast } = useToast();
  const show = props.show;
  const userInfo = props.userInfo;
  const { userId, userType } = userInfo;

  const handleClose = () => props.setShow(false);

  const isPasswordEquals = (passwordData) => {
    return passwordData.password1 === passwordData.password2;
  }

  const handleSubmit = () => {
    const passwordData = {
      password1: document.getElementById('password1').value,
      password2: document.getElementById('password2').value
    }
    if (!isPasswordEquals(passwordData)) {
      showToast('비밀번호가 일치하지 않습니다.', 'error')
      return;
    }

    const editInfo = {
      password: passwordData.password1
    }

    jwtAxios.put(`${API_SERVER_HOST}/api/users/${userId}/${userType}`, editInfo)
      .then(res => {
        showToast('비밀번호가 변경되었습니다.', 'success')
      }
      );
    props.setShow(false);
  }
  return (
    <Modal
      show={show}
      onHide={handleClose}
      size="sm"
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header closeButton>
        <h6>비밀번호를 입력해주세요</h6>
      </Modal.Header>
      <Modal.Body>
        <table>
          <tbody>
            <tr>
              <td><h6>새 비밀번호</h6></td>
              <td><input type="password" id="password1" /></td>
            </tr>
            <tr>
              <td><h6>비밀번호 확인</h6></td>
              <td><input type="password" id="password2" /></td>
            </tr>
          </tbody>
        </table>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="primary" onClick={handleSubmit}>
          Submit
        </Button>
      </Modal.Footer>
    </Modal>
  );
}


export default ChangePasswordModal;