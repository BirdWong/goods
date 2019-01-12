package cn.jijiking51.goods.user.web.servlet;

import cn.jijiking51.goods.commons.CommonUtils;
import cn.jijiking51.Servlet.BaseServlet;
import cn.jijiking51.goods.user.Exception.UserException;
import cn.jijiking51.goods.user.domain.User;
import cn.jijiking51.goods.user.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户模块WEB层
 * @author h4795
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
		//调用Session#invalidate销毁session进行退出
        	request.getSession().invalidate();
        	//重定向到 jsps/user/login.jsp
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
		//1.通过CommonUtils.toBean方法，把request#getParameter中属性映射到user中
		User fromUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		//2. 从session中获取user
		User user = (User)req.getSession().getAttribute("sessionUser");
		//3. 验证是否登录
		if(user == null){
			req.setAttribute("msg", "您没有登录");
			return "r:/jsps/user/login.jsp";
		}

		String vCode = (String) req.getSession().getAttribute("vCode");
		String inputCode = (String) req.getParameter("verifyCode");

		if(!vCode.equalsIgnoreCase(inputCode)){

			req.setAttribute("msg", "验证码错误");
			return "r:/jsps/user/pwd.jsp";
		}

		/*
		 * 4. 使用uid newpassword  loginpass 调用service#updatePassword修改密码
		 * 	修改成功则将成功信息转发到jsps/msg.jsp
		 * 	修改失败将捕捉的错误信息转发到jsps/user/pwd.jsp
		 */
		try {
			userService.updatePassword(user.getUid(),fromUser.getNewpass(), fromUser.getLoginpass());
			req.setAttribute("msg", "密码修改成功");
			req.setAttribute("code", "success");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			//保存信息到session
			req.setAttribute("msg", e.getMessage());
			//为了回显
			req.setAttribute("user", fromUser);
			return "f:/jsps/user/pwd.jsp";
		}
		
	}


	/**
	 * 登录方法
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.通过CommonUtils.toBean方法，把request#getParameter中属性映射到user中
		User fromUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		
		//2.调用validateLogin校验表单填写规范 如果有错误 将错误转发到jsps/user/login.jsp
		Map<String,String> errors = validateLogin(fromUser, req.getSession());
		if(errors.size() > 0) {

			req.setAttribute("user", fromUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/login.jsp";
		}


		//3.使用service#login查询，得到User
		User user = userService.login(fromUser);
		/*
		 * 4.查看用户是否存在，如果不存在：
		 * 		保存错误信息：用户名或密码错误
		 * 		保存用户数据：为了回显
		 * 		转发到login.jsp
		 */
		if(user==null){
			System.out.println(fromUser.getLoginname());
			System.out.println(fromUser.getLoginpass());
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
				 * 		保存当前用户名称到cookie，注意  中文编码需要调用 URLEncoder#encode处理
				 */
				/*
				 * 1.保存user到session中
				 * 2.获取用户名的cookie
				 * 3. 将用户名保存到loginname 调用setMaxAge设置最大时间30分钟
				 */
				req.getSession().setAttribute("sessionUser", user);
				String loginname = user.getLoginname();
				loginname = URLEncoder.encode(loginname,"utf-8");
				
				Cookie  cookie = new Cookie("loginname",loginname);
				cookie.setMaxAge(60*30);
				resp.addCookie(cookie);
				//重定向到主页
				return "r:/index";
			}
		}
		
		
	}


	/**
	 * 校验登录表单的数据是否符合规定
	 * @param formUser
	 * @param session
	 * @return
	 */
	private Map<String,String> validateLogin(User formUser, HttpSession session) {
		Map<String,String> errors = new HashMap<String,String>();
		//1. 校验登录名
		String loginname = formUser.getLoginname();
		if(loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		}
		
		
		// 2. 校验登录密码
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} 
	
		//3. 验证码校验 用户表单中填写的code和session中存储的code校验 1.是否为空，2 .忽略大小写验证是否填写正确
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		
		return errors;
	}


	/**
	 * 激活用户
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String activation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*
		 * 1. 获取激活码
		 * 2. 用激活码调用service#activation方法完成激活
		 * 	》可能抛出异常，提取异常信息到requst， 打印到msg.jsp
		 * 3.保存成功信息到requst，转发msg.jsp显示
		 */
		String code = req.getParameter("activationCode");
		
			try {
				userService.activation(code);
				//通知msg.jsp 显示正确
				req.setAttribute("code", "success");
				req.setAttribute("status",-1 );
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
		//1. 从request#getParameter获取用户名
		String loginname = req.getParameter("loginname");
		//2. 使用loginname调用service#ajaxValidateLoginname得到校验结果
		boolean b = userService.ajaxValidateLoginname(loginname);
		//3. 通过response#...#print发给客户端
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
		//1. 从request#getParameter获取Email
		String email = req.getParameter("email");
		//2. 使用loginname调用service#ajaxValidateEmail得到校验结果
		boolean b = userService.ajaxValidateEmail(email);
		//3. 通过response#...#print发给客户端
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
		//1. 从request#getParameter取输入框中的验证码
		String verifyCode = req.getParameter("verifyCode");
		//2. 获取图片上真实的校验码
		String vcode = (String) req.getSession().getAttribute("vCode");
		//3. 进行忽略大小写比较，得到结果
		boolean b = verifyCode.equalsIgnoreCase(vcode);
		//4. 通过response#...#print发送给客户端
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
		//1.通过CommonUtils.toBean方法，把request#getParameter中属性映射到user中
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		//获取邮箱服务器名
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
		//如果是qq邮箱，无法发送邮件，提示错误
		String qqEmail = "qq.com";
		if(email.equals(qqEmail)){
			errors.put("email", "不支持qq邮箱注册");
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/regist.jsp";
		}
		//3. 使用service#regist完成业务
		try{
			userService.regist(formUser);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		

		/*
		 *4 . 保存成功信息，转发到msg.jsp显示！
		 * 	4-1.通过邮件服务器调用getEmail获取用户登录的地址
		 */
		req.setAttribute("email", getEmail(email));
		req.setAttribute("emailName", "点击邮箱激活");
		req.setAttribute("code", "success");
		req.setAttribute("msg", "注册功能，请马上到邮箱激活！");
		return "f:/jsps/msg.jsp";
	}


	/**
	 * 获取邮箱链接
	 * @param email
	 * @return
	 */
	private String getEmail(String email){
		String wangyi163 = "163.com";
		String wangyi126 = "126.com";
		String googleEmail = "gmail.com";
		String sinaCom = "sina.com";
		String sinaCn = "sina.cn";
		String chinaMobile = "139.com";
		String souhu = "sohu.com";
		if(email.equals(wangyi163)){
			return "https://mail.163.com/";
		}else if(email.equals(wangyi126)){
			return "https://www.126.com/";
		}else if(email.equals(googleEmail)){
			return "https://www.google.com/intl/zh-CN/gmail/about/";
		}else if(email.equals(sinaCom)||email.equals(sinaCn)){
			return "https://mail.sina.com.cn/";
		}else if(email.equals(chinaMobile)){
			return "https://mail.10086.cn";
		}else if(email.equals(souhu)){
			return "https://mail.sohu.com";
		}else {
			return "https://www.hao123.com/mail";
		}
	}

	/**
	 * 注册校验
	 * 对表单的字段进行逐个校验，如果有错误，使用当前字段名称为key，错误信息为value，保存到map中
	 * 返回map
	 * @param formUser
	 * @param session
	 * @return
	 */
	private Map<String,String> validateRegist(User formUser, HttpSession session) {
		Map<String,String> errors = new HashMap<String,String>();
		//1. 校验登录名 是否为空，长度限制，调用ajaxValidateLoginname查看是否已经注册
		int minLength = 3;
		int maxLength = 20;
		String loginname = formUser.getLoginname();
		if(loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		} else if(loginname.length() < minLength || loginname.length() > maxLength) {
			errors.put("loginname", "用户名长度必须在3~20之间！");
		} else if(!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "用户名已被注册！");
		}
		
		//2. 校验登录密码 是否为空， 长度验证
		String loginpass = formUser.getLoginpass();

		if(loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if(loginpass.length() < minLength || loginpass.length() > maxLength) {
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}
		
		//3. 确认密码校验 是否为空  两次是否一致
		String reloginpass = formUser.getReloginpass();
		if(reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "确认密码不能为空！");
		} else if(!reloginpass.equals(loginpass)) {
			errors.put("reloginpass", "两次输入不一致！");
		}
		
		//4. 校验email 是否为空， 格式校验  是否注册
		String email = formUser.getEmail();
		if(email == null || email.trim().isEmpty()) {
			errors.put("email", "Email不能为空！");
		} else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "Email格式错误！");
		} else if(!userService.ajaxValidateEmail(email)) {
			errors.put("email", "Email已被注册！");
		}
		
		//5. 验证码校验 用户表单中填写的code和session中存储的code校验 1.是否为空，2 .忽略大小写验证是否填写正确
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		//返回错误
		return errors;
	}
	

}
