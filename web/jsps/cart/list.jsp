<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

 
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title></title>
		<link rel="stylesheet" href="css/admin.css" />
	    <link rel="stylesheet" type="text/css" href="css/amazeui.min.css"/>
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
	    <script src="<c:url value='/js/round.js'/>"></script>
		<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/cart/list.css'/>"/>
	    <script type="text/javascript">
			$(function(){
				showTotal();
				/*
				 * 给全选添加click事件
				 */ 
				$("#selectAll").click(function(){

					if (bool = $("#selectAll").is(':checked')){

					    $('input[name="checkboxBtn"]').prop("checked",true);
                        setJieSuan(true);
                        alert("全部开启");
					} else{
                        $('input[name="checkboxBtn"]').prop("checked",false);
                        setJieSuan(false);//结算按钮关闭
						alert("全部管理");
                    }
					// setItemCheckBox(bool);
					
					showTotal();
				});
				
				/*
				 * 给所有条目添加事件
				 */	
				 $('input[name="checkboxBtn"]').click(function(){
				 	var all = $('input[name="checkboxBtn"]').length;//所有条目数量
				 	var select = $('input[name="checkboxBtn"]:checked').length;//勾选选中条目数量
				 	if(all == select && select != 0){//全部选中
				 		$("#selectAll").prop("checked",true);//勾选全部复选框
				 		setJieSuan(true);//结算按钮有效
				 		showTotal();//重新结算按钮有效
				 		
				 	}else if(select == 0) {//一个都没选中
				 		$("#selectAll").prop("checked",false);//关闭全选
				 		setJieSuan(false);//结算按钮关闭
				 	}else {//有选中的， 但是没有全部选中
				 		 $("#selectAll").prop("checked",false);//关闭全选
				 		 setJieSuan(true);//结算按钮开启
				 	}
				 	showTotal();
				 });
			 
			 	//给减加click事件
			 	$(".jian").click(function() {
			 		//获取cartitem
			 		var id = $(this).attr("id").substring(0,32);
			 		var quantity = $("#"+id+"Quantity").val();
			 		
			 		if(quantity == 1){
			 			if(confirm("是否真的删除该条目")){
			 				location = "/CartItemServlet?method=batchDelect&cartItemIds="+ id;
			 			}
			 		}else {
			 			
			 			sendUpdateQuantity(id,Number(quantity)-1);
			 			
			 		}
			 	});
			 	
			 	
			 	 	//给jia加click事件
			 	$(".jia").click(function() {
			 		//获取cartitem
			 		var id = $(this).attr("id").substring(0,32);
			 		var quantity = $("#"+id+"Quantity").val();
			 		sendUpdateQuantity(id,Number(quantity)+1);
			 		
			 	})
			 	//显示数量框事件
			
			 	$(".quantity").blur(function() {
			 		
					var id = $(this).attr("id").substring(0,32);
					
			 		var quantity = $("#"+id+"Quantity").val();
			 		if(Number(quantity) == 0){
			 			location = "/CartItemServlet?method=batchDelect&cartItemIds="+id;
			 		}
			 		else {
			 			sendUpdateQuantity(id,quantity);
			 		}
			 		
				});
			
			});
			
			
			// 请求服务器，修改数量。
			function sendUpdateQuantity(id, quantity) {
				$.ajax({
					async:false,
					cache:false,
					url:"/CartItemServlet",
					data:{method:"updateQuantity",cartItemId:id,quantity:quantity},
					type:"POST",
					dataType:"json",
					success:function(result) {
						//1. 修改数量
						$("#" + id + "Quantity").val(result.quantity);
						//2. 修改小计
						$("#" + id + "Subtotal").text(result.subtotal);
						//3. 重新计算总计
						showTotal();
					}
				});
			}
			
			
			
			/*
			 * 计算总计
			 */
			function showTotal(){
			
				/*
				 * 计算所有被勾选的复选框
				 */	
				 var total = 0;
				 $('input[name="checkboxBtn"]:checked').each(function(){
				 	//获取复选框的值	cart Item   
				 	var id = $(this).val();

				 	//通过cartItem找到小计
				 	var text = $("#" + id +"Subtotal").text();
				 	//累加
				 	
				 	total += Number(text);
				 	//显示到总计
				 	
				 });
				 $("#total").text(round(total,2));

			}	 
				
			/*
			 * 统一设置所有条目
			 */	
			function setItemCheckBox(bool){
				$('input[name="checkboxBtn"]').attr("checked",bool);
				alert("正在设置："+bool);
			}
			/*
			 * 结算
			 */
			function setJieSuan(bool){
				if(bool){
					$("#jiesuan").removeClass("kill").addClass("jiesuan");
					$("#jiesuan").unbind("click");
					
				}else {
					$("#jiesuan").removeClass("jiesuan").addClass("kill");
					$("#jiesuan").click(function(){return false;});
				}
				
			
			}
			/*
			 * 批量删除
			 */
			function batchDelect(){
				
				//1.获取被选中条目录的复选块
				//2.把所有被选中的复选块的值存到数组中
				//3.指定location为CartItemServlet，参数method=batchDelect， 参数CartItemIds = 数组的toString（）；
				var cartItemArray = new Array();
				$('input[name="checkboxBtn"]:checked').each(function(){
				 	cartItemArray.push($(this).val());
				 	
				 	
				 });
				
				location = "/CartItemServlet?method=batchDelect&cartItemIds="+cartItemArray;
			}
			
			/*
			 * 结算
			 */
			 function jiesuan(){
			 	//1.获取所有被选中的id放到数组中
			 	var cartItemArray = new Array();
				$('input[name="checkboxBtn"]:checked').each(function(){
				 	cartItemArray.push($(this).val()); 	
				 });
				
			 	//2.把数组的值toStirng，然后付给表单的cartItemIds这个hidden
			 	$("#cartItemIds").val(cartItemArray.toString());
			 	$("#hiddentotal").val($("#total").text());
			 	//3.提交这个表单
			 	$("#jiesuanform").submit();
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
			
			<c:choose>
			
			
				<c:when test="${empty cartItemList }">
				<table width="95%" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td align="right">
							<img align="top" src="<c:url value='/images/icon_empty.png'/>"/>
						</td>
						<td>
							<span class="spanEmpty">您的购物车中暂时没有商品</span>
						</td>
					</tr>
				</table>  
				</c:when>
				<c:otherwise>
					<table width="95%" align="center" cellpadding="0" cellspacing="0">
				<tr align="center" bgcolor="#efeae5">
					<td align="left" width="10%">
						<input type="checkbox" id="selectAll" name="selectAll" checked="checked"/><label for="selectAll">全选</label>
					</td>
					<td colspan="2">商品名称</td>
					<td>单价</td>
					<td>数量</td>
					<td>小计</td>
					<td>操作</td>
				</tr>
			
			
			
			<c:forEach items="${cartItemList }" var="cartItem">
			
			
				<tr align="center">
					<td align="left">
						<input value="${cartItem.cartItemId }" type="checkbox" name="checkboxBtn" checked="checked"/>
					</td>
					<td align="left" width="70px">
						<a class="linkImage" href="<c:url value='/BookServlet?method=load&bid=${cartItem.book.bid }'/>"><img border="0" width="54" align="top" src="<c:url value='/${cartItem.book.image_b }'/>"/></a>
					</td>
					<td align="left" width="400px">
					    <a href="<c:url value='/BookServlet?method=load&bid=${cartItem.book.bid }'/>"><span>${cartItem.book.bname }</span></a>
					</td>
					<td><span>&yen;<span class="currPrice" id="${cartItem.cartItemId }CurrPrice">${cartItem.book.currPrice }</span></span></td>
					<td>
						<a class="jian" id="${cartItem.cartItemId }Jian"></a><input class="quantity" onkeyup="this.value=this.value.replace(/\D/g, '')" id="${cartItem.cartItemId }Quantity" type="text" value="${cartItem.quantity }"/><a class="jia" id="${cartItem.cartItemId }Jia"></a>
					</td>
					<td width="100px">
						<span class="price_n">&yen;<span class="subTotal" id="${cartItem.cartItemId }Subtotal">${cartItem.subtotal }</span></span>
					</td>
					<td>
						<a href="<c:url value='/CartItemServlet?method=batchDelect&cartItemIds=${cartItem.cartItemId } '/>">删除</a>
					</td>
				</tr>
			
					
			
			
			
			</c:forEach>
			
			
			
			
			
			
			
			
			
			
			
			
			
				
				<tr>
					<td colspan="4" class="tdBatchDelete">
						<a href="javascript:batchDelect();">批量删除</a>
					</td>
					<td colspan="3" align="right" class="tdTotal">
						<span>总计：</span><span class="price_t">&yen;<span id="total"></span></span>
					</td>
				</tr>
				<tr>
					<td colspan="7" align="right">
						<a href="javascript:jiesuan()" id="jiesuan" class="jiesuan"></a>
					</td>
				</tr>
			</table>
				<form id="jiesuanform" action="<c:url value='/CartItemServlet'/>" method="post">
					<input type="hidden" name="cartItemIds" id="cartItemIds"/>
					<input type="hidden" name="method" value="loadCartItems"/>
					<input type="hidden" name="total" id="hiddentotal"/>
				</form>
			
				</c:otherwise>
			</c:choose>
		</div>
	
	</body>
</html>
