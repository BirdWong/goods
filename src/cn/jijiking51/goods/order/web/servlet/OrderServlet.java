package cn.jijiking51.goods.order.web.servlet;


import cn.jijiking51.Servlet.BaseServlet;
import cn.jijiking51.goods.cart.domain.CartItem;
import cn.jijiking51.goods.cart.service.CartItemService;
import cn.jijiking51.goods.commons.CommonUtils;
import cn.jijiking51.goods.order.domain.Order;
import cn.jijiking51.goods.order.domain.OrderItem;
import cn.jijiking51.goods.order.service.OrderService;
import cn.jijiking51.goods.pager.PageBean;
import cn.jijiking51.goods.user.domain.User;
import com.alipay.demo.trade.model.GoodsDetail;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class OrderServlet extends BaseServlet {
	private OrderService orderService =  new OrderService();
	private CartItemService cartItemService = new CartItemService();
	private AlipayMain alipayMain = new AlipayMain();
	
	/**
	 * 准备支付
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String paymentPer(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1. 从request的getParameter获取查询条件oid
		String oid = req.getParameter("oid");
		//2. 使用oid调用Service#load得到Order
		Order order = orderService.load(oid);
		//3. 转发到 /jsps/order/pay.jsp
		req.setAttribute("order",order );
		return "f:/jsps/order/pay.jsp";
	}



	/**
	 * 支付方法 没有回调地址，无法正常回调
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String payment(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1. 创建一个Properties配置类
		Properties props = new Properties();
		//2. 获取配置文件 this.getClass().getClassLoader()  类加载器的类 getResourceAsStream加载资源文件
		props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));

		/*
		 * 3. 设置资源信息
		 * 	3-1. 业务类型，固定值Buy
		 * 	3-2. 	商号编码，在易宝的唯一标识
		 * 	3-3. 订单编码
		 * 	3-4. 支付金额
		 * 		3-4-1: 调用request#getParameter获取oid 并使用oid调用Service#load 得到Order后得到total
		 * 		3-4-2: 获取到total后转为字符串
		 * 	3-5. 	交易币种，固定值CNY
		 * 	3-6. 商品名称
		 * 	3-7. 商品种类
		 * 	3-8. 商品描述
		 * 	3-9. 在支付成功后，易宝会访问这个地址。
		 * 	3-10. 送货地址
		 * 	3-11. 扩展信息
		 * 	3-12. 支付通道
		 * 	3-13. 应答机制，固定值1
		 */
		String p0_Cmd = "Buy";
		String p1_MerId = props.getProperty("p1_MerId");
		String p2_Order = req.getParameter("oid");
		String p3_Amt = "0.01";
		//String p3_Amt = orderService.load(req.getParameter("oid")).getTotal()+"";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = props.getProperty("p8_Url");
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = req.getParameter("yh");
		String pr_NeedResponse = "1";
		
		/*
		 * 2. 计算hmac
		 * 	2-1. 从properties获取keyValue的值
		 * 	2-2. 调用PaymentUtil#buildHmac  依次传入上面定义的值，并在最后加入keyValue	（一共13个参数和一个keyValue）
		 */
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
		
		/*
		 * 3. 重定向到易宝的支付网关
		 * 生成的样例：
		 * https://www.yeepay.com/app-merchant-proxy/node?p0_Cmd=Buy&p1_MerId=10001126856&p2_Order=8BADA700266240058B38263BBC2F5FAB&p3_Amt=0.01&p4_Cur=CNY&p5_Pid=&p6_Pcat=&p7_Pdesc=&p8_Url=http://118.199.116.217:8080/goods/OrderServlet?method=back&p9_SAF=&pa_MP=&pd_FrpId=CEB-NET-B2C&pr_NeedResponse=1&hmac=bacb907b86d803e4e7052529a83fb190
		 * '='后的值由上面补充
		 */
		StringBuilder sb = new StringBuilder("https://www.yeepay.com/app-merchant-proxy/node");
		sb.append("?").append("p0_Cmd=").append(p0_Cmd);
		sb.append("&").append("p1_MerId=").append(p1_MerId);
		sb.append("&").append("p2_Order=").append(p2_Order);
		sb.append("&").append("p3_Amt=").append(p3_Amt);
		sb.append("&").append("p4_Cur=").append(p4_Cur);
		sb.append("&").append("p5_Pid=").append(p5_Pid);
		sb.append("&").append("p6_Pcat=").append(p6_Pcat);
		sb.append("&").append("p7_Pdesc=").append(p7_Pdesc);
		sb.append("&").append("p8_Url=").append(p8_Url);
		sb.append("&").append("p9_SAF=").append(p9_SAF);
		sb.append("&").append("pa_MP=").append(pa_MP);
		sb.append("&").append("pd_FrpId=").append(pd_FrpId);
		sb.append("&").append("pr_NeedResponse=").append(pr_NeedResponse);
		sb.append("&").append("hmac=").append(hmac);
		//4. 重定向到支付页面
		resp.sendRedirect(sb.toString());
		return null;
	}
	
	
	
	/**
	 * 支付成功后回馈
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 1.获取12个参数
		 * 2.获取keyValue
		 * 	2-1. 读取配置文件
		 * 	2-2.获取keyValue
		 * 3.调用PaymentUtil#verifyCallback校验调用者的身份
		 * 		>.保存错误信息转发到msg.jsp
		 * 4.校验通过
		 * 		>.重定向还是点对点
		 * 5.修改订单状态，保存成功信息，	转发msg.jsp
		 * 		>.如果是点对点：修改订单状态，返回success
		 */
		String p1_MerId = req.getParameter("p1_MerId");
		String r0_Cmd = req.getParameter("r0_Cmd");
		String r1_Code = req.getParameter("r1_Code");
		String r2_TrxId = req.getParameter("r2_TrxId");
		String r3_Amt = req.getParameter("r3_Amt");
		String r4_Cur = req.getParameter("r4_Cur");
		String r5_Pid = req.getParameter("r5_Pid");
		String r6_Order = req.getParameter("r6_Order");
		String r7_Uid = req.getParameter("r7_Uid");
		String r8_MP = req.getParameter("r8_MP");
		String r9_BType = req.getParameter("r9_BType");
		String hmac = req.getParameter("hmac");
		
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		
		String keyValue = props.getProperty("keyValue");
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId,
				r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType,
				keyValue);
		if(!bool){
			req.setAttribute("code", "error");
			req.setAttribute("status", "1");
			req.setAttribute("msg", "无效的签名，支付失败");
			return "f:/jsps/msg.jsp";
		}
		String successCode = "1";
		String successTypeRedirect = "1";
		String successTypePointToPoint = "2";
		if(successCode.equals(r1_Code)){
			orderService.updateStatus(r6_Order, 2);
			if(successTypeRedirect.equals(r9_BType)){
				req.setAttribute("code", "success");
				req.setAttribute("status", "1");
				req.setAttribute("msg", "支付成功");
				return "f:/jsps/msg.jsp";
			}else if(successTypePointToPoint.equals(r9_BType)){
				resp.getWriter().print("success");
			}
			
		}
		return null;
		
	}


	/**
	 * 支付宝支付
	 * @param req
	 * @param rep
	 * @throws IOException
	 */
	public void alipayment(HttpServletRequest req,HttpServletResponse rep) throws IOException {
		//1. 从request#getParameter中获取oid
		String oid = String.valueOf(req.getParameter("oid"));
		//2. 使用oid调用service#load获取Order
		Order orderload = orderService.load(oid);
		//3. 定义统计商品数量变量和商品（GoodsDetail）清单队列
		int sumGoods = 0;
		List<GoodsDetail> listGoodsDetail = new ArrayList<GoodsDetail>();
		//4. foreach遍历查询的order中的详细清单列表，将详细清单列表中的内容转化为GoodsDetail对象添加到列表，并累加商品总数信息
		for(OrderItem orderItem : orderload.getOrderItemList()){
			sumGoods +=orderItem.getQuantity();
			GoodsDetail goodsDetail = GoodsDetail.newInstance(orderItem.getBook().getBid(), orderItem.getBook().getBname(),Double.valueOf(orderItem.getBook().getCurrPrice()*100).longValue(), orderItem.getQuantity());
			listGoodsDetail.add(goodsDetail);
		}
		//5. 使用this，oid，total，sumGoods，listGoodsDetaik调用支付方法AlipayMain#test_trade_precreate
		String result = alipayMain.test_trade_precreate(this,oid, orderload.getTotal(), sumGoods, listGoodsDetail);
		/*
		 * 6. 判断是下单成功
		 * 	6-1. 如果成功
		 * 		1. 调用this#getServletContext#getRealPath方法得到一个读取alipayimg中订单id为名的png图片路径
		 * 		2. 使用ImageIO读取这个路径下的文件获得BufferedImage
		 * 		3. 通过ImageIO将BufferedImage写入到response的OutputStream中
		 * 		4. 将订单查询码写入session中用于查询
		 */
		String successCode = "成功";
		if (successCode.equals(result)){
			String realPath = this.getServletContext().getRealPath("/alipayimg/" + oid + ".png");
			BufferedImage bufferedImage = ImageIO.read(new File(realPath));
			ImageIO.write( bufferedImage,"png",rep.getOutputStream() );
			req.getSession().setAttribute("outTradeNo", oid);
		}
	}


	/**
	 * 支付宝支付查询方法
	 * @param req
	 * @param rep
	 * @return
	 */
	public String alipayBack(HttpServletRequest req,HttpServletResponse rep){
		//1. 从Session#getAttribute中获取订单号
		String outTradeNo = String.valueOf(req.getSession().getAttribute("outTradeNo"));
		//2. 实例化AlipayMain 并使用订单号调用 test_trade_query
		AlipayMain alipayMain = new AlipayMain();
		String result = alipayMain.test_trade_query(outTradeNo);
		/*
		 * 3. 判断是否支付成功
		 * 	1. 支付成功后调用service#updateStatus更新订单的状态为2
		 * 	2. 将支付结果转发到	jsps/msg.jsp （status用于判断是否显示登录的提示）
		 */
		String successCode = "成功";
		if(successCode.equals(result)){
			orderService.updateStatus(outTradeNo, 2);
			req.setAttribute("code", "success");
			req.setAttribute("status", "1");
			req.setAttribute("msg", "支付成功");
			return "f:/jsps/msg.jsp";
		}else{
			req.setAttribute("code", "error");
			req.setAttribute("status", "1");
			req.setAttribute("msg", result);
			return "f:/jsps/msg.jsp";
		}
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
		//1. 从request#getParameter中获取oid
		String oid = req.getParameter("oid");
		/*
		 * 2.取消订单
		 * 	1. 使用oid调用Service#findStatus方法获取订单状态
		 * 	2. 如果状态不是待支付状态则无法取消
		 * 	3. 将取消结果转发到	/jsps/msg.jsp
		 */
		int status = orderService.findStatus(oid);
		int waitPayment = 1;
		if(status != waitPayment){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "无法取消");
		}else {
			orderService.updateStatus(oid, 5);
			req.setAttribute("code", "success");
			req.setAttribute("msg", "取消成功");
		}
		
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * 提交订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String confirm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1. 从request#getParameter中获取oid
		String oid = req.getParameter("oid");
		/*
		 * 2. 校验订单
		 * 	1. 使用oid调用Service#findStatus获得状态
		 * 	2. 如果订单状态不是等待确认状态则无法确认
		 * 	3. 将信息写入request#setAttribute中 并转发到/jsps/msg.jsp
		 */
		int status = orderService.findStatus(oid);
		req.setAttribute("status", status);
		int waitSubmit = 3;
		if(status != waitSubmit){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "无法确认");
		}else {
			orderService.updateStatus(oid, 4);
			req.setAttribute("code", "success");
			req.setAttribute("msg", "交易成功");
		}
		
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * 加载订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1. 从request#getParameter中获取oid
		String oid = req.getParameter("oid");
		//2. 使用oid调用service#load获取Order
		Order order = orderService.load(oid);
		//3. 从request#getParameter 中获得btn 并将btn和order写入request#setAttribute中 转发到jsps/order/desc.jsp
		req.setAttribute("order", order);
		String btn = req.getParameter("btn");
		req.setAttribute("btn", btn);
		return "f://jsps/order/desc.jsp";
	}

	/**
	 * 生成订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String createOrder(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
		/*
		 * 1. 查询购物车条目
		 * 	1-1. 从request#getParameter获取cartItem的id
		 * 	1-2. 使用	cartItemIds 调用Service#loadCartItems查询购物车清单
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
		/*
		 * 2. 生成订单
		 * 	1. 创建order
		 * 	2. 调用CommonUtils#uuid生成主键
		 * 	3. 调用String#format生成下单时间 格式规则"%tF %<tT"
		 * 	4. 设置订单状态1
		 * 	5. 从request#getParameter获得地址
		 * 	6. 从Session#getAttribute获取用户
		 * 	7. 设置订单金额
		 * 		7-1.	BigDecimal进行金额计算防止精度丢失
		 */
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(String.format("%tF %<tT", new Date()));
		order.setStatus(1);
		order.setAddress(req.getParameter("address"));
		order.setUser((User)req.getSession().getAttribute("sessionUser"));
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem : cartItemList){
			total = total.add(new BigDecimal(cartItem.getSubtotal()+""));

		}
		order.setTotal(total.doubleValue());

		/*
		 * 3. 添加订单详情
		 *         3-1. 创建List<OrderItem>
		 *         3-2. foreach遍历购物车条目，并将订单信息转为订单条目
		 *         3-3. 设置订单详情列表
		 */
		List<OrderItem> orderList = new ArrayList<OrderItem>();
		for(CartItem cartItem : cartItemList){
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(CommonUtils.uuid());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setBook(cartItem.getBook());
			orderItem.setOrder(order);
			orderList.add(orderItem);
		}
		order.setOrderItemList(orderList);
		//4. 使用order调用Service#createOrder创建订单
		orderService.createOrder(order);
		//5. 使用cartItemIds调用Service#batchDelect删除购物车
		cartItemService.batchDelect(cartItemIds);
		//6. 将order转发到 jsps/order/ordersucc.jsp
		req.setAttribute("order", order);
		return "f://jsps/order/ordersucc.jsp";
	}



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
	 * 我的订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		//2.获取url
		String url = getUrl(req);
		//3.从当前session中获取用户	如果没有登录则转发到jsps/user/login.jsp
		User user = (User)req.getSession().getAttribute("sessionUser");
		if(user == null){
			req.setAttribute("msg", "您没有登录");
			return "r:/jsps/user/login.jsp";
		}
		
		//4.使用pc和cid调用Service#findByCategory得到PageBean
		PageBean<Order> pb = orderService.myOrders(user.getUid(), pc);
		
		//5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/order/list.jsp";
	}
}
