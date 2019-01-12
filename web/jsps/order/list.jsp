<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
	    <script src="js/H-ui.js"></script>
	    <script type="text/javascript" src="js/jquery.SuperSlide.min.js "></script>
	    
	    <script type="text/javascript">
	        function _go() {
	            var pc = $("#pageCode").val();//获取文本框中的当前页码
	            if(!/^[1-9]\d*$/.test(pc)) {//对当前页码进行整数校验
	                alert('请输入正确的页码！');
	                return;
	            }
	            if(pc > ${pb.tp}) {//判断当前页码是否大于最大页
	                alert('请输入正确的页码！');
	                return;
	            }
	            location = "${pb.url}&pc=" + pc;
	        }
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
			
			 <div class="admin-content">
		        <div class="admin-content-body">
		            <div class="am-cf am-padding">

		            </div>

		            <hr>

		            <div class="am-g">
		                <div class="am-u-sm-12 am-u-md-6">
		                    <div class="am-btn-toolbar">
		                        <div class="am-btn-group am-btn-group-xs">


		
		                            <%--<button type="button" class="am-btn am-btn-default"><a href="<c:url value='/admin/AdminOrderServlet?method=findAll'/>">全部</a></button>--%>
		                            <%--<button type="button" class="am-btn am-btn-default"><a href="<c:url value='/admin/AdminOrderServlet?method=findByStatus&status=1'/>">未付款</a></button>--%>
		                            <%--<button type="button" class="am-btn am-btn-default"><a href="<c:url value='/admin/AdminOrderServlet?method=findByStatus&status=2'/>">已付款</a></button>--%>
		                            <%--<button type="button" class="am-btn am-btn-default"><a href="<c:url value='/admin/AdminOrderServlet?method=findByStatus&status=3'/>">已发货</a></button>--%>
		                            <%--<button type="button" class="am-btn am-btn-default"><a href="<c:url value='/admin/AdminOrderServlet?method=findByStatus&status=4'/>">交易成功</a></button>--%>
		                            <%--<button type="button" class="am-btn am-btn-default"><a href="<c:url value='/admin/AdminOrderServlet?method=findByStatus&status=5'/>">已取消</a></button>--%>
		<%----%>
		                        </div>
		                    </div>
		                </div>
		                <div class="am-u-sm-12 am-u-md-3">
		                    <div class="am-form-group">
		                    </div>
		                </div>
		            </div>
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		            <div class="am-g">
		                <div class="am-u-sm-12">
		                    <form class="am-form">
		                        <table class="am-table am-table-striped am-table-hover table-main">
		                            <thead>
		                            <tr>
		                                <%--<th class="table-check"><input type="checkbox"></th>--%>
		                                    <th class="order-id">订单号</th>
		                                    <th class="order-inf">商品信息</th>
		                                    <th class="order-time">下单时间</th>
		                                    <th class="order-price am-hide-sm-only">金额</th>
		                                    <th class="order-status am-hide-sm-only">订单状态</th>
		                                    <th class="table-set">操作</th>
		                            </tr>
		                            </thead>
		                            <tbody>
		                                <c:forEach items="${pb.beanList }" var="order">
		                                    <tr>
		                                        <td><a  href="<c:url value='/OrderServlet?method=load&oid=${order.oid }'/>">${order.oid }</a></td>
		                                        <td>
		                                            <c:forEach items="${order.orderItemList }" var="orderItem">
														<a class="link2" href="<c:url value='/BookServlet?method=load&bid=${orderItem.book.bid }'/>">
															<img border="0" width="70" src="<c:url value='/${orderItem.book.image_b }'/>"/>
														</a>
		                                            </c:forEach>
		                                        </td>
		                                        <td>${order.ordertime }</td>
		                                        <td class="am-hide-sm-only">&yen;${order.total}</td>
		                                        <td>
		                                            <c:choose>
		                                                <c:when test="${order.status eq 1 }">(等待付款)</c:when>
		                                                <c:when test="${order.status eq 2 }">(准备发货)</c:when>
		                                                <c:when test="${order.status eq 3 }">(等待确认)</c:when>
		                                                <c:when test="${order.status eq 4 }">(交易成功)</c:when>
		                                                <c:when test="${order.status eq 5 }">(已取消)</c:when>
		                                            </c:choose>
		                                        </td>
		                                        <td>
		                                            <div class="am-btn-toolbar">
		                                                <div class="am-btn-group am-btn-group-xs">
		                                                    <button class="am-btn am-btn-default am-btn-xs am-hide-sm-only">
		                                                        <span class="am-icon-copy"></span><a href="<c:url value='/OrderServlet?method=load&oid=${order.oid }'/>">查看</a>
		                                                    </button>

															<c:if test="${order.status eq 1 }">
															<button class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only">
																<span class="am-icon-pencil-square-o"></span><a href="<c:url value='/OrderServlet?method=paymentPer&oid=${order.oid }'/>">支付</a>
															</button>
															<button class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only">
																<span class="am-icon-trash-o"></span><a href="<c:url value='/OrderServlet?method=load&oid=${order.oid }&btn=cancel'/>">取消</a>
															</button>
															</c:if>

		                                                    <c:if test="${order.status eq 3 }">
		                                                        <button class="am-btn am-btn-default am-btn-xs am-text-secondary">
		                                                            <span class="am-icon-pencil-square-o"></span><a href="<c:url value='/OrderServlet?method=load&oid=${order.oid }&btn=confirm'/>">确认收货</a>
		                                                        </button>
		                                                    </c:if>
		
		                                                </div>
		                                            </div>
		                                        </td>
		                                    </tr>
		                                </c:forEach>
		                            </tbody>
		                        </table>
		
		
		                        <div class="am-margin am-cf">
		                            <hr>
		                            <div class="am-fl ">
		                                <p style="margin:  1.6rem 0;">
		                                    <span>共${pb.tp }页 记录到</span>
		
		                                    <input type="text" class="inputPageCode" id="pageCode" maxlength="2" value="${pb.pc }" style="display: inline-block"/>页
		                                    <button type="submit"><a href="javascript:_go();" class="aSubmit am-btn am-btn-primary am-btn-xs">确定</a></button>
		                                </p>
		
		                            </div>
		                            <style>
		                                #pageCode{
		                                    width: 2em;
		                                }
		                            </style>
		
		                            <ol class="am-pagination am-fr">
		                                <c:choose>
		                                    <c:when test="${pb.pc eq 1}" >
		                                        <li class="am-disabled"><a href="#">«</a></li>
		                                    </c:when>
		                                    <c:otherwise>
		                                        <li>
		                                            <a href="${pb.url}&pc=${pb.pc-1}" >«</a>
		                                        </li>
		                                    </c:otherwise>
		                                </c:choose>
		
		
		                                <c:choose>
		                                    <c:when test="${pb.tp <= 6 }">
		                                        <c:set var="begin" value="1"/>
		                                        <c:set var="end" value="${pb.tp }"/>
		                                    </c:when>
		                                    <c:otherwise>
		                                        <c:set var="begin" value="${pb.pc-2 }"/>
		                                        <c:set var="end" value="${pb.pc + 3}"/>
		                                        <c:if test="${begin < 1 }">
		                                            <c:set var="begin" value="1"/>
		                                            <c:set var="end" value="6"/>
		                                        </c:if>
		                                        <c:if test="${end > pb.tp }">
		                                            <c:set var="begin" value="${pb.tp-5 }"/>
		                                            <c:set var="end" value="${pb.tp }"/>
		                                        </c:if>
		                                    </c:otherwise>
		                                </c:choose>
		
		
		                                <c:forEach begin="${begin }" end="${end }" var="i">
		                                    <c:choose>
		                                        <c:when test="${i eq pb.pc }">
		                                            <li class="am-active"><a href="#">${i}</a></li>
		                                        </c:when>
		                                        <c:otherwise>
		                                            <li><a href="${pb.url }&pc=${i}" >${i }</a></li>
		                                        </c:otherwise>
		                                    </c:choose>
		                                </c:forEach>
		
		                                <c:if test="${end < pb.tp }">
		                                    <li class="am-disabled"><a href="#">...</a></li>
		                                </c:if>
		
		
		                                <c:choose>
		                                    <c:when test="${pb.pc >= pb.tp }">
		                                        <li class="am-disabled"><a href="#">»</a></li>
		                                    </c:when>
		                                    <c:otherwise>
		                                        <li>
		                                            <a href="${pb.url }&pc=${pb.pc+1}" class="aBtn bold">»</a>
		                                        </li>
		
		                                    </c:otherwise>
		                                </c:choose>
		
		
		                            </ol>
		                        </div>
		
		                        <hr>
		                        <p>注：.....</p>
		                    </form>
		                </div>
		
		            </div>
		
		        </div>
		
		
		        <footer class="admin-content-footer">
		            <hr>
		            <p class="am-padding-left">© 2014 AllMobilize, Inc. Licensed under MIT license.</p>
		        </footer>
		    </div>
		</div>
	
	
		
	</body>
</html>
