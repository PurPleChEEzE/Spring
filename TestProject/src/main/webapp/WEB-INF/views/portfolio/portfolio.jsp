<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<%@ include file="../common/head.jsp"%>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<%@ include file="../common/nav.jsp"%>

	<section>
		<h2>포트폴리오</h2>
		<div class="project">
			<div class="input-area" id="input-area">
				<input type="text" id="project-title" placeholder="프로젝트 제목 입력">
				<textarea id="project-description" placeholder="프로젝트 설명 입력"></textarea>
				<button id="add-project-button">새로운 프로젝트 추가하기</button>
			</div>
		</div>
	</section>

	<%@ include file="../common/footer.jsp"%>
</body>
</html>

<script>
	document.getElementById("add-project-button").addEventListener(
			'click',
			function() {
				const projectTitle = document.getElementById("project-title"); // 제목
				const projectDescription = document
						.getElementById("project-description"); // 설명
				const inputArea = document.getElementById("input-area"); // div 태그

				if (projectTitle.value.length > 100) {
					alert("제목이 너무 깁니다.");
				} else if (projectTitle.value === "") {
					alert("제목을 입력해주세요.");
				} else if (projectDescription.value === "") {
					alert("내용을 입력해주세요.");
				}

				const h3Element = document.createElement("h3"); // h3 요소 생성
				h3Element.innerHTML = projectTitle.value;

				const pDes = document.createElement("p") // p 요소 생성
				pDes.innerHTML = projectDescription.value;

				inputArea.appendChild(h3Element);
				inputArea.appendChild(pDes);
			})
</script>


