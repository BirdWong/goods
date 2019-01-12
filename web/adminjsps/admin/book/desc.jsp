<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/21 0021
  Time: 16:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html class="no-js fixed-layout">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Amaze UI Admin index Examples</title>
    <meta name="description" content="这是一个 index 页面">
    <meta name="keywords" content="index">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <script type="text/javascript" src="/lib/jquery/1.9.1/jquery.min.js"></script>
    <link rel="icon" type="image/png" href="/assets/i/favicon.png">
    <link rel="apple-touch-icon-precomposed" href="/assets/i/app-icon72x72@2x.png">
    <meta name="apple-mobile-web-app-title" content="Amaze UI" />
    <script type="text/javascript" src="https://api.uixsj.cn/hitokoto/en.php?code=js"></script>
    <link rel="stylesheet" href="/adminjsps/admin/css/H-ui.min.css">
    <link rel="stylesheet" href="/adminjsps/admin/css/H-ui.css">
    <link rel="stylesheet" href="/adminjsps/admin/css/H-ui.ie.css">
    <link rel="stylesheet" href="/adminjsps/admin/css/H-ui.reset.css">
    <script type="text/javascript" src="/adminjsps/admin/js/H-ui.js"></script>
    <script type="text/javascript" src="/adminjsps/admin/js/H-ui.min.js"></script>
    <link rel="stylesheet" href="/assets/css/amazeui.min.css"/>
    <link rel="stylesheet" href="/assets/css/admin.css">

    <link rel="stylesheet" type="text/css" href="<c:url value='/jquery/jquery.datepick.css'/>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/book/desc.css'/>">
    <script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/jquery/jquery.datepick.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/jquery/jquery.datepick-zh-CN.js'/>"></script>

    <script type="text/javascript" src="<c:url value='/adminjsps/admin/js/book/desc.js'/>"></script>


    <script type="text/javascript">

        $(function() {
            $("#box").attr("checked", false);
            $("#formDiv").css("display", "none");
            $("#show").css("display", "");

            // 操作和显示切换
            $("#box").click(function() {
                if($(this).attr("checked")) {
                    $("#show").css("display", "none");
                    $("#formDiv").css("display", "");
                } else {
                    $("#formDiv").css("display", "none");
                    $("#show").css("display", "");
                }
            });
        });


        function loadChildren(){
            var pid = $("#pid").val();
            $.ajax({
                async:true,
                cache:false,
                url:"/admin/AdminBookServlet",
                data:{method:"ajaxFindChildren",pid:pid},
                type:"POST",
                dataType:"json",
                success:function(arr){

                    //得到cid,删除他的内容
                    $("#cid").empty();//删除元素内容
                    $("#cid").append($("<option>====请选择2级分类====</option>"));
                    //循环遍历数组把每个对象转换成option

                    for(var i = 0;  i < arr.length;i++){
                        var option = $("<option>").val(arr[i].cid).text(arr[i].cname);

                        $("#cid").append(option);
                    }
                }
            });
        }


        function editForm(){
            $("#method").val("edit");
            $("#form").submit();
        }

        function  delForm(){
            $("#method").val("delete");
            $("#form").submit();
        }
    </script>



</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
    以获得更好的体验！</p>
<![endif]-->

<header class="am-topbar am-topbar-inverse admin-header">
    <div class="am-topbar-brand">
        <strong>图书商城</strong> <small>后台管理</small>
    </div>

    <button class="am-topbar-btn am-topbar-toggle am-btn am-btn-sm am-btn-success am-show-sm-only" data-am-collapse="{target: '#topbar-collapse'}"><span class="am-sr-only">导航切换</span> <span class="am-icon-bars"></span></button>

    <div class="am-collapse am-topbar-collapse" id="topbar-collapse">

        <ul class="am-nav am-nav-pills am-topbar-nav am-topbar-right admin-header-list">

            <li class="am-dropdown" data-am-dropdown>
                <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;">
                    <span class="am-icon-users"></span> ${sessionScope.admin.adminname} <span class="am-icon-caret-down"></span>
                </a>
                <ul class="am-dropdown-content">
                    <li><a target="_top" href="<c:url value='/AdminServlet?method=quit'/>"><span class="am-icon-power-off"></span> 退出</a></li>
                </ul>
            </li>
        </ul>
    </div>
