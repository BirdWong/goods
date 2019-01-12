package cn.jijiking51.goods.cart.web.servlet;


import cn.jijiking51.goods.book.domain.Book;
import cn.jijiking51.goods.cart.domain.CartItem;
import cn.jijiking51.goods.cart.service.CartItemService;
import cn.jijiking51.goods.commons.CommonUtils;
import cn.jijiking51.Servlet.BaseServlet;
import cn.jijiking51.goods.user.domain.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author  h4795
 * @description 购物车Servlet
 */
public class CartItemServlet extends BaseServlet {
	CartItemService cartItemService = new CartItemService();
	

	/**
	 * 加载多个cartItem
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String loadCartItems(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//1. 从request的getParameter获取查询条件cartItemIds
		String cartItemIds = req.getParameter("cartItemIds");
		//2.从request的getParameter获取查询条件total，并且使用Double的parseDouble转化为未封装的double
		double total = Double.parseDouble(req.getParameter("total"));
		//3. 使用cartItemIds调用Service#loadCartItems得到List<CartItem>
		List<CartItem> cartItemList  = cartItemService.loadCartItems(cartItemIds);
		//4. 将 cartItemList、total、cartItemIds 转发到 /jsps/cart/showitem.jsp
		req.setAttribute("cartItemList", cartItemList);
		req.setAttribute("total", total);
		req.setAttribute("cartItemIds", cartItemIds);
		return "f:/jsps/cart/showitem.jsp";
	}
	
	/**
	 * 删除修改购物车中物品数目
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateQuantity(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1. 从request的getParameter获取查询条件cartItemIds
		String cartItemIds = req.getParameter("cartItemId");;
		//2.从request的getParameter获取查询条件quantity，并且使用Integer的parseInt转化为未封装的int
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		//3. 使用cartItemIds和quantity调用Service#updateQuantity得到CartItem
		CartItem cartItem = cartItemService.updateQuantity(cartItemIds, quantity);
		/*
		 * 4. 使用StringBuilder 将返回值写成json格式
		 * {
		 * 	"quantity":xxx,
		 * 	"subtotal":xxxxx
		 * }
		 */
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
		sb.append("}");
		//使用response#getWriger#print输出json
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
		 * 1. 从request的getParameter获取查询条件cartItemIds
		 * 2. 使用cartItemIds调用Service#batchDelect处理
		 * 3. 调用myCart返回
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
		 * 1.从request的getParameter获取查询条件CartItem
		 * 	1-1.从request中取出ParameterMap
		 * 	1-2.使用CommonUtils的toBean方法封装成一个Book对象
		 */
		CartItem cartItem = CommonUtils.toBean(req.getParameterMap(), CartItem.class);
		/*
		 * 2.从request的getParameter获取查询条件book
		 * 	2-1.从request中取出ParameterMap
		 * 	2-2.使用CommonUtils的toBean方法封装成一个Book对象
		 */
		Book book = CommonUtils.toBean(req.getParameterMap(), Book.class);
		//3. 从request的getParameter查询sessionUser
		User user = (User)req.getSession().getAttribute("sessionUser");
		//4. 如果没有sessionUser 则说明返回消息提示登录重定向发到jsps/user/login.jsp
		if(user == null){
			req.setAttribute("msg", "您没有登录");
			return "r:/jsps/user/login.jsp";
		}
		//5. 给CartItem设置书籍 和 用户
		cartItem.setBook(book);
		cartItem.setUser(user);
		//6. 使用cartItem调用Service#add处理
		cartItemService.add(cartItem);
		//7.  调用myCart返回
		return myCart(req,resp);
	}

	/**
	 * 我的购物车详细信息
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myCart(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 从request的getParameter查询sessionUser
		 * 然后获取uid
		 */
		User user = (User)req.getSession().getAttribute("sessionUser");
		String uid = user.getUid();
		
		
		//使用uid调用Service#myCart处理
		List<CartItem> cartItemList = cartItemService.myCart(uid);
		
		
		 //保存起来， 转发到jsps/cart/list.jsp
		req.setAttribute("cartItemList",cartItemList);
		return "f:/jsps/cart/list.jsp";
		
	}
	
}
