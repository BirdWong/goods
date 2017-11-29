<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>cartlist.jsp</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script src="<c:url value='/js/round.js'/>"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/cart/list.css'/>">
<script type="text/javascript">
$(function(){
	showTotal();
	/*
	 * 给全选添加click事件
	 */ 
	$("#selectAll").click(function(){
		var bool = $("#selectAll").attr("checked");
		setItemCheckBox(bool);
		
		showTotal();
	});
	
	/*
	 * 给所有条目添加事件
	 */	
	 $(":checkbox[name=checkboxBtn]").click(function(){
	 	var all = $(":checkbox[name=checkboxBtn]").length;//所有条目数量
	 	var select = $(":checkbox[name=checkboxBtn][checked=true]").length;//勾选选中条目数量
	 	if(all == select && select != 0){//全部选中
	 		$("#selectAll").attr("checked",true);//勾选全部复选框
	 		setJieSuan(true);//结算按钮有效
	 		showTotal();//重新结算按钮有效
	 		
	 	}else if(select == 0) {//一个都没选中
	 		$("#selectAll").attr("checked",false);//关闭全选
	 		setJieSuan(false);//结算按钮关闭
	 	}else {//有选中的， 但是没有全部选中
	 		 $("#selectAll").attr("checked",false);//关闭全选
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
 				location = "/goods/CartItemServlet?method=batchDelect&cartItemIds="+ id;
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
 			location = "/goods/CartItemServlet?method=batchDelect&cartItemIds="+id;	
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
		url:"/goods/CartItemServlet",
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
	 $(":checkbox[name=checkboxBtn][checked=true]").each(function(){
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
	$(":checkbox[name=checkboxBtn]").attr("checked",bool);
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
	$(":checkbox[name=checkboxBtn][checked=true]").each(function(){
	 	cartItemArray.push($(this).val());
	 	
	 	
	 });
	
	location = "/goods/CartItemServlet?method=batchDelect&cartItemIds="+cartItemArray;
}

/*
 * 结算
 */
 function jiesuan(){
 	//1.获取所有被选中的id放到数组中
 	var cartItemArray = new Array();
	$(":checkbox[name=checkboxBtn][checked=true]").each(function(){
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
		<td align="left" width="50px">
			<input type="checkbox" id="selectAll" checked="checked"/><label for="selectAll">全选</label>
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
  </body>
</html>