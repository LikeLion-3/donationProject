import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import './CommunityList.css';
import Pagination from "react-js-pagination";


const CommunityList = () => {
	const [posts, setPosts] = useState([]);
	const [sortOrder, setSortOrder] = useState('recent');
	const [currentPage, setCurrentPage] = useState(1);
	const [totalPages, setTotalPages] = useState(0);
	const [page, setPage] = useState(0);
	const [totalCnt, setTotalCnt] = useState(0);
	const [pageRange, setPageRange] = useState(0);

	useEffect(() => {
		const calculatePageRange = () => {
			// 현재 페이지 그룹 계산
			const currentPageGroup = Math.ceil(currentPage / 5);

			// 페이지 그룹에 따른 시작 페이지 및 끝 페이지 계산
			let startPage = (currentPageGroup - 1) * 5 + 1;
			let endPage = startPage + 4;

			// 끝 페이지가 총 페이지 수를 초과하지 않도록 조정
			endPage = Math.min(endPage, totalPages);

			// 페이지 범위 생성
			return Array.from({ length: (endPage - startPage + 1) }, (_, i) => startPage + i);
		};

		setPageRange(calculatePageRange());

		const fetchData = async () => {
			try {
				console.log(currentPage);
				const response = await fetch(`http://localhost:8080/api/communities?page=${currentPage - 1}&size=12&sort=${sortOrder}`);
				const data = await response.json();
				console.log(data);
				setPosts(data.content);
				setTotalPages(data.totalPages);
				setTotalCnt(data.totalElements);
				setPage(data.number + 1); // 서버는 페이지를 0부터 시작하지만, UI에서는 1부터 시작
			} catch (error) {
				console.error('Error fetching data:', error);
			}
		};

		fetchData();
	}, [sortOrder, currentPage, totalPages]);

	const handleSort = (order) => {
		setSortOrder(order);
	};

	const handlePageChange = (pageNumber) => {
		setCurrentPage(pageNumber);
	};

	const formatDate = (dateString) => {
		const date = new Date(dateString);
		const today = new Date();
		if (date.toDateString() === today.toDateString()) {
			return date.toLocaleTimeString('ko-KR', {
				hour: '2-digit',
				minute: '2-digit',
				hour12: false
			});
		} else {
			return date.toLocaleDateString('ko-KR', {
				year: '2-digit',
				month: '2-digit',
				day: '2-digit'
			}).slice(0, -1);
		}
	};

	return (
		<section className="community-list-page-section" id="contact">
			<div className="container mt-5">
				<div className="row">
					<div className="col-12 col-lg-10 mx-auto">
						<div className="mb-3">
							<div className="d-flex justify-content-between align-items-center mb-3">
								<div>
									<button className="btn " onClick={() => handleSort('recent')} style={{ backgroundColor: '#FABA96', borderColor: '#fff', color: "#FFFFFF" }}>최신순</button>
									<button className="btn btn-primary" onClick={() => handleSort('popular')} style={{ backgroundColor: '#FABA96', borderColor: '#fff', color: "#FFFFFF" }}>조회순</button>
								</div>
								<Pagination
									activePage={page}
									itemsCountPerPage={12} // 한 페이지에 표시되는 아이템 수
									totalItemsCount={totalCnt}
									pageRangeDisplayed={5} // 한 번에 보여지는 페이지 범위
									prevPageText={"‹"}
									nextPageText={"›"}
									onChange={handlePageChange}
								/>
							</div>
						</div>
						<table className="table table-hover">
							<colgroup>
								<col style={{ width: '8%' }} />
								<col style={{ width: '8%' }} />
								<col />
								<col style={{ width: '8%' }} />
								<col style={{ width: '6%' }} />
							</colgroup>
							<thead>
								<tr>
									{/* 카테고리 작성자 제목 */}
									<th scope="col">카테고리</th>
									<th scope="col">글쓴이</th>
									<th scope="col">제목</th>
									<th scope="col">작성일</th>
									<th scope="col">조회</th>
								</tr>
							</thead>
							<tbody>
								{posts.map(post => (
									<tr key={post.id}>
										<td>{post.category}</td>
										<td style={{ textAlign: 'center' }}>{post.writer}</td>
										<td><Link to={`/communities/${post.id}`} className="black-link">{post.title}</Link></td>
										<td style={{ textAlign: 'center' }}>{formatDate(post.createdAt)}</td>
										<td style={{ textAlign: 'center' }}>{post.hit}</td>
									</tr>
								))}
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</section>
	);
};

export default CommunityList;

// // pageable": {
//   "pageNumber": 0,
//   "pageSize": 20,
//   "sort": {
//       "empty": true,
//       "sorted": false,
//       "unsorted": true
//   },
//   "offset": 0,
//   "paged": true,
//   "unpaged": false
// },
// "totalElements": 66,
// "totalPages": 4,
// "last": false,
// "size": 20,
// "number": 0,
// "sort": {
//   "empty": true,
//   "sorted": false,
//   "unsorted": true
// },
// "first": true,
// "numberOfElements": 21,
// "empty": false
