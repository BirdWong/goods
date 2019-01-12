<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/19 0019
  Time: 20:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>登录</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="alternate icon" type="image/png" href="../../assets/i/favicon.png">
    <link rel="stylesheet" href="../../assets/css/amazeui.min.css"/>
    <script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/jsps/js/user/login.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/common.js'/>"></script>
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

        body{

        background-image: url("/images/login-ground.jpg");
            /* 背景图垂直、水平均居中 */

            background-position: center center;

            /* 背景图不平铺 */

            background-repeat: no-repeat;

            /* 当内容高度大于图片高度时，背景图像的位置相对于viewport固定 */
            background-attachment: fixed;  //此条属性必须设置否则可能无效/

            /* 让背景图基于容器大小伸缩 */
        background-size: cover;

            /* 设置背景颜色，背景图加载过程中会显示背景色 */
            background-color: #ffd100;

        }
    </style>

    <script type="text/javascript">
        $(function(){
            var loginname = window.decodeURI("${cookie.loginname.value}");

            if("${requestScope.user.loginname}"){
                loginname = "${requestScope.user.loginname}";
            }
            $("#loginname").val(loginname);

        });

    </script>
</head>
<body>
<div class="abc">

    <script>
        var top2 ;
        //手机端头部,不显示时,可能变量冲突, 可以换个变量名

        var  left = '/images/guangao.png' //左侧对联图片

        var right = '/images/guangao.png' //右侧对联图片

        var auto ;

        var pageWidth =  window.screen.width - 420 ;

        var slidWidth = 200; //左右两侧的图片宽度
        var slidHeight = 300; //左右两侧的图片高度
        var autoHeight = 220; //固定图片高度
        var autoTop = 0; //固定图片距离顶部的位置
        var phoneHeight = 150; //手机顶部图片高度


        var link2 = '#'; //对联左侧链接
        var link3 = '#'; //对联右侧链接


        var style = document.createElement('style');
        style.innerHTML = '#fixed-left,#fixed-right{position: fixed;width:'+ slidWidth +'px;height: '+ slidHeight +'px;margin-top: -'+ slidHeight/2 +'px;z-index:99999;top:50%;}#fixed-left{left:50%;margin-left:-'+ (pageWidth/2+slidWidth) +'px;}#fixed-right{right:50%;margin-right:-'+ (pageWidth/2+slidWidth) +'px;}#fixed-left img,#fixed-top img,#fixed-right img,#fixed-auto img{display: block;width:100%;height: 100%;}#fixed-top,{position: fixed;width: '+ pageWidth +'px;left:50%;margin-left: -'+ pageWidth/2 +'px;}#fixed-top{top:0;height: '+phoneHeight+'px}#fixed-auto{width: 100%;height:'+ autoHeight +'px}.fclose{position: absolute;right: 0;top: 0;background: red;color:#fff;padding:2px 3px;font-size: 14px;}';
        document.head.appendChild(style);

        //对联广告
        var fixedleftright = document.createElement('div');
        fixedleftright.innerHTML = '<div id="fixed-left"><a href="'+ link2 +'"><img src="'+ left +'"  alt=""></a><div id="fclose-left" class="fclose">关闭</div></div><div id="fixed-right"><a href="'+ link3 +'"><img src="'+ right +'"  alt=""></a>\
        <div id="fclose-right" class="fclose">关闭</div></div>';
        document.getElementsByTagName('body')[0].appendChild(fixedleftright);

     if(/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {//在手机上,对联及固定广告都隐藏

            document.getElementById('fixed-left').style.display = "none";
            document.getElementById('fixed-right').style.display = "none";
            
        } else {//电脑上,顶部广告直接隐藏
           
        }
        var fclose = document.getElementsByClassName('fclose');
        for (var i = 0; i < fclose.length; i++){
            fclose[i].onclick = function(){
                this.parentNode.style.display = "none";
            }
        }
    </script>


</div>
<div class="header">
    <div class="am-g">
        <h1>Web ide</h1>
        <p>Integrated Development Environment<br/>代码编辑，代码生成，界面设计，调试，编译</p>
    </div>
    <hr />
</div>
<div class="am-g">
    <div class="am-u-lg-3 am-u-md-8 am-u-sm-centered" style="background-color: #fff;border-radius: 4px;">
        <h3>登录</h3>
        <hr>
        <div class="am-btn-group">
            <c:choose>
                <c:when test="${not empty errors.loginname }">
                    <label id="loginnameError" class="error">${errors.loginname }</label>
                </c:when>
                <c:when test="${not empty errors.loginpass}">
                    <label id="loginpassError" class="error">${errors.loginpass }</label>
                </c:when>

                <c:when test="${not empty errors.verifyCode}">
                    <label id="verifyCodeError" class="error">${errors.verifyCode }</label>
                </c:when>


                <c:when test="${not empty msg}">
                    <label id="msg" class="error">${msg }</label>
                </c:when>

                <c:otherwise>
                    <br>
                </c:otherwise>
            </c:choose>




        </div>



        <form  class="am-form" target="_top" action="<c:url value='/UserServlet'/>" method="post" id="loginForm">
            <input type="hidden" name="method" value="login" />
            <label for="loginname">邮箱:</label>
            <input  class="input" type="text" name="loginname" id="loginname" value="${user.loginname }">
            <br>
            <label for="loginpass">密码:</label>
            <input class="input" type="password" name="loginpass" id="loginpass" value="${user.loginpass }">
            <br>
            <label for="verifyCode">验证码:</label>
            <input class="input" type="text" name="verifyCode" id="verifyCode" value="${user.verifyCode }"/>
            <img id="vCode" src="<c:url value='/VerifyCodeServlet'/>"/>
            <label><a id="verifyCode" href="javascript:_change()">换一张</a></label>
            <br />
            <div class="am-cf">
                <input type="image" id="submit" src="<c:url value='/images/login1.jpg'/>" class="loginBtn am-fl"/>
                <div class="am-btn am-btn-default am-btn-sm am-fr">
                    <a href="<c:url value='/jsps/user/regist.jsp'/>">注册</a>
                </div>
            </div>
        </form>
        <hr>
        <p>© 2014 AllMobilize, Inc. Licensed under MIT license.</p>
    </div>
</div>

</body>
</html>
