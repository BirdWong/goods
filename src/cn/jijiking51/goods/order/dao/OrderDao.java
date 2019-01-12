package cn.jijiking51.goods.order.dao;

import cn.jijiking51.goods.book.domain.Book;
import cn.jijiking51.goods.commons.CommonUtils;
import cn.jijiking51.jdbc.TxQueryRunner;
import cn.jijiking51.goods.order.domain.Order;
import cn.jijiking51.goods.order.domain.OrderItem;
import cn.jijiking51.goods.pager.Expression;
import cn.jijiking51.goods.pager.PageBean;
import cn.jijiking51.goods.pager.PageConstants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();
	
	
	/**
	 * 查询订单状态
	 * @throws SQLException 
	 * 
	 */
	public int findStatus(String oid) throws SQLException{
		/*
		 * 1. 以oid为查询总数条件编写sql
		 * 2.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用ScalarHandler，第三个参数传入查询条件值，需要将list转为数组
		 * 3.返回回来的值使用Number对象接收，再使用Number方法中的intValue转为int
		 * 4. 返回number的值 使用三元运算符进行判断是否为null
		 * 注：<code>ScalarHandler</code>用于获取结果集中第一行某列的数据并转换成 T 表示的实际对象。
		 *       该类对结果集的处理直接在 handle 方法中进行，不涉及 dbutils 库的其他类。
		 */
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
		//1. 以oid为条件，编写sql语句修改：状态
		String sql = "update t_order set status=? where oid=?";
		//2. 使用QueryRun的update方法更新数据
		qr.update(sql,status,oid);
	}
	
	/**
	 * 加载方法
	 * @throws SQLException 
	 */
	public Order load(String oid) throws SQLException{
		/*
		 * 1.获取订单
		 * 	1-1. 以oid为条件，编写sql查询语句
		 * 	1-2.通过QueryRun的query查询Book集合
		 * 		(sql, rsh, Object... params) 第二个参数rsh BeanHandler 需要设置泛型为Order，并且传入初始化class
		 * 注：<code>BeanHandler</code>用于获取结果集中的第一行数据，并将其封装到JavaBean对象。
		 *		 整个转换过程最终会在 BeanProcessor 类中完成。
		 */
		String sql = "select * from t_order where oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
		//2. 使用order调用loadOrderItem加载订单详情
		loadOrderItem(order);
		//3. 返回order
		return order;
	}
	
	
	
	/**
	 * 生成订单
	 * @param order
	 * @throws SQLException 
	 */
	public void add(Order order) throws SQLException{
		//1. 编写sql语句，分别插入的值 订单id，订单时间，订单总价，订单状态，订单地址，订单用户uid
		String sql = "insert into t_order values(?,?,?,?,?,?)";
		//2. 编写Object数组，分别按顺序存储上面内容的值
		Object[] params = {order.getOid(),order.getOrdertime(),order.getTotal(),
				order.getStatus(),order.getAddress(),order.getUser().getUid()
		};
		//3. 使用QueryRun的update方法插入数据
		qr.update(sql,params);
		//4. 编写sql语句，分别插入的值 订单详情id， 订单详情总数，订单详情总价，订单详情书籍id，订单详情书籍名，订单详情书籍实际价格，订单详情书籍小图
		sql = "insert into t_orderitem values(?,?,?,?,?,?,?,?)";
		/*
		 * 5. 插入详细订单
		 * 	1. 获得详细订单总数
		 * 	2. 创建一个总数长度的二维数组
		 * 	3. 遍历循环将详情值加入到二维数组
		 * 	4. 调用QueryRun#batch 批量插入订单详情
		 *
		 */
		int len = order.getOrderItemList().size();
		Object[][] objs = new Object[len][];
		for(int i = 0; i < len ;i++){
			OrderItem orderItem = order.getOrderItemList().get(i);
			objs[i] = new Object[]{orderItem.getOrderItemId(),orderItem.getQuantity(),orderItem.getSubtotal(),orderItem.getBook().getBid(),
			    orderItem.getBook().getBname(), orderItem.getBook().getCurrPrice(),orderItem.getBook().getImage_b(),orderItem.getOrder().getOid()};
			
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
		/*
		 * 1.通过PageConstants的BOOK_PAGE_SIZE赋值给ps
		 * 每页记录数
		 */
		int ps = PageConstants.ORDER_PAGE_SIZE;//每页记录数

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
			if(!expr.getOperator().equals("is null")){
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
		String sql = "select count(*) from t_order " + wheresql ;
		Number number = (Number)qr.query(sql, new ScalarHandler() , params.toArray());
		int tr = number.intValue();


		/*
		 * 5.获取当前页记录
		 * 	5-1.拼接sql语句 完成select ， from ， order by和limit部分，order by 使用orderBy字段排序（order by默认为升序排序）
		 * 	5-2.添加条件 首行记录下标。计算为 当前页（pc） 减去 1 再乘以 每一页的记录数 算出来的为：之前页面记录数
		 * 	5-3. 添加条件 一共查询几行， 就是每一页的记录数
		 * 	5-4.通过QueryRun的query查询Book集合
		 * 		(sql, rsh, Object... params) 第二个参数rsh BeanListHandler 需要设置泛型为Order，并且传入初始化class，第三个参数传入查询条件值，需要将list转为数组
		 * 注：<code>BeanListHandler</code>用于将结果集的每一行数据转换为Javabean，再将这个Javabean添加到ArrayList中。
		 * 	可以简单的看着是BeanHandler的高级版，只不过是多了一步，就是将生成的Javabean添加到ArrayList中，其他的处理都和BeanHandler一样。
		 */
		sql = "select * from t_order" + wheresql + " order by ordertime desc limit ?,?";
		params.add((pc - 1 ) * ps);
		params.add(ps);
		List<Order> orderList = qr.query(sql , new BeanListHandler<Order>(Order.class),params.toArray());

		//6. 使用order调用loadOrderItem加载订单详情
		for(Order order : orderList){
			loadOrderItem(order);
		}
		//7. 创建PageBean ， 设置参数
		PageBean<Order> pb = new PageBean<Order>();
		/*
		 * 8. 给PageBean设置返回值集合，当前页，页面记录数，总数
		 *	其中PageBean没有url，这个任务由Servlet完成
		 */
		pb.setBeanList(orderList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		
		return pb;
	}

	/**
	 * 按用户查询订单
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByUser(String uid , int pc) throws SQLException{
		//1. 创建Expression的List
		List<Expression> exprList = new ArrayList<Expression>();
		//2. 给创建新对象，name='uid'，operator='like'，value=%uid%,并添加到list中
		exprList.add(new Expression("uid" ,"like",uid));
		/*
		 * 3.通过findByCriteria查询结果
		 * 并返回
		 */
		return findByCriteria(exprList, pc);
	}


	/**
	 * 为指定的order加载他的所有OrderItem
	 * @param order
	 * @throws SQLException
	 */
	private void loadOrderItem(Order order) throws SQLException {
		/*
		 * 1. 给sql语句select * from t_orderitem where oid=?
		 * 2. 执行之，得到List<OrderItem>
		 * 3. 设置给Order对象
		 */
		//1. 编写sql语句查询t_orderitem表，以oid为查询条件
		String sql = "select * from t_orderitem where oid=?";
		/*
		 * 2.获取总详情订单
		 *
		 *	2-1.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用MapListHandler，第三个参数传入查询条件值，需要将list转为数组或者单个值
		 *	2-2. 使用toCartItemList将MapList转为List<OrderItem>并返回
		 * 注：<code>MapListHandler</code>用于将结果集每行数据转换为Map（处理过程等同与MapHandler），
		 * 	再将Map添加到ArrayList中。简单点，就是将每行数据经过MapHandler处理后添加到ArrayList中。
		 */
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		//3. 给order设置订单详情列表
		order.setOrderItemList(orderItemList);
	}

	/**
	 * 把多个Map转换成多个OrderItem
	 * @param mapList
	 * @return
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		//1. 创建List<OrderItem>存储对象
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		//2. foreach循环遍历List<Map> 将map转化为OrderItem并加入List<OrderItem>对象中
		for(Map<String,Object> map : mapList) {
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);
		}
		//3. 返回List<OrderItem>
		return orderItemList;
	}


	/**
	 * 把一个Map转换成一个OrderItem
	 * @param map
	 * @return
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		//1. 使用map调用CommonUtils#toBean方法将map中的值提取出来转化为OrderItem
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		//2. 使用map调用CommonUtils#toBean方法将map中的值提取出来转化为Book
		Book book = CommonUtils.toBean(map, Book.class);
		//3. 给OrderItem设置book
		orderItem.setBook(book);
		//6. 返回OrderItem
		return orderItem;
	}
	
	/**
	 * 查询所有
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findAll(int pc) throws SQLException{
		//1. 创建Expression的List
		List<Expression> exprList = new ArrayList<Expression>();
		/*
		 * 2.通过findByCriteria查询结果
		 * 并返回
		 */
		return findByCriteria(exprList, pc);
	}


	/**
	 * 按状态查询
	 * @param status
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByStatus(int status , int pc) throws SQLException{
		//1. 创建Expression的List
		List<Expression> exprList = new ArrayList<Expression>();
		//2. 给创建新对象，name=status'，operator='like'，value=%status%,并添加到list中
		exprList.add(new Expression("status" ,"like",status+""));
		/*
		 * 3.通过findByCriteria查询结果
		 * 并返回
		 */
		return findByCriteria(exprList, pc);
	}


	/**
	 * 获取所有订单
	 * @return
	 */
	public List<Order> findAll() throws SQLException {
		//1. 编写查询所有信息的sql
		String sql = "select * from t_order";
		/*
		 * 2.获取总记录
		 * 	2-1.通过QueryRun的query查询Book集合
		 * 		(sql, rsh, Object... params) 第二个参数rsh BeanListHandler 需要设置泛型为Order，并且传入初始化class，第三个参数传入查询条件值，需要将list转为数组
		 * 注：<code>BeanListHandler</code>用于将结果集的每一行数据转换为Javabean，再将这个Javabean添加到ArrayList中。
		 * 	可以简单的看着是BeanHandler的高级版，只不过是多了一步，就是将生成的Javabean添加到ArrayList中，其他的处理都和BeanHandler一样。
		 */
		List<Order> orderList = qr.query(sql , new BeanListHandler<Order>(Order.class),new ArrayList().toArray());
		//3. 返回orderlist
		return orderList;
	}
	
	
}
