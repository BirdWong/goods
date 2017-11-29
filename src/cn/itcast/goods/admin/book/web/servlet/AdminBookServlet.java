	package cn.itcast.goods.admin.book.web.servlet;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.Service.CategoryService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {

	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	
	/**
	 * 添加图书：第一步
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 获取所有一级分类转发到add.jsp
		 * 下拉可以显示全部
		 */
		List<Category> parents = categoryService.findParent();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	
	public String ajaxFindChildren(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 获取pid
		 * 通过pid 查询出所有的二级分类
		 * 把list<category>转化为json
		 */
		String pid = req.getParameter("pid");
		List<Category> children = categoryService.findChildren(pid);
		String json = toJson(children);
		
		resp.getWriter().print(json);
		
		return null;
	}	
	
	private String toJson(Category category){
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
		sb.append(",");
		sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
	//[{"cid":"fgdf" , "cname":"dafasdfasg"},{"cid":"fgdf" , "cname":"dafasdfasg"},{"cid":"fgdf" , "cname":"dafasdfasg"}]
	private String toJson(List<Category> categoryList){
		StringBuilder sb = new StringBuilder("[");
		for(int i = 0; i < categoryList.size();i++){
			sb.append(toJson(categoryList.get(i)));
			if(i < categoryList.size() - 1){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	
	/**
	 * 显示所有分页
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findCategoryAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 通过serivce得到所有分类
		 * 2. 保存到request中， 转发到left.jsp中
		 */
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/left.jsp";
	}
	
	
	
	
	
	/**
	 * 按照bid查询
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		//获取所有一级分类
		req.setAttribute("parents", categoryService.findParent());
		//获取该一级目录下的所有二级目录
		String pid = book.getCategory().getParent().getCid();
		req.setAttribute("children", categoryService.findChildren(pid)); 
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	
	
	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req){
		int pc = 1;
		String param = req.getParameter("pc");
		if(param !=  null && !param.trim().isEmpty()){
			try{
				pc = Integer.parseInt(param);
			}catch(RuntimeException e){}
		}
		return pc;
	}
	
	
	
	
	/**
	 * 截取url 页面中的分页导航中需要他作为超链接
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req){
		String url = req.getRequestURI() +"?" +req.getQueryString();
		int index = url.lastIndexOf("&pc=");
		if(index!=-1){
			url=url.substring(0,index);
		}
		return url;
	}
	
	
	/**
	 * 按分类查
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		/*
		 * 2.获取url
		 */
		String url = getUrl(req);
		/*
		 * 3.获取查询条件cid
		 */
		String cid = req.getParameter("cid");
		/*
		 * 4.使用pc和cid调用Service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		
		/*
		 * 5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
		 */
		
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	/**
	 * 按作者查
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByAuthor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		/*
		 * 2.获取url
		 */
		String url = getUrl(req);
		/*
		 * 3.获取查询条件author
		 */
		String author = req.getParameter("author");
		/*
		 * 4.使用pc和author调用Service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		
		/*
		 * 5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
		 */
		
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/book/list.jsp";
	
	}
	/**
	 * 按书名查
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByBname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		/*
		 * 2.获取url
		 */
		String url = getUrl(req);
		/*
		 * 3.获取查询条件Bname
		 */
		String bname = req.getParameter("bname");
		/*
		 * 4.使用bname和author调用Service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByBname(bname, pc);
		
		/*
		 * 5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
		 */
		
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/book/list.jsp";
	
	}
	
	/**
	 * 按出版社
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByPress(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		/*
		 * 2.获取url
		 */
		String url = getUrl(req);
		/*
		 * 3.获取查询条件press
		 */
		String press = req.getParameter("press");
		/*
		 * 4.使用press和author调用Service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByPress(press, pc);
		
		/*
		 * 5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
		 */
		
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/book/list.jsp";
	
	}
	
	
	/**
	 * 按多条件组合
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCombination(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		/*
		 * 2.获取url
		 */
		String url = getUrl(req);
		/*
		 * 3.获取查询条件book
		 */
		Book criteria = CommonUtils.toBean(req.getParameterMap(), Book.class);
		/*
		 * 4.使用bname和author调用Service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByCombination(criteria, pc);
		
		/*
		 * 5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
		 */
		
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/book/list.jsp";
	
	}
	/**
	 * 修改书籍
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map map = req.getParameterMap();
		Book book = CommonUtils.toBean(map, Book.class);
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		bookService.edit(book);
		req.setAttribute("msg", "修改成功！");
		return "f:/adminjsps/msg.jsp";
	}
	
	
	/**
	 * 删除图书
	 * @param req
	 * @param resp
	 * @return
	 */
	public String delete(HttpServletRequest req , HttpServletResponse resp){
		Map  map  = req.getParameterMap();
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		String savePath = this.getServletContext().getRealPath("/");//获取真实路径
		System.out.println(savePath+book.getImage_b());
		new File(savePath,book.getImage_b()).delete();
		new File(savePath,book.getImage_w()).delete();
		
		bookService.delete(book);
		req.setAttribute("msg", "删除成功");
		return "f:/adminjsps/msg.jsp";
	}
	
}
