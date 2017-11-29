package cn.itcast.goods.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.itcast.goods.user.domain.User;


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
			chain.doFilter(request, response);//放行
		}
		
	}


	public void init(FilterConfig fConfig) throws ServletException { 
		
	}

}
