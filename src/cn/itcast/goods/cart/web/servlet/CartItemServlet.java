package cn.itcast.goods.cart.web.servlet;



import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.cart.service.CartItemService;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class CartItemServlet extends BaseServlet {
	CartItemService cartItemService = new CartItemService();
	
	/**
	 * 加载多个cartItem
	 */
	public String loadCartItems(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		String cartItemIds = req.getParameter("cartItemIds");
		double total = Double.parseDouble(req.getParameter("total"));
		List<CartItem> cartItemList  = cartItemService.loadCartItems(cartItemIds);
		req.setAttribute("cartItemList", cartItemList);
		req.setAttribute("total", total);
		req.setAttribute("cartItemIds", cartItemIds);
		return "f:/jsps/cart/showitem.jsp";
	}
	
	/**
	 * 
	 * 删除修改购物车中物品数目
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateQuantity(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cartItemIds = req.getParameter("cartItemId");;
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		CartItem cartItem = cartItemService.updateQuantity(cartItemIds, quantity);
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
		sb.append("}");
		resp.getWriter().print(sb);
		return null;
	}
	
	
	
	/** 
	 * 批量删除功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String batchDelect(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 获取cartItemIds参数
		 * 调用service处理
		 * 得到参数返回到list.jsp
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		cartItemService.batchDelect(cartItemIds);
		return myCart(req, resp);
		
	}
	
	/**
	 * 添加购物车条目
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		/*
		 * 封装表单数据到CartItem
		 */
		CartItem cartItem = CommonUtils.toBean(req.getParameterMap(), CartItem.class);
		Book book = CommonUtils.toBean(req.getParameterMap(), Book.class);
		User user = (User)req.getSession().getAttribute("sessionUser");
		if(user == null){
			req.setAttribute("msg", "您没有登录");
			return "r:/jsps/user/login.jsp";
		}
		cartItem.setBook(book);
		cartItem.setUser(user);
	
		cartItemService.add(cartItem);
	
		/*
		 * 查询出所有条目， 转发到list.jsp 中显示
		 */
		return myCart(req,resp);
	}
	
	
	public String myCart(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 得到uid
		 */
		User user = (User)req.getSession().getAttribute("sessionUser");
		String uid = user.getUid();
		
		
		/*
		 * 得到Service
		 */
		List<CartItem> cartItemList = cartItemService.myCart(uid);
		
		
		 /*
		 * 保存起来， 转发到cart/list.jsp
		 */
		req.setAttribute("cartItemList",cartItemList);
		return "f:/jsps/cart/list.jsp";
		
	}
	
}
