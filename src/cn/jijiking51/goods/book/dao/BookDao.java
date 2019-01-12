package cn.jijiking51.goods.book.dao;

import cn.jijiking51.goods.book.domain.Book;
import cn.jijiking51.goods.book.domain.BookPrice;
import cn.jijiking51.goods.category.domain.Category;
import cn.jijiking51.goods.commons.CommonUtils;
import cn.jijiking51.jdbc.TxQueryRunner;
import cn.jijiking51.goods.pager.Expression;
import cn.jijiking51.goods.pager.PageBean;
import cn.jijiking51.goods.pager.PageConstants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author  h4795
 * book的Dao层操作类
 * 使用QueryRunner简化数据库操作
 */
public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	
	
	/**
	 * 按照bid查询
	 * @throws SQLException 
	 * 
	 */
	public Book findByBid(String bid) throws SQLException{

		/*
		 * 1.通过t_book表和t_category通过cid的属性关联，并且通过bid查询
		 * 一行记录中， 包含很多book属性， 还有一个cid属性
		 * 通过连接获取到这个cid 的所有属性
		 */
		String sql = "select * from t_book b,t_category c where c.cid=b.cid and bid=?";
		/*
		 * 2.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh MapHandler，第三个参数传入查询条件值bid
		 * 注：<code>MapHandler</code> 用于获取结果集中的第一行数据，并将其封装到一个Map中，Map 中 key 是数据的列别名（as label）,
		 * 	如果没有就是列的实际名称，Map 中 value 就是列的值，注意代表列的 key 不区分大小写。
		 */
		Map<String,Object> map = qr.query(sql, new MapHandler(),bid);
		//3.通过CommonUtils.toBean方法，把map中除cid属性映射到book中
		Book book = CommonUtils.toBean(map, Book.class);
		//4.通过CommonUtils.toBean方法，把map中cid属性映射到category
		Category category = CommonUtils.toBean(map, Category.class);
		//5.将book和category建立联系
		book.setCategory(category);
		/*
		 * 6.判断map中是否有pid
		 * category中的父级分类没有添加进去，所以将pid封装成一个独立的category，然后赋给二级category
		 */
		String parentIdName = "pid";
		if(map.get(parentIdName)!=null){
			Category parent = new Category();
			parent.setCid((String)map.get(parentIdName));
			category.setParent(parent);
		}

		/*
		 * 7.返回这个book
		 */
		return book;
	}
	
	/**
	 * 按照分类查询
	 * @param cid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByCategory(String cid , int pc) throws SQLException{
		//1. 创建Expression的List
		List<Expression> exprList = new ArrayList<Expression>();
		//2. 给创建新对象，name='cid'，operator='='，value=cid,并添加到list中
		exprList.add(new Expression("cid" ,"=",cid));
		/*
		 * 3.通过findByCriteria查询结果
		 * 并返回
		 */
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按照名字查询
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByBname(String bname , int pc) throws SQLException{
		//1. 创建Expression的List
		List<Expression> exprList = new ArrayList<Expression>();
		//2. 给创建新对象，name='author'，operator='like'，value=%bname%,并添加到list中
		exprList.add(new Expression("bname" ,"like","%"+bname+"%"));
		/*
		 * 3.通过findByCriteria查询结果
		 * 并返回
		 */
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按照作者查
	 * @param author
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByAuthor(String author , int pc) throws SQLException{

		 //1. 创建Expression的List
		List<Expression> exprList = new ArrayList<Expression>();
		 //2. 给创建新对象，name='author'，operator='like'，value=%author%,并添加到list中
		exprList.add(new Expression("author" ,"like","%"+author+"%"));
		/*
		 * 3.通过findByCriteria查询结果
		 * 并返回
		 */
		return findByCriteria(exprList, pc);
	}
	
	
	/**
	 * 按照出版社查
	 * @param press
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByPress(String press , int pc) throws SQLException{
		//1. 创建Expression的List
		List<Expression> exprList = new ArrayList<Expression>();
		//2. 给创建新对象，name='author'，operator='like'，value=%author%,并添加到list中
		exprList.add(new Expression("press" ,"like","%"+press + "%"));
		/*
		 * 3.通过findByCriteria查询结果
		 * 并返回
		 */
		return findByCriteria(exprList, pc);
	}
	
	
	
	/**
	 * 多条件组合查询
	 */
	public PageBean<Book> findByCombination(Book book , int pc) throws SQLException{
		//1. 创建Expression的List
		List<Expression> exprList = new ArrayList<Expression>();
		//2. 给创建新对象，name='press'，operator='like'，value=%press%,并添加到list中
		exprList.add(new Expression("press" ,"like","%"+book.getPress() + "%"));
		//3. 给创建新对象，name='bname'，operator='like'，value=%bname%,并添加到list中
		exprList.add(new Expression("bname" ,"like","%"+book.getBname() + "%"));
		//4. 给创建新对象，name='author'，operator='like'，value=%author%,并添加到list中
		exprList.add(new Expression("author" ,"like","%"+book.getAuthor() + "%"));
		/*
		 * 3.通过findByCriteria查询结果
		 * 并返回
		 */
		return findByCriteria(exprList, pc);
	}
	
	
	
	/**
	 * 查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByCriteria(List<Expression> exprList , int pc) throws SQLException{
		/*
		 * 1.得到ps
		 * 2.得到tr
		 * 3.得到beanlist
		 * 4.创建PageBean， 返回
		 */
		/*
		 * 1.通过PageConstants的BOOK_PAGE_SIZE赋值给ps
		 * 每页记录数
		 */
		int ps = PageConstants.BOOK_PAGE_SIZE;
		
		
		/*
		 * 2.通过 Expression 生成where子句
		 * 	2-1.添加一个条件 以 and 开头 ， select count（*） t_book where 1=1 and xxx = ？           1=1  不影响查找，
		 * 	2-2.sql 中有问号，  对应问号的值
		 * 	2-3.用一个list存储查询的条件值
		 */
		StringBuilder wheresql = new StringBuilder(" where 1=1");
		List<Object> params = new ArrayList<Object>();
		/*
		 * 3.将条件和之前的wheresql连接成一条sql语句
		 * 	3-1.以and 开头
		 * 	3-2.条件的名称
		 * 	3-3.条件的运算符可以是  = , != ,>, < .......   is null没有值
		 * 例：	条件sql ： where 1=1 and name= ? and score is null
		 *  		1. 上面一步已经拥有 where 1=1
		 *  		2. 将 'and' + ' '	 + 'name' + '='  +' '
		 * 		3. 判断是否条件是is null
		 *  		4. 如果不是is null则将'?'连接
		 *  		5. 将	value 也就是条件值加入到params中用于补充'?"的内容
		 */
		for(Expression expr : exprList){
			wheresql.append(" and ").append(expr.getName()).append(" ").append(expr.getOperator()).append(" ");
			if(!"is null".equals(expr.getOperator())){
				wheresql.append("?");
				params.add(expr.getValue());
			}
		}
		
		/*
		 * 4.获取总记录数
		 *	4-1.将sql补充完成
		 *		4-1-1.完成select 和 from	部分 并且与 wheresql拼接
		 *	4-2.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用ScalarHandler，第三个参数传入查询条件值，需要将list转为数组
		 *	4-3.返回回来的值使用Number对象接收，再使用Number方法中的intValue转为int
		 * 注：<code>ScalarHandler</code>用于获取结果集中第一行某列的数据并转换成 T 表示的实际对象。
		 *       该类对结果集的处理直接在 handle 方法中进行，不涉及 dbutils 库的其他类。
		 */
		String sql = "select count(*) from t_book " + wheresql ;
		Number number = (Number)qr.query(sql, new ScalarHandler() , params.toArray());
		int tr = number.intValue();

		
		/*
		 * 5.获取当前页记录
		 * 	5-1.拼接sql语句 完成select ， from ， order by和limit部分，order by 使用orderBy字段排序（order by默认为升序排序）
		 * 	5-2.添加条件 首行记录下标。计算为 当前页（pc） 减去 1 再乘以 每一页的记录数 算出来的为：之前页面记录数
		 * 	5-3. 添加条件 一共查询几行， 就是每一页的记录数
		 * 	5-4.通过QueryRun的query查询Book集合
		 * 		(sql, rsh, Object... params) 第二个参数rsh BeanListHandler 需要设置泛型为Book，并且传入初始化class，第三个参数传入查询条件值，需要将list转为数组
		 * 注：<code>BeanListHandler</code>用于将结果集的每一行数据转换为Javabean，再将这个Javabean添加到ArrayList中。
		 * 	可以简单的看着是BeanHandler的高级版，只不过是多了一步，就是将生成的Javabean添加到ArrayList中，其他的处理都和BeanHandler一样。
		 */
		sql = "select * from t_book" + wheresql + " order by orderBy limit ?,?";
		params.add((pc - 1 ) * ps);
		params.add(ps);
		List<Book> bookList = qr.query(sql , new BeanListHandler<Book>(Book.class),params.toArray());
		
		//创建PageBean ， 设置泛型参数
		PageBean<Book> pb = new PageBean<Book>();
		/*
		 * 6. 给PageBean设置返回值集合，当前页，页面记录数，总数
		 *	其中PageBean没有url，这个任务由Servlet完成
		 */
		pb.setBeanList(bookList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		//7. 返回PageBean
		return pb;
	}
	
	
	/**
	 * 查询该分类下的book数量
	 * 
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public int findBookCountByCategory(String cid) throws SQLException{
		/*
		 * 1. 以cid为查询总数条件编写sql
		 * 2.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用ScalarHandler，第三个参数传入查询条件值，需要将list转为数组
		 * 3.返回回来的值使用Number对象接收，再使用Number方法中的intValue转为int
		 * 4. 返回number的值 使用三元运算符进行判断是否为null
		 * 注：<code>ScalarHandler</code>用于获取结果集中第一行某列的数据并转换成 T 表示的实际对象。
		 *       该类对结果集的处理直接在 handle 方法中进行，不涉及 dbutils 库的其他类。
		 */
		String sql = "select count(*) from t_book where cid=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(),cid);
		return number == null?0:number.intValue();
	}

	/**
	 * 添加图书
	 * @param book
	 * @throws SQLException 
	 */
	public boolean add(Book book) throws SQLException {
		//1.从BookPrice中分别获取值：书籍的最大价格限制、书籍的最大优惠限制
		int price = BookPrice.BOOK_MAX_PRICE;
		int currPrice = BookPrice.BOOK_MAX_PRICE;
		int discount = BookPrice.BOOK_MAX_DISCOUNT;
		//2.进行判断书籍的定价，实际价格，折扣价格是否符合规矩，如果不符合直接返回false
		if(book.getPrice()>price||book.getCurrPrice()>currPrice||book.getDiscount()>discount){
			return false;
		}
		//3.编写sql语句，分别插入的值	 书籍id，书籍名称，作者，定价，实际价格，折扣，出版社，出版时间，版次，页数，字数，印刷时间，纸张开数，纸质，所属分类id，大图链接，小图链接
		String sql = "insert into t_book(bid,bname,author,price,currPrice," +
				"discount,press,publishtime,edition,pageNum,wordNum,printtime," +
				"booksize,paper,cid,image_w,image_b)" +
				" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		//4. 编写Object数组，分别按顺序存储上面内容的值
		Object[] params = {book.getBid(),book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
				book.getImage_w(),book.getImage_b()};
		//5. 使用QueryRun的update方法插入数据
		qr.update(sql, params);
		//6. 返回成功信息
		return true;
	}
	
	/**
	 * 修改图书
	 */
	public void edit(Book book) throws SQLException{
		//1. 以bid为条件，编写sql语句修改：书籍名称，作者，定价，实际价格，折扣，出版社，出版时间，版次，页数，字数，印刷时间，纸张开数，纸质，所属分类id
		String sql = "update t_book set bname=?, author=?, price=?, currPrice=?, " +
				"discount=? , press=? , publishtime=? , edition=? , pageNum=? , wordNum = ? , printtime=? , " +
				"booksize=? , paper=? , cid = ? where bid=?";
		//2. 编写Object数组，分别按顺序存储上面内容的值
		Object[] params = {book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
				book.getBid()};
		//3. 使用QueryRun的update方法更新数据
		qr.update(sql,params);
	}
	
	
	/**
	 * 删除图书
	 */
	public void delete(Book book) throws SQLException{
		//1. 以书籍名称，作者，定价，实际价格，折扣，出版社，出版时间，版次，页数，字数，印刷时间，纸张开数，纸质，所属分类id，书籍id为条件的sql语句
		String sql = "delete from t_book where bname=? and author=? and price=? and currPrice=? and " +
				"discount=? and press=? and publishtime=? and edition=? and pageNum=? and wordNum = ? and printtime=? and " +
				"booksize=? and paper=? and cid = ? and bid=?";
		//2. 编写Object数组，分别按顺序存储上面内容的值
		Object[] params = {book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
				book.getBid()};
		//3. 使用QueryRun的update方法更新数据
		qr.update(sql,params);
	}
}
