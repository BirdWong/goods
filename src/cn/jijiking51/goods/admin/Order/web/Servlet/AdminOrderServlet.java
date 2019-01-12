package cn.jijiking51.goods.admin.Order.web.Servlet;


import cn.jijiking51.goods.admin.Order.commons.OrderExcel;
import cn.jijiking51.goods.order.domain.Order;
import cn.jijiking51.goods.order.service.OrderService;
import cn.jijiking51.goods.pager.PageBean;
import cn.jijiking51.Servlet.BaseServlet;
import cn.jijiking51.goods.user.domain.User;
import cn.jijiking51.goods.user.service.UserService;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminOrderServlet extends BaseServlet {

	private UserService userService = new UserService();
	private OrderService orderService = new OrderService();
	
	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req){
		//设定为初始1，如果getParameter中有值会将其覆盖
		int pc = 1;
		//1. 获取pc（分页）的数值
		String param = req.getParameter("pc");
		//判断是否为null，或者为空
		if(param !=  null && !param.trim().isEmpty()){
			try{
				pc = Integer.parseInt(param);
			}catch(RuntimeException e){}
		}
		//2.返回pc值
		return pc;
	}
	
	/**
	 * 截取url 页面中的分页导航中需要他作为超链接
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req){
		/*
		 * 1. 通过request的getRequestURI方法获取请求的类名
		 * 通过request的getQueryString方法获取？后面的参数
		 * 将两个通过‘？’连接起来恢复原来的url
		 */
		String url = req.getRequestURI() +"?" +req.getQueryString();
		/*
		 * 2. 通过String的lastIndexOf方法查询‘&pc=’的位置
		 * 如果位置不等于-1
		 * 则使用String的substring方法截取0-该位置的字符创
		 */
		int index = url.lastIndexOf("&pc=");
		if(index!=-1){
			url=url.substring(0,index);
		}
		//3.返回截取后的url
		return url;
	}
	
	
	
	
	/**
	 * 查询所有
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		//2. 调用getUrl获取url
		String url = getUrl(req);
		//3.使用pc和cid调用Service#findByCategory得到PageBean
		PageBean<Order> pb = orderService.findAll(pc);
		
		//4.给PageBean设置url， 保存PageBean，转发到 /adminjsps/admin/order/list.jsp
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}


	/**
	 * 通过状态查找订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		//2. 调用getUrl获取url
		String url = getUrl(req);
		//3.调用request#getParameter获取链接参数 status
		int status = Integer.parseInt(req.getParameter("status"));
		//4.使用pc和cid调用Service#findByCategory得到PageBean
		PageBean<Order> pb = orderService.findByStatus(status, pc);
		//5.给PageBean设置url， 保存PageBean，转发到 /adminjsps/admin/order/list.jsp
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	
	
	/**
	 * 查看订单详细信息
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.调用request#getParameter 得到参数 oid
		String oid = req.getParameter("oid");
		//2. 调用orderService#load查询order
		Order order = orderService.load(oid);
		//3. 设置信息后转发到adminjsps/admin/order/desc.jsp
		req.setAttribute("order", order);
		String btn = req.getParameter("btn");
		req.setAttribute("btn", btn);
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	
	
	/**
	 * 取消订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String cancel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.调用request#getParameter 得到参数 oid
		String oid = req.getParameter("oid");
		//调用findStatus 校验订单 是否为未付款状态 如果是就修改订单状态并将结果转发到adminjsps/msg.jsp
		int status = orderService.findStatus(oid);
		if(status != 1){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "无法取消");
		}else {
			orderService.updateStatus(oid, 5);
			req.setAttribute("code", "success");
			req.setAttribute("msg", "取消成功");
		}
		
		return "f:/adminjsps/msg.jsp";
	}
	
	/**
	 * 发货功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deliver(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.调用request#getParameter 得到参数 oid
		String oid = req.getParameter("oid");
		//调用findStatus 校验订单 是否为付款状态 如果是则修改订单状态并将结果转发到adminjsps/msg.jsp
		int status = orderService.findStatus(oid);
		if(status != 2){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能发货");
		}else {
			orderService.updateStatus(oid,3);
			req.setAttribute("code", "success");
			req.setAttribute("msg", "已发货，请关注物流");
		}
		
		return "f:/adminjsps/msg.jsp";
	}


	/**
	 * 获取excel
	 * @param req
	 * @param rep
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void getExcel(HttpServletRequest req, HttpServletResponse rep) throws SQLException, ClassNotFoundException, IOException {
		//调用SimpleDateFormat格式化时间参数
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		//获得格式化后的时间参数
		String format = simpleDateFormat.format(new Date());
		//设置标题
		String name = format + "图书商城业务报表";
		//输出Excel
		OrderExcel orderExcel = new OrderExcel();
		orderExcel.getExcel(rep,name );


	}

	/**
	 * 返回数据用于画图
	 * @param req
	 * @param rep
	 * @return
	 */
	public String  getEchartsJson(HttpServletRequest req, HttpServletResponse rep) throws IOException {
		//调用orderService # findAll查询出所有订单
		List<Order> status = orderService.findAll();

		int num1 = 0;
		int num2 = 0;
		int num3 = 0;
		int num4 = 0;
		int num5 = 0;
		//获取每种订单的具体数目
		for(Order order : status){
			switch (order.getStatus()){
				case 1:
					num1++;
					break;
				case 2:
					num2++;
					break;
				case 3:
					num3++;
					break;
				case 4:
					num4++;
					break;
				case 5:
					num5++;
					break;
				default: break;
			}
		}

		//写入到数组转化为list， 发送打前台
		Integer[] nums = {num1,num2,num3,num4,num5};

		List<Integer> list = Arrays.asList(nums);
		rep.getWriter().print(list);
		return null;
	}




}
