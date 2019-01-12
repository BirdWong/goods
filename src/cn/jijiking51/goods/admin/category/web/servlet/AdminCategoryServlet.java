package cn.jijiking51.goods.admin.category.web.servlet;


import cn.jijiking51.Servlet.BaseServlet;
import cn.jijiking51.goods.book.service.BookService;
import cn.jijiking51.goods.category.Service.CategoryService;
import cn.jijiking51.goods.category.domain.Category;
import cn.jijiking51.goods.commons.CommonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AdminCategoryServlet extends BaseServlet {
	
	private CategoryService categoryService = new CategoryService();
	
	private BookService bookService = new BookService();
	
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
		 * 1. 通过serivce#findAll得到所有分类
		 * 2. 保存到request中， 转发到adminjsps/admin/category/list.jsp
		 */
		req.setAttribute("parents", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	
	/**
	 * 添加一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//调用CommonUtils#toBean获取map并分装成category
		Category parent = CommonUtils.toBean(req.getParameterMap(), Category.class);
		//调用CommonUtils#uuid()手动添加cid
		parent.setCid(CommonUtils.uuid());
		//调用categoryService#add 添加一级分类
		categoryService.add(parent);
		//调用findAll返回页面
		return findAll(req,resp);
		
	}
	
	/**
	 * 返回该二级分类的父分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//调用request#getParameter获取cid
		String cid = req.getParameter("cid");
		// 调用categoryService#findParent获取parents的list集合
		List<Category> parents = categoryService.findParent();
		//设置信息，并转发到adminjsps/admin/category/add2.jsp
		req.setAttribute("pid", cid);
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/add2.jsp";
	}
	
	/**
	 * 查询二级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//调用CommonUtils#toBean获取map并分装成category
		Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);
		//调用CommonUtils#uuid()手动添加cid
		child.setCid(CommonUtils.uuid());
		Category parent = new Category();
		//调用request#getParameter获取pid
		String pid = req.getParameter("pid");
		parent.setCid(pid);
		child.setParent(parent);
		//调用categoryService#add 添加二级分类
		categoryService.add(child);
		//调用findAll返回页面
		return findAll(req,resp);
		
	}
	
	
	/**
	 * 修改一级分类   第一步
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParentPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//调用request#getParameter获取cid
		String cid = req.getParameter("cid");
		//调用categoryService#load 使用cid查询一级分类
		Category parent = categoryService.load(cid);
		// 将分类添加到request 并返回adminjsps/admin/category/edit.jsp
		req.setAttribute("parent", parent);
		return "f:/adminjsps/admin/category/edit.jsp";
	}
	
	
	/**
	 * 修改一级分类 第二部
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//调用CommonUtils#toBean获取map并分装成category
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		//调用categoryService#edit 修改分类
		categoryService.edit(category);
		//调用findAll返回页面
		return findAll(req,resp);
	}
	
	
	
	/**
	 * 修改二级分类   第一步
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//调用request#getParameter获取cid
		String cid = req.getParameter("cid");
		//调用categoryService#load 使用cid查询二级分类
		Category child = categoryService.load(cid);
		// 将分类添加到request 并返回adminjsps/admin/category/edit.jsp
		req.setAttribute("child", child);
		req.setAttribute("parents", categoryService.findParent());
		return "f:/adminjsps/admin/category/edit2.jsp";
	}
	
	
	/**
	 * 修改二级分类 第二部
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//调用CommonUtils#toBean获取map并分装成category
		Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);
		//调用request#getParameter获取pid
		String pid = req.getParameter("pid");
		//补充child分类
		Category parent = new Category();
		parent.setCid(pid);
		child.setParent(parent);
		//调用categoryService#edit修改二级分类
		categoryService.edit(child);
		//调用findAll返回页面
		return findAll(req,resp);
	}
	
	/**
	 * 删除一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//调用request#getParameter获取pid
		String pid = req.getParameter("pid");
		//调用categoryService#findChildrenCountByParent 获得一级分类下的二级分类数目
		int count = categoryService.findChildrenCountByParent(pid);
		// 如果二级分类的数目不为0 则拒绝删除，返回信息到adminjsps/msg.jsp
		if(count != 0){
			req.setAttribute("msg", "该分类还有二级分类， 不能删除");
			return "f:/adminjsps/msg.jsp";
		}else {
			//调用category#delete删除分类 并调用findAll返回
			categoryService.delete(pid);
			req.setAttribute("msg", "删除成功");
			return findAll(req, resp);
		}
	}
	
	/**
	 * 删除二级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteChildren(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//调用request#getParameter获取pid
		String cid = req.getParameter("cid");
		//调用bookService#findBookCountByCategory方法查询书籍数量
		int count = bookService.findBookCountByCategory(cid);
		//如果书籍数目不为0 则拒绝删除，并将信息转发到adminjsps/msg.jsp
		if(count != 0){
			req.setAttribute("msg", "该分类还有书籍， 不能删除");
			return "f:/adminjsps/msg.jsp";
		}else {
			//调用categoryService#delete删除分类 并调用findAll返回
			categoryService.delete(cid);
			req.setAttribute("msg", "删除成功");
			return findAll(req, resp);
		}
	}
	
}
