<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<%@ include file="../common/head.jsp"%>
<style>
#message-counter {
	margin-top: 0px;
	float: right;
}
</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<%@ include file="../common/nav.jsp"%>

  <section id="contact">
    <h2>연락하기</h2>
    <p>문의사항이나 연락하고 싶은 내용을 작성하세요.</p>
    <form >
        <label for="name">이름:</label>
        <input type="text" id="name" name="name" onkeyup="checkName()" required>
        <span id="name-msg"></span>

        <label for="email">이메일:</label>
        <input type="email" id="email" name="email" onkeyup="checkEmail()" required>
        <span id="email-msg"></span>

        <label for="message">메시지:</label>
        <textarea id="message" name="message" rows="4" onkeyup="checkMessage()" required></textarea>
        <p id="message-counter">0/500</p>
        
        <button type="submit">전송</button>
    </form>
  </section>

  <script>
    function checkName() {
      const name = document.getElementById("name");

      document.getElementById("name").style.backgroundColor = "green";
      
      $("#name").css("backgroundColor", "green");

      const msg = document.getElementById("name-msg");
      const regx = /^[가-힣]+$/;

      if(regx.test(name.value)) {
        msg.innerHTML = "사용 가능합니다.";
      } else {
        msg.innerHTML = "이름은 한글만 가능합니다.";
      }
    }

    function checkEmail() {
      const email = document.getElementById("email");
      const msg = document.getElementById("email-msg");
      const regx = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,}$/;

      if(regx.test(email.value)) {
        msg.innerHTML = "사용 가능합니다.";
      } else {
        msg.innerHTML = "이메일 양식이 유효하지 않습니다.";
      }
    }

    function checkMessage() {
      const message = document.getElementById("message");
      const counter = document.getElementById("message-counter");
      counter.innerHTML = message.value.length + "/500";
      console.log(message);

      if(message.value.length > 500) {
        console.log("if문 들어옴");
        message.value = message.value.substring(0, 500);
      } 

    }

  </script>

	<%@ include file="../common/footer.jsp"%>
</body>
</html>
