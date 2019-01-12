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
	    <!--<link rel="stylesheet" type="text/css" href="css/H-ui.min.css"/>-->
	    <!--<link rel="stylesheet" type="text/css" href="css/H-ui.reset.css"/>-->
	    <link rel="stylesheet" type="text/css" href="/lib/1.0.9/iconfont.css"/>
	    <script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>
	    <script src="/lib/jquery/1.9.1/jquery.js"></script>
	    <script src="/lib/jquery/1.9.1/jquery.min.js"></script>
	    <script src="lib/jquery/1.9.1/jquery-ui.min.js"></script>
	    <script src="/js/H-ui.js"></script>
	    <script type="text/javascript" src="/js/jquery.SuperSlide.min.js "></script>
	    <link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/order/pay.css'/>">
	    
	    <script type="text/javascript">
		$(function() {
			$("img").click(function() {
				$("#" + $(this).attr("name")).attr("checked", true);
			});
		});





	</script>

		<script>
            function modalbank(){
                $("#modal-bank").modal("show")
            };

		</script>


		<script>
            function modalalipay(){
                $("#pay").attr("src", "/OrderServlet?method=alipayment&oid=${order.oid}");
                $("#modal-alipay").modal("show");

            };

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
	
	
		<div class="container">
				<div class="divContent">
					<span class="spanPrice">支付金额：</span><span class="price_t">&yen;${order.total }</span>
					<span class="spanOid">编号：${order.oid }</span>
				</div>

			<br>
				<div class="container">
					<div class="col-lg-offset-3">

						<button class="btn radius btn-primary size-L" onClick="modalbank()">易贝支付</button>
						<div id="modal-bank" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-dialog container" style="width: auto;height: auto; text-align: center">
								<div class="modal-content radius">
									<div class="modal-header">
										<h3 class="modal-title">选择银行</h3>
										<a class="close" data-dismiss="modal" aria-hidden="true" href="javascript:void();">×</a>
									</div>
									<div class="modal-body">
										<form action="<c:url value='/OrderServlet'/>" method="post" id="form1" target="_top">
											<input type="hidden" name="method" value="payment"/>
											<input type="hidden" name="oid" value="${order.oid }"/>
											<div class="divBank">
												<div class="divText">选择网上银行</div>
												<div style="margin-left: 20px;">
													<div style="margin-bottom: 20px;">
														<input id="ICBC-NET-B2C" type="radio" name="yh" value="ICBC-NET-B2C" checked="checked"/>
														<img name="ICBC-NET-B2C" align="middle" src="<c:url value='/bank_img/icbc.bmp'/>"/>

														<input id="CMBCHINA-NET-B2C" type="radio" name="yh" value="CMBCHINA-NET-B2C"/>
														<img name="CMBCHINA-NET-B2C" align="middle" src="<c:url value='/bank_img/cmb.bmp'/>"/>

														<input id="ABC-NET-B2C" type="radio" name="yh" value="ABC-NET-B2C"/>
														<img name="ABC-NET-B2C" align="middle" src="<c:url value='/bank_img/abc.bmp'/>"/>

														<input id="CCB-NET-B2C" type="radio" name="yh" value="CCB-NET-B2C"/>
														<img name="CCB-NET-B2C" align="middle" src="<c:url value='/bank_img/ccb.bmp'/>"/>
													</div>
													<div style="margin-bottom: 20px;">
														<input id="BCCB-NET-B2C" type="radio" name="yh" value="BCCB-NET-B2C"/>
														<img name="BCCB-NET-B2C" align="middle" src="<c:url value='/bank_img/bj.bmp'/>"/>

														<input id="BOCO-NET-B2C" type="radio" name="yh" value="BOCO-NET-B2C"/>
														<img name="BOCO-NET-B2C" align="middle" src="<c:url value='/bank_img/bcc.bmp'/>"/>

														<input id="CIB-NET-B2C" type="radio" name="yh" value="CIB-NET-B2C"/>
														<img name="CIB-NET-B2C" align="middle" src="<c:url value='/bank_img/cib.bmp'/>"/>

														<input id="NJCB-NET-B2C" type="radio" name="yh" value="NJCB-NET-B2C"/>
														<img name="NJCB-NET-B2C" align="middle" src="<c:url value='/bank_img/nanjing.bmp'/>"/>
													</div>
													<div style="margin-bottom: 20px;">
														<input id="CMBC-NET-B2C" type="radio" name="yh" value="CMBC-NET-B2C"/>
														<img name="CMBC-NET-B2C" align="middle" src="<c:url value='/bank_img/cmbc.bmp'/>"/>

														<input id="CEB-NET-B2C" type="radio" name="yh" value="CEB-NET-B2C"/>
														<img name="CEB-NET-B2C" align="middle" src="<c:url value='/bank_img/guangda.bmp'/>"/>

														<input id="BOC-NET-B2C" type="radio" name="yh" value="BOC-NET-B2C"/>
														<img name="BOC-NET-B2C" align="middle" src="<c:url value='/bank_img/bc.bmp'/>"/>

														<input id="PINGANBANK-NET" type="radio" name="yh" value="PINGANBANK-NET"/>
														<img name="PINGANBANK-NET" align="middle" src="<c:url value='/bank_img/pingan.bmp'/>"/>
													</div>
													<div style="margin-bottom: 20px;">
														<input id="CBHB-NET-B2C" type="radio" name="yh" value="CBHB-NET-B2C"/>
														<img name="CBHB-NET-B2C" align="middle" src="<c:url value='/bank_img/bh.bmp'/>"/>

														<input id="HKBEA-NET-B2C" type="radio" name="yh" value="HKBEA-NET-B2C"/>
														<img name="HKBEA-NET-B2C" align="middle" src="<c:url value='/bank_img/dy.bmp'/>"/>

														<input id="NBCB-NET-B2C" type="radio" name="yh" value="NBCB-NET-B2C"/>
														<img name="NBCB-NET-B2C" align="middle" src="<c:url value='/bank_img/ningbo.bmp'/>"/>

														<input id="ECITIC-NET-B2C" type="radio" name="yh" value="ECITIC-NET-B2C"/>
														<img name="ECITIC-NET-B2C" align="middle" src="<c:url value='/bank_img/zx.bmp'/>"/>
													</div>
													<div style="margin-bottom: 20px;">
														<input id="SDB-NET-B2C" type="radio" name="yh" value="SDB-NET-B2C"/>
														<img name="SDB-NET-B2C" align="middle" src="<c:url value='/bank_img/sfz.bmp'/>"/>

														<input id="GDB-NET-B2C" type="radio" name="yh" value="GDB-NET-B2C"/>
														<img name="GDB-NET-B2C" align="middle" src="<c:url value='/bank_img/gf.bmp'/>"/>

														<input id="SHB-NET-B2C" type="radio" name="yh" value="SHB-NET-B2C"/>
														<img name="SHB-NET-B2C" align="middle" src="<c:url value='/bank_img/sh.bmp'/>"/>

														<input id="SPDB-NET-B2C" type="radio" name="yh" value="SPDB-NET-B2C"/>
														<img name="SPDB-NET-B2C" align="middle" src="<c:url value='/bank_img/shpd.bmp'/>"/>
													</div>
													<div style="margin-bottom: 20px;">
														<input id="POST-NET-B2C" type="radio" name="yh" value="POST-NET-B2C"/>
														<img name="POST-NET-B2C" align="middle" src="<c:url value='/bank_img/post.bmp'/>"/>

														<input id="BJRCB-NET-B2C" type="radio" name="yh" value="BJRCB-NET-B2C"/>
														<img name="BJRCB-NET-B2C" align="middle" src="<c:url value='/bank_img/beijingnongshang.bmp'/>"/>

														<input id="HXB-NET-B2C" type="radio" name="yh" value="HXB-NET-B2C"/>
														<img name="HXB-NET-B2C" align="middle" src="<c:url value='/bank_img/hx.bmp'/>"/>

														<input id="CZ-NET-B2C" type="radio" name="yh" value="CZ-NET-B2C"/>
														<img name="CZ-NET-B2C" align="middle" src="<c:url value='/bank_img/zheshang.bmp'/>"/>
													</div>
												</div>
												<%--<div style="margin: 40px;">--%>
												<%--<a href="" class="linkNext">下一步</a>--%>
												<%--</div>--%>
											</div>
										</form>
									</div>
									<div class="modal-footer">
										<button class="btn btn-primary" onclick="javascript:$('#form1').submit();">确定</button>
										<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
									</div>
								</div>
							</div>
						</div>


						<button class="btn radius btn-primary size-L" onClick="modalalipay()">支付宝支付</button>


						<div id="modal-alipay" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content radius">
									<div class="modal-header">
										<h3 class="modal-title">请支付</h3>
										<a class="close" data-dismiss="modal" aria-hidden="true" href="javascript:void();">×</a>
									</div>
									<div class="modal-body" style="text-align: center;">
										<div>
											<img id="pay" src="" alt="">
											<div class="Huialert Huialert-danger">
												<i class="Hui-iconfont">&#xe6a6;</i>
												总金额：${order.total}元&nbsp;&nbsp;（注：未完成支付前请不要关闭该窗口）
											</div>
										</div>
									</div>
									<div class="modal-footer">
										<button class="btn btn-primary" onclick="window.location.href='<c:url value='/OrderServlet?method=alipayBack'/>'">确定</button>
										<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
									</div>
								</div>
							</div>
						</div>


					</div>



				</div>




			
			
		</div>
		
		
	
	
	</body>
</html>
