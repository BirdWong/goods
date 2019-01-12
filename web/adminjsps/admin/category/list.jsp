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
    <link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/category/list.css'/>">

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
            <script type="text/javascript" src="https://api.uixsj.cn/hitokoto/en.php?code=js"></script>
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
                <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">分类管理</strong> / <small></small></div>
            </div>

            <hr>
            <h2 style="text-align: center;">分类列表</h2>
            <table align="center" border="1" cellpadding="0" cellspacing="0">
                <caption class="captionAddOneLevel">
                    <a href="<c:url value='/adminjsps/admin/category/add.jsp'/>">添加一级分类</a>
                </caption>
                <tr class="trTitle">
                    <th>分类名称</th>
                    <th>描述</th>
                    <th>操作</th>
                </tr>

                <c:forEach items="${parents }" var="parent">
                    <tr class="trOneLevel">
                        <td width="200px;">${parent.cname }</td>
                        <td>${parent.desc }</td>
                        <td width="200px;">
                            <a href="<c:url value='/admin/AdminCategoryServlet?method=addChildPre&cid=${parent.cid }'/>">添加二级分类</a>
                            <a href="<c:url value='/admin/AdminCategoryServlet?method=editParentPre&cid=${parent.cid }'/>">修改</a>
                            <a onclick="return confirm('您是否真要删除该一级分类？')" href="<c:url value='/admin/AdminCategoryServlet?method=deleteParent&pid=${parent.cid }'/>">删除</a>
                        </td>
                    </tr>
                    <c:forEach items="${parent.children }"  var="child">
                        <tr class="trTwoLevel">
                            <td>${child.cname }</td>
                            <td>${child.desc }</td>
                            <td width="200px;" align="right">
                                <a href="<c:url value='/admin/AdminCategoryServlet?method=editChildPre&cid=${child.cid }'/>">修改</a>
                                <a onclick="return confirm('您是否真要删除该二级分类？')" href="<c:url value='/admin/AdminCategoryServlet?method=deleteChildren&cid=${child.cid }'/>">删除</a>
                            </td>
                        </tr>
                    </c:forEach>

                </c:forEach>



            </table>

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
