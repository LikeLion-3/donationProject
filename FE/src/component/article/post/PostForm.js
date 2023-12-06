import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'simplelightbox/dist/simple-lightbox.min.css';
import './PostForm.css';

const PostForm = () => {
  const [images, setImages] = useState([]);
  const [categories, setCategories] = useState([
    '의류',
    '의료품',
    '식품',
    '잡화',
    '기타',
  ]);
  const [selectedCategories, setSelectedCategories] = useState(
    JSON.parse(localStorage.getItem('selectedCategories')) || []
  );
  const [title, setTitle] = useState(localStorage.getItem('postTitle') || '');
  const [content, setContent] = useState(
    localStorage.getItem('postContent') || ''
  );

  useEffect(() => {
    // Navbar shrink function
    const navbarShrink = () => {
      const navbarCollapsible = document.body.querySelector('#mainNav');
      if (!navbarCollapsible) {
        return;
      }
      if (window.scrollY === 0) {
        navbarCollapsible.classList.remove('navbar-shrink');
      } else {
        navbarCollapsible.classList.add('navbar-shrink');
      }
    };
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

      // Append images to formData
      images.forEach((image, index) => {
        formData.append(`imageFiles`, image);
      });

      // Append DTO data to formData
      formData.append('title', title);
      formData.append('content', content);
      formData.append('prodCategory', JSON.stringify(selectedCategories));

      // Use fetch or axios to send formData with images and DTO to the server
      const response = await fetch('http://localhost:8080/api/articles', {
        method: 'POST',
        body: formData,
        enctype: "multipart/form-data",
        contentType: false
      });

      // Handle the server response as needed
      const data = await response.json();
      console.log('Server response:', data);

      // Clear form data if submission is successful
      setImages([]);
      setTitle('');
      setContent('');
      setSelectedCategories([]);
    } catch (error) {
      console.error('Error submitting form:', error);
    }
  };

  // Save selectedCategories to localStorage when it changes
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
            {/* Form and other HTML content go here */}
            <form onSubmit={handleSubmit}>
              {/* Image input */}
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
                  multiple // 여러 이미지를 선택 가능하도록 함
                />
                {/* 이미지 미리보기 */}
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
              {/* Category input */}
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

              {/* Title input */}
              <div className="form-floating mb-3">
                <input
                  type="text"
                  className="form-control"
                  id="title"
                  placeholder="글 제목을 입력해주세요."
                  value={title}
                  onChange={handleTitleChange}
                  // Add any necessary validation or attributes
                />
                <label className="form-label" style={{ color: 'dimgray' }}>글 제목</label>
              </div>
              {/* Content input */}
              <div className="form-floating mb-3">
                <textarea
                  className="form-control"
                  id="content"
                  placeholder="글 내용을 입력해주세요"
                  value={content}
                  onChange={handleContentChange}
                  style={{ height: '10rem' }}
                  // Add any necessary validation or attributes
                ></textarea>
                <label className="form-label" style={{ color: 'dimgray' }}>글 내용</label>
              </div>
              {/* Submit Button */}
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