@echo off
echo 正在构建Spring Boot应用程序...

REM 检查是否安装了Maven
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven。请先安装Maven。
    pause
    exit /b 1
)

REM 清理项目
echo 正在清理项目...
call mvn clean

REM 构建项目
echo 正在构建项目...
call mvn package

pause
REM 检查构建是否成功
if exist "target\api-1.0.0.jar" (
    echo 构建成功!
    echo JAR文件位置: target\api-1.0.0.jar
    echo 构建完成。
) else (
    echo 构建失败，请检查错误信息。
    pause
    exit /b 1
)
pause