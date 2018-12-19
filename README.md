# J360-SHIRO Demos

# 概述
shiro在app客户端、html web应用场景的使用

# Module
 - shiro-appclient
 - shiro-appclient-webapp
 - shiro-webclient


# 介绍

> The minimum requirements to run the quick start are: 
>  * JDK 1.8 or above
>  * A java-based project management software like [Maven][maven] or [Gradle][gradle]

# 集成

API验证接口集成swagger文档服务,参考: https://github.com/xuminwlt/j360-swagger

## 目的
服务端接口在给客户端提供接口服务时，原则上服务器不信任任何外部调用接口的客户端，需要对外部接口调用者进行身份识别和信任，在接口信任级别上，需要区分非用户级别的白名单接口和需要用户身份识别的会话接口，这里将两者区分，但是不完全隔离，原则上用户身份识别的Token认证基于并高于白名单认证。
设计说明
加密规则：HMacSHA256，ios和java使用以下信息进行验证生成的值是否正确
-> HmacSHA256Utils.digest("key","param");

->  ddcec856e0329ea3f99754b7086f444d0d61bf690fdd19b21cf68d5afa6cf7dd

### 白名单：
客户端身份标示：appId appSecret
客户端请求头代理接口认证：clientAgent：
appId/version/buildVersion/osNumber/osVersion/deviceModel/deviceUUID
客户端通用请求头：时间戳 timestamp
客户端加密凭证：token=hmacSHA256(appSecret,appId+appUUID+timestamp)


用户身份：
用户id：uid
用户加密凭证：token=hmacSHA256(userSecret,appId+appUUID+timestamp+uid)


技术实现
白名单凭证：

   ```

定义Map -> appId appUUID timestamp参与token计算
map.put("appId",appId1);
map.put("appUUID",deviceUUID);
map.put("timestamp",timestamp);
String token = HmacSHA256Utils.digest(appSecret1, map);
   ```


用户凭证：

   ```

定义Map -> appid appUUID timestamp uid 参与token计算
Map<String,String> map2 = Maps.newHashMap();
map2.put("appId",appId1);
map2.put("appUUID",deviceUUID);
map2.put("timestamp",timestamp);
map2.put("uid",uid);
String token2 = HmacSHA256Utils.digest(secret,map2);
   ```

appUUID每个终端会在服务器记录本次访问的时间戳 timestamp，同时同一个终端的时间戳每次访问时间不能小于上次时间，否则会返回失败
【注】map中的key 需要按照字典进行排序
接口说明
客户端接口设计：
客户端在第一次App打开需要生成一份数据ClientAgent

   ```

Client-Agent: Fotoplace/3.1.0/1602/iOS/7.1/iPhone 5s (A1457/A1518/A1528/A1530)/7EAB70B1-624F-463A-943C-E7FF235A9A0C
//在传递参数的时候setOsNumber安卓=1，苹果=iOS
ClientAgent clientAgent = new ClientAgent();
clientAgent.setAppName(userAgent[0]);
clientAgent.setVersion(userAgent[1]);
clientAgent.setBuildVersion(Integer.parseInt(userAgent[2]));
clientAgent.setOsNumber(userAgent[3]);
clientAgent.setOsVersion(userAgent[4]);
clientAgent.setDeviceModel(userAgent[5]);
clientAgent.setDeviceUUID(userAgent[6]);
【注】
//因为iPhone里面有括号斜线，导致获取失败，用正则去掉（）及里面的内容
clientAgentString = clientAgentString.replaceAll(" \\(.*?\\)", "");
   ```


客户端保存分配的AppId和AppSecret：例如

   ```

private static final String appId1 = "b35d7751bc8e123487892a6abcb1234";
private static final String appSecret1 = "b35d7751bc8ef46b87892a6abcb8f86ede884481ef2e94aa49a7b48fffd4c13c";
   ```

## 通用头:客户端请求头需要加入以下内容：
1. 头名称
说明
timestamp	时间戳，带上毫秒数
Client-Agent
appName/version/buildVersion/osNumber/osVersion/deviceModel/deviceUUID


2. 白名单：
见【通用头部】
用户认证：

头名称
说明
uid	uid
【注】登录时用户需要拿到对应的userSecret，保存到本地，并且用于token的生成

## 测试案例
 * 假设数值
 
 ```
private static final String appId1 = "b35d7751bc8e123487892a6abcb1234";
private static final String appSecret1 = "b35d7751bc8ef46b878921234cb8f86ede884481ef2e94aa49a7b48fffd4c13c";

String uid = "20000000";
String secret = "b35d7751bc8e123487892a6abcb1234";
   ```

