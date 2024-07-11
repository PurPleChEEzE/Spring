<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<%@ include file="../../common/head.jsp"%>
</head>
<body>
	<%@ include file="../../common/header.jsp"%>
	<%@ include file="../../common/nav.jsp"%>

	<section class="container mt-4" style="min-height: 90vh">
		<div class="card text-center" style="height: 100%">
			<form action="/news/edit.do" method="POST" enctype="multipart/form-data">
				<input type="hidden" name="boardNo" value="${result.boardNo}">
				<input type ="hidden" name = "memberNo" value = "${result.memberNo}">

				<div class="card-header">
				<input type = "text" name = "boardTitle" value = "${result.boardTitle}" required>
				</div>
				<div class="card-body">
					<div class="d-flex justify-content-center mb-3">
						<div class="mx-3">
							작성자: <span id="nb-writer">${result.memberName }</span>
						</div>
						<div class="mx-3">
							작성일: <span id="nb-date">${result.boardIndate }</span>
						</div>
						<div class="mx-3">
							조회수: <span id="nb-views">${result.boardViews+1 }</span>
						</div>
					</div>
					<hr>
					<div style="margin-top: 20px; margin-bottom: 20px;">
						<c:choose>

								<c:when test="${not empty result.uploadName}">
									<c:choose>
										<c:when	test="${fn:endsWith(result.uploadName, '.mp4') or fn:endsWith(result.uploadName, '.avi') or fn:endsWith(result.uploadName, '.mov')}">
											<video controls 
												style=" height: 300px;">
												<source src="${result.uploadPath}${result.uploadName}">
											</video>
										</c:when>
										<c:otherwise>
											<img src="${result.uploadPath}${result.uploadName}"
												 alt="뉴스 이미지"
												style=" height: 300px;" />
										</c:otherwise>
									</c:choose>


								</c:when>
								<c:otherwise>
									<img src="/resources/download.jpg" 
										alt="뉴스 이미지" style=" height: 400px;" />
								</c:otherwise>
							</c:choose>
						
						<textarea name = "boardContent" required>${result.boardContent}</textarea>
						<p class="card-text">${result.boardContent }</p>
					</div>
					
					<input type = "file" name = "upload">
				</div>
				<div class="card-footer d-flex justify-content-center">
					<button type="button" class="btn btn-secondary mx-2"
						onclick="window.history.back()">뒤로가기</button>

					<c:if test="${loginMemberNo == result.memberNo }">
						<button type="submit" class="btn btn-primary mx-2">수정</button>
					</c:if>
				</div>

			</form>
		</div>
	</section>

	<%@ include file="../../common/footer.jsp"%>
</body>
</html>
