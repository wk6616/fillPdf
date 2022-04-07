# 基础镜像使用java
FROM java:8
# 作者
MAINTAINER wk@email.com
# 将jar包添加到容器中并更名为hani.jar
ADD hani-0.1.jar hani.jar
# 配置容器启动后执行的命令
ENTRYPOINT ["java", "-jar", "hani.jar"]