* 用户接口：比如
http://localhost:8080/api3/home/ks3/auth?http_verb=PUT&content_md5=&conent_type&file_name=&date=
头名称
值
说明
Client-Agent	b35d7751bc8ef46b87892a6abcb8f8/3.0.4/2/1/9.0.2/6S/a6cf3aaf715844ee9977b0a1e2460c9a
token	3d413010dd6b46fc8b4e7859a6a8f9c8829f8be69406089a7c12c6910d5f54e1
timestamp	1463370349560
uid	20000000

* 白名单接口
http://localhost:8080/api3/home/ks3/auth?http_verb=PUT&content_md5=&conent_type&file_name=&date=
头名称
值
说明
Client-Agent	b35d7751bc8e123487892a6abcb1234/3.0.4/2/1/9.0.2/6S/a6cf3aaf715844ee9977b0a1e2460c9a
token
2cb083955ffc2f97cf49854343c56b439fbd633b4cb8ca9ea28667a97652b016

timestamp	1463370349560

* 白名单列表
 - 登录
 - 注册
 - 发送验证码
 - 忘记密码

## 用户密码生成策略

```
//数据库已经保存的加密码
String enpassword = "b35d7751bc8ef46b87892a6abcb8f86e12345481ef2e94aa49a7b48fffd4c13c";
//加密算法
String algorithmName = "SHA-256";
String publicSalt = "fotoplace";
//数据库存储的私盐
String privateSalt = "1ab0889665c8ecf592125f2f3b22d567";
int hashIterations = 2;

SimpleHash hash = new SimpleHash(algorithmName, password, uid + publicSalt + privateSalt, hashIterations);
String encodedPassword = hash.toHex();
//验证密码
return encodedPassword.equals(encodedPassword);

2.用户私盐和用户的secret生成规则，产生随机的32位值
String privateSalt = new SecureRandomNumberGenerator().nextBytes().toHex();

String secret = new SecureRandomNumberGenerator().nextBytes().toHex();
```
数据库

```
CREATE TABLE `user_secret` (
  `id` bigint(20) NOT NULL,
  `uid` bigint(20) NOT NULL,
  `private_salt` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '私盐',
  `password` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'uid 公盐+私盐 password = password',
  `secret` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户密钥 用户生成动态生成token加密',
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```

## webapp-demo
* j360-shiro-appclient-webapp shiro的用法，自带一个简单的demo
   - web.xml 添加shiro拦截器
   - 引入shiro的spring配置文件
   - 新增一个springmvc的controller
   - 新增几个用于测试的requestMapping
   - 分别控制每个url的权限 - 修改servlet-shiro.xml文件

* 请自行配置相关redis和jdbc datasource配置
   - root-context.xml 设置jdbc redis等客户端
* 用法：本地直接jetty:run
   - jetty:run在pom中配置了local的profile会覆盖resources中的文件，只做本地测试需要


## 其他说明
* 关于同一个servlet容器如何使用session和nosession的配置中，需要开启servlet-shiro.xml

```
<!--使用原生的HttpSession，所有的分享的内容,sessionManager暂不使用-->
    <bean id="servletContainerSessionManager" class="org.apache.shiro.web.session.mgt.ServletContainerSessionManager"/>
```

 - 并且注释掉下面的false，同时根据具体的filter标示session的使用
 
```
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {
    public Subject createSubject(SubjectContext context) {
        //不创建session
        //context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
```

 - Filter中的preHandler中做出以下调整，request.setAttribute
 
 ```
 /**
      * Returns <code>true</code> if
      * {@link #isAccessAllowed(ServletRequest, ServletResponse,Object) isAccessAllowed(Request,Response,Object)},
      * otherwise returns the result of
      * {@link #onAccessDenied(ServletRequest, ServletResponse,Object) onAccessDenied(Request,Response,Object)}.
      *
      * @return <code>true</code> if
      *         {@link #isAccessAllowed(ServletRequest, ServletResponse, Object) isAccessAllowed},
      *         otherwise returns the result of
      *         {@link #onAccessDenied(ServletRequest, ServletResponse) onAccessDenied}.
      * @throws Exception if an error occurs.
      */
     public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
         request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, Boolean.FALSE);
         return isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response, mappedValue);
     }
 ```


## Web浏览器客户端情况下

 * 简单使用模式
  
    - 用户输入网址登录
    - 登录后跳转网页1
    - 用户退出
    - 用户再访问网页1强制跳转到登录界面

 * 复杂使用模式
  
  - 常规登录
  - 二维码登录
  - Oauth2.0授权登录
  - 多账号体系登录
  - 鉴权
  - 自带Oauth2.0API
  - CAS SSO

## SpringBoot项目

require: spring-boot v2.0+

### Session项目
  
springsession + shiro

-  设置:shiro web = true

### 无状态Token项目

jwt + shiro

-  设置:shiro web = false
 