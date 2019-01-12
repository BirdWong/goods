package cn.jijiking51.goods.admin.admin.service;

import cn.jijiking51.goods.admin.admin.Dao.AdminDao;
import cn.jijiking51.goods.admin.admin.domain.Admin;

import java.sql.SQLException;

public class AdminService {
	AdminDao adminDao = new AdminDao();
	
	/**
	 * 调用dao#find登录功能
	 * @param admin
	 * @return
	 */
	public Admin login(Admin admin){
		try {
			return adminDao.find(admin.getAdminname(), admin.getAdminpwd());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}
}
