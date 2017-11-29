package cn.itcast.goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.Exception.UserException;
import cn.itcast.goods.user.dao.UserDao;
import cn.itcast.goods.user.domain.User;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

/**
 * 用户模块业务层
 * @author qdmmy6
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
	public void updatePassword(String uid,String newPass,String oldPass) throws UserException{
		try {
			/*
			 * 验证原始密码
			 */
			boolean bool = userDao.findByUidAndPassWord(uid, oldPass);
			if(!bool){
				throw new UserException("原密码错误");
			}
			/*
			 * 修改密码
			 */
			else{
				userDao.updatePassword(uid, newPass);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * 登录功能
	 */
	public User login(User user){
		try{
			return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 激活功能
	 */
	public void activation(String code) throws UserException{
		/*
		 * 1.通过激活码查询用户
		 * 2.如果User为null，说明是无效激活码，抛出异常，给出异常信息（无效激活码）
		 * 3.如果User存在，如果为true，抛出异常，给出异常信息（请勿二次激活）
		 * 4.修改用户信息
		 */
		try{
			User user = userDao.findByCode(code);
			if(user == null)throw new UserException("无效的激活码");
			if(user.isStatus())throw new UserException("请勿重复激活");
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
		 */
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());
		/*
		 * 2. 发邮件
		 */
		/*
		 * 把配置文件内容加载到prop中
		 */
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		/*
		 * 登录邮件服务器，得到session
		 */
		String host = prop.getProperty("host");//服务器主机名
		String name = prop.getProperty("username");//登录名
		String pass = prop.getProperty("password");//登录密码
		Session session = MailUtils.createSession(host, name, pass);
		
		/*
		 * 创建Mail对象
		 */
		String from = prop.getProperty("from");
		String to = user.getEmail();
		String subject = prop.getProperty("subject");
		// MessageForm.format方法会把第一个参数中的{0},使用第二个参数来替换。
		// 例如MessageFormat.format("你好{0}, 你{1}!", "张三", "去死吧"); 返回“你好张三，你去死吧！”
		String content = MessageFormat.format(prop.getProperty("content"), user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);
		/*
		 * 发送邮件
		 */
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		/*
		 * 3. 向数据库插入
		 */
		try {
			userDao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
}
