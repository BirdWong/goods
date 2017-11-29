package cn.itcast.goods.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.cart.dao.CartItemDao;
import cn.itcast.goods.cart.domain.CartItem;


public class CartItemService {
	CartItemDao cartItemDao = new CartItemDao();
	
	
	/*
	 * 加载多个cartItem
	 */
	public List<CartItem> loadCartItems(String cartItemIds){
		try {
			return cartItemDao.loadCartItems(cartItemIds);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 修改购物车条目数目
	 */
	public CartItem updateQuantity(String cartItemId , int quantity){
		try{
			
			cartItemDao.updateQuantity(cartItemId, quantity);
			return cartItemDao.findByCartItemId(cartItemId);
		}catch(SQLException e ){
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * 删除购物出条目
	 */
	public void batchDelect(String cartItemIds){
		try {
			cartItemDao.batchDelect(cartItemIds);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	/*
	 * 添加条目
	 */
	public void add(CartItem cartItem){
		
		//查询uid 和 bid 是否存在
		try {
			CartItem _cartIem = cartItemDao.findByUidAndBid(cartItem.getUser().getUid(),cartItem.getBook().getBid());
			if(_cartIem == null){//没有这个条目就添加条目
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			}else {//如果有这个条目  ，  就增加这个条目
				//用旧条目和新条目之和赋给旧条目
				int quantity = cartItem.getQuantity() + _cartIem.getQuantity();
				//数量之和赋给旧条目
				cartItemDao.updateQuantity(_cartIem.getCartItemId(),quantity);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}
	
	/*
	 * 我的购物车
	 */
	public List<CartItem> myCart(String uid){
		try {
			return cartItemDao.findByUser(uid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}
