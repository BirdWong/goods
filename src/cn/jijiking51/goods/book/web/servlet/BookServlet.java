package cn.jijiking51.goods.book.web.servlet;


import cn.jijiking51.goods.book.domain.Book;
import cn.jijiking51.goods.book.service.BookService;
import cn.jijiking51.goods.commons.CommonUtils;
import cn.jijiking51.goods.pager.PageBean;
import cn.jijiking51.Servlet.BaseServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class BookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	
	/**
	 * 通过load方法，按照bid查询
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1. 从request的getParameter 获取查询条件cid
		String bid = req.getParameter("bid");
		//2. 使用bid调用Service#load得到Book
		Book book = bookService.load(bid);
		//3. 转发到 /jsps/book/desc.jsps
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
	
	
	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req){
		//设定为初始1，如果getParameter中有值会将其覆盖
		int pc = 1;
		//1. 获取pc（分页）的数值
		String param = req.getParameter("pc");
		//判断是否为null，或者为空
		if(param !=  null && !param.trim().isEmpty()){
			try{
				pc = Integer.parseInt(param);
			}catch(RuntimeException e){}
		}
		//2.返回pc值
		return pc;
	}
	
	
	
	
	/**
	 * 截取url 页面中的分页导航中需要他作为超链接
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req){

		/*
		 * 1. 通过request的getRequestURI方法获取请求的类名
		 * 通过request的getQueryString方法获取？后面的参数
		 * 将两个通过‘？’连接起来恢复原来的url
		 */
		String url = req.getRequestURI() +"?" +req.getQueryString();
		/*
		 * 2. 通过String的lastIndexOf方法查询‘&pc=’的位置
		 * 如果位置不等于-1
		 * 则使用String的substring方法截取0-该位置的字符创
		 */
		int index = url.lastIndexOf("&pc=");
		if(index!=-1){
			url=url.substring(0,index);
		}
		//3.返回截取后的url
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
		 * 1.用过getPc得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		//2.通过getUrl获取url
		String url = getUrl(req);
		//3.从request的getParameter 获取查询条件cid
		String cid = req.getParameter("cid");
		//4.使用pc和cid调用Service#findByCategory得到PageBean
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		//5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
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
		 * 1.用过getPc得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		//2.通过getUrl获取url
		String url = getUrl(req);
		//3.从request的getParameter获取查询条件author
		String author = req.getParameter("author");

		//4.使用pc和author调用Service#findByCategory得到PageBean
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		
		//5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
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
		 * 1.用过getPc得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		//2.通过getUrl获取url
		String url = getUrl(req);
		//3.从request的getParameter获取查询条件Bname
		String bname = req.getParameter("bname");
		//4.使用bname和author调用Service#findByCategory得到PageBean
		PageBean<Book> pb = bookService.findByBname(bname, pc);
		
		//5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsp
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
		 * 1.用过getPc得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		//2.通过getUrl获取url
		String url = getUrl(req);
		//3.从request的getParameter获取查询条件press
		String press = req.getParameter("press");
		//4.使用press和author调用Service#findByCategory得到PageBean
		PageBean<Book> pb = bookService.findByPress(press, pc);
		//5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
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
		 * 1.用过getPc得到 pc
		 *  >页面传使用页面的，页面不传  pc = 1；
		 */
		int pc = getPc(req);
		//2.通过getUrl获取url
		String url = getUrl(req);
		/*
		 * 3.从request的getParameter获取查询条件book
		 * 	3-1.从request中取出ParameterMap
		 * 	3-2.使用CommonUtils的toBean方法封装成一个Book对象
		 */
		Book criteria = CommonUtils.toBean(req.getParameterMap(), Book.class);
		//4.使用bname和author调用Service#findByCategory得到PageBean
		PageBean<Book> pb = bookService.findByCombination(criteria, pc);
		
		//5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsps
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	
	}
}
