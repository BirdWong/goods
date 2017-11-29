package cn.itcast.goods.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryImmediateEditor;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class CartItemDao {
	QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 加载多个CartItem
	 * @param cartItemIds
	 * @return
	 * @throws SQLException 
	 */
	public List<CartItem> loadCartItems(String cartItemIds) throws SQLException{
		/*
		 * 把cartItemIds转换成数组
		 */
		Object[] cartItemArray = cartItemIds.split(",");
		String whereSql = toWhereSql(cartItemArray.length);
		
		String sql = "select * from t_cartitem c, t_book  b where c.bid = b.bid and "+whereSql;
		/*
		 * 执行sql
		 */
		return toCartItemList(qr.query( sql , new MapListHandler(),cartItemArray));
	}
	
	/*
	 * 按照id查询
	 */
	public CartItem findByCartItemId(String cartItemId) throws SQLException{
		String sql = "select * from t_cartitem c , t_book b where c.bid = b.bid and c.cartItemId=?";
		Map<String ,Object> map = qr.query(sql, new MapHandler(),cartItemId);
		return toCartItem(map);
	}
	
	private  String toWhereSql(int len){
		StringBuilder whereSql = new StringBuilder("cartItemId in(");
		for(int i  = 0 ; i < len ; i++){
			whereSql.append("?");
			if(i < len - 1)
				whereSql.append(",");
		}
		whereSql.append(")");
		return whereSql.toString();
	}
	
	/*
	 * 删除购物车中一个条目
	 */
	public void batchDelect(String cartItemIds) throws SQLException{
		/*
		 * 需要吧cartItemids转化为数组
		 * 把cartItemIds转化为一个whereSQL子句
		 * 把delect from链接到一起
		 */
		Object[] cartItemArray = cartItemIds.split(",");
		String whereSql = toWhereSql(cartItemArray.length);
		String sql = "delete from t_cartItem where "+ whereSql;
		qr.update(sql,cartItemArray);
	}
	
	/*
	 * 查询某个用户的某本图书是否存在
	 */
	public CartItem findByUidAndBid(String uid ,String bid) throws SQLException{
		String sql = "select * from t_cartitem where uid=? and bid=?";
		Map<String, Object> map = qr.query(	sql , new MapHandler(),uid,bid);
		CartItem cartItem = toCartItem(map);
		return cartItem;
	}
	
	
	/*
	 * 修改指定条目数量
	 */
	public void updateQuantity(String cartItem, int quantity ) throws SQLException{
		String sql = "update t_cartitem set quantity=? where cartItemId=?";
		qr.update(sql,quantity,cartItem);
	}
	
	/*
	 * 添加条目
	 */
	
	public void addCartItem(CartItem cartItem) throws SQLException{
		String sql = "insert into t_cartitem(cartItemId,quantity ,bid,uid)" +
				" values(?,?,?,?)";
		Object[] params = {cartItem.getCartItemId(),cartItem.getQuantity(),
				cartItem.getBook().getBid(),cartItem.getUser().getUid()};
		
		
		
		qr.update(sql,params);
	}
	
	/*
	 * 把map映射成CartItem
	 */
	private CartItem toCartItem(Map<String ,Object> map){
		if(map == null||map.size() == 0){
			return null;
		}
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = CommonUtils.toBean(map, User.class);
		cartItem.setBook(book);
		cartItem.setUser(user);
		return cartItem;
	}
	
	
	private List<CartItem> toCartItemList(List<Map<String,Object>> mapList){
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		for(Map<String , Object> map :mapList){
			CartItem cartItem = toCartItem(map);
			cartItemList.add(cartItem);
		}
		return cartItemList;
	}
	
	/*
	 * 通过用户查询购物车条目
	 */
	public List<CartItem> findByUser(String uid) throws SQLException{
		String sql = "select * from t_cartitem c,t_book b where c.bid=b.bid and uid=? order by c.orderBy";
		List<Map<String, Object>> mapList =qr.query(sql, new MapListHandler(),uid);
		return toCartItemList(mapList);
	}
}
