package cn.itcast.goods.admin.Order.web.Servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.service.OrderService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	
	private OrderService orderService = new OrderService();
	
	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req){
		int pc = 1;
		String param = req.getParameter("pc");
		if(param !=  null && !param.trim().isEmpty()){
			try{
				pc = Integer.parseInt(param);
			}catch(RuntimeException e){}
		}
		return pc;
	}
	
	/**
	 * 截取url 页面中的分页导航中需要他作为超链接
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req){
		String url = req.getRequestURI() +"?" +req.getQueryString();
		int index = url.lastIndexOf("&pc=");
		if(index!=-1){
			url=url.substring(0,index);
		}
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
		/*
		 * 2.获取url
		 */
		String url = getUrl(req);
		/*
		 * 3.使用pc和cid调用Service#findByCategory得到PageBean
		 */
		PageBean<Order> pb = orderService.findAll(pc);
		
		/*
		 * 4.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
		 */
		
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	
	
	
	public String findByStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		/*
		 * 2.获取url
		 */
		String url = getUrl(req);
		/*
		 * 3.获取链接参数 status
		 */
		int status = Integer.parseInt(req.getParameter("status"));
		/*
		 * 4.使用pc和cid调用Service#findByCategory得到PageBean
		 */
		PageBean<Order> pb = orderService.findByStatus(status, pc);
		
		/*
		 * 5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
		 */
		
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
		String oid = req.getParameter("oid");
		Order order = orderService.load(oid);
		req.setAttribute("order", order);
		String btn = req.getParameter("btn");
		req.setAttribute("btn", btn);
		return "/adminjsps/admin/order/desc.jsp";
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
		String oid = req.getParameter("oid");
		/*
		 * 校验订单
		 */
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
		String oid = req.getParameter("oid");
		/*
		 * 校验订单
		 */
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
}
