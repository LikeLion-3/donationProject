import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, useParams, Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'simplelightbox/dist/simple-lightbox.min.css';
import Cookies from 'js-cookie';
import { getCookie } from '../../util/cookieUtil';
import jwtAxios from '../../util/jwtUtil';

const CommunityUpdate = () => {
  const navigate = useNavigate();
  const params = useParams();
  const location = useLocation();
  const { communityId } = useParams();

  const [title, setTitle] = useState('');
  const [contents, setContents] = useState('');
  const [category, setCategory] = useState('');
  const [image, setImage] = useState([]);
  const [previewImage, setPreviewImage] = useState(''); // 미리보기

  const community = location.state ? location.state.community : null;

  useEffect(() => {
    const { community } = location.state || {};
    if (community) {
      setTitle(community.title || '');
      setContents(community.contents || '');
      setCategory(community.category || '');
      setImage(community.images || '');
    }
  }, [location.state]);

  const [changedFields, setChangedFields] = useState({
    title: false,
    contents: false,
    category: false,
    images: false,
  });

  const handleTitleChange = (event) => {
    const newTitle = event.target.value;
    setTitle(newTitle);
    setChangedFields({ ...changedFields, title: true });
    localStorage.setItem('postTitle', newTitle);
  };

  const handleContentsChange = (event) => {
    const newContents = event.target.value;
    setContents(newContents);
    setChangedFields({ ...changedFields, contents: true });
    localStorage.setItem('postContent', newContents);
  };

  const handleCategoryChange = (event) => {
    const newCategory = event.target.innerText;
    setCategory(newCategory);
    setChangedFields({ ...changedFields, category: true });
    localStorage.setItem('postCategory', newCategory);
  };

  const handleImageClick = () => {
    document.getElementById('imageUpload').click();
  };

  const handleImageChange = (event) => {
          // 깃허브Url 등록 -> 이미지 바꿨지만 url 삭제 안 되는 중
    const newImage = event.target.files[0];
    if (newImage) {
      setImage(newImage);
      setChangedFields({ ...changedFields, images: true });
  
          // 이미지 미리보기 생성
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviewImage(reader.result);
      };
      reader.readAsDataURL(newImage);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const formData = new FormData();

      if (changedFields.title) formData.append('title', title);
      if (changedFields.contents) formData.append('contents', contents);
      if (changedFields.category) formData.append('category', category);

      if (changedFields.images && image) {
        formData.append('images', image);
      }

      const response = await jwtAxios.put(`http://localhost:8080/api/communities/${params.communityId}`, formData);

      const data = await response.data; // response.data는 java 객체
      console.log('Server response:', data);
      navigate(`/community/${data.id}`);
    } catch (error) {
      console.error('Error submitting form:', error);
    }
  };

  return (
    <section className="community-list-page-section" id="contact">
      <div className="container" style={{ maxWidth: '900px' }}>
        <h1>수정</h1>

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
                <div className="col-md-12" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '402px', marginTop: '30px', marginBottom: '30px' }}>
                  <img
                    src={previewImage || (community?.images && community.images.length > 0 ? community.images[0] : '/images/community_default.PNG')}
                    // previewImage
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
                      onClick={handleCategoryChange}
                      style={{
                        cursor: 'pointer',
                        border: `1px solid #FABA96`,
                        color: cat === category ? 'white' : '#FABA96',
                        backgroundColor: cat === category ? '#FABA96' : 'white',
                        padding: '0.5em 0.8em',
                        fontSize: '1rem',
                        borderRadius: '0.25rem'
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
                    value={contents}
                    onChange={handleContentsChange}
                    style={{ height: '10rem' }}
                  ></textarea>
                  <label className="form-label" style={{ color: 'dimgray' }}>내용</label>
                </div>
              </div>
            </div>
          </div>
          <div className="d-flex justify-content-end" style={{ marginTop: '20px', marginBottom: '10px' }}>
            <Link to={`/community/${params.communityId}`}>
              <button
                className="btn btn-primary btn-xl"
                style={{ backgroundColor: '#FABA96', borderColor: '#FABA96', marginRight: '10px' }}>
                취소하기
              </button>
            </Link>
            <button
              className="btn btn-primary btn-xl"
              onClick={handleSubmit}
              style={{ backgroundColor: '#FABA96', borderColor: '#FABA96' }}>
              저장하기
            </button>
          </div>
        </form>
      </div>

    </section >
  );
};

export default CommunityUpdate;