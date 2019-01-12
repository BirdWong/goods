package cn.jijiking51.goods.user.dao;

import cn.jijiking51.jdbc.TxQueryRunner;
import cn.jijiking51.goods.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;

/**
 * 用户模块持久层
 * @author h4795
 *
 */
public class UserDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 通过id和密码查询是否有该用户
	 * @param uid
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public boolean findByUidAndPassWord(String uid,String password) throws SQLException{
		//1. 编写sql数量统计语句，查询t_user表，以uid和loginpass为查询条件
		String sql = "select count(*) from t_user where uid=? and loginpass=?";

		/*
		 * 2.获取总记录数
		 *	2-1.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用ScalarHandler，第三个参数传入查询条件值，需要将list转为数组
		 *	2-2.返回回来的值使用Number对象接收，再使用Number方法中的intValue转为int
		 * 注：<code>ScalarHandler</code>用于获取结果集中第一行某列的数据并转换成 T 表示的实际对象。
		 *       该类对结果集的处理直接在 handle 方法中进行，不涉及 dbutils 库的其他类。
		 */
		Number number = (Number)qr.query(sql, new ScalarHandler(),uid,password);
		//3. 返回number是否大余0的判断结果
		return number.intValue()>0;
	}
	/**
	 * 更改用户信息
	 * @param uid
	 * @param password
	 * @throws SQLException
	 */
	public void updatePassword(String uid,String password) throws SQLException{
		//1. 编写t_user的更新语句，更新的属性为loginpass 更新条件为uid
		String sql = "update t_user set loginpass=? where uid=?";
		//2. 使用QueryRun的update方法更新数据
		qr.update(sql,password,uid); 
	}

	/**
	 * 按用户名和密码查询
	 * @param loginname
	 * @param loginpass
	 * @return
	 * @throws SQLException
	 */
	public User findByLoginnameAndLoginpass(String loginname, String loginpass) throws SQLException{

		/*
		 * 1.获取用户
		 * 	1-1. 以loginname，loginpass为条件，编写sql查询语句
		 * 	1-2.通过QueryRun的query查询User
		 * 		(sql, rsh, Object... params) 第二个参数rsh BeanHandler 需要设置泛型为User，并且传入初始化class
		 * 注：<code>BeanHandler</code>用于获取结果集中的第一行数据，并将其封装到JavaBean对象。
		 *		 整个转换过程最终会在 BeanProcessor 类中完成。
		 */
		String sql = "select * from t_user where loginname=? and loginpass=?";
		return qr.query(sql, new BeanHandler<User>(User.class),loginname,loginpass);
	}

	/**
	 * 通过激活码查询用户状态
	 * @param code
	 * @return
	 * @throws SQLException
	 */
	public User findByCode(String code) throws SQLException{
		/*
		 * 1.获取用户
		 * 	1-1. 以activationCode为条件，编写sql查询语句
		 * 	1-2.通过QueryRun的query查询User
		 * 		(sql, rsh, Object... params) 第二个参数rsh BeanHandler 需要设置泛型为User，并且传入初始化class
		 * 注：<code>BeanHandler</code>用于获取结果集中的第一行数据，并将其封装到JavaBean对象。
		 *		 整个转换过程最终会在 BeanProcessor 类中完成。
		 */
		String sql = "select * from t_user where activationCode=?";
		return qr.query(sql,new BeanHandler<User>(User.class),code);
		
	}

	/**
	 * 修改状态
	 * @param uid
	 * @param status
	 * @throws SQLException
	 */
	public void updateStatus(String uid , boolean status) throws SQLException{
		//1. 编写t_user的更新语句，更新的属性为status 更新条件为uid
		String sql = "update t_user set status=? where uid=?";
		//2. 使用QueryRun的update方法更新数据
		qr.update(sql,status,uid);
	}
	

	
	
	
	
	/**
	 * 校验用户名是否注册
	 * @param loginname
	 * @return
	 * @throws SQLException 
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException {
		//1. 编写sql数量统计语句，查询t_user表，以loginname为查询条件
		String sql = "select count(1) from t_user where loginname=?";
		/*
		 * 2.获取总记录数
		 *	2-1.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用ScalarHandler，第三个参数传入查询条件值，需要将list转为数组
		 *	2-2.返回回来的值使用Number对象接收，再使用Number方法中的intValue转为int
		 * 注：<code>ScalarHandler</code>用于获取结果集中第一行某列的数据并转换成 T 表示的实际对象。
		 *       该类对结果集的处理直接在 handle 方法中进行，不涉及 dbutils 库的其他类。
		 */
		Number number = (Number)qr.query(sql, new ScalarHandler(), loginname);
		//3. 返回number是否等于0的结果值
		return number.intValue() == 0;
	}
	
	/**
	 * 校验Email是否注册
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException {
		//1. 编写sql数量统计语句，查询t_user表，以email为查询条件
		String sql = "select count(1) from t_user where email=?";
		/*
		 * 2.获取总记录数
		 *	2-1.使用QueryRun的query方法查询总记录数
		 *		(sql, rsh, Object... params) rsh 使用ScalarHandler，第三个参数传入查询条件值，需要将list转为数组
		 *	2-2.返回回来的值使用Number对象接收，再使用Number方法中的intValue转为int
		 * 注：<code>ScalarHandler</code>用于获取结果集中第一行某列的数据并转换成 T 表示的实际对象。
		 *       该类对结果集的处理直接在 handle 方法中进行，不涉及 dbutils 库的其他类。
		 */
		Number number = (Number)qr.query(sql, new ScalarHandler(), email);
		//3. 返回number是否等于0的判断结果值
		return number.intValue() == 0;
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @throws SQLException 
	 */
	public void add(User user) throws SQLException {
		//1.编写sql语句，分别插入的值	用户id，用户登录名，用户密码，用户email，用户状态，用户激活码
		String sql = "insert into t_user values(?,?,?,?,?,?)";
		//2. 编写Object数组，分别按顺序存储上面内容的值
		Object[] params = {user.getUid(), user.getLoginname(), user.getLoginpass(),
				user.getEmail(), user.isStatus(), user.getActivationCode()};
		//3.使用QueryRun的update方法插入数据
		qr.update(sql, params);
	}


	/**
	 * 通过uid查询用户
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public User findByUid(String uid) throws SQLException {
		/*
		 * 1.获取用户
		 * 	1-1. 以uid为条件，编写sql查询语句
		 * 	1-2.通过QueryRun的query查询User
		 * 		(sql, rsh, Object... params) 第二个参数rsh BeanHandler 需要设置泛型为User，并且传入初始化class
		 * 注：<code>BeanHandler</code>用于获取结果集中的第一行数据，并将其封装到JavaBean对象。
		 *		 整个转换过程最终会在 BeanProcessor 类中完成。
		 */
		String sql = "select * from t_user where uid=?";
		User user = qr.query(sql, new BeanHandler<User>(User.class), uid);
		return user;
	}
	
}
