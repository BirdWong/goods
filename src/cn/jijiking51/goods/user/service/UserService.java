package cn.jijiking51.goods.user.service;

import cn.jijiking51.goods.commons.CommonUtils;
import cn.jijiking51.mail.Mail;
import cn.jijiking51.mail.MailUtils;
import cn.jijiking51.goods.user.Exception.UserException;
import cn.jijiking51.goods.user.dao.UserDao;
import cn.jijiking51.goods.user.domain.User;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * 用户模块业务层
 * @author h4795
 *
 */
public class UserService {
	private UserDao userDao = new UserDao();
	/**
	 * 修改密码
	 * @param uid
	 * @param newPass
	 * @param oldPass
	 * @throws UserException
	 */
	public void updatePassword(String uid,String newPass,String oldPass) throws UserException {
		try {
			//调用dao#findByUidAndPassWord验证原始密码
			boolean bool = userDao.findByUidAndPassWord(uid, oldPass);
			if(!bool){
				throw new UserException("原密码错误");
			}
			//调用dao#updatePassword修改密码
			else{
				userDao.updatePassword(uid, newPass);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 登录功能
	 * @param user
	 * @return
	 */
	public User login(User user){
		try{
			//调用dao#findByLoginnameAndLoginpass 并返回
			return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 激活功能
	 * @param code
	 * @throws UserException
	 */
	public void activation(String code) throws UserException {
		/*
		 * 1.调用dao#findByCode通过激活码查询用户
		 * 2.如果User为null，说明是无效激活码，抛出异常，给出异常信息（无效激活码）
		 * 3.如果User存在，如果为true，抛出异常，给出异常信息（请勿二次激活）
		 * 4.调用dao#updateStatus修改用户信息
		 */
		try{
			User user = userDao.findByCode(code);
			if(user == null){
				throw new UserException("无效的激活码");
			}
			if(user.isStatus()){
				throw new UserException("请勿重复激活");

			}
			userDao.updateStatus(user.getUid(), true);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 用户名注册校验
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLoginname(String loginname) {
		try {
			//调用dao#ajaxValidateLoginname校验用户名是否被注册 并返回
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Email校验
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email) {
		try {
			//调用dao#ajaxValidateEmail校验邮箱是否被注册 并返回
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 注册功能
	 * @param user
	 */
	public void regist(User user) {
		/*
		 * 1. 数据的补齐
		 * 	1-1.调用CommonUtils#uuid生成用户id
		 * 	1-2.设置用户状态未激活
		 * 	1-3.设置激活码（调用两次CommonUtils#uuid，两串字符相加
		 */
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());
		/*
		 * 2. 发邮件
		 * 	1-1. 创建配置类
		 * 	1-2. 获取配置文件 this.getClass().getClassLoader()  类加载器的类 getResourceAsStream加载资源文件
		 */
		Properties prop = new Properties();
		try {

			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		/*
		 * 3. 登录邮件服务器，得到session
		 * 	3-1. 获取 host username password 等配置
		 * 	3-2. 调用	MailUtils#createSession获得Session
		 */
		//服务器主机名
		String host = prop.getProperty("host");
		//登录名
		String name = prop.getProperty("username");
		//登录密码
		String pass = prop.getProperty("password");
		Session session = MailUtils.createSession(host, name, pass);
		
		/*
		 * 4. 创建Mail对象
		 *	4-1.获取 发送方，接收方，标题的名称
		 *	4-2.补充文档内容调用MessageFormat#format
		 *	4-3.获得Mail对象
		 */
		String from = prop.getProperty("from");
		String to = user.getEmail();
		String subject = prop.getProperty("subject");
		// MessageForm.format方法会把第一个参数中的{0},使用第二个参数来替换。
		// 例如MessageFormat.format("你好{0}, 你{1}!", "张三", "去死吧"); 返回“你好张三，你去死吧！”
		String content = MessageFormat.format(prop.getProperty("content"), user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);
		//5. 使用session和mail调用MailUtils#send发送邮件
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		//6. 调用dao#add数据库插入
		try {
			userDao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}


	/**
	 * 通过用户uid调用dao#findByUid查询用户
	 * @param uid
	 * @return
	 */
	public User findByUid(String uid){
		try{
			return userDao.findByUid(uid);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
}
