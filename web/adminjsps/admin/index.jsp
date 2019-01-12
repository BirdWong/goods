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

    <link rel="stylesheet" href="/adminjsps/admin/css/H-ui.min.css">
    <link rel="stylesheet" href="/adminjsps/admin/css/H-ui.css">
    <link rel="stylesheet" href="/adminjsps/admin/css/H-ui.ie.css">
    <link rel="stylesheet" href="/adminjsps/admin/css/H-ui.reset.css">
    <script type="text/javascript" src="/adminjsps/admin/js/H-ui.js"></script>
    <script type="text/javascript" src="/adminjsps/admin/js/H-ui.min.js"></script>
    <link rel="stylesheet" href="/assets/css/amazeui.min.css"/>
    <link rel="stylesheet" href="/assets/css/admin.css">
    <script type="text/javascript" src="/adminjsps/admin/js/echarts.min.js"></script>
    <script type="text/javascript" src="https://api.uixsj.cn/hitokoto/en.php?code=js"></script>
    <style>
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
                    <p id="enhitokoto"><script> enhitokoto();splitCn();</script></p>
                </div>
            </div>


        </div>
    </div>
    <!-- sidebar end -->

    <!-- content start -->
    <div class="admin-content">
        <div class="admin-content-body">
            <div class="am-cf am-padding">
                <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">首页</strong> / <small>一些常用模块</small></div>
            </div>

            <hr>

             <div class="am-g">
                    <div class="am-u-md-6">


                        <div id="main"  style="width: auto;height: 25em;">

                        </div>


                    </div>

                    <div class="am-u-md-6">

                        <div id="container" style="width: auto;height: 25em;"></div>


                    </div>

                 <script type="text/javascript">
                     var myChart;//定义一个全局的图表变量，供后面动态加载时使用
                     var numbers = [];
                     myChart = echarts.init(document.getElementById('container'));
                     myChart.showLoading();


                     myChart =echarts.init(document.getElementById('main'));
                     myChart.showLoading();

                     function echartsdata() {


                         $.ajax({
                             type:"get",
                             async:true,
                             url:"/admin/AdminOrderServlet?method=getEchartsJson",
                             success : function (result) {


                                 numbers = result.split('[')[1].split(']')[0].split(',');

                                 console.log(result);
                                 // for (var  i = 0; i < result.length;i++){
                                 //     numbers.push(result[i]);
                                 //     console.log(result[i] +"  "+numbers[i]);
                                 // }

                                 // numbers = result;
                                 console.log(numbers);
                                 myChart.hideLoading();
                                 myChart.setOption({
                                     title: {
                                         text: '订单数据'
                                     },
                                     tooltip: {},
                                     legend: {
                                         data:['数量']
                                     },
                                     xAxis: {
                                         data: ["等待支付","等待发货","等待确认","交易完成","已取消"]
                                     },
                                     yAxis: {},
                                     series: [{
                                         name: '数量',
                                         type: 'bar',
                                         data: numbers
                                     }]
                                 });


                                 myChart = echarts.init(document.getElementById('container'));
                                 myChart.hideLoading();
                                 myChart.setOption({
                                     title : {
                                         text: '订单状态',
                                         subtext: new Date().toLocaleString(),
                                         x:'center'
                                     },
                                     tooltip : {
                                         trigger: 'item',
                                         formatter: "{a} <br/>{b} : {c} ({d}%)"
                                     },
                                     legend: {
                                         orient: 'vertical',
                                         left: 'left',
                                         data: ['等待支付','等待发货','等待确认','交易完成','已取消']
                                     },
                                     series : [
                                         {
                                             name: '交易状态',
                                             type: 'pie',
                                             radius : '55%',
                                             center: ['50%', '60%'],
                                             data:[
                                                 {value:numbers[0], name:'等待支付'},
                                                 {value:numbers[1], name:'等待发货'},
                                                 {value:numbers[2], name:'等待确认'},
                                                 {value:numbers[3], name:'交易完成'},
                                                 {value:numbers[4], name:'已取消'}
                                             ],
                                             itemStyle: {
                                                 emphasis: {
                                                     shadowBlur: 10,
                                                     shadowOffsetX: 0,
                                                     shadowColor: 'rgba(0, 0, 0, 0.5)'
                                                 }
                                             }
                                         }
                                     ]
                                 });
                             },
                             error:function (errorMsg) {
                                 alert("数据请求失败");
                                 myChart.hideLoading();
                                 myChart = echarts.init(document.getElementById('container'));
                                 myChart.hideLoading();
                             }
                         })
                     };



                     echartsdata();

                 </script>



                </div>





        <footer class="admin-content-footer">
            <hr>
            <p class="am-padding-left">© 2014 AllMobilize, Inc. Licensed under MIT license.</p>
        </footer>
         </div>
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
