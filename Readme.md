# 自动化测试框架（Java + HTTPClient  + TestNG + Maven）

前提：需要新建一个数据库，如`application.yml`文件中的数据库信息，自己新建了数据库，需要把`application.yml`的数据库连接信息也修改一下

## 运行项目

1.为了方便测试，因此在项目中自己编写了一个接口用于测试`UserInfoController.class` 。运行`DemoApplication.class`启动项目，调用下方接口地址，调用成功，会返回接口内容。

```
接口地址： httP://127.0.0.1:8080/demo/pi/user/queryUserInfo/list
```

2.项目启动后，运行测试用例 

```
运行测试用例有两种方式：
1.直接执行单个测试用例，找到@Test注解，执行测试方法，如QueryUserInfoTest.class 类中有测试方法，可以直接执行
2.运行 testdemo/testSuit下的xml文件
```

3.测试用例运行结束以后，可以查看测试报告。测试报告在`testdemo/target/test-report` 目录中，

### 实现步骤

1. 先写一个接口，user的增删改查， ok
2. 针对这些接口，新增测试用例，输出测试报告  ok
3. 接上测试套件，接log4j (日志)
4. 接各种工具类Util
5. 后面再增加token验证
6. 把数据库的配置集中管理：多个数据库咋办
7. 对testNG的深度理解 掌握

指南：https://github.com/Jsir07/TestHub

指导：[Java接口自动化测试实战笔记](https://www.cnblogs.com/zylhxd/articles/10879351.html)  https://www.cnblogs.com/zylhxd/articles/10879351.html#extentreports  

github：  https://github.com/eatherTan/AutoTest

java自动化测试框架第六篇-处理多数据库配置 ： https://www.jianshu.com/p/f1dc35db3f7f



