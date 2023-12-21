import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'simplelightbox/dist/simple-lightbox.min.css';
import { CommunityCategoryStyles } from '../CommunityCategoryStyles';
import jwtAxios from "../../util/jwtUtil";
import { API_SERVER_HOST } from "../../util/jwtUtil";

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

  const handleRemoveImage = () => {
    setImages([]);
    setPreviewImage(null); // 미리보기 이미지 제거
    // 필요한 경우 추가적인 상태 업데이트
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!title.trim()) {
      alert('제목을 입력해주세요.');
      return;
    }
    if (!content.trim()) {
      alert('내용을 입력해주세요.');
      return;
    }
    if (!category.trim()) {
      alert('카테고리를 선택해주세요.');
      return;
    }
    if (!(title.length >= 5 && title.length <= 1000)) {
      alert('제목을 5글자 이상 50글자 이하로 입력해주세요.');
      return;
    }
    if (!(content.length >= 10 && content.length <= 1000)) {
      alert('내용을 10글자 이상 1000글자 이하로 입력해주세요.');
      return;
    }

    try {
      const formData = new FormData();

      formData.append('title', title);
      formData.append('contents', content);
      formData.append('category', category);

      images.forEach((image, index) => {
        formData.append(`images`, image);
      });

      const response = await jwtAxios.post(`${API_SERVER_HOST}/api/communities`, formData);

      console.log('Server response:', response);
      const data = response.data;
      navigate(`/community/${data.id}`);

      localStorage.removeItem('postTitle');
      localStorage.removeItem('postContent');
      localStorage.removeItem('postCategory');

      setTitle('');
      setContent('');
      setCategory('');
      setImages([]);
      setPreviewImage(null);

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
                    />
                    <label className="form-label" style={{ color: 'dimgray' }}>제목</label>
                  </div>
                </div>
              </div>
              <div classnames="community-body">
                <div className="col-md-12" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', height: '402px', marginTop: '30px', marginBottom: '30px', position: 'relative' }}>
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
                  {previewImage && (
                    <button
                      onClick={handleRemoveImage}
                      style={{
                        // position: 'absolute',
                        // top: '5px', // 버튼의 상단 위치 조정
                        // right: '5px', // 버튼의 우측 위치 조정
                        border: 'grey',
                        background: 'rgba(255, 255, 255, 0.7)', // 배경 색상과 투명도 추가
                        marginTop: '10px', // 버튼과 이미지 사이의 간격
                        color: 'grey',
                        fontSize: '0.8rem',
                        cursor: 'pointer',
                        padding: '2px 5px', // 패딩 추가
                        borderRadius: '50%', // 원형 버튼으로 디자인
                        zIndex: 2 // z-index 추가
                      }}>
                      삭제
                    </button>
                  )}
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
                      onClick={() => setCategory(cat)}
                      style={{
                        ...CommunityCategoryStyles[cat],
                        cursor: 'pointer',
                        padding: '0.5em 0.8em',
                        fontSize: '1rem',
                        borderRadius: '0.25rem',
                        margin: '0 0.5em',
                        border: `1px solid ${CommunityCategoryStyles[cat].backgroundColor}`,
                        color: cat === category ? 'white' : CommunityCategoryStyles[cat].backgroundColor,
                        backgroundColor: cat === category ? CommunityCategoryStyles[cat].backgroundColor : 'white'
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
              작성하기
            </button>
          </div>
        </form>
      </div>

    </section >
  );
};

export default CommunityWrite;