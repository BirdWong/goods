package cn.itcast.goods.admin.category.web.servlet;



import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.Service.CategoryService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.servlet.BaseServlet;

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
		Category parent = CommonUtils.toBean(req.getParameterMap(), Category.class);//获取category并分装
		parent.setCid(CommonUtils.uuid());//手动添加cid
		categoryService.add(parent);//添加一级分类
		return findAll(req,resp);//返回页面
		
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
			String cid = req.getParameter("cid");
			List<Category> parents = categoryService.findParent();
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
		Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);//获取category并分装
		child.setCid(CommonUtils.uuid());//手动添加cid
		Category parent = new Category();
		String pid = req.getParameter("pid");
		parent.setCid(pid);
		child.setParent(parent); 
		categoryService.add(child);//添加二级分类
		
		return findAll(req,resp);//返回页面
		
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
		String cid = req.getParameter("cid");
		Category parent = categoryService.load(cid);
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
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		categoryService.edit(category);
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
		String cid = req.getParameter("cid");
		Category child = categoryService.load(cid);
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
		Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);
		
		String pid = req.getParameter("pid");
		Category parent = new Category();
		parent.setCid(pid);
		child.setParent(parent);
		categoryService.edit(child);
		if(child.getParent() == null)
			System.out.println("aaaaaaaaaaaaaaa");
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
		String pid = req.getParameter("pid");
		int count = categoryService.findChildrenCountByParent(pid);
		if(count != 0){
			req.setAttribute("msg", "该分类还有二级分类， 不能删除");
			return "f:/adminjsps/msg.jsp";
		}else {
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
		String cid = req.getParameter("cid");
		int count = bookService.findBookCountByCategory(cid);
		if(count != 0){
			req.setAttribute("msg", "该分类还有书籍， 不能删除");
			return "f:/adminjsps/msg.jsp";
		}else {
			categoryService.delete(cid);
			req.setAttribute("msg", "删除成功");
			return findAll(req, resp);
		}
	}
	
}
