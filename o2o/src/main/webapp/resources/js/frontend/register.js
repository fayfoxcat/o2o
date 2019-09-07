/**
 * 
 */
$(function(){
	var registerUrl = "";
	var loginUrl = "";
	
	
	
	
	
	
	
	
	$("#change1").click(function(){
		$("#choice-1").css("display","inline");
		$("#choice-2").css("display","inline");
		$("#choice-3").css("display","inline");
		$("#choice-4").hide();
		$("#choice-5").css("display","inline");
		$(".title").text("注册");
	});
	
	$("#change2").click(function(){
		$("#choice-1").css("display","none");
		$("#choice-2").css("display","none");
		$("#choice-3").css("display","none");
		$("#choice-4").show();
		$("#choice-5").css("display","none");
		$(".title").text("登录");
	});
	
})


