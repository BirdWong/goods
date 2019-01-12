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


    <link rel="stylesheet" href="/assets/css/amazeui.min.css"/>
    <link rel="stylesheet" href="/assets/css/admin.css">
    <link rel="stylesheet" type="text/css" href="<c:url value='/jquery/jquery.datepick.css'/>">
    <script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/jquery/jquery.datepick.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/jquery/jquery.datepick-zh-CN.js'/>"></script>
    <script type="text/javascript" src="https://api.uixsj.cn/hitokoto/en.php?code=js"></script>
    <script type="text/javascript">
        $(function () {
            $("#publishtime").datepick({dateFormat:"yy-mm-dd"});
            $("#printtime").datepick({dateFormat:"yy-mm-dd"});

            $("#btn").addClass("btn1");
            $("#btn").hover(
                function() {
                    $("#btn").removeClass("btn1");
                    $("#btn").addClass("btn2");
                },
                function() {
                    $("#btn").removeClass("btn2");
                    $("#btn").addClass("btn1");
                }
            );

            $("#btn").click(function() {
                var bname = $("#bname").val();
                var currPrice = $("#currPrice").val();
                var price = $("#price").val();
                var discount = $("#discount").val();
                var author = $("#author").val();
                var press = $("#press").val();
                var pid = $("#pid").val();
                var cid = $("#cid").val();
                var image_w = $("#image_w").val();
                var image_b = $("#image_b").val();

                if(!bname || !currPrice || !price || !discount || !author || !press || !pid || !cid || !image_w || !image_b) {
                    alert("图名、当前价、定价、折扣、作者、出版社、1级分类、2级分类、大图、小图都不能为空！");
                    return false;
                }

                if(isNaN(currPrice) || isNaN(price) || isNaN(discount)) {
                    alert("当前价、定价、折扣必须是合法小数！");
                    return false;
                }
                $("#form").submit();
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
                <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg"><a href="<c:url value='/admin/AdminBookServlet?method=findCategoryAll'/>">图书管理</a></strong> / <small>添加书籍</small></div>
            </div>

            <hr>






            <div class="am-g">
                <p style="color: red;">${msg }</p>
                <form action="<c:url value='/admin/AdminAddBookServlet'/>" enctype="multipart/form-data" method="post" id="form" class="am-form am-form-horizontal">

                    <div class="am-u-sm-12 am-u-md-4 am-u-md-push-8">
                        <div class="am-panel am-panel-default">
                            <div class="am-panel-bd">
                                <div class="am-g">
                                    <div class="am-u-md-12">
                                        <p>大图：</p>
                                            <div class="am-form-group">
                                                <input type="file" id="image_w" name="image_w">
                                                <p class="am-form-help">请选择要上传的文件...</p>
                                            </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="am-panel am-panel-default">
                            <div class="am-panel-bd">
                                <div class="am-g">
                                    <div class="am-u-md-12">
                                        <p>小图：</p>
                                        <div class="am-form-group">
                                            <input type="file" id="image_b" name="image_b">
                                            <p class="am-form-help">请选择要上传的文件...</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>

                    <div class="am-u-sm-12 am-u-md-8 am-u-md-pull-4">

                        <div class="am-form-group">
                            <label for="bname" class="am-u-sm-3 am-form-label">书名</label>
                            <div class="am-u-sm-9">
                                <input type="text" id="bname" name="bname" placeholder="书名">
                            </div>
                        </div>

                        <div class="am-form-group">
                            <label for="price" class="am-u-sm-3 am-form-label">当前价</label>
                            <div class="am-u-sm-9">
                                <input type="text" id="price" name="price" placeholder="输入当前价" >
                            </div>
                        </div>

                        <div class="am-form-group">
                            <label for="currPrice" class="am-u-sm-3 am-form-label">定价</label>
                            <div class="am-u-sm-9">
                                <input id="currPrice" type="text" name="currPrice" placeholder="请输入定价"/>
                            </div>
                        </div>

                        <div class="am-form-group">
                            <label for="discount" class="am-u-sm-3 am-form-label">折扣</label>
                            <div class="am-u-sm-9">
                                <input id="discount" type="text" name="discount" placeholder="请输入折扣" />
                            </div>
                        </div>

                        <div class="am-form-group">
                            <label for="author" class="am-u-sm-3 am-form-label">作者</label>
                            <div class="am-u-sm-9">
                                <input type="text" id="author" name="author" placeholder="请输入作者"/>
                            </div>
                        </div>

                        <div class="am-form-group">
                            <label for="press" class="am-u-sm-3 am-form-label">出版社</label>
                            <div class="am-u-sm-9">
                                <input type="text" name="press" id="press" placeholder="请输入出版社"/>
                            </div>
                        </div>

                        <div class="am-form-group">
                            <label for="publishtime" class="am-u-sm-3 am-form-label">出版时间</label>
                            <div class="am-u-sm-9">
                                <input type="text" id="publishtime" name="publishtime" placeholder="请输入出版时间"/>
                            </div>
                        </div>
                        <div class="am-form-group">
                            <label for="edition" class="am-u-sm-3 am-form-label">版次</label>
                            <div class="am-u-sm-9">
                                <input type="text" name="edition" id="edition" placeholder="请输入版次"/>
                            </div>
                        </div>
                        <div class="am-form-group">
                            <label for="pageNum" class="am-u-sm-3 am-form-label">页数</label>
                            <div class="am-u-sm-9">
                                <input type="text" name="pageNum" id="pageNum" placeholder="请输入书籍页数"/>
                            </div>
                        </div>
                        <div class="am-form-group">
                            <label for="wordNum" class="am-u-sm-3 am-form-label">字数</label>
                            <div class="am-u-sm-9">
                                <input type="text" name="wordNum" id="wordNum" placeholder="请输入书籍字数"/>
                            </div>
                        </div>
                        <div class="am-form-group">
                            <label for="printtime" class="am-u-sm-3 am-form-label">印刷时间</label>
                            <div class="am-u-sm-9">
                                <input type="text" name="printtime" id="printtime" placeholder="请输入印刷时间"/>
                            </div>
                        </div>
                        <div class="am-form-group">
                            <label for="booksize" class="am-u-sm-3 am-form-label">开本</label>
                            <div class="am-u-sm-9">
                                <input type="text" name="booksize" id="booksize" placeholder="请输入开本"/>
                            </div>
                        </div>
                        <div class="am-form-group">
                            <label for="paper" class="am-u-sm-3 am-form-label">纸张</label>
                            <div class="am-u-sm-9">
                                <input type="text" name="paper" id="paper" placeholder="请输入纸张"/>
                            </div>
                        </div>



                        <div class="am-form-group">
                            <label for="pid" class="am-u-sm-3 am-form-label">一级分类：</label>
                            <div class="am-u-sm-9">
                                <select name="pid" id="pid" onchange="loadChildren()">
                                    <option value="">====请选择1级分类====</option>
                                    <c:forEach items="${parents }" var="parent">
                                        <option value="${parent.cid }">${parent.cname }</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="am-form-group">
                            <label for="cid" class="am-u-sm-3 am-form-label">二级分类</label>
                            <div class="am-u-sm-9">
                                <select name="cid" id="cid">
                                    <option value="">====请选择2级分类====</option>

                                </select>
                            </div>
                        </div>



                        <div class="am-form-group">
                            <div class="am-u-sm-9 am-u-sm-push-3">
                                <button type="submit" class="am-btn am-btn-primary">新书上架</button>
                                <button type="button" class="am-btn am-btn-primary"><a href="<c:url value='/adminjsps/admin/'/>" style="color: #fff">取消</a></button>
                            </div>
                        </div>




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
