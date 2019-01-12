package cn.jijiking51.goods.category.web.servlet;

import cn.jijiking51.goods.category.Service.CategoryService;
import cn.jijiking51.goods.category.domain.Category;
import cn.jijiking51.Servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 分类的servlet
 * @author h4795
 *
 */
public class CategoryServlet extends BaseServlet {
	
	private CategoryService categoryService = new CategoryService(); 
	
	/**
	 * 查询所有分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 通过调用Service#findAll得到List<Category>
		 * 2. 保存到request中， 转发到left.jsp中
		 */
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/index";
	}
}
