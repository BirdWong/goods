package cn.jijiking51.goods.order.service;

import cn.jijiking51.goods.order.dao.OrderDao;
import cn.jijiking51.goods.order.domain.Order;
import cn.jijiking51.goods.pager.PageBean;
import cn.jijiking51.jdbc.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

public class OrderService {
	private OrderDao orderDao = new OrderDao();
	/**
	 * 调用findStatus查询订单状态
	 * 
	 * @param oid
	 * @return
	 */
	public int findStatus(String oid){
		try {
			return orderDao.findStatus(oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 调用updateStatus修改订单状态
	 */
	public void updateStatus(String oid,int status){
		try {
			orderDao.updateStatus(oid, status);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 调用load加载订单
	 * @param oid
	 * @return
	 */
	public Order load(String oid){
		/*
		 * 调用JdbcUtils#beginTransaction开启数据库事件
		 * 使用oid调用orderDao#load
		 * 调用JdbcUtils#commitTransaction提交数据库事件
		 *
		 * 出错
		 * 	JdbcUtils#rollbackTransaction回滚数据库
		 */
		try {
			JdbcUtils.beginTransaction();
			Order order =  orderDao.load(oid);
			JdbcUtils.commitTransaction();
			return order;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 调用add生成订单
	 * @param order
	 */
	public void createOrder(Order order){
		/*
		 * 调用JdbcUtils#beginTransaction开启数据库事件
		 * 使用order调用orderDao#add
		 * 调用JdbcUtils#commitTransaction提交数据库事件
		 *
		 * 出错
		 * 	JdbcUtils#rollbackTransaction回滚数据库
		 */
		try {
			JdbcUtils.beginTransaction();
			orderDao.add(order);
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
			throw new RuntimeException(e);
			
		}
	}
	
	
	/**
	 * 调用findByUser我的订单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Order> myOrders(String uid, int pc){
		/*
		 * 调用JdbcUtils#beginTransaction开启数据库事件
		 * 使用uid,pc调用orderDao#findByUser
		 * 调用JdbcUtils#commitTransaction提交数据库事件
		 *
		 * 出错
		 * 	JdbcUtils#rollbackTransaction回滚数据库
		 */
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByUser(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
			throw new RuntimeException(e);
			
		}
	}
	
	
	
	/**
	 * 调用findAll按照分页查询所有
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findAll(int pc){
		/*
		 * 调用JdbcUtils#beginTransaction开启数据库事件
		 * 使用pc调用orderDao#findAll
		 * 调用JdbcUtils#commitTransaction提交数据库事件
		 *
		 * 出错
		 * 	JdbcUtils#rollbackTransaction回滚数据库
		 */
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findAll( pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
			throw new RuntimeException(e);
			
		}
	}

	/**
	 * 查询所有
	 * @return
	 */
	public List<Order> findAll(){
		/*
		 * 调用JdbcUtils#beginTransaction开启数据库事件
		 * 调用orderDao#findAll
		 * 调用JdbcUtils#commitTransaction提交数据库事件
		 *
		 * 出错
		 * 	JdbcUtils#rollbackTransaction回滚数据库
		 */
		try {
			JdbcUtils.beginTransaction();
			List<Order> list = orderDao.findAll();
			JdbcUtils.commitTransaction();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
			throw new RuntimeException(e);

		}
	}
	
	
	
	
	
	/**
	 * 调用findByStatus按照状态查询
	 * @param status
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findByStatus(int status, int pc){
		/*
		 * 调用JdbcUtils#beginTransaction开启数据库事件
		 * 使用status，pc调用orderDao#findByStatus
		 * 调用JdbcUtils#commitTransaction提交数据库事件
		 *
		 * 出错
		 * 	JdbcUtils#rollbackTransaction回滚数据库
		 */
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByStatus(status, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
			throw new RuntimeException(e);
			
		}
	}
}
