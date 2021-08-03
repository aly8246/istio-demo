#openJdk为基础构建镜像
FROM openjdk:8 as builder
#工作空间
WORKDIR application
#把本地编译的jar包拷贝进来，改名为application.jar
COPY target/*.jar application.jar
#jar包分层
RUN java -Djarmode=layertools -jar application.jar extract

#运行时镜像
FROM openjdk:8-jre-alpine
#工作空间
WORKDIR application
#从builder构建镜像拷贝分层代码
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./
#设置时区
ENV TZ="Asia/Shanghai"
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#java选项
ENV JVM_OPTS="-XX:MaxRAMPercentage=80.0"
ENV JAVA_OPTS=""
#入口点，启动java程序
ENTRYPOINT ["sh","-c","java $JVM_OPTS $JAVA_OPTS org.springframework.boot.loader.JarLauncher"]