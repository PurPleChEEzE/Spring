<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>Spring 평가</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
        $(document).ready(function() {
            $.ajax({
                url: "/kpi",
                type: "GET",
                success: function(data) {
                    var tbody = '';
                    $.each(data, function(index, kpi) {
                        tbody += '<tr>';
                        tbody += '<td>' + kpi.subKpi + '</td>';
                        tbody += '<td>' + kpi.kpiName + '</td>';
                        tbody += '<td>' + kpi.mainKpi + '</td>';
                        tbody += '</tr>';
                    });
                    $('#kpiTable tbody').html(tbody);
                },
                error: function(error) {
                    console.log("Error:", error);
                }
            });
        });
    </script>
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">KPI Pool</h1>
        <table class="table" id="kpiTable">
            <thead>
                <tr>
                    <th>SUBKPI</th>
                    <th>KPINAME</th>
                    <th>MAINKPI</th>
                </tr>
            </thead>
            <tbody>
            
            </tbody>
        </table>
    </div>
</body>
</html>
