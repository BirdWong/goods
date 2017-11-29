package cn.itcast.goods.admin.admin.Dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.goods.admin.admin.domain.Admin;
import cn.itcast.jdbc.TxQueryRunner;

public class AdminDao {
	QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 查找
	 * @param adminname
	 * @param adminpwd
	 * @return
	 * @throws SQLException
	 */
	public Admin find(String adminname ,String adminpwd) throws SQLException{
		String sql = "select  * from t_admin where adminname=? and adminpwd=?";
		return qr.query(sql, new BeanHandler<Admin>(Admin.class),adminname,adminpwd);
		
	}
}
