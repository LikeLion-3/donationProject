import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'simplelightbox/dist/simple-lightbox.min.css';
import './PostForm.css';

const PostForm = () => {
  const navigate = useNavigate();
  const [images, setImages] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [title, setTitle] = useState(localStorage.getItem('postTitle') || '');
  const [content, setContent] = useState(localStorage.getItem('postContent') || '');

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await fetch('/api/categories');
        const data = await response.json();
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

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const formData = new FormData();

      images.forEach((image, index) => {
        formData.append(`imageFiles`, image);
      });

      formData.append('title', title);
      formData.append('content', content);
      formData.append('prodCategories', JSON.stringify(selectedCategories));

      const response = await fetch('/api/articles', {
        method: 'POST',
        body: formData,
      });

      const data = await response.json();
      console.log('Server response:', data);

      // 글 작성 후 해당 게시글 조회 페이지로 이동
      navigate(`/donate/${data.articleId}`);
    } catch (error) {
      console.error('Error submitting form:', error);
    }
  };

  useEffect(() => {
    localStorage.setItem('selectedCategories', JSON.stringify(selectedCategories));
  }, [selectedCategories]);

  return (
    <section className="page-section" id="contact">
      <div className="container px-4 px-lg-5">
        <div className="row gx-4 gx-lg-5 justify-content-center">
          <div className="col-lg-8 col-xl-6 text-center">
            <h2 className="mt-0">게시글 작성</h2>
            <hr className="divider" />
            <p className="text-muted mb-5">
              "세상을 변화시키는 일은 작은 시작에서 비롯됩니다. 당신의 작은 기부가 큰 의미를 갖습니다."
            </p>
          </div>
        </div>
        <div className="row gx-4 gx-lg-5 justify-content-center mb-5">
          <div className="col-lg-8 col-xl-6">
            <form onSubmit={handleSubmit}>
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
                      <img
                        key={index}
                        src={URL.createObjectURL(image)}
                        alt={`Preview-${index}`}
                        className="img-preview me-2 mb-2 col-lg-3 col-md-4 col-sm-6"
                        style={{ width: '139px', height: '150px', objectFit: 'cover' }}
                      />
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
                        className={`btn ${
                          selectedCategories.includes(category) ? 'btn-primary' : 'btn-outline-primary'
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
              <button
                className="btn btn-primary btn-xl"
                type="submit"
                style={{ backgroundColor: '#ffa500', borderColor: '#ffa500' }}>
                Submit
              </button>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
};

export default PostForm;