<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<html>
<head>
   <title>RESTful API 평가</title>
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>
<body>
<div class="container mt-5">
  <h1 class="mb-4">KPI Pool</h1>
  <input type="text" id="mainKpi" placeholder="mainKpi를 입력해주세요.">
  <button type="button" onclick="getKpiDetail()">제출</button>
  <div id = "kpiDetail"></div>
  
  <script>
        function getKpiDetail() {
            const mainKpi = document.getElementById("mainKpi").value;

            $.ajax({
                type : "GET",
                url : "/kpi/" + mainKpi,
                contentType : "application/json",
                success : function(res) {
                    let kpiDetailListHtml = "<ul>";
                    res.forEach(function(kpi) {
                    	kpiDetailListHtml += "<li>" 
                    	+ kpi.subKpi + " - "
                        + kpi.kpiName + " - " 
                        + kpi.mainKpi + "</li>";
                    });
                    kpiDetailListHtml += "</ul>";
                    $('#kpiDetail').html(kpiDetailListHtml);
                },
                error : function(err) {
                    if (err.status === 404) {
                        $('#kpiDetail').html("<p>해당 mainKpi가 존재하지 않습니다.</p>");
                    } else {
                        alert("");
                    }
                }
            });
        }
    </script>
  
  <!--
  	 
     http://localhost/api/kpi/입력된값
     위의 URL로 비동기 통신하여 입력된 값(mainKpi)에 맞는
     정보(subKpi, kpiName, mainKpi)를 콘솔 로그로 출력하세요.
  -->
  
  
</div>
</body>
</html>