package cn.itcast.goods.category.Dao;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.jdbc.TxQueryRunner;

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
		String sql = "insert into t_category (cid,cname,pid,`desc`) values(?,?,?,?)";
		/*
		 * 因为一级分类没有parent  二级分类有
		 * 
		 */
		String pid =null;//假设为一级分类
		if(category.getParent()!=null){
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCid(),category.getCname(),pid,category.getDesc()};
		qr.update(sql,params);
	}
	
	
	private Category toCategory(Map<String , Object> map){
		/*
		 * map{ cid : xx,	cname:xx,	 pid : xx , desc:xx , orderBy:xxx}
		 * Category{cid:xx , cname:xxx,	parent:xx,	desc:xx}
		 */
		Category category = CommonUtils.toBean(map, Category.class);
		String pid = (String)map.get("pid");
		if(pid != null){ // 如果父分类不为空
			/*
			 * 使用一个父分类对象来装载pid
			 * 再把父分类设置给category
			 */
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
			
		}
		return category;
	}
	
	/*
	 * 把一个Map(List<Category>) 映射成多个Category(List<Category>)
	 */
	private List<Category> toCategoryList(List<Map<String, Object >> mapList){
		List<Category> categoryList = new ArrayList<Category>();
		for(Map<String ,Object> map :mapList){
			Category c = toCategory(map);
			categoryList.add(c);
			
		}
		return categoryList;
		
	}
	
	
	/**
	 * 返回所有分类
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findAll() throws SQLException{
		String sql = "select * from t_category where pid is null order By orderBy";
		List<Map<String,Object>> mapList = qr.query(sql,	new MapListHandler());
		List<Category> parents  =   toCategoryList(mapList);
		
		for(Category parent:parents){
			List<Category> children = findByParent(parent.getCid());
			parent.setChildren(children);
		}
		
 		return parents;
		
	}
	
	/**
	 * 获取所有父分类
	 */
	public List<Category> findParent() throws SQLException{
		String sql = "select * from t_category where pid is null order By orderBy";
		List<Map<String,Object>> mapList = qr.query(sql,	new MapListHandler());
		return toCategoryList(mapList);		
	}
	
	
	/**
	 * 通过父类解释子类
	 * @throws SQLException 
	 */
	public List<Category> findByParent(String uid) throws SQLException{
		String sql = "select * from t_category where pid=? order by orderBy";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),uid);
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
		String sql = "select * from t_category where cid=?";
		return toCategory(qr.query(sql, new MapHandler(),cid));
	}
	
	/**
	 * 修改Caregory
	 * @param category
	 * @throws SQLException 
	 */
	public void edit(Category category) throws SQLException{
		String sql = "update t_category set cname=? , pid=?, `desc`=? where cid=?";
		/*
		 * 如果是一级分类， 没有pid
		 * 如果是一级分类 ， 返回null
		 */
		
		String pid = null;
		if(category.getParent().getCid()!=null)
		pid = category.getParent().getCid();
		
		Object[] params = {category.getCname(),pid,category.getDesc(),category.getCid()};
		
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
		String sql = "select count(*) from t_category where pid=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(),pid);
		return number==null?0:number.intValue();
	}
	
	
	/**
	 * 删除分类
	 * @param cid
	 * @throws SQLException
	 */
	public void delete(String cid) throws SQLException{
		String sql = "delete from t_category where cid = ?";
		qr.update(sql,cid);
	}
}
