package cn.itcast.goods.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.category.Service.CategoryService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.servlet.BaseServlet;

/**
 * web层
 * @author 91271
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
		 * 1. 通过serivce得到所有分类
		 * 2. 保存到request中， 转发到left.jsp中
		 */
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/jsps/left.jsp";
	}
}