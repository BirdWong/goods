<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<title>管理员登录页面</title>

	<head>
		<meta charset="utf-8" />
		<title></title>

		<link rel="stylesheet" type="text/css" href="/css/H-ui.css"/>
		<!--<link rel="stylesheet" type="text/css" href="css/H-ui.min.css"/>-->
		<!--<link rel="stylesheet" type="text/css" href="css/H-ui.reset.css"/>-->
		<link rel="stylesheet" type="text/css" href="/lib/1.0.9/iconfont.css"/>
		<script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>
		<script src="/lib/jquery/1.9.1/jquery.js"></script>
		<script src="/lib/jquery/1.9.1/jquery.min.js"></script>
		<script src="lib/jquery/1.9.1/jquery-ui.min.js""></script>
        <script src="/js/H-ui.js"></script>
		<script type="text/javascript" src="/js/jquery.SuperSlide.min.js "></script>
	</head>
	<script type="text/javascript">
        function checkForm() {
            if(!$("#adminname").val()) {
                alert("管理员名称不能为空！");
                return false;
            }
            if(!$("#adminpwd").val()) {
                alert("管理员密码不能为空！");
                return false;
            }
            return true;
        }
	</script>
</head>

<body>

<div class="container">
	<article class="page-404 minWP text-c">


		<c:choose>
			<c:when test="${not empty msg}">
				<p class="error-title"><i class="Hui-iconfont va-m">&#xe688;</i>
					<span class="va-m"> 警告</span>
				</p>
				<p class="error-description">${msg}</p>

			</c:when>
			<c:otherwise>
				<p class="error-title"><i class="Hui-iconfont va-m">&#xe648;</i>
					<span class="va-m"> 欢迎</span>
				</p>
				<p class="error-description">请登录</p>

			</c:otherwise>

		</c:choose>

		<form class="form form"  action="<c:url value='/AdminServlet'/>" method="post" onsubmit="return checkForm()">
			<input type="hidden" name="method" value="login"/>

			<div class="row cl">
				<label for="adminname" class="form-label col-xs-4 col-sm-3" >管理员账户：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text col-xs-8" name="adminname" value="" id="adminname"/>
				</div>


			</div>

			<div class="row cl">
				<label for="adminpwd" class="form-label col-xs-4 col-sm-3" >密　　　码：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="password" class="input-text col-xs-8" name="adminpwd" value="" id="adminpwd"/>
				</div>
			</div>


			<div class="row cl">
				<div class="col-xs-8 col-sm-9 ">
					<p class="error-info">您可以：
						<button type="submit" class="btn btn-primary radius">
							进入后台
						</button>


							<button class="btn btn-danger radius" onclick="window.location.href='http://127.0.0.1/index'" type="button">
								去首页
							</button>

					</p>
				</div>
			</div>



		</form>
	</article>

</div>

</body>
</html>