</header>

<div class="am-cf admin-main">
    <!-- sidebar start -->
    <div class="admin-sidebar am-offcanvas" id="admin-offcanvas">
        <div class="am-offcanvas-bar admin-offcanvas-bar">
            <ul class="am-list admin-sidebar-list">
                <li><a href="/adminjsps/admin"><span class="am-icon-home"></span> 首页</a></li>
                <li><a href="<c:url value='/admin/AdminCategoryServlet?method=findAll'/>"><span class="am-icon-table"></span> 分类管理</a></li>
                <li><a href="<c:url value='/admin/AdminBookServlet?method=findCategoryAll'/>"><span class="am-icon-th"></span> 图书管理</a></li>
                <li><a href="<c:url value='/admin/AdminOrderServlet?method=findAll'/>"><span class="am-icon-pencil-square-o"></span> 订单管理</a></li>
                <li><a href="<c:url value='/AdminServlet?method=quit'/>"><span class="am-icon-sign-out"></span> 注销</a></li>
            </ul>
            <script type="text/javascript">
                function splitCn() {
                    var elementById = document.getElementById("enhitokoto");
                    var elementById1 = document.getElementById("hitokoto");
                    var innerText = elementById.innerText;
                    var stringCn;
                    var stringEn;
                    for(var i = 0; i < innerText.length ; i++){
                        if(innerText[i] >= '\u4e00' && innerText[i] <= '\u9fa5'){
                            stringEn = innerText.substring(0,i);
                            stringCn = innerText.substring(i,innerText.length);
                            break;
                        }
                    }
                    elementById1.innerText = stringCn;
                    elementById.innerText = stringEn;
                }

            </script>
            <div class="am-panel am-panel-default admin-sidebar-panel">
                <div class="am-panel-bd">
                    <p><span class="am-icon-bookmark"></span> 公告</p>
                    <p id="hitokoto"></p>
                </div>
            </div>


            <div class="am-panel am-panel-default admin-sidebar-panel">
                <div class="am-panel-bd">
                    <p><span class="am-icon-tag"></span> wiki</p>
                    <p id="enhitokoto"><script>enhitokoto();splitCn();</script></p>
                </div>
            </div>
        </div>
    </div>
    <!-- sidebar end -->

    <!-- content start -->
    <div class="admin-content">
        <div class="admin-content-body">
            <div class="am-cf am-padding">
                <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg"><a href="<c:url value='/admin/AdminBookServlet?method=findCategoryAll'/>">图书管理</a></strong> / <small>修改书籍</small></div>
            </div>

            <hr>


            <input type="checkbox" id="box" class="box"><label for="box">编辑或删除</label>
            <br/>
            <br/>
            <div id="show" class="show">
                <img align="top" src="<c:url value='/${book.image_w }'/>" class="tp"/>
                <div id="book" style="float:left;">
                    <ul>
                        <li style="word-wrap: break-word;word-break: break-all; overflow: hidden; width: 50em;">书名：${book.bname}</li>
                        <li>商品编号：${book.bid }</li>
                        <li>当前价：<span class="price_n">&yen;${book.currPrice }</span></li>
                        <li>定价：<span style="text-decoration:line-through;">&yen;${book.price }</span>　折扣：<span style="color: #c30;">${book.discount }</span>折</li>
                    </ul>
                    <hr style="margin-left: 50px; height: 1px; color: #dcdcdc"/>
                    <table class="tab">
                        <tr>
                            <td colspan="3">
                                作者：${book.author }著
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
                            <td>页数 ${book.pageNum }</td>
                            <td>字数：${book.wordNum	 }</td>
                        </tr>
                        <tr>
                            <td width="180">印刷时间：${book.printtime }</td>
                            <td>开本：${book.booksize }开</td>
                            <td>纸张：${book.paper }</td>
                        </tr>
                    </table>
                </div>
            </div>


            <div id="formDiv" class="formDiv">
                <div class="sm">&nbsp;</div>
                <form action="<c:url value='/admin/AdminBookServlet'/>" method="post" id="form">
                    <input type="hidden" name="method" id="method"/>
                    <input type="hidden" name="bid" value="${book.bid }"/>
                    <img align="top" src="<c:url value='/${book.image_w }'/>" class="tp"/>
                    <div style="float:left;">
                        <ul>
                            <li>商品编号：${book.bid }</li>
                            <li>书名：　<input id="bname" type="text" name="bname" value="${book.bname }" style="width:50em;"/></li>
                            <li>当前价：<input id="currPrice" type="text" name="currPrice" value="${book.currPrice }" style="width:50px;"/></li>
                            <li>定价：　<input id="price" type="text" name="price" value="${book.price }" style="width:50px;"/>
                                折扣：<input id="discount" type="text" name="discount" value="${book.discount }" style="width:30px;"/>折</li>
                        </ul>
                        <hr style="margin-left: 50px; height: 1px; color: #dcdcdc"/>
                        <table class="tab">
                            <tr>
                                <td colspan="3">
                                    作者：　　<input id="author" type="text" name="author" value="${book.author }" style="width:150px;"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3">
                                    出版社：　<input id="press" type="text" name="press" value="${book.press }" style="width:200px;"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3">出版时间：<input id="publishtime" type="text" name="publishtime" value="${book.publishtime}" style="width:100px;"/></td>
                            </tr>
                            <tr>
                                <td>版次：　　<input id="edition" type="text" name="edition" value="${book.edition }" style="width:40px;"/></td>
                                <td>页数：　　<input id="pageNum" type="text" name="pageNum" value="${book.pageNum }" style="width:50px;"/></td>
                                <td>字数：　　<input id="wordNum" type="text" name="wordNum" value="${book.wordNum }" style="width:80px;"/></td>
                            </tr>
                            <tr>
                                <td width="250px">印刷时间：<input id="printtime" type="text" name="printtime" value="${book.printtime }" style="width:100px;"/></td>
                                <td width="250px">开本：　　<input id="booksize" type="text" name="booksize" value="${book.booksize }" style="width:30px;"/></td>
                                <td>纸张：　　<input id="paper" type="text" name="paper" value="${book.paper }" style="width:80px;"/></td>
                            </tr>
                            <tr>
                                <td>
                                    一级分类：<select name="pid" id="pid" onchange="loadChildren()">
                                    <option value="">====请选择1级分类====</option>
                                    <c:forEach items="${parents }" var="parent">
                                        <option value="${parent.cid }" <c:if test="${book.category.parent.cid eq parent.cid }">selected="selected"</c:if>>${parent.cname }</option>
                                    </c:forEach>
                                </select>
                                </td>
                                <td>
                                    二级分类：<select name="cid" id="cid">
                                    <option value="">====请选择2级分类====</option>
                                    <c:forEach items="${children }" var="child">
                                        <option value="${child.cid }" <c:if test="${book.category.cid eq child.cid }">selected="selected"</c:if>>${child.cname }</option>
                                    </c:forEach>
                                </select>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <input onclick="editForm()" type="button" name="method" id="editBtn" class="btn" value="编　　辑">
                                    <input onclick="delForm()" type="button" name="method" id="delBtn" class="btn" value="删　　除">
                                </td>
                                <td></td>
                            </tr>
                        </table>
                    </div>
                </form>
            </div>

        </div>


        <footer class="admin-content-footer">
            <hr>
            <p class="am-padding-left">© 2014 AllMobilize, Inc. Licensed under MIT license.</p>
        </footer>
    </div>
    <!-- content end -->

</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<!--[if lt IE 9]>
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script src="http://cdn.staticfile.org/modernizr/2.8.3/modernizr.js"></script>
<script src="/assets/js/amazeui.ie8polyfill.min.js"></script>
<![endif]-->

<!--[if (gte IE 9)|!(IE)]><!-->
<script src="/assets/js/jquery.min.js"></script>
<!--<![endif]-->
<script src="/assets/js/amazeui.min.js"></script>
<script src="/assets/js/app.js"></script>
</body>
</html>
