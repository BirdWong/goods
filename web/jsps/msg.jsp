<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title></title>
	
	    <link rel="stylesheet" type="text/css" href="css/H-ui.css"/>
	    <!--<link rel="stylesheet" type="text/css" href="css/H-ui.min.css"/>-->
	    <!--<link rel="stylesheet" type="text/css" href="css/H-ui.reset.css"/>-->
	    <link rel="stylesheet" type="text/css" href="lib/1.0.9/iconfont.css"/>
	    <script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
	    <script src="/lib/jquery/1.9.1/jquery.js"></script>
	    <script src="/lib/jquery/1.9.1/jquery.min.js"></script>
	    <script src="lib/jquery/1.9.1/jquery-ui.min.js""></script>
	    <script src="js/H-ui.js"></script>
	    <script type="text/javascript" src="js/jquery.SuperSlide.min.js "></script>
	</head>
	<body>
	
	
		<header class="navbar-wrapper">
		    <div class="navbar navbar-black" style="background-color: #0e90d2;">
		        <div class="container cl">
		            <a class="logo navbar-logo f-l mr-10 hidden-xs" href="/index">图书商城</a>
		            <a aria-hidden="false" class="nav-toggle Hui-iconfont visible-xs" href="javascript:;">&#xe667;</a>
		            <nav class="nav navbar-nav nav-collapse" role="navigation" id="Hui-navbar">
		
	
		                <c:choose>
		                    <c:when test="${empty sessionScope.sessionUser }">
		
		                    <ul class="r">
		                        <li><a href="<c:url value='/jsps/user/login.jsp'/>" target="_parent">会员登录</a></li>
		                        <li><a href="<c:url value='/jsps/user/regist.jsp'/>" target="_parent">注册会员</a></li>
		                    </ul>
		
		
		                    </c:when>
		                    <c:otherwise>
		
		                        <ul class="r">
		                            <li><a href="<c:url value='/CartItemServlet?method=myCart'/>">我的购物车</a></li>
		                            <li><a href="<c:url value='/OrderServlet?method=myOrder'/>">我的订单</a></li>
		                            <li class="dropDown dropDown_hover"><a href="#" class="dropDown_A">${sessionScope.sessionUser.loginname} <i class="Hui-iconfont">&#xe6d5;</i></a>
		                                <ul class="dropDown-menu menu radius box-shadow">
		                                    <li><a href="<c:url value='/jsps/user/pwd.jsp'/>">修改密码</a></li>
		                                    <li><a href="<c:url value='/UserServlet?method=quit'/>">注销</a></li>
		                                </ul>
		                            </li>
		                            <li>

                                <script>
                                    function modaldemo(){
                                        $("#modal-demo").modal("show")
                                    }
                                </script>
                                <a onClick="modaldemo()">高级搜索</a>
                                <div id="modal-demo" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="color: #000;">
                                    <div class="modal-dialog">
                                        <div class="modal-content radius">
                                            <div class="modal-header">
                                                <h3 class="modal-title">高级搜索</h3>
                                                <a class="close" data-dismiss="modal" aria-hidden="true" href="javascript:void();">×</a>
                                            </div>
                                            <form action="<c:url value='/BookServlet'/>" method="get" class="form form-horizontal responsive">
                                                <input type="hidden" name="method" value="findByCombination"/>

                                                <div class="modal-body">
                                                    <div class="row cl">
                                                        <label for="bname" class="form-label col-xs-3">书名：</label>
                                                        <div class="formControls  col-xs-8">
                                                            <input type="text" class="input-text" name="bname"  id="bname"/>
                                                        </div>

                                                    </div>


                                                    <div class="row cl">
                                                        <label for="author" class="form-label col-xs-3" >作者：</label>
                                                        <div class="formControls col-xs-8">
                                                            <input type="text" class="input-text" name="author" id="author" />
                                                        </div>

                                                    </div>


                                                    <div class="row cl">
                                                        <label for="press" class="form-label  col-xs-3">出版社：</label>
                                                        <div class="formControls col-xs-8">
                                                            <input type="text" class="input-text" name="press" id="press" />
                                                        </div>

                                                    </div>


                                                </div>
                                                <div class="modal-footer">
                                                    <button class="btn btn-primary" type="submit">确定</button>
                                                    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </li>
		                        </ul>
		                    </c:otherwise>
		                </c:choose>
	
		
		            </nav>
		            <nav class="navbar-userbar hidden-xs">
		            </nav>
		        </div>
		    </div>
		</header>
		
		<div class="container">
			<article class="page-404 minWP text-c">
				
				<c:choose>
				  	<c:when test="${code eq 'success' }"><%--如果code是success 它显示正确的图片和文字 --%>
				  		<p class="error-title"><i class="Hui-iconfont va-m">&#xe648;</i>
				  			<span class="va-m"> 成功</span>
						</p>
				  	</c:when>
				  	<c:when test="${code eq 'error' }"><%--如果code是error  它显示的是错误的图片和文字  --%>
				  		<p class="error-title"><i class="Hui-iconfont va-m">&#xe688;</i>
							<span class="va-m"> 失败</span>
						</p>
				  	</c:when>
				  	
				</c:choose>
				
				<p class="error-description">${msg }</p>
				<p class="error-info">您可以：
					
					<c:if test="${status < 0 }">
						<span style="margin-left: 50px;">
							<a target="_top" href="<c:url value='/jsps/user/login.jsp'/>">登录</a><span class="ml-20">|</span>
						</span>
					</c:if>


					<c:choose>
						<c:when test="${empty email}">
							<a href="javascript:;" onclick="history.go(-1)" class="c-primary">&lt; 返回上一页</a>
						</c:when>
						<c:otherwise>
							<a target="_blank" href="<c:url value='${email }'/>">&lt;${emailName }</a>
						</c:otherwise>
					</c:choose>

					<span class="ml-20">|</span>
					<a href="/index" class="c-primary ml-20">去首页 &gt;</a>
				</p>
			</article>
		</div>
		
	</body>
</html>
