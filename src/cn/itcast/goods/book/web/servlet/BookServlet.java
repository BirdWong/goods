package cn.itcast.goods.book.web.servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	
	/**
	 * 按照bid查询
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
	
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
		return "f:/jsps/book/list.jsp";
	
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
		return "f:/jsps/book/list.jsp";
	
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
		return "f:/jsps/book/list.jsp";
	
	}
}
