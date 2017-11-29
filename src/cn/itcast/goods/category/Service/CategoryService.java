package cn.itcast.goods.category.Service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.goods.category.Dao.CategoryDao;
import cn.itcast.goods.category.domain.Category;
/**
 * 业务层
 * @author 91271
 *
 */
public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	
	/**
	 * 添加分类
	 * @param category
	 */
	public void add(Category category){
		try {
			categoryDao.add(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
			
		}
		
	}
	/**
	 * 查找所有图书
	 * @return
	 */
	public List<Category> findAll(){
		
			try {
				return categoryDao.findAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		
	}
	
	/**
	 * 寻找父分类
	 * @return
	 */
	public List<Category> findParent(){
		try {
			return categoryDao.findParent();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 加载分类
	 */
	public Category load(String cid){
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 修改
	 */
	public void edit(Category category){
		try {
			categoryDao.edit(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 查找指定父类下的子类个数
	 * @param pid
	 * @return
	 */
	public int findChildrenCountByParent(String pid){
		try {
			return categoryDao.findChildrenCountByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 删除指定cid
	 * @param cid
	 */
	public void delete(String cid){
		try {
			categoryDao.delete(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Category> findChildren(String pid){
		try {
			return categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
