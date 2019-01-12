	package cn.jijiking51.goods.admin.book.web.servlet;


	import cn.jijiking51.Servlet.BaseServlet;
	import cn.jijiking51.goods.book.domain.Book;
	import cn.jijiking51.goods.book.service.BookService;
	import cn.jijiking51.goods.category.Service.CategoryService;
	import cn.jijiking51.goods.category.domain.Category;
	import cn.jijiking51.goods.commons.CommonUtils;
	import cn.jijiking51.goods.pager.PageBean;

	import javax.servlet.ServletException;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import java.io.File;
	import java.io.IOException;
	import java.util.List;
	import java.util.Map;

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
			 * 调用category#findParent获取所有一级分类转发到add.jsp
			 * 下拉可以显示全部
			 */
			List<Category> parents = categoryService.findParent();
			req.setAttribute("parents", parents);
			return "f:/adminjsps/admin/book/add.jsp";
		}

		public String ajaxFindChildren(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			/*
			 * 调用request#getParameter获取pid
			 * 通过pid 调用categoryService#findChildren查询出所有的二级分类
			 * 调用toJson把list<category>转化为json
			 * 通过response#getWriter#print写出
			 */
			String pid = req.getParameter("pid");
			List<Category> children = categoryService.findChildren(pid);
			String json = toJson(children);

			resp.getWriter().print(json);

			return null;
		}

		/**
		 * 将对象转化为json
		 * @param category
		 * @return
		 */
		private String toJson(Category category){
			StringBuilder sb = new StringBuilder("{");
			sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
			sb.append(",");
			sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
			sb.append("}");
			return sb.toString();
		}
		//[{"cid":"fgdf" , "cname":"dafasdfasg"},{"cid":"fgdf" , "cname":"dafasdfasg"},{"cid":"fgdf" , "cname":"dafasdfasg"}]

		/**
		 * 将对象列表转化为Json
		 * @param categoryList
		 * @return
		 */
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
			 * 1. 通过serivce#findAll得到所有分类
			 * 2. 保存到request中， 转发到left.jsp中
			 */
			List<Category> parents = categoryService.findAll();
			req.setAttribute("parents", parents);
			return "f:/adminjsps/admin/book/booklist.jsp";
		}





		/**
		 * 按照bid查询
		 */
		public String load(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			//1. 调用request#getParameter获得bid
			String bid = req.getParameter("bid");
			//2. 调用bookService#load得到book ，并添加book到request
			Book book = bookService.load(bid);
			req.setAttribute("book", book);
			//获取所有一级分类 通过调用categoryService#findParent获得一级分类
			req.setAttribute("parents", categoryService.findParent());
			//获取该一级目录下的所有二级目录 通过调用categoryService#findChildren
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
			//返回pc
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
			 * 1.得到 pc
			 *  >页面传使用页面的，页面不传  pc = 1；
			 */
			int pc = getPc(req);
			//2.通过getUrl获取url
			String url = getUrl(req);
			//3.从request的getParameter 获取查询条件cid
			String cid = req.getParameter("cid");
			//4.使用pc和cid调用Service#findByCategory得到PageBean
			PageBean<Book> pb = bookService.findByCategory(cid, pc);
			//5.给PageBean设置url， 保存PageBean，转发到 adminjsps/admin/book/list.jsp
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
			//2.通过getUrl获取url
			String url = getUrl(req);
			//3.从request的getParameter获取查询条件author
			String author = req.getParameter("author");
			//4.使用pc和author调用Service#findByCategory得到PageBean
			PageBean<Book> pb = bookService.findByAuthor(author, pc);

			//5.给PageBean设置url， 保存PageBean，转发到 /adminjsps/admin/book/list.jsp
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
			//2.通过getUrl获取url
			String url = getUrl(req);
			//3.从request的getParameter获取查询条件Bname
			String bname = req.getParameter("bname");
			//4.使用bname和author调用Service#findByCategory得到PageBean
			PageBean<Book> pb = bookService.findByBname(bname, pc);
			//5.给PageBean设置url， 保存PageBean，转发到 /jsps/book/list.jsp
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
			//2.通过getUrl获取url
			String url = getUrl(req);
			//3.从request的getParameter获取查询条件press
			String press = req.getParameter("press");
			//4.使用press和author调用Service#findByCategory得到PageBean
			PageBean<Book> pb = bookService.findByPress(press, pc);
			//5.给PageBean设置url， 保存PageBean，转发到 /adminjsps/admin/book/list.jsp
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
			//1. 从request#getParameterMap获取数据map 并调用CommonUtils#toBean封装成book和category
			Map map = req.getParameterMap();
			Book book = CommonUtils.toBean(map, Book.class);
			Category category = CommonUtils.toBean(map, Category.class);
			book.setCategory(category);
			//2. 调用service#edit 修改book
			bookService.edit(book);
			//3. 返回成功信息 转发到adminjsps/msg.jsp
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
			//1. 从request#getParameterMap获取数据map 再获取bid
			Map  map  = req.getParameterMap();
			String bid = req.getParameter("bid");
			//2. 调用bookService#load 加载书籍
			Book book = bookService.load(bid);
			//3. 获取当前真是地址
			String savePath = this.getServletContext().getRealPath("/");//获取真实路径
			//4. 使用File#delete删除大图和小图
			new File(savePath,book.getImage_b()).delete();
			new File(savePath,book.getImage_w()).delete();
			//5. 调用bookService#delete删除book
			bookService.delete(book);
			//6. 提示删除成功并转发到adminjsps/msg.jsp
			req.setAttribute("msg", "删除成功");
			return "f:/adminjsps/msg.jsp";
		}

	}
