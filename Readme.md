# 自动化测试框架（Java + HTTPClient  + TestNG + Maven）

## 运行项目

1.为了方便测试，因此在项目中自己编写了一个接口用于测试`UserInfoController.class` 。运行`DemoApplication.class`启动项目，调用下方接口地址即可。

```
httP://127.0.0.1:8080/demo/pi/user/queryUserInfo/list
```

2.项目启动后，可以运行 `QueryUserInfoTest.class` 的测试用例，查看测试用例的运行结果。



### 步骤

1. 先写一个接口，user的增删改查，
2. 针对这些接口，新增测试用例，输出测试报告  ok
3. 接上测试套件，接log4j (日志)
4. 接各种工具类Util
5. 后面再增加token验证
6. 把数据库的配置集中管理
7. 对testNG的深度理解 掌握

指南：https://github.com/Jsir07/TestHub

指导：[Java接口自动化测试实战笔记](https://www.cnblogs.com/zylhxd/articles/10879351.html)  https://www.cnblogs.com/zylhxd/articles/10879351.html#extentreports  

github：  https://github.com/eatherTan/AutoTest

java自动化测试框架第六篇-处理多数据库配置 ： https://www.jianshu.com/p/f1dc35db3f7f



