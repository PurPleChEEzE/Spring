<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<link>
<%@ include file="../../common/head.jsp"%>
</head>
<body>
	<%@ include file="../../common/header.jsp"%>
	<%@ include file="../../common/nav.jsp"%>

 <section class="container mt-4" style="height: 70vh">
    <div class="card text-center" style="height: 100%">
    	<form action = "/free/editForm.do" method = "POST">
    	<input type="hidden" name = "boardNo" value = "${result.boardNo}">
    	
        <div class="card-header">
            <h2 id="fb-title">${result.boardTitle}</h2>
        </div>
        <div class="card-body">
            <div class="d-flex justify-content-center mb-3">
                <div class="mx-3">작성자: <span id="fb-writer">${result.memberName }</span></div>						
                <div class="mx-3">작성일: <span id="fb-date">${result.boardIndate }</span></div>
                <div class="mx-3">조회수: <span id="fb-views">${result.boardViews }</span></div>
            </div>
            <hr> 
            <div style="margin-top:20px; margin-bottom: 20px;">
            <c:if test = "${not empty result.uploadName}">
            	<img src="${result.uploadPath}${result.uploadName }" 
            		width="300px" height="300px">
            </c:if>
                <p class="card-text">
                    ${result.boardContent }
                </p>
            </div>
        </div>
        <div class="card-footer d-flex justify-content-center">
            <button type="button" class="btn btn-secondary mx-2" onclick="window.history.back()">뒤로가기</button>						
	            
	            <c:if test = "${loginMemberNo == result.memberNo }">
	            	<button type="submit" class="btn btn-primary mx-2" >수정</button>
	            	<button type="button" class="btn btn-danger mx-2" onclick="location.href='/free/delete.do?boardNo=${result.boardNo}&memberNo=${result.memberNo}'">삭제</button>
	            </c:if>
        </div>
        
        </form>
    </div>
</section>
	<%@ include file="../../common/footer.jsp"%>
</body>
</html>
