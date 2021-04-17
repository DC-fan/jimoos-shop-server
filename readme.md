# 积墨开源商城 服务端代码

![Build Status](https://github.com/jimoos-cn/jimoos-shop-server/workflows/Java%20CI%20with%20Maven/badge.svg)
![最近提交](https://img.shields.io/github/last-commit/jimoos-cn/jimoos-shop-server)
![测试接口服务在线](https://img.shields.io/website?down_message=%E4%B8%8D%E5%9C%A8%E7%BA%BF&label=%E6%B5%8B%E8%AF%95API%E6%9C%8D%E5%8A%A1&url=http%3A%2F%2F150.158.189.200%3A9000%2Fv1%2Fpings)
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-2-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

## 快速开始

### 下载代码

#### git 命令

1. 新建存放代码的文件夹
2. 初始化空的git仓库：`git init`
3. clone代码：`git clone https://github.com/jimoos-cn/jimoos-shop-server.git`

![git clone](docs/_media/git_clone.png)

#### 下载压缩包

![下载压缩包](docs/_media/download.png)



点击下载zip，解压到本地

### 数据库导入

1.  新建数据库

   ```sql
   mysql -u root -p 
   create database jimoos_shop;
   ```

   ![新建数据库](docs/_media/create_database.png)

2. 修改数据库配置文件

   ```shell
   vim migration/src/main/resources/application.yml  
   // 当前在jimoos-shop-server目录下，在构建项目之前需要修改jimoos-shop-server/starter/api-starter/src/main/resources/application.yml以及jimoos-shop-server/starter/portal-starter/src/main/resources/application.yml中数据库相应的设置
   // 将spring.datasource.username和spring,dataroutce.password修改为自己的username,password
   ```

   ![配置文件](docs/_media/database_setting.png)

   

3. 执行数据迁移命令行(在jimoos-shop-server目录下)

   ```shell
   mvn -Dtest=MigrationDone test -DfailIfNoTests=false 
   迁移成功如下图，此时可以从刚才创建的数据库中找到对应的迁移成功的表和数据
   ```

   ![迁移成功](docs/_media/success_migration.png)

   ![查看表](docs/_media/tables.png)



### 构建及启动项目

```shell
// 构建项目
mvn clean package -Dmaven.test.skip=true -s docker/settings.xml 
// 启动客户端接口模块
cd starter/api-starter/target 
java -jar jm-api-starter-0.0.1.jar 
```

启动成功

![启动成功](docs/_media/success_started.png)

## 业务建模图

![业务建模图](docs/_media/model.jpg)

## 数据库导入

执行数据迁移命令行

`mvn -Dtest=MigrationDone test -DfailIfNoTests=false`

## 开发文档入口

文档地址: https://jimoos-cn.github.io/jimoos-shop-server/

## TODO(v1.0.0)

[X] 数据库配置读取     
[X] 华为云OBS接入    
[X] 用户注册    
[X] Banner管理    
[X] 商品管理    
[X] 购物车     
[X] 下单      
[X] 优惠券     
[X] 我的订单    
[ ] 商品集合  
[X] 商城介绍


----
[] 接口文档  
[-] 接口部署

## 项目结构

- api-starter 客户端接口入口
- portal-starter 后台管理接口入口
- migration 数据库迁移管理
- `addons/@`下 为项目内插件
- `addons/depends`为外部插件，外部插件禁止修改。

## Contributors ✨

我非常重视每一个对这个项目的贡献者，我会将贡献者列表更新到这里。
不限于提交 Pull Request 的小伙伴，还可以包括提交 Issue 以及在社群中有所贡献的人。

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://github.com/keepcleargas"><img src="https://avatars.githubusercontent.com/u/1215177?v=4?s=50" width="50px;" alt=""/><br /><sub><b>keepcleargas</b></sub></a><br /><a href="https://github.com/jimoos/jimoos-shop-server/commits?author=keepcleargas" title="Code">💻</a> <a href="#video-keepcleargas" title="Videos">📹</a> <a href="#tutorial-keepcleargas" title="Tutorials">✅</a> <a href="#ideas-keepcleargas" title="Ideas, Planning, & Feedback">🤔</a> <a href="#maintenance-keepcleargas" title="Maintenance">🚧</a></td>
    <td align="center"><a href="https://github.com/suao123"><img src="https://avatars.githubusercontent.com/u/36814429?v=4?s=50" width="50px;" alt=""/><br /><sub><b>sleepsleep</b></sub></a><br /><a href="https://github.com/jimoos/jimoos-shop-server/commits?author=suao123" title="Code">💻</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!