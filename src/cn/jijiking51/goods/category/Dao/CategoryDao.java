package cn.jijiking51.goods.category.Dao;


import cn.jijiking51.goods.category.domain.Category;
import cn.jijiking51.goods.commons.CommonUtils;
import cn.jijiking51.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分类的持久层
 * @author 91271
 *
 */
public class CategoryDao {

	
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 添加一级分类
	 * @throws SQLException 
	 */
	public void add(Category category) throws SQLException{
		//1.编写sql语句，分别插入的值	 分类id，分类名称，父类id，描述
		String sql = "insert into t_category (cid,cname,pid,`desc`) values(?,?,?,?)";
		/*
		 * 2. 因为一级分类没有parent  二级分类有
		 * 	2-1.假设为一级分类则pid为null
		 * 	2-2. 如果有Category一级分类id不为空
		 * 	2-3. 给pid设置一级分类的id
		 */
		String pid =null;
		if(category.getParent()!=null){
			pid = category.getParent().getCid();
		}
		//3. 从category中抽取与上面顺序对应的数据分装成一个数组
		Object[] params = {category.getCid(),category.getCname(),pid,category.getDesc()};
		//4. 使用QueryRun的update方法更新数据
		qr.update(sql,params);
	}


	/**
	 * 把map映射成Category
	 * map{ cid : xx,	cname:xx,	 pid : xx , desc:xx , orderBy:xxx}
	 * Category{cid:xx , cname:xxx,	parent:xx,	desc:xx}
	 * @param map
	 * @return
	 */
	private Category toCategory(Map<String , Object> map){

		//1.使用map调用CommonUtils#toBean方法将map中的值提取出来转化为Category
		Category category = CommonUtils.toBean(map, Category.class);
		String pid = (String)map.get("pid");
		//2. 如果父分类不为空
		if(pid != null){
			/*
			 * 使用一个父分类对象来装载pid
			 * 再把父分类设置给category
			 */
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
			
		}
		//3. 返回Category
		return category;
	}

