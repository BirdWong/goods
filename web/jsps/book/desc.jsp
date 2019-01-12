<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title></title>
	<link rel="stylesheet" href="/css/admin.css" />
	<link rel="stylesheet" type="text/css" href="/css/amazeui.min.css"/>
	<link rel="stylesheet" type="text/css" href="/css/H-ui.css"/>
	<link rel="stylesheet" type="text/css" href="/lib/1.0.9/iconfont.css"/>
	<script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>
	<script src="/lib/jquery/1.9.1/jquery.js"></script>
	<script src="/lib/jquery/1.9.1/jquery.min.js"></script>
	<script src="/lib/jquery/1.9.1/jquery-ui.min.js""></script>
    <script src="/js/H-ui.js"></script>
	<script type="text/javascript" src="/js/jquery.SuperSlide.min.js "></script>
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/book/desc.css'/>">
	<script src="<c:url value='/jsps/js/book/desc.js'/>"></script>
</head>
<body >



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


        // 固定广告
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



<div style="padding-top:5em ;"></div>
<div class="container">

	<div class="divBookName">${book.bname }</div>
	<div>
		<img align="top" src="<c:url value='/${book.image_w }'/>" class="img_image_w"/>
		<div class="divBookDesc">
			<ul>
				<li>商品编号：${book.bid }</li>
				<li>特价：<span class="price_n">&yen;${book.currPrice }</span></li>
				<li>定价：<span class="spanPrice">&yen;${book.price }</span>　折扣：<span style="color: #c30;">${book.discount }</span>折</li>
			</ul>
			<hr class="hr1"/>
			<table>
				<tr>
					<td colspan="3">
						作者：${book.author } 著
					</td>
				</tr>
				<tr>
					<td colspan="3">
						出版社：${book.press }
					</td>
				</tr>
				<tr>
					<td colspan="3">出版时间：${book.publishtime }</td>
				</tr>
				<tr>
					<td>版次：${book.edition }</td>
					<td>页数：${book.pageNum }</td>
					<td>字数：${book.wordNum }</td>
				</tr>
				<tr>
					<td width="180">印刷时间：${book.printtime }</td>
					<td>开本：${book.booksize } 开</td>
					<td>纸张：${book.paper }</td>
				</tr>
			</table>
			<div class="divForm">
				<form id="form1" action="<c:url value='/CartItemServlet'/>" method="post">
					<input type="hidden" name="method" value="add"/>
					<input type="hidden" name="bid" value="${book.bid }"/>
					我要买：<input id="cnt" style="width: 40px;text-align: center;" type="text" name="quantity" value="1"/>件
				</form>
				<a id="btn" href="javascript:$('#form1').submit();"></a>
			</div>
		</div>
	</div>

</div>
</body>
</html>
