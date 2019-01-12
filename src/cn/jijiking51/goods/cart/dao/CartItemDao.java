package cn.jijiking51.goods.cart.dao;

import cn.jijiking51.goods.book.domain.Book;
import cn.jijiking51.goods.cart.domain.CartItem;
import cn.jijiking51.goods.commons.CommonUtils;
import cn.jijiking51.jdbc.TxQueryRunner;
import cn.jijiking51.goods.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartItemDao {
	QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 加载多个CartItem
	 * @param cartItemIds
	 * @return
	 * @throws SQLException 
	 */
	public List<CartItem> loadCartItems(String cartItemIds) throws SQLException{
		//1.把cartItemIds转换成数组，通过','分词
		Object[] cartItemArray = cartItemIds.split(",");
		//2. 获取替换位的where条件语句
		String whereSql = toWhereSql(cartItemArray.length);
		//3.以 bid为连接，查询t_cartitem 和 t_book表 并且 拼接where语句
		String sql = "select * from t_cartitem c, t_book  b where c.bid = b.bid and "+whereSql;
		/*
		 * 4.获取总购物车条目
		 *
		 *	4-1.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用MapListHandler，第三个参数传入查询条件值，需要将list转为数组
		 *	4-2. 使用toCartItemList将MapList转为List<CartItem>并返回
		 * 注：<code>MapListHandler</code>用于将结果集每行数据转换为Map（处理过程等同与MapHandler），
		 * 	再将Map添加到ArrayList中。简单点，就是将每行数据经过MapHandler处理后添加到ArrayList中。
		 */
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), cartItemArray);
		return toCartItemList(mapList);
	}

	/**
	 * 按照id查询
	 * @param cartItemId
	 * @return cn.jijiking51.goods.cart.domain.CartItem
	 * @throws SQLException
	 */
	public CartItem findByCartItemId(String cartItemId) throws SQLException{
		//1.以 bid为连接，查询t_cartitem 和 t_book表 并且以cartItemId为查询条件
		String sql = "select * from t_cartitem c , t_book b where c.bid = b.bid and c.cartItemId=?";
		/*
		 * 2.使用QueryRun的query方法查询相关对象
		 *		(sql, rsh, Object... params) rsh MapHandler，第三个参数传入查询条件值bid
		 * 注：<code>MapHandler</code> 用于获取结果集中的第一行数据，并将其封装到一个Map中，Map 中 key 是数据的列别名（as label）,
		 * 	如果没有就是列的实际名称，Map 中 value 就是列的值，注意代表列的 key 不区分大小写。
		 */
		Map<String ,Object> map = qr.query(sql, new MapHandler(),cartItemId);
		//3. 调用toCartItem将map转为CartItem 并返回
		return toCartItem(map);
	}


	/**
	 * 生成cartItemId的替换位的查询条件语句
	 * @param len
	 * @return java.lang.String
	 */
	private  String toWhereSql(int len){
		//拼接出的内容应该为	'cartItemId in(?,?,?)' ?个数取决于len(条件的个数)
		StringBuilder whereSql = new StringBuilder("cartItemId in(");
		for(int i  = 0 ; i < len ; i++){
			whereSql.append("?");
			if(i < len - 1) {
				whereSql.append(",");
			}
		}
		whereSql.append(")");
		String string = whereSql.toString();
		return string;
	}

	/**
	 * 通过ids删除购物车条目
	 * @param cartItemIds
	 * @throws SQLException
	 */
	public void batchDelect(String cartItemIds) throws SQLException{
		//1.把cartItemIds转换成数组，通过','分词
		Object[] cartItemArray = cartItemIds.split(",");
		//2. 获取替换位的where条件语句
		String whereSql = toWhereSql(cartItemArray.length);
		//3.删除t_cartitem 表中数据 并且 拼接where语句
		String sql = "delete from t_cartItem where "+ whereSql;
		//4. 调用QueryRun#update方法更新数据
		qr.update(sql,cartItemArray);
	}

	/**
	 * 查询某个用户的某本图书是否存在
	 * @param uid
	 * @param bid
	 * @return cn.jijiking51.goods.cart.domain.CartItem
	 * @throws SQLException
	 */
	public CartItem findByUidAndBid(String uid , String bid) throws SQLException{
		//1. 编写从 t_cartitem的关于uid和bid的查询语句
		String sql = "select * from t_cartitem where uid=? and bid=?";
		/*
		 * 2.使用QueryRun的query方法查询相关对象
		 *		(sql, rsh, Object... params) rsh MapHandler，第三个参数传入查询条件值uid和bid
		 * 注：<code>MapHandler</code> 用于获取结果集中的第一行数据，并将其封装到一个Map中，Map 中 key 是数据的列别名（as label）,
		 * 	如果没有就是列的实际名称，Map 中 value 就是列的值，注意代表列的 key 不区分大小写。
		 */
		Map<String, Object> map = qr.query(sql , new MapHandler(),uid,bid);
		//3.调用toCartItem将map转为CartItem 并返回
		CartItem cartItem = toCartItem(map);
		return cartItem;
	}


	/**
	 * 修改指定条目数量
	 * @param cartItem
	 * @param quantity
	 * @throws SQLException
	 */
	public void updateQuantity(String cartItem, int quantity ) throws SQLException{
		//1. 编写t_cartitem的更新语句，更新的属性为quantity 更新条件为cartItemId
		String sql = "update t_cartitem set quantity=? where cartItemId=?";
		//2. 使用QueryRun的update方法更新数据
		qr.update(sql,quantity,cartItem);
	}

	/**
	 * 添加条目
	 * @param cartItem
	 * @throws SQLException
	 */
	public void addCartItem(CartItem cartItem) throws SQLException{
		//1. 编写sql插入语句，分别插入cartItemId，quantity，bid，uid
		String sql = "insert into t_cartitem(cartItemId,quantity ,bid,uid)" +
				" values(?,?,?,?)";
		//2. 从cartItem中抽取与上面顺序对应的数据分装成一个数组
		Object[] params = {cartItem.getCartItemId(),cartItem.getQuantity(),
				cartItem.getBook().getBid(),cartItem.getUser().getUid()};
		//3. 使用QueryRun的update方法更新数据
		qr.update(sql,params);
	}

	/**
	 * 把map映射成CartItem
	 * @param map
	 * @return
	 */
	private CartItem toCartItem(Map<String ,Object> map){
		//1. 判断map是否为null 或者 内容为0
		if(map == null||map.size() == 0){
			return null;
		}
		//2. 使用map调用CommonUtils#toBean方法将map中的值提取出来转化为CartItem
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		//3. 使用map调用CommonUtils#toBean方法将map中的值提取出来转化为Book
		Book book = CommonUtils.toBean(map, Book.class);
		//4. 使用map调用CommonUtils#toBean方法将map中的值提取出来转化为User
		User user = CommonUtils.toBean(map, User.class);
		//5. 给CartItem设置book和user
		cartItem.setBook(book);
		cartItem.setUser(user);
		//6. 返回CartItem
		return cartItem;
	}


	/**
	 * 利用toCartItem将List<Map>中的Map转成CartItem，并封装成List<CartItem>
	 * @param mapList
	 * @return
	 */
	private List<CartItem> toCartItemList(List<Map<String,Object>> mapList){
		//1. 创建List<CartItem>存储对象
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		//2. foreach循环遍历List<Map> 将map转化为CartItem并加入List<CartItem>对象中
		for(Map<String , Object> map :mapList){
			CartItem cartItem = toCartItem(map);
			cartItemList.add(cartItem);
		}
		//3. 返回List<CartItem>
		return cartItemList;
	}

	/**
	 * 通过用户查询购物车条目
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> findByUser(String uid) throws SQLException{
		//1. 编写sql语句以bid为连接 查询t_cartitem和t_book表，以uid为查询条件，并且将查询的结果按照t_cartitem中的orderBy属性升序排列
		String sql = "select * from t_cartitem c,t_book b where c.bid=b.bid and uid=? order by c.orderBy";
		/*
		 * 2.获取总购物车条目
		 *
		 *	2-1.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用MapListHandler，第三个参数传入查询条件值，需要将list转为数组
		 *	2-2. 使用toCartItemList将MapList转为List<CartItem>并返回
		 * 注：<code>MapListHandler</code>用于将结果集每行数据转换为Map（处理过程等同与MapHandler），
		 * 	再将Map添加到ArrayList中。简单点，就是将每行数据经过MapHandler处理后添加到ArrayList中。
		 */
		List<Map<String, Object>> mapList =qr.query(sql, new MapListHandler(),uid);
		return toCartItemList(mapList);
	}
}
