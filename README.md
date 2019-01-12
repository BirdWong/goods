README

------

zfbinfo.properties中：

1. 添加支付宝沙箱环境的pid和appid。
2. private_key 和 public_key请自行添加设置
3. 支付宝公钥已经设置为RSA2加密公钥，如果更改请按需更改

email_template.properties中：

1. from填写您的邮箱发送用户
2. host为邮箱的服务器smtp地址（已经设定为163，请酌情更改）
3. username为邮箱登录地址
4. password为邮箱的smtp密码，非登录密码

c3p0-config.xml中：

1. 数据库配置信息请按实际情况填写
2. 参数配置请按需更改

payment.properties中：

1. 默认信息为易贝测试账户
2. 回调地址请按需更改

cn.jijiking51.goods.admin.Order.commons.JdbcUtil中：

1. 数据库配置用于查询数据库信息，生成excel文件，由于偷懒，测试代码执行成功后就直接copy过来了，并没有集成到项目的数据库操作中，请手动更改：**driver，passname，password，url**信息



