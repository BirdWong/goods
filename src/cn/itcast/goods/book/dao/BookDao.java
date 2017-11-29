package cn.itcast.goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	
	
	/**
	 * 按照bid查询
	 * @throws SQLException 
	 * 
	 */
	public Book findByBid(String bid) throws SQLException{
		String sql = "select * from t_book b,t_category c where c.cid=b.cid and bid=?";
		//一行记录中， 包含很多book属性， 还有一个cid属性
		Map<String,Object> map = qr.query(sql, new MapHandler(),bid);
		//把map中除cid属性映射到book中
		Book book = CommonUtils.toBean(map, Book.class);
		//把map中cid属性映射到category
		Category category = CommonUtils.toBean(map, Category.class);
		//将book和category建立联系
		book.setCategory(category);
		if(map.get("pid")!=null){
			Category parent = new Category();
			parent.setCid((String)map.get("pid"));
			category.setParent(parent);
		}
		
		
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
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("cid" ,"=",cid));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按照名字查询
	 * @param cid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByBname(String bname , int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname" ,"like","%"+bname+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按照作者查
	 * @param cid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByAuthor(String author , int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("author" ,"like","%"+author+"%"));
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
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("press" ,"like","%"+press + "%"));
		return findByCriteria(exprList, pc);
	}
	
	
	
	/**
	 * 多条件组合查询
	 */
	public PageBean<Book> findByCombination(Book book , int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("press" ,"like","%"+book.getPress() + "%"));
		exprList.add(new Expression("bname" ,"like","%"+book.getBname() + "%"));
		exprList.add(new Expression("author" ,"like","%"+book.getAuthor() + "%"));
		
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
		
		int ps = PageConstants.BOOK_PAGE_SIZE;//每页记录数
		
		
		/*
		 * 通过 Expression 生成where子句
		 */
		
		StringBuilder wheresql = new StringBuilder(" where 1=1");//添加一个条件 以 and 开头 ， select count（*） t_book where 1=1 and xxx = ？           1=1  不影响查找， 
		List<Object> params = new ArrayList<Object>(); // sql 中有问号，  对应问号的值
		/*
		 * 1.以and 开头
		 * 2.条件的名称
		 * 3.条件的运算符可以是  = , != ,>, < .......   is null没有值
		 */
		for(Expression expr : exprList){
			wheresql.append(" and ").append(expr.getName()).append(" ").append(expr.getOperator()).append(" ");
			if(!expr.getOperator().equals("is null")){
				wheresql.append("?");
				params.add(expr.getValue());
			}
		}
		
		/*
		 * 总记录数
		 */
		String sql = "select count(*) from t_book " + wheresql ;
		Number number = (Number)qr.query(sql, new ScalarHandler() , params.toArray());
		int tr = number.intValue();

		
		/*
		 * 当前页记录
		 */
		sql = "select * from t_book" + wheresql + " order by orderBy limit ?,?";
		params.add((pc - 1 ) * ps);//首行记录下标
		params.add(ps);// 一共查询几行， 就是每一页的记录数
		List<Book> bookList = qr.query(sql , new BeanListHandler<Book>(Book.class),params.toArray());
		
		/*
		 * 创建PageBean ， 设置参数
		 */
		
		PageBean<Book> pb = new PageBean<Book>();
		//其中PageBean没有url，这个任务由Servlet完成
		
		pb.setBeanList(bookList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		
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
		if(book.getPrice()>1000000||book.getCurrPrice()>1000000||book.getDiscount()>99){
			return false;
		}
		String sql = "insert into t_book(bid,bname,author,price,currPrice," +
				"discount,press,publishtime,edition,pageNum,wordNum,printtime," +
				"booksize,paper,cid,image_w,image_b)" +
				" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {book.getBid(),book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
				book.getImage_w(),book.getImage_b()};
		qr.update(sql, params);
		return true;
	}
	
	/*
	 * 修改图书
	 */
	public void edit(Book book) throws SQLException{
		String sql = "update t_book set bname=?, author=?, price=?, currPrice=?, " +
				"discount=? , press=? , publishtime=? , edition=? , pageNum=? , wordNum = ? , printtime=? , " +
				"booksize=? , paper=? , cid = ? where bid=?";
		Object[] params = {book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
				book.getBid()};
		
		qr.update(sql,params);
	}
	
	
	/*
	 * 删除图书
	 */
	public void delete(Book book) throws SQLException{
		String sql = "delete from t_book where bname=? and author=? and price=? and currPrice=? and " +
				"discount=? and press=? and publishtime=? and edition=? and pageNum=? and wordNum = ? and printtime=? and " +
				"booksize=? and paper=? and cid = ? and bid=?";
		Object[] params = {book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
				book.getBid()};
		qr.update(sql,params);
	}
}
