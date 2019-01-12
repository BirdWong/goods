package cn.jijiking51.goods.admin.admin.Dao;

import cn.jijiking51.goods.admin.admin.domain.Admin;
import cn.jijiking51.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

public class AdminDao {
	QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 查找
	 * @param adminname
	 * @param adminpwd
	 * @return
	 * @throws SQLException
	 */
	public Admin find(String adminname , String adminpwd) throws SQLException{
		/*
		 * 1.获取管理员
		 * 	1-1. 以adminname和adminpwd为条件，编写sql查询语句
		 * 	1-2.通过QueryRun的query查询Admin
		 * 		(sql, rsh, Object... params) 第二个参数rsh BeanHandler 需要设置泛型为Admin，并且传入初始化class
		 * 注：<code>BeanHandler</code>用于获取结果集中的第一行数据，并将其封装到JavaBean对象。
		 *		 整个转换过程最终会在 BeanProcessor 类中完成。
		 */
		String sql = "select  * from t_admin where adminname=? and adminpwd=?";
		return qr.query(sql, new BeanHandler<Admin>(Admin.class),adminname,adminpwd);
		
	}
}
