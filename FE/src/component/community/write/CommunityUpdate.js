import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, useParams, Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'simplelightbox/dist/simple-lightbox.min.css';
import jwtAxios from '../../util/jwtUtil';
import { CommunityCategoryStyles } from '../CommunityCategoryStyles';

const CommunityUpdate = () => {
  const navigate = useNavigate();
  const params = useParams();
  const location = useLocation();

  const [title, setTitle] = useState('');
  const [contents, setContents] = useState('');
  const [category, setCategory] = useState('');
  const [image, setImage] = useState([]);
  const [previewImage, setPreviewImage] = useState('');

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

  const handleCategoryChange = (newCategory) => {
    console.log(newCategory);
    setCategory(newCategory);
    setChangedFields({ ...changedFields, category: true });
    localStorage.setItem('postCategory', newCategory);
  };

  const handleImageClick = () => {
    document.getElementById('imageUpload').click();
  };

  const handleImageChange = (event) => {
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

    if (!title.trim()) {
      alert('제목을 입력해주세요.');
      return;
    }
    if (!contents.trim()) {
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
    if (!(contents.length >= 10 && contents.length <= 1000)) {
      alert('내용을 10글자 이상 1000글자 이하로 입력해주세요.');
      return;
    }

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

      localStorage.removeItem('postTitle');
      localStorage.removeItem('postContent');
      localStorage.removeItem('postCategory');

      setTitle('');
      setContents('');
      setCategory('');
      setImage([]);
      setPreviewImage(null);

    } catch (error) {
      console.error('Error submitting form:', error);
    }
  };

  const handleRemoveImage = () => {
    setImage([]);
    setPreviewImage(null); // 미리보기 이미지 제거
  };

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
                  {previewImage && (
                    <button
                      onClick={handleRemoveImage}
                      style={{
                        border: 'grey',
                        background: 'rgba(255, 255, 255, 0.7)',
                        marginTop: '10px',
                        color: 'grey',
                        fontSize: '0.8rem',
                        cursor: 'pointer',
                        padding: '2px 5px',
                        borderRadius: '50%',
                        zIndex: 2
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
                      onClick={() => handleCategoryChange(cat)}
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