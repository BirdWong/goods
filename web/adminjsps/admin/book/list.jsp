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
    <style>
        #bookname{
            display: block;
            width: 15em;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }
        body {
            position: relative;
            background: #fff;
            font-family: "Segoe UI","Lucida Grande",Helvetica,Arial,"Microsoft YaHei",FreeSans,Arimo,"Droid Sans","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",FontAwesome,sans-serif;
            font-weight: 400;
            line-height: 1.6;
            color: #333;
            font-size: 1.6rem
        }

    </style>

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
                <div class="am-fl am-cf">
                    <a href="<c:url value='/admin/AdminBookServlet?method=findCategoryAll'/>"><strong class="am-text-primary am-text-lg">图书管理</strong></a>
                    /<small>书籍列表</small>
                </div>
            </div>
            <hr>




            <ul class="am-avg-sm-1 am-avg-md-2 am-avg-lg-4 am-margin gallery-list">
                <c:forEach items="${pb.beanList}" var="book">
                    <li>
                        <a class="pic" href="<c:url value='/admin/AdminBookServlet?method=load&bid=${book.bid }'/>">
                            <img src="<c:url value='/${book.image_b }'/>" class="am-img-thumbnail am-img-bdrs" />
                        </a>
                        <div class="gallery-desc">
                            <p class="price" >
                                <span class="price_n">&yen;${book.currPrice }</span>
                                <span class="price_r">&yen;${book.price }</span>
                                (<span class="price_s">${book.discount }折</span>)
                            </p>
                        </div>
                        <div class="gallery-title">
                            <p>
                                <a  id="bookname" title="${book.bname }" href="<c:url value='/admin/AdminBookServlet?method=load&bid=${book.bid }'/>">${book.bname }</a>
                            </p>
                        </div>
                        <div class="gallery-desc">
                            <p>
                                <span>作者：</span><a href="<c:url value="/admin/AdminBookServlet?method=findByAuthor&author=${book.author }"/>" name='P_zz' title='${book.author }'>${book.author }</a>
                            </p>
                            <p >
                                <span>出版社：</span><a href="<c:url value='/admin/AdminBookServlet?method=findByPress&press=${book.press }'/>">${book.press }</a>
                            </p>
                        </div>
                    </li>
                </c:forEach>
            </ul>


            <div class="am-margin am-cf">
                <hr>
                <div class="am-fl am-text-center">
                    <p style="margin:  1.6rem 0;">
                        <span>共${pb.tp }页 记录</span>
                        <span>到</span>
                        <input type="text" class="inputPageCode" id="pageCode" maxlength="2" value="${pb.pc }"/>页
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
