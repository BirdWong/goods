package cn.itcast.goods.admin.admin.service;

import java.sql.SQLException;

import cn.itcast.goods.admin.admin.Dao.AdminDao;
import cn.itcast.goods.admin.admin.domain.Admin;

public class AdminService {
	AdminDao adminDao = new AdminDao();
	
	/**
	 * 登录功能
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
