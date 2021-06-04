# openjdk镜像是容器的运行环境
FROM openjdk:8-jre-slim as ship

# 为了安全起见，在生产环境运行容器时不要用指root帐号和群组
RUN addgroup --system app \
    && adduser --system --ingroup app app

#设置工作目录
WORKDIR /home/app

#jar包名字
ARG JAR_FILE_NAME

# 把编译好的jar复制到/home/app目录下
ADD ./target/$JAR_FILE_NAME /home/app

#把jar包改成app.jar方便后续使用
RUN cp /home/app/$JAR_FILE_NAME ./app.jar

EXPOSE 8001

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./app.jar --sping.profiles.active=prd"]