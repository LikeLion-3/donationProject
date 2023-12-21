import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import './CommunityList.css';
import { CommunityCategoryStyles } from '../CommunityCategoryStyles';
import Pagination from "react-js-pagination";
import { useSelector } from 'react-redux';
import jwtAxios from '../../util/jwtUtil';
import { API_SERVER_HOST } from "../../util/jwtUtil"
import axios from 'axios';


const CommunityList = () => {
	const [posts, setPosts] = useState([]);
	const [sortOrder, setSortOrder] = useState('recent');
	const [currentPage, setCurrentPage] = useState(1);
	const [totalPages, setTotalPages] = useState(0);
	const [page, setPage] = useState(0);
	const [totalCnt, setTotalCnt] = useState(0);
	const [pageRange, setPageRange] = useState(0);
	const loginState = useSelector(state => state.loginSlice);

	const noPostImageUrl = "https://warmwave-bucket.s3.ap-northeast-2.amazonaws.com/common/%E1%84%83%E1%85%B3%E1%86%BC%E1%84%85%E1%85%A9%E1%86%A8%E1%84%83%E1%85%AC%E1%86%AB+%E1%84%80%E1%85%A6%E1%84%89%E1%85%B5%E1%84%80%E1%85%B3%E1%86%AF%E1%84%8B%E1%85%B5+%E1%84%8B%E1%85%A5%E1%86%B9%E1%84%89%E1%85%B3%E1%86%B8%E1%84%82%E1%85%B5%E1%84%83%E1%85%A1.jpg";


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
				const response = await axios.get(`${API_SERVER_HOST}/api/communities?page=${currentPage - 1}&size=12&sort=${sortOrder}`);
				const data = await response.data;
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
									<button className="btn " onClick={() => handleSort('recent')} style={{ backgroundColor: '#FABA96', borderColor: '#fff', color: "#FFFFFF", marginRight: '8px' }}>최신순</button>
									<button className="btn btn-primary" onClick={() => handleSort('popular')} style={{ backgroundColor: '#FABA96', borderColor: '#fff', color: "#FFFFFF" }}>조회순</button>
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
								{loginState.id &&

									<Link to="/community/write">
										<button
											className="btn btn-primary btn-xl"
											type="submit"
											style={{ backgroundColor: '#FABA96', borderColor: '#FABA96' }}>
											글 작성
										</button>
									</Link>
								}
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
										<td>
											<span
												style={{
													...CommunityCategoryStyles[post.category],
													cursor: 'pointer',
													padding: '0.4em 0.6em', // 패딩을 줄여서 뱃지 크기 감소
													fontSize: '0.8rem', // 폰트 크기 감소
													borderRadius: '0.25rem' // 둥근 모서리 반경 감소
												}}
											>
												{post.category}
											</span>
										</td>										<td style={{ textAlign: 'center' }}>{post.writer}</td>
										<td><Link to={`/community/${post.id}`} className="black-link">{post.title}</Link></td>
										<td style={{ textAlign: 'center' }}>{formatDate(post.createdAt)}</td>
										<td style={{ textAlign: 'center' }}>{post.hit}</td>
									</tr>
								))}
							</tbody>
						</table>
						<div>
							{posts.length === 0 ? (
								// 게시글이 없을 때 표시할 이미지
								<div className="text-center">
									<img src={noPostImageUrl} alt="No posts available" style={{ maxWidth: '85%', height: 'auto' }} />
								</div>
							) : (
								// 게시글이 있을 때 표시할 내용
								<table className="table table-hover">
									{/* 테이블 내용 */}
								</table>
							)}
						</div>
					</div>
				</div>
			</div>
		</section>
	);
};

export default CommunityList;