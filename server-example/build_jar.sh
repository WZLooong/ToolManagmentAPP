#!/bin/bash

echo "正在构建Spring Boot应用程序..."

# 检查是否安装了Maven
if ! command -v mvn &> /dev/null
then
    echo "错误: 未找到Maven。请先安装Maven。"
    exit 1
fi

# 清理项目
echo "正在清理项目..."
mvn clean

# 构建项目
echo "正在构建项目..."
mvn package

# 检查构建是否成功
if [ -f "target/api-1.0.0.jar" ]; then
    echo ""
    echo "构建成功!"
    echo "JAR文件位置: target/api-1.0.0.jar"
    echo ""
    echo "构建完成。"
else
    echo ""
    echo "构建失败，请检查错误信息。"
    exit 1
fi