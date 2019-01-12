package cn.jijiking51.goods;

import cn.jijiking51.goods.book.domain.Book;
import cn.jijiking51.goods.book.service.BookService;
import cn.jijiking51.goods.category.Service.CategoryService;
import cn.jijiking51.goods.category.domain.Category;
import cn.jijiking51.goods.pager.PageBean;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author Administrator
 * @date 2018/12/20 0020
 */
@WebServlet("/index")
public class TestServlet extends HttpServlet {
	CategoryService categoryService = new CategoryService();
	BookService bookService = new BookService();
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse rep) throws IOException, ServletException {
		List<Category> parents = categoryService.findAll();

		req.setAttribute("parents", parents);
		PageBean<Book> pb1 = bookService.findByCategory("5F79D0D246AD4216AC04E9C5FAB3199E", 1);
		req.setAttribute("pb1", pb1);

		PageBean<Book> pb2 = bookService.findByCategory("D45D96DA359A4FEAB3AB4DCF2157FC06", 1);
		req.setAttribute("pb2", pb2);

		PageBean<Book> pb3 = bookService.findByCategory("922E6E2DB04143D39C9DDB26365B3EE8", 1);
		req.setAttribute("pb3", pb3);

		PageBean<Book> pb4 = bookService.findByCategory("84ECE401C2904DBEA560D04A581A66D9", 1);
		req.setAttribute("pb4", pb4);

		req.getRequestDispatcher("/index_main.jsp").forward(req,rep);
	}
}
