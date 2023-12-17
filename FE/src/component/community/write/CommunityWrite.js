import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'simplelightbox/dist/simple-lightbox.min.css';
import jwtAxios from "../../util/jwtUtil";
import { API_SERVER_HOST } from "../../util/jwtUtil";
import Cookies from 'js-cookie';

const CommunityWrite = () => {
  const navigate = useNavigate();
  const [images, setImages] = useState([]);
  const [title, setTitle] = useState(localStorage.getItem('postTitle') || '');
  const [content, setContent] = useState(localStorage.getItem('postContent') || '');
  const [category, setCategory] = useState(localStorage.getItem('postCategory') || '');
  const [previewImage, setPreviewImage] = useState(null);

  const handleTitleChange = (event) => {
    const newTitle = event.target.value;
    setTitle(newTitle);
    localStorage.setItem('postTitle', newTitle);
  };

  const handleContentChange = (event) => {
    const newContent = event.target.value;
    setContent(newContent);
    localStorage.setItem('postContent', newContent);
  };

  const handleImageClick = () => {
    document.getElementById('imageUpload').click();
  };

  const handleImageChange = (event) => {
    const selectedImages = Array.from(event.target.files);
    setImages((prevImages) => [...prevImages, ...selectedImages]);

    // 이미지 미리보기 생성
    if (selectedImages[0]) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviewImage(reader.result);
      };
      reader.readAsDataURL(selectedImages[0]);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const formData = new FormData();

      formData.append('title', title);
      formData.append('contents', content);
      formData.append('category', category);

      images.forEach((image, index) => {
        formData.append(`images`, image);
      });

      const userToken = Cookies.get('user');
      const parsedToken = userToken ? JSON.parse(decodeURIComponent(userToken)) : null;
      const token = parsedToken.accessToken;

      const response = await jwtAxios.post(`${API_SERVER_HOST}/api/communities`, formData, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
  
      console.log('Server response:', response);
      const data = response.data;
      navigate(`/community/${data.id}`);
      
    } catch (error) {
      console.error('Error submitting form:', error);
    }
  }

  return (
    <section className="community-list-page-section" id="contact">
      <div className="container" style={{ maxWidth: '900px' }}>

        <form onSubmit={handleSubmit}>
          <div className="row gx-4 gx-lg-5 align-items-center" style={{ border: '2px solid #E2E2E2' }}>
            <div className='community-contents'>
              <div className='community-header' style={{ border: '0px solid #E2E2E2', borderBottom: '2px solid #E2E2E2' }}>
                <div className="col-md-12" style={{ marginTop: '10px' }}>
                  <div className="form-floating mb-3" style={{ marginTop: '20px' }}>
                    <input
                      type="text"
                      className="form-control"
                      id="title"
                      placeholder="글 제목을 입력해주세요."
                      value={title}
                      onChange={handleTitleChange}
                    // style={{border: 'none'}}
                    />
                    <label className="form-label" style={{ color: 'dimgray' }}>제목</label>
                  </div>
                </div>
              </div>
              <div classnames="community-body">
                <div className="col-md-12" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '402px', marginTop: '30px', marginBottom: '30px' }}>
                  <img
                    src={previewImage || '/images/community_default.PNG'} // 미리보기 이미지 또는 기본 이미지
                    alt='사진 등록하기'
                    style={{
                      maxWidth: '400px',
                      maxHeight: '266px',
                      width: 'auto',
                      height: 'auto'
                    }}
                    onClick={handleImageClick}
                  />
                  <input
                    type='file'
                    id='imageUpload'
                    style={{ display: 'none' }}
                    onChange={handleImageChange}
                    multiple
                  />
                </div>
                <div className="d-flex justify-content-start" style={{ marginTop: '10px' }}>
                  {['봉사인증', '봉사모집', '잡다구리'].map((cat) => (
                    <span
                      key={cat}
                      className={`badge m-1 ${cat === category ? 'selected-badge' : 'unselected-badge'}`}
                      onClick={() => setCategory(cat)}
                      style={{
                        cursor: 'pointer',
                        border: `1px solid #FABA96`,
                        color: cat === category ? 'white' : '#FABA96',
                        backgroundColor: cat === category ? '#FABA96' : 'white',
                        padding: '0.5em 0.8em', // 세로 및 가로 패딩 조정
                        fontSize: '1rem', // 글씨 크기 조정
                        borderRadius: '0.25rem' // 둥근 모서리 조정
                      }}
                    >
                      {cat}
                    </span>
                  ))}
                </div>
                <div className="form-floating mb-3" style={{ marginTop: '20px' }}>
                  <textarea
                    className="form-control"
                    id="content"
                    placeholder="글 내용을 입력해주세요"
                    value={content}
                    onChange={handleContentChange}
                    style={{ height: '10rem' }}
                  ></textarea>
                  <label className="form-label" style={{ color: 'dimgray' }}>내용</label>
                </div>
              </div>
            </div>
          </div>
          <div className="d-flex justify-content-end" style={{ marginTop: '20px', marginBottom: '10px' }}>
            <button
              className="btn btn-primary btn-xl"
              type="submit"
              style={{ backgroundColor: '#FABA96', borderColor: '#FABA96' }}>
              Submit
            </button>
          </div>
        </form>
      </div>

    </section >
  );
};

export default CommunityWrite;