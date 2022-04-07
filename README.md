# fillPDF

一个简易的PDF上传模板自动填充并输出PDF文件

### 基于itex7

### 快速开始

1. 接口打印日志已关闭，如需放开则在application.yml中打开
2. 项目端口为8080，如需更改自行设置
3. 接口路径：/fordan/main/fillPdf
4. POST请求，接收数据格式**multipart/form-data**，上传文档参数名称为**file**

#### Jar部署说明：

 以jar包方式运行，在正式环境中启动如启动命令 java -jar hani-0.1.jar &

#### Docker容器部署

可上传jar包到服务器的指定目录，并在该目录下创建Dockerfile 文件

Dockerfile文件内容

```shell
# 基础镜像使用java
FROM java:8
# 作者
MAINTAINER wk@email.com
# 将jar包添加到容器中并更名为hani.jar
ADD hani-0.1.jar hani.jar
# 配置容器启动后执行的命令
ENTRYPOINT ["java", "-jar", "hani.jar"]
```

##### 镜像制作

```
docker build --rm -t fillpdf .
```

**.** 表示当前路径，可进行通过-f指定要使用的Dockerfile路径

##### 镜像运行

```
docker run --name fillPDF -p 8080:8080 fillpdf
```



