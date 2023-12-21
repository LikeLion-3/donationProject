import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'simplelightbox/dist/simple-lightbox.min.css';
import Cookies from 'js-cookie';
import './PostForm.css';
import jwtAxios, { API_SERVER_HOST } from '../../util/jwtUtil';
import { useNavigate, useLocation, useParams } from 'react-router-dom';

const PatchForm = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const params = useParams();
  const [images, setImages] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [donationTypeSelected, setDonationTypeSelected] = useState(false);
  const [needTypeSelected, setNeedTypeSelected] = useState(false);
  const [verificationTypeSelected, setVerificationTypeSelected] = useState(false);
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [originalImageUrls, setOriginalImageUrls] = useState();

  const formData = new FormData();

  useEffect(() => {
    const originalImageUrls = (location.state && location.state.article && location.state.article.images)
      ? location.state.article.images.map((image) => image.imgUrl)
      : [];
    setOriginalImageUrls(JSON.stringify(originalImageUrls));
  }, [])


  useEffect(() => {
    const { article } = location.state || {};
    if (article) {
      setTitle(article.title || '');
      setContent(article.content || '');
      setImages(article.images || []);
      setSelectedCategories(article.prodCategories || []);
      handleTypeChange(article.articleType || '기부해요');
    }
  }, [location.state]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await jwtAxios.get(`${API_SERVER_HOST}/api/categories`);
        const data = await response.data;
        const categoryNames = data.content.map(category => category.name);
        setCategories(categoryNames);
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    };

    fetchCategories();
  }, []);

  const handleImageChange = (event) => {
    const selectedImages = Array.from(event.target.files);
    setImages((prevImages) => [...prevImages, ...selectedImages]);
  };

  const handleRemoveImage = (index) => {
    setImages((prevImages) => {
      const newImages = [...prevImages];
      const removedImage = newImages.splice(index, 1)[0]; // 선택한 인덱스의 이미지를 제거
      return newImages;
    });
  };

  const handleRemoveArticleImage = (index) => {
    setImages((prevImages) => {
      const newImages = [...prevImages];
      newImages.splice(index, 1); // 배열 비구조화 할당을 사용하여 첫 번째 요소만 얻기
      console.log('Initial Images:', newImages); // 이미지 배열 확인

      const imageUrls = newImages.map((image) => image?.imgUrl);

      setOriginalImageUrls(JSON.stringify(imageUrls));

      return newImages;
    });
  };

  const handleRemoveImageAndArticleImage = (index) => {
    // 이미지 삭제
    handleRemoveImage(index);
    // Article 이미지 삭제
    handleRemoveArticleImage(index);

    console.log('handleRemoveImageAndArticleImage called'); // 로그 추가
  };

  const handleCategoryChange = (selectedCategory) => {
    setSelectedCategories((prevCategories) => {
      const index = prevCategories.indexOf(selectedCategory);
      if (index !== -1) {
        return [...prevCategories.slice(0, index), ...prevCategories.slice(index + 1)];
      } else {
        return [...prevCategories, selectedCategory];
      }
    });
  };

  const handleTypeChange = (type) => {
    setDonationTypeSelected(type === '기부해요');
    setNeedTypeSelected(type === '필요해요');
    setVerificationTypeSelected(type === '인증해요');
  };

  const handleTitleChange = (event) => {
    const newTitle = event.target.value;
    setTitle(newTitle);
  };

  const handleContentChange = (event) => {
    const newContent = event.target.value;
    setContent(newContent);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      // 이미지가 존재하는 경우에만 FormData에 추가
      if (images.length > 0) {
        images.forEach((image, index) => {
          formData.append(`imageFiles`, image);
        });
      }

      formData.append('title', title);
      formData.append('content', content);
      formData.append('prodCategories', JSON.stringify(selectedCategories));
      formData.append('articleType', getSelectedType());
      formData.append('originalImageUrls', originalImageUrls);

      console.log(formData.get('originalImageUrls'))

      const response = await jwtAxios.put(`${API_SERVER_HOST}/api/articles/${params.articleId}`, formData);

      const data = await response.data;
      console.log('Server response:', data);

      navigate(`/donate/${data.articleId}`);
    } catch (error) {
      console.error('Error submitting form:', error);
    }
  };

  const getSelectedType = () => {
    if (donationTypeSelected) {
      return '기부해요';
    } else if (needTypeSelected) {
      return '필요해요';
    } else if (verificationTypeSelected) {
      return '인증해요';
    }

    return '기부해요';
  };

  return (
    <section className="page-section" id="contact">
      <div className="container px-4 px-lg-5">
        <div className="row gx-4 gx-lg-5 justify-content-center">
          <div className="col-lg-8 col-xl-6 text-center">
            <h2 className="mt-0">게시글 수정</h2>
            <hr className="divider" />
          </div>
        </div>
        <div className="row gx-4 gx-lg-5 justify-content-center mb-5">
          <div className="col-lg-8 col-xl-6">
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label" style={{ color: 'dimgray' }}>게시글 유형</label>
                <div className="d-flex flex-wrap">
                  <div className="me-2 mb-2">
                    <button
                      type="button"
                      className={`btn ${donationTypeSelected ? 'btn-primary' : 'btn-outline-primary'}`}
                      onClick={() => handleTypeChange('기부해요')}
                      style={{
                        color: donationTypeSelected ? '#ffffff' : '#ffa500',
                        backgroundColor: donationTypeSelected ? '#ffa500' : 'transparent',
                        borderColor: '#ffa500',
                      }}
                    >
                      기부해요
                    </button>
                  </div>
                  <div className="me-2 mb-2">
                    <button
                      type="button"
                      className={`btn ${needTypeSelected ? 'btn-primary' : 'btn-outline-primary'}`}
                      onClick={() => handleTypeChange('필요해요')}
                      style={{
                        color: needTypeSelected ? '#ffffff' : '#007bff',
                        backgroundColor: needTypeSelected ? '#007bff' : 'transparent',
                        borderColor: '#007bff',
                      }}
                    >
                      필요해요
                    </button>
                  </div>
                  <div className="me-2 mb-2">
                    <button
                      type="button"
                      className={`btn ${verificationTypeSelected ? 'btn-primary' : 'btn-outline-primary'}`}
                      onClick={() => handleTypeChange('인증해요')}
                      style={{
                        color: verificationTypeSelected ? '#ffffff' : '#28a745',
                        backgroundColor: verificationTypeSelected ? '#28a745' : 'transparent',
                        borderColor: '#28a745',
                      }}
                    >
                      인증해요
                    </button>
                  </div>
                </div>
              </div>
              <div className="mb-3">
                <label className="form-label" style={{ color: 'dimgray' }}>
                  이미지 추가
                </label>
                <input
                  type="file"
                  className="form-control"
                  id="image"
                  accept="image/*"
                  onChange={handleImageChange}
                  multiple
                />
                {images.length > 0 && (
                  <div className="mt-3 d-flex flex-wrap">
                    {images.map((image, index) => (
                      <div key={index} className="position-relative me-2 mb-2">
                        <button
                          type="button"
                          className="btn btn-danger btn-remove-image position-absolute top-0 end-0"
                          onClick={() => handleRemoveImageAndArticleImage(index)}
                          style={{
                            padding: '0.2rem 0.4rem',
                            backgroundColor: '#a9a9a9',
                            borderColor: '#a9a9a9',
                            color: '#fff',
                            fontSize: '0.8rem',
                          }}
                        >
                          X
                        </button>
                        <img
                          src={image.imgUrl ? image.imgUrl : URL.createObjectURL(image)}
                          alt={`Preview-${index}`}
                          className="img-preview me-2 mb-2 col-lg-3 col-md-4 col-sm-6"
                          style={{ width: '131px', height: '150px', objectFit: 'cover' }}
                        />
                      </div>
                    ))}
                  </div>
                )}
              </div>
              <div className="mb-3">
                <label className="form-label" style={{ color: 'dimgray' }}>물품종류</label>
                <div className="d-flex flex-wrap">
                  {categories.map((category) => (
                    <div key={category} className="me-2 mb-2">
                      <button
                        type="button"
                        className={`btn ${selectedCategories.includes(category) ? 'btn-primary' : 'btn-outline-primary'
                          }`}
                        onClick={() => handleCategoryChange(category)}
                        style={{
                          color: selectedCategories.includes(category) ? '#ffffff' : '#ffa500',
                          backgroundColor: selectedCategories.includes(category) ? '#ffa500' : 'transparent',
                          borderColor: '#ffa500',
                        }}
                      >
                        {category}
                      </button>
                    </div>
                  ))}
                </div>
              </div>
              <div className="form-floating mb-3">
                <input
                  type="text"
                  className="form-control"
                  id="title"
                  placeholder="글 제목을 입력해주세요."
                  value={title}
                  onChange={handleTitleChange}
                />
                <label className="form-label" style={{ color: 'dimgray' }}>글 제목</label>
              </div>
              <div className="form-floating mb-3">
                <textarea
                  className="form-control"
                  id="content"
                  placeholder="글 내용을 입력해주세요"
                  value={content}
                  onChange={handleContentChange}
                  style={{ height: '10rem' }}
                ></textarea>
                <label className="form-label" style={{ color: 'dimgray' }}>글 내용</label>
              </div>
              <div className="d-flex justify-content-end">
                <button
                  className="btn btn-primary btn-xl"
                  type="submit"
                  style={{ backgroundColor: '#ffa500', borderColor: '#ffa500' }}
                >
                  수정
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
};

export default PatchForm;