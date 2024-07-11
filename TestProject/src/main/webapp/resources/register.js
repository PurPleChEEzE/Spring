  function checkid(){
	  const id = document.getElementById("new-userid").value;
	  
	  $.ajax({
		  type :"POST",
		  url : "/member/checkId.do",
		  data : {memberId : id},
		  success : function(res){
		  },
		  error:function(err){
		  }
	  })
  }