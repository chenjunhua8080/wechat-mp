FROM openjdk:8
###复制文件到容器app-springboot
ADD target/wechat-mp-0.0.1-SNAPSHOT.jar /wechat-mp-0.0.1-SNAPSHOT.jar
###声明启动端口号
EXPOSE 8057
###配置容器启动后执行的命令
ENTRYPOINT ["java","-jar","/wechat-mp-0.0.1-SNAPSHOT.jar"]
