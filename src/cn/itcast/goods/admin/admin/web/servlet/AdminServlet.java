package cn.itcast.goods.admin.admin.web.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.admin.admin.domain.Admin;
import cn.itcast.goods.admin.admin.service.AdminService;
import cn.itcast.servlet.BaseServlet;

public class AdminServlet extends BaseServlet {
	AdminService adminService = new AdminService();
	
	
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Admin from = CommonUtils.toBean(req.getParameterMap(), Admin.class);
		Admin admin = adminService.login(from);
		if(admin == null){
			req.setAttribute("msg", "用户名或者密码错误");
			return "/adminjsps/login.jsp";
		}
		req.getSession().setAttribute("admin", admin);
		return "r:/adminjsps/admin/index.jsp";
	}
	
	public String quit(HttpServletRequest req,HttpServletResponse resp){
		req.getSession().invalidate();
		return "r:/jsps/main.jsp";
	}
}
