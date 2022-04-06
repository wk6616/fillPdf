# fillPdf

一个简易的pdf上传模板自动填充并输出pdf文件

#### 基于itex7

#### 部署说明：

1. 项目以jar包方式运行，在正式环境中启动如启动命令 java -jar hani-0.1.jar
2. 接口打印日志已关闭，如需放开则在application.yml中打开
3. 端口号设置为8080，如需更改自行设置
4. 接口路径：/fordan/main/fillPdf
5. POST请求，接收数据格式**multipart/form-data**，上传文档参数名称为**file**
