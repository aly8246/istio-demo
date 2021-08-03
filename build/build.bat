@REM 设置构建脚本参数
set image-name=%1
set project-version=%2
set project-basedir=%3
set docker-credential=%4

@REM 先删除同版本镜像和latest版本镜像
docker rmi %image-name%:%project-version%
docker rmi %image-name%:latest

@REM 开始构建镜像
echo while build image :%image-name%:%project-version%
docker build --rm -t %image-name%:%project-version% %project-basedir%

@REM 登陆到dockerhub
docker login %docker-credential%

@REM 推送到dockerhub
docker push %image-name%:%project-version%

@REM 构建latest镜像
docker tag %image-name%:%project-version% %image-name%:latest
docker push %image-name%:latest