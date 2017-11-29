package cn.itcast.goods.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.Exception.UserException;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.UserService;
import cn.itcast.servlet.BaseServlet;

/**
 * 用户模块WEB层
 * @author qdmmy6
 *
 */
public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();

	/**
	 * 退出方法
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String quit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getSession().invalidate();
        return "r:/jsps/user/login.jsp";
    }
	/**
	 * 修改密码
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updatePassword(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 1.封装表单信息
		 * 2.从session中获取uid
		 * 3.使用uid和表单  中的oldpass和newpass来调用service方法
		 * 	》如果出现异常，保存到异常信息到request中，转发到msg.jsp
		 * 4.保存成功信息到request中
		 * 5.转发到msg.jsp 中
		 */
		
		User fromUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		User user = (User)req.getSession().getAttribute("sessionUser");
		if(user == null){
			req.setAttribute("msg", "您没有登录");
			return "r:/jsps/user/login.jsp";
		}
		
		
		try {
			userService.updatePassword(user.getUid(),fromUser.getNewpass(), fromUser.getLoginpass());
			
			req.setAttribute("msg", "密码修改成功");
			req.setAttribute("code", "success");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			req.setAttribute("msg", e.getMessage());//保存信息到session
			req.setAttribute("user", fromUser);//为了回显
			return "f:/jsps/user/pwd.jsp";
		}
		
	}
	
	
	/*
	 * 登录方法
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.封装表单数据到fromUser
		 */
		User fromUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		
		/*
		 * 2.校验表单
		 */
		Map<String,String> errors = validateLogin(fromUser, req.getSession());
		
		/*
		 * 3.使用service查询，得到User
		 */
		User user = userService.login(fromUser);
		if(errors.size() > 0) {
			
			req.setAttribute("user", fromUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/login.jsp";
		}
		/*
		 * 4.查看用户是否存在，如果不存在：
		 * 		保存错误信息：用户名或密码错误
		 * 		保存用户数据：为了回显
		 * 		转发到login.jsp
		 */
		else if(user==null){
			req.setAttribute("msg", "用户名或者密码错误");
			req.setAttribute("user",fromUser);
			return "f:/jsps/user/login.jsp";
		}else {
			
			/*
			 * 5.如果用户存在查看状态，如果状态为false
			 * 		保存错误信息：您没有激活
			 * 		保存错误表单：为了回显
			 * 		转发到login.jsp
			 */
			if(!user.isStatus()){
				req.setAttribute("msg", "用户名未激活");
				req.setAttribute("user",fromUser);
				return "f:/jsps/user/login.jsp";
			}else {
				/*
				 * 6.登录成功
				 * 		保存当前查询到的user到session中
				 * 		保存当前用户名称到cookie，注意  中文编码需要处理
				 */
						/*
						 * 1.保存user到session中
						 * 2.获取用户名的cookie
						 */
				req.getSession().setAttribute("sessionUser", user);
				String loginname = user.getLoginname();
				loginname = URLEncoder.encode(loginname,"utf-8");
				
				Cookie  cookie = new Cookie("loginname",loginname);
				cookie.setMaxAge(60*30);//保存30分钟
				resp.addCookie(cookie);
				return "r:/index.jsp";//重定向到主页
			}
		}
		
		
	}
	
	
	
	private Map<String,String> validateLogin(User formUser, HttpSession session) {
		Map<String,String> errors = new HashMap<String,String>();
		/*
		 * 1. 校验登录名
		 */
		String loginname = formUser.getLoginname();
		
		if(loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		}
		
		
		/*
		 * 2. 校验登录密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} 
	
		/*
		 * 3. 验证码校验
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		
		return errors;
	}
	
	
	
	
	/*
	 * 激活用户
	 */
	public String activation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*
		 * 1. 获取激活码
		 * 2. 用激活码调用service方法完成激活
		 * 	》可能抛出异常，提取异常信息到requst， 打印到msg.jsp
		 * 3.保存成功信息到requst，转发msg.jsp显示
		 */
		
		String code = req.getParameter("activationCode");
		
			try {
				userService.activation(code);
				req.setAttribute("code", "success");//通知msg.jsp 显示正确
				req.setAttribute("msg", "恭喜，激活成功，请登录！");
			} catch (UserException e) {
				// TODO Auto-generated catch block
				req.setAttribute("code", "error");
				req.setAttribute("msg",e.getMessage());
			}
			return "f:/jsps/msg.jsp";
			
	}
	/**
	 * ajax用户名是否注册校验
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 获取用户名
		 */
		String loginname = req.getParameter("loginname");
		/*
		 * 2. 通过service得到校验结果
		 */
		boolean b = userService.ajaxValidateLoginname(loginname);
		/*
		 * 3. 发给客户端
		 */
		resp.getWriter().print(b);
		return null;
	}
	
	/**
	 * ajax Email是否注册校验
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 获取Email
		 */
		String email = req.getParameter("email");
		/*
		 * 2. 通过service得到校验结果
		 */
		boolean b = userService.ajaxValidateEmail(email);
		/*
		 * 3. 发给客户端
		 */
		resp.getWriter().print(b);
		return null;
	}
	
	/**
	 * ajax验证码是否正确校验
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 获取输入框中的验证码
		 */
		String verifyCode = req.getParameter("verifyCode");
		/*
		 * 2. 获取图片上真实的校验码
		 */
		String vcode = (String) req.getSession().getAttribute("vCode");
		/*
		 * 3. 进行忽略大小写比较，得到结果
		 */
		boolean b = verifyCode.equalsIgnoreCase(vcode);
		/*
		 * 4. 发送给客户端
		 */
		resp.getWriter().print(b);
		return null;
	}

	/**
	 * 注册功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 封装表单数据到User对象
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		String emailString = formUser.getEmail();
		String email = emailString.substring(emailString.indexOf("@")+1);
		
		/*
		 * 2. 校验之, 如果校验失败，保存错误信息，返回到regist.jsp显示
		 */
		Map<String,String> errors = validateRegist(formUser, req.getSession());
		if(errors.size() > 0) {
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/regist.jsp";
		}
		if(email.equals("qq.com")){
			errors.put("email", "不支持qq邮箱注册");
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/regist.jsp";
		}
		/*
		 * 3. 使用service完成业务
		 */
		try{
			userService.regist(formUser);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		/*
		 * 4.得到注册的邮箱
		 */
		/*
		 * 5. 保存成功信息，转发到msg.jsp显示！
		 */
		req.setAttribute("email", getEmail(email));
		req.setAttribute("emailName", "点击邮箱激活");
		req.setAttribute("code", "success");
		req.setAttribute("msg", "注册功能，请马上到邮箱激活！");
		return "f:/jsps/msg.jsp";
	}
	
	
	/*
	 * 获取邮箱链接
	 */
	private String getEmail(String email){
		if(email.equals("163.com")){
			return "http://mail.163.com/";
		}else if(email.equals("126.com")){
			return "http://www.126.com/";
		}else if(email.equals("gmail.com")){
			return "https://www.google.com/intl/zh-CN/gmail/about/";
		}else if(email.equals("sina.com")||email.equals("sina.cn")){
			return "http://mail.sina.com.cn/";
		}else if(email.equals("139.com")){
			return "http://mail.10086.cn";
		}else if(email.equals("sohu.com")){
			return "https://mail.sohu.com";
		}else {
			return "https://www.hao123.com/mail";
		}
	}
	/*
	 * 注册校验
	 * 对表单的字段进行逐个校验，如果有错误，使用当前字段名称为key，错误信息为value，保存到map中
	 * 返回map
	 */
	private Map<String,String> validateRegist(User formUser, HttpSession session) {
		Map<String,String> errors = new HashMap<String,String>();
		/*
		 * 1. 校验登录名
		 */
		String loginname = formUser.getLoginname();
		if(loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		} else if(loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在3~20之间！");
		} else if(!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "用户名已被注册！");
		}
		
		/*
		 * 2. 校验登录密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if(loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}
		
		/*
		 * 3. 确认密码校验
		 */
		String reloginpass = formUser.getReloginpass();
		if(reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "确认密码不能为空！");
		} else if(!reloginpass.equals(loginpass)) {
			errors.put("reloginpass", "两次输入不一致！");
		}
		
		/*
		 * 4. 校验email
		 */
		String email = formUser.getEmail();
		if(email == null || email.trim().isEmpty()) {
			errors.put("email", "Email不能为空！");
		} else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "Email格式错误！");
		} else if(!userService.ajaxValidateEmail(email)) {
			errors.put("email", "Email已被注册！");
		}
		
		/*
		 * 5. 验证码校验
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		
		return errors;
	}
	

}
