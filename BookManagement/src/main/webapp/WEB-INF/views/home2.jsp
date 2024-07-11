<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>도서 관리 시스템</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
    <h1>도서 관리 시스템</h1>

    <h2>1. 도서 전체 목록 조회</h2>
    <button onclick="getList()">전체 목록 조회</button>
    <div id="bookList"></div>
    <script>
        function getList() {
            $.ajax({
                type : "GET",
                url : "/books",
                contentType : "application/json",
                success : function(res) {
                    let bookListHtml = "<ul>";
                    res.forEach(function(book) {
                        bookListHtml += "<li>" + book.bookId + " - "
                                + book.bookTitle + " - " + book.bookAuthor
                                + " - " + book.bookStatus + " - "
                                + book.bookIndate + "</li>";
                    });
                    bookListHtml += "</ul>";
                    $('#bookList').html(bookListHtml);
                },
                error : function(err) {
                    alert("도서 목록을 불러오는 데 실패했습니다.");
                }
            });
        }
    </script>

    <br>
    <hr>
    <br>

    <h2>2. 특정 도서 조회</h2>
    <input type="text" id="bookTitle" placeholder="도서 제목을 입력해주세요.">
    <button onclick="getDetailList()">조회</button>
    <div id="bookDetailList"></div>

    <script>
        function getDetailList() {
            const bookTitle = document.getElementById("bookTitle").value;

            $.ajax({
                type : "GET",
                url : "/books/" + encodeURIComponent(bookTitle),
                contentType : "application/json",
                success : function(res) {
                    let bookDetailListHtml = "<ul>";
                    res.forEach(function(book) {
                        bookDetailListHtml += "<li>" + book.bookId + " - "
                                + book.bookTitle + " - " + book.bookAuthor
                                + " - " + book.bookStatus + " - "
                                + book.bookIndate + "</li>";
                    });
                    bookDetailListHtml += "</ul>";
                    $('#bookDetailList').html(bookDetailListHtml);
                },
                error : function(err) {
                    if (err.status === 404) {
                        $('#bookDetailList').html("<p>해당 도서가 존재하지 않습니다.</p>");
                    } else {
                        alert("도서 목록을 불러오는 데 실패했습니다.");
                    }
                }
            });
        }
    </script>
    <br>
    <hr>
    <br>

    <h2>3. 도서 대여</h2>
    <input type="text" id="borrowBookTitle" placeholder="대여할 도서 제목을 입력해주세요.">
    <button onclick="borrowBook()">대여</button>
    <div id="borrowResult"></div>

    <script>
        function borrowBook() {
            const bookTitle = document.getElementById("borrowBookTitle").value;

            $.ajax({
                type : "POST",
                url : "/books/" + encodeURIComponent(bookTitle) + "/borrow",
                contentType : "application/json",
                success : function(response) {
                    $('#borrowResult').html("<p>" + response + "</p>");
                },
                error : function(err) {
                    if (err.status === 409) {
                        $('#borrowResult').html("<p>이미 대여된 책이거나 오류가 발생했습니다.</p>");
                    } else if (err.status === 404) {
                        $('#borrowResult').html("<p>해당 도서가 존재하지 않습니다.</p>");
                    } else {
                        $('#borrowResult').html("<p>오류가 발생했습니다.</p>");
                    }
                }
            });
        }
    </script>

    <br>
    <hr>
    <br>

    <h2>4. 도서 반납</h2>
    <input type="text" id="returnBookTitle" placeholder="반납할 도서 제목을 입력해주세요.">
    <button onclick="returnBook()">반납</button>
    <div id="returnResult"></div>

    <script>
        function returnBook() {
            const bookTitle = document.getElementById("returnBookTitle").value;

            $.ajax({
                type: "PUT",
                url: "/books/" + encodeURIComponent(bookTitle) + "/return",
                contentType: "application/json",
                success: function(response) {
                    $('#returnResult').html("<p>" + response + "</p>");
                },
                error: function(err) {
                    if (err.status === 409) {
                        $('#returnResult').html("<p>이미 반납된 책이거나 오류가 발생했습니다.</p>");
                    } else if (err.status === 404) {
                        $('#returnResult').html("<p>해당 도서가 존재하지 않습니다.</p>");
                    } else {
                        $('#returnResult').html("<p>오류가 발생했습니다.</p>");
                    }
                }
            });
        }
    </script>
</body>
</html>
