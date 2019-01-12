<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>注册页面</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/user/regist.css'/>">

    <script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/jsps/js/user/regist.js'/>"></script>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="alternate icon" type="image/png" href="../../../../assets/i/favicon.png">
    <link rel="stylesheet" href="../../../../assets/css/amazeui.min.css"/>
    <style>
        .header {
            text-align: center;
        }
        .header h1 {
            font-size: 200%;
            color: #333;
            margin-top: 30px;
        }
        .header p {
            font-size: 14px;
        }
    </style>
</head>

<body>


<div class="header">
    <div class="am-g">
        <h1>Web ide</h1>
        <p>Integrated Development Environment<br/>代码编辑，代码生成，界面设计，调试，编译</p>
    </div>
    <hr />
</div>
<div class="am-g">
    <div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
        <h3>注册</h3>
        <hr>
        <div class="am-btn-group">
            <%--<label id="loginnameError" class="error">${errors.loginname }</label>--%>
            <%--<label id="loginpassError" class="error">${errors.loginpass }</label>--%>
            <%--<label id="verifyCodeError" class="error">${errors.verifyCode }</label>--%>
        </div>
        <br>
        <br>

        <form  class="am-form" target="_top" form action="<c:url value='/UserServlet'/>" method="post" id="registForm">
            <input type="hidden" name="method" value="regist"/>
            <label for="loginname">用户名:</label>
            <input class="inputClass" type="text" name="loginname" id="loginname" placeholder="至少3个字符" value="${form.loginname }"/>
            <label class="errorClass" id="loginnameError">${errors.loginname }</label>
            <br>
            <label for="loginpass">登录密码:</label>
            <input class="inputClass" type="password" name="loginpass" id="loginpass" autocomplete="off" placeholder="至少3个字符" value="${form.loginpass }"/>
            <label class="errorClass" id="loginpassError">${errors.loginpass }</label>
            <br>

            <label for="loginpass">确认密码:</label>
            <input class="inputClass" type="password" name="reloginpass" id="reloginpass" autocomplete="off" placeholder="至少3个字符" value="${form.reloginpass }"/>
            <label class="errorClass" id="reloginpassError">${errors.reloginpass}</label>
            <br>

            <label for="email">Email:</label>
            <input class="inputClass" type="text" name="email" id="email" value="${form.email }"/>
            <label class="errorClass" id="emailError">${errors.email}</label>
            <br>



            <label for="verifyCode">验证码:</label>
            <input class="inputClass" type="text" name="verifyCode" id="verifyCode" value="${form.verifyCode }"/>
           <img id="imgVerifyCode" src="<c:url value='/VerifyCodeServlet'/>"/>
            <label><a href="javascript:_hyz()">换一张</a></label>
            <label class="errorClass" id="verifyCodeError">${errors.verifyCode}</label>
            <br />



            <div class="am-cf">
                <input type="image" src="<c:url value='/images/regist1.jpg'/>" id="submitBtn" class="am-fl"/>
                <div class="am-btn am-btn-default am-btn-sm am-fr">
                    <a href="<c:url value='/jsps/user/login.jsp'/>">登录</a>
                </div>
            </div>
        </form>
        <hr>
        <p>© 2014 AllMobilize, Inc. Licensed under MIT license.</p>
    </div>
</div>

</body>
</html>
