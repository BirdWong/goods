package cn.jijiking51.goods.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class LoginFilter implements Filter {


	
	public void destroy() {
		
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		/*
		 * 获取session中的user
		 */
		HttpServletRequest req = (HttpServletRequest)request;
		Object user = req.getSession().getAttribute("sessionUser");
		if(user == null){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "请先登录");
			req.setAttribute("status", -1);
			req.getRequestDispatcher("jsps/msg.jsp").forward(req, response);
		}else{
			//放行
			chain.doFilter(request, response);
		}
		
	}


	public void init(FilterConfig fConfig) throws ServletException { 
		
	}

}
