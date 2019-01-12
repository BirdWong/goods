package cn.jijiking51.goods.cart.service;

import cn.jijiking51.goods.cart.dao.CartItemDao;
import cn.jijiking51.goods.cart.domain.CartItem;
import cn.jijiking51.goods.commons.CommonUtils;

import java.sql.SQLException;
import java.util.List;


public class CartItemService {
	CartItemDao cartItemDao = new CartItemDao();
	
	
	/**
	 * 通过loadCartItems加载多个cartItem
	 */
	public List<CartItem> loadCartItems(String cartItemIds){
		try {
			return cartItemDao.loadCartItems(cartItemIds);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过updateQuantity修改购物车条目数目
	 */
	public CartItem updateQuantity(String cartItemId , int quantity){
		try{
			//1. 使用cartItemId和quantity调用dao#updateQuantity修改购物车条目数目
			cartItemDao.updateQuantity(cartItemId, quantity);
			//2. 使用cartItemId调用dao#findByCartItemId查找购物车条目数，并返回
			return cartItemDao.findByCartItemId(cartItemId);
		}catch(SQLException e ){
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 通过batchDelect删除购物出条目
	 */
	public void batchDelect(String cartItemIds){
		try {
			cartItemDao.batchDelect(cartItemIds);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	/**
	 * 通过add添加条目
	 */
	public void add(CartItem cartItem){
		
		
		try {
			/*
			 * 1.查询uid 和 bid 是否存在
			 * 	***1-1存在***
			 * 	1-1-1. 使用cartItem中uid和cartItem中bid调用dao#findByUidAndBid修改购物车条目数目
			 * 	1-1-2. 没有这个条目就添加条目
			 * 		1-1-2-1. 调用CommonUtils#uuid给cartItem添加id
			 * 		1-1-2-2. 使用cartItem调用dao#addCartItem
			 * 	***1-2***不存在
			 * 	1-2. 	将传进来的的购物条目数+查询出来的购物条目数=总条目数
			 * 	1-2. 使用查询出来的CartItemId和quantity调用dao#updateQuantity更新数据
			 */
			CartItem byCartIem = cartItemDao.findByUidAndBid(cartItem.getUser().getUid(),cartItem.getBook().getBid());
			if(byCartIem == null){
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			}else {
				int quantity = cartItem.getQuantity() + byCartIem.getQuantity();
				cartItemDao.updateQuantity(byCartIem.getCartItemId(),quantity);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 通过myCart查询我的购物车
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
