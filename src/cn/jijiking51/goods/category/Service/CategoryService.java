package cn.jijiking51.goods.category.Service;

import cn.jijiking51.goods.category.Dao.CategoryDao;
import cn.jijiking51.goods.category.domain.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * 分类的Service
 * @author h4795
 *
 */
public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	
	/**
	 * 通过add方法添加分类
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
	 * 通过findAll方法查找所有图书
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
	 * 寻通过findParent方法找父所有分类
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
	 * 通过load方法加载分类
	 */
	public Category load(String cid){
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 通过edit方法修改
	 */
	public void edit(Category category){
		try {
			categoryDao.edit(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 通过findChildrenCountByParent查找指定父类下的子类个数
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
	 * 通过delete删除指定cid的分类
	 * @param cid
	 */
	public void delete(String cid){
		try {
			categoryDao.delete(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过findChildren方法查询指定pid的子分类
	 * @param pid
	 * @return
	 */
	public List<Category> findChildren(String pid){
		try {
			return categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
