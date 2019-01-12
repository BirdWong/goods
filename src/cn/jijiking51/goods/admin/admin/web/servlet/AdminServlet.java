package cn.jijiking51.goods.admin.admin.web.servlet;


import cn.jijiking51.goods.admin.admin.domain.Admin;
import cn.jijiking51.goods.admin.admin.service.AdminService;
import cn.jijiking51.goods.commons.CommonUtils;
import cn.jijiking51.Servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员用户登录退出功能
 * @author  h4795
 */
public class AdminServlet extends BaseServlet {
	AdminService adminService = new AdminService();

	/**
	 * 管理员登录功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.通过CommonUtils.toBean方法，把request#getParameter中属性映射到admin中
		Admin from = CommonUtils.toBean(req.getParameterMap(), Admin.class);
		//2. 以from为查询条件调用adminService#login查询管理员
		Admin admin = adminService.login(from);
		//3. 如果查询的结果为空 提示错误返回到adminjsps/login.jsp，否则登录成功将管理员添加到session，跳转到后台首页
		if(admin == null){
			req.setAttribute("msg", "用户名或者密码错误");
			return "/adminjsps/login.jsp";
		}
		req.getSession().setAttribute("admin", admin);
		return "r:/adminjsps/admin/index.jsp";
	}

	/**
	 * 管理员退出
	 * @param req
	 * @param resp
	 * @return
	 */
	public String quit(HttpServletRequest req,HttpServletResponse resp){
		//调用Session#invalidate销毁session进行退出
		req.getSession().invalidate();
		//重定向到 jsps/user/login.jsp
		return "r:/index";
	}
}