	/**
	 * 把一个Map(List<Category>) 映射成多个Category(List<Category>)
	 * @param mapList
	 * @return
	 */
	private List<Category> toCategoryList(List<Map<String, Object >> mapList){
		//1. 创建List<CartItem>存储对象
		List<Category> categoryList = new ArrayList<Category>();
		//2. foreach循环遍历List<Map> 将map转化为CartItem并加入List<CartItem>对象中
		for(Map<String ,Object> map :mapList){
			Category c = toCategory(map);
			categoryList.add(c);
		}
		//3. 返回List<CartItem>
		return categoryList;
		
	}
	
	
	/**
	 * 返回所有分类
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findAll() throws SQLException{
		//1. 编写sql语句查询t_category表，查询pid为空的数据，并且将查询的结果按照t_cartitem中的orderBy属性升序排列
		String sql = "select * from t_category where pid is null order By orderBy";
		/*
		 * 2.获取所有一级分类
		 *
		 *	2-1.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用MapListHandler，第三个参数传入查询条件值，需要将list转为数组
		 *	2-2. 使用toCategoryList将MapList转为List<Category>
		 * 注：<code>MapListHandler</code>用于将结果集每行数据转换为Map（处理过程等同与MapHandler），
		 * 	再将Map添加到ArrayList中。简单点，就是将每行数据经过MapHandler处理后添加到ArrayList中。
		 */
		List<Map<String,Object>> mapList = qr.query(sql,	new MapListHandler());
		List<Category> parents  =   toCategoryList(mapList);
		//3. foreach遍历所有一级分类
		for(Category parent:parents){
			//使用parent.cid调用findByParent查询子类信息
			List<Category> children = findByParent(parent.getCid());
			//给parent设置子类信息
			parent.setChildren(children);
		}
		//4.返回parent的list
 		return parents;
		
	}

	/**
	 * 获取所有父分类
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findParent() throws SQLException{
		//1. 编写sql语句查询t_category表，查询pid为空的数据，并且将查询的结果按照t_cartitem中的orderBy属性升序排列
		String sql = "select * from t_category where pid is null order By orderBy";
		List<Map<String,Object>> mapList = qr.query(sql,new MapListHandler());
		/*
		 * 2.获取所有一级分类
		 *
		 *	2-1.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用MapListHandler，第三个参数传入查询条件值，需要将list转为数组
		 *	2-2. 使用toCategoryList将MapList转为List<Category>并返回
		 * 注：<code>MapListHandler</code>用于将结果集每行数据转换为Map（处理过程等同与MapHandler），
		 * 	再将Map添加到ArrayList中。简单点，就是将每行数据经过MapHandler处理后添加到ArrayList中。
		 */
		return toCategoryList(mapList);		
	}
	
	
	/**
	 * 通过父类解释子类
	 * @throws SQLException 
	 */
	public List<Category> findByParent(String pid) throws SQLException{
		//1.  编写sql语句查询t_category表，以pid为查询条件，并且将查询的结果按照t_cartitem中的orderBy属性升序排列
		String sql = "select * from t_category where pid=? order by orderBy";
		/*
		 * 2.获取所有一级分类
		 *
		 *	2-1.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用MapListHandler，第三个参数传入查询条件值，需要将list转为数组
		 *	2-2. 使用toCategoryList将MapList转为List<Category>并返回
		 * 注：<code>MapListHandler</code>用于将结果集每行数据转换为Map（处理过程等同与MapHandler），
		 * 	再将Map添加到ArrayList中。简单点，就是将每行数据经过MapHandler处理后添加到ArrayList中。
		 */
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),pid);
		return toCategoryList(mapList);
	}
	
	/**
	 * 加载分类
	 * 一级二级都可以加载
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public Category load(String cid) throws SQLException{
		//1. 编写sql语句查询t_category表，以cid为查询条件
		String sql = "select * from t_category where cid=?";
		/*
		 * 2.使用QueryRun的query方法查询相关对象，并调用toCategory返回
		 *		(sql, rsh, Object... params) rsh MapHandler，第三个参数传入查询条件值cid
		 * 注：<code>MapHandler</code> 用于获取结果集中的第一行数据，并将其封装到一个Map中，Map 中 key 是数据的列别名（as label）,
		 * 	如果没有就是列的实际名称，Map 中 value 就是列的值，注意代表列的 key 不区分大小写。
		 */
		return toCategory(qr.query(sql, new MapHandler(),cid));
	}
	
	/**
	 * 修改Caregory
	 * @param category
	 * @throws SQLException 
	 */
	public void edit(Category category) throws SQLException{
		//1. 编写sql更新语句，分别插入分类名称，父类id，描述，分类id
		String sql = "update t_category set cname=? , pid=?, `desc`=? where cid=?";
		/*
		 * 2. 因为一级分类没有parent  二级分类有
		 * 	2-1.假设为一级分类则pid为null
		 * 	2-2. 如果有Category一级分类id不为空
		 * 	2-3. 给pid设置一级分类的id
		 */
		String pid = null;
		if(category.getParent().getCid()!=null){
			pid = category.getParent().getCid();
		}
		//3. 从category中抽取与上面顺序对应的数据分装成一个数组
		Object[] params = {category.getCname(),pid,category.getDesc(),category.getCid()};
		//4. 调用QueryRun#update更新数据
		qr.update(sql,params);
	}
	
	
	/**
	 * 通过父类id查询有多少个子类
	 * 
	 * @param pid
	 * @return
	 * @throws SQLException
	 */
	public int findChildrenCountByParent(String pid) throws SQLException{
		//1. 编写sql数量查询语句从 t_category 表查询以pid为查询条件
		String sql = "select count(*) from t_category where pid=?";
		/*
		 * 2.获取总记录数
		 *	2-1.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用ScalarHandler，第三个参数传入查询条件值，需要将list转为数组
		 *	2-2.返回回来的值使用Number对象接收，再使用Number方法中的intValue转为int
		 * 注：<code>ScalarHandler</code>用于获取结果集中第一行某列的数据并转换成 T 表示的实际对象。
		 *       该类对结果集的处理直接在 handle 方法中进行，不涉及 dbutils 库的其他类。
		 */
		Number number = (Number)qr.query(sql, new ScalarHandler(),pid);
		//3. 返回number的值 使用三元运算符进行判断是否为null
		return number==null?0:number.intValue();
	}
	
	
	/**
	 * 删除分类
	 * @param cid
	 * @throws SQLException
	 */
	public void delete(String cid) throws SQLException{
		//1. 编写sql删除语句从 t_category 表 以cid为删除条件
		String sql = "delete from t_category where cid = ?";
		//2. 调用QueryRun#update更新数据
		qr.update(sql,cid);
	}
}
