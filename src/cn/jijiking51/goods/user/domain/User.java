package cn.jijiking51.goods.user.domain;

/**
 * 用户模块实体类
 * @author h4795
 *
 */

public class User {
	//对应数据库表
	//主键
	private String uid;
	 //登录名
	private String loginname;
	 //登录密码
	private String loginpass;
	//邮箱
	private String email;
	 // 状态，true表示已激活，或者未激活
	private boolean status;
	 //激活码，它是唯一值！即每个用户的激活码是不同的！
	private String activationCode;
	
	// 注册表单
	 // 确认密码
	private String reloginpass;
	 //验证码
	private String verifyCode;
	
	// 修改密码表单
	 // 新密码
	private String newpass;

	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}


	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public String getNewpass() {
		return newpass;
	}
	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", loginname=" + loginname + ", loginpass="
				+ loginpass + ", email=" + email + ", status=" + status
				+ ", activationCode=" + activationCode + ", reloginpass="
				+ reloginpass + ", verifyCode=" + verifyCode + ", newpass="
				+ newpass + "]";
	}

}
