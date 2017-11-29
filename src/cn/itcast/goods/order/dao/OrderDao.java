package cn.itcast.goods.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.sun.org.apache.xml.internal.utils.IntVector;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.domain.OrderItem;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();
	
	
	/**
	 * 查询订单状态
	 * @throws SQLException 
	 * 
	 */
	public int findStatus(String oid) throws SQLException{
		String sql = "select status from t_order where oid=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(),oid);
		return number.intValue();
	}
	/**
	 * 修改订单状态
	 * @param oid
	 * @param status
	 * @throws SQLException
	 */
	public void updateStatus(String oid,int status) throws SQLException{
		String sql = "update t_order set status=? where oid=?";
		qr.update(sql,status,oid);
	}
	
	/**
	 * 加载方法
	 * @throws SQLException 
	 */
	public Order load(String oid) throws SQLException{
		String sql = "select * from t_order where oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
		loadOrderItem(order);
		return order;
	}
	
	
	
	/**
	 * 生成订单
	 * @param order
	 * @throws SQLException 
	 */
	public void add(Order order) throws SQLException{
		/*
		 * 1插入订单
		 */
		String sql = "insert into t_order values(?,?,?,?,?,?)";
		Object[] params = {order.getOid(),order.getOrdertime(),order.getTotal(),
				order.getStatus(),order.getAddress(),order.getUser().getUid()
		};
		qr.update(sql,params);
		
		/*
		 * 2.循环遍历所有订单条目
		 * 一个条目一个object[]
		 * 由多个一维数组  变为 二维数组
		 */
		
		sql = "insert into t_orderitem values(?,?,?,?,?,?,?,?)";
		int len = order.getOrderItemList().size();
		Object[][] objs = new Object[len][];
		for(int i = 0; i < len ;i++){
			OrderItem orderItem = order.getOrderItemList().get(i);
			objs[i] = new Object[]{orderItem.getOrderItemId(),orderItem.getQuantity(),orderItem.getSubtotal(),orderItem.getBook().getBid(),orderItem.getBook().getBname(),
					orderItem.getBook().getCurrPrice(),orderItem.getBook().getImage_b(),orderItem.getOrder().getOid()};
			
		}
		qr.batch(sql, objs);
		
		
	}
	
	/**
	 * 查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByCriteria(List<Expression> exprList , int pc) throws SQLException{
		/*
		 * 1.得到ps
		 * 2.得到tr
		 * 3.得到beanlist
		 * 4.创建PageBean， 返回
		 */
		
		int ps = PageConstants.ORDER_PAGE_SIZE;//每页记录数
		
		
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
		String sql = "select count(*) from t_order " + wheresql ;
		Number number = (Number)qr.query(sql, new ScalarHandler() , params.toArray());
		int tr = number.intValue();

		
		/*
		 * 当前页记录
		 */
		sql = "select * from t_order" + wheresql + " order by ordertime desc limit ?,?";
		params.add((pc - 1 ) * ps);//首行记录下标
		params.add(ps);// 一共查询几行， 就是每一页的记录数
		List<Order> orderList = qr.query(sql , new BeanListHandler<Order>(Order.class),params.toArray());
		
		
		for(Order order : orderList){
			loadOrderItem(order);
		}
		/*
		 * 创建PageBean ， 设置参数
		 */
		
		PageBean<Order> pb = new PageBean<Order>();
		//其中PageBean没有url，这个任务由Servlet完成
		
		pb.setBeanList(orderList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		
		return pb;
	}
	/*
	 * 按用户
	 */
	public PageBean<Order> findByUser(String uid , int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("uid" ,"like",uid));
		return findByCriteria(exprList, pc);
	}
	
	/*
	 * 为指定的order加载他的所有OrderItem
	 */
	private void loadOrderItem(Order order) throws SQLException {
		/*
		 * 1. 给sql语句select * from t_orderitem where oid=?
		 * 2. 执行之，得到List<OrderItem>
		 * 3. 设置给Order对象
		 */
		String sql = "select * from t_orderitem where oid=?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		
		order.setOrderItemList(orderItemList);
	}

	/**
	 * 把多个Map转换成多个OrderItem
	 * @param mapList
	 * @return
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map<String,Object> map : mapList) {
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}

	/*
	 * 把一个Map转换成一个OrderItem
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}
	
	/**
	 * 查询所有
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findAll(int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		return findByCriteria(exprList, pc);
	}
	
	
	/*
	 * 按状态查询
	 */
	public PageBean<Order> findByStatus(int status , int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("status" ,"like",status+""));
		return findByCriteria(exprList, pc);
	}
	
	
}