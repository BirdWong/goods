package cn.itcast.goods.book.service;

import java.sql.SQLException;

import javax.management.RuntimeErrorException;

import com.sun.org.apache.regexp.internal.recompile;

import cn.itcast.goods.book.dao.BookDao;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.pager.PageBean;

public class BookService {
	private BookDao bookDao = new BookDao();
	
	
	/*
	 * 删除图书
	 */
	public void delete(Book book){
		try {
			bookDao.delete(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 修改图书
	 */
	public void edit(Book book){
		try {
			bookDao.edit(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * bid查询
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
	 * 按照分类查
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
	 * 按照作者查
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
	 * 按照出版社查
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
	 * 组合查询
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
	 * 书名查询
	 * @param book
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
	 * 查询该分类下的book数量
	 * 
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
		try{
			bookDao.add(book);
		}catch(SQLException e){
			return false;
			
		}
		return true;
	}
}
