package cn.jijiking51.goods.book.service;

import cn.jijiking51.goods.book.dao.BookDao;
import cn.jijiking51.goods.book.domain.Book;
import cn.jijiking51.goods.pager.PageBean;

import java.sql.SQLException;


/**
 *  @author h4795
 *  @description book的Service处理层
 */
public class BookService {
	private BookDao bookDao = new BookDao();


	/**
	 * 使用delete方法删除图书
	 * @param book
	 */
	public void delete(Book book){
		try {
			bookDao.delete(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用edit方法修改图书
	 * @param book
	 */
	public void edit(Book book){
		try {
			bookDao.edit(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * 通过findByBid进行查询
	 * @param bid
	 * @return
	 */
	public Book load(String bid){
		try {
			return bookDao.findByBid(bid);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	/**
	 * 通过findByCategory按照分类查
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCategory(String cid , int pc){
		try {
			return bookDao.findByCategory(cid, pc);
		} catch (SQLException e) {
			throw  new RuntimeException(e);
		}
	}
	
	/**
	 * 通过findByAuthor按照作者查
	 * @param author
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String author , int pc){
		try {
			return bookDao.findByAuthor(author, pc);
		} catch (SQLException e) {
			throw  new RuntimeException(e);
		}
	}
	
	
	/**
	 * 通过findByPress按照出版社查
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String press , int pc){
		try {
			return bookDao.findByPress(press, pc);
		} catch (SQLException e) {
			throw  new RuntimeException(e);
		}
	}
	
	
	/**
	 * 通过findByCombination按照组合查询
	 * @param book
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCombination(Book book , int pc){
		try {
			return bookDao.findByCombination(book, pc);
		} catch (SQLException e) {
			throw  new RuntimeException(e);
		}
	}
	/**
	 * 通过findByBname按照书名查询
	 * @param bname
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByBname(String bname , int pc){
		try {
			return bookDao.findByBname(bname, pc);
		} catch (SQLException e) {
			throw  new RuntimeException(e);
		}
	}
	
	
	/**
	 * 通过findBookCountByCategory查询该分类下的book数量
	 * @param cid
	 * @return
	 */
	public int findBookCountByCategory(String cid){
		try {
			return bookDao.findBookCountByCategory(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}


	public Boolean add(Book book) {
		// TODO Auto-generated method stub
		Boolean bool;
		try{
			bool = bookDao.add(book);
		}catch(SQLException e){
			return false;
			
		}
		return bool;
	}
}
