<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<script>
	function alertFunction(text, title, icon) {
		Swal.fire({
			icon : icon,
			title : title,
			text : text
		});
	}

	if (`${text}` != ``) {
		$().ready(function() {
			alertFunction(`${text}`, `${title}` , `${icon}`);
		})
	};
	
	
	
</script>