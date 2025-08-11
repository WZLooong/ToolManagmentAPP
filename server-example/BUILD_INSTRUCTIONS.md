# 清理项目
mvn clean

# 构建项目并打包
mvn package

# 或者跳过测试构建（如果测试失败但代码可正常运行）
mvn package -DskipTests# Spring Boot API应用程序构建说明

本文档详细说明如何构建Spring Boot API应用程序并生成JAR文件。

## 环境要求

1. Java JDK 11 或更高版本
2. Apache Maven 3.6 或更高版本

## 构建步骤

### 1. 安装Java JDK

#### Windows系统
- 下载并安装Oracle JDK或OpenJDK 11+
- 配置环境变量JAVA_HOME指向JDK安装目录
- 将%JAVA_HOME%\bin添加到PATH环境变量

#### Linux/macOS系统
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-11-jdk -y

# CentOS/RHEL
sudo yum install java-11-openjdk-devel -y

# macOS (使用Homebrew)
brew install openjdk@11
```

### 2. 安装Maven

#### Windows系统
1. 下载Maven二进制包: https://maven.apache.org/download.cgi
2. 解压到指定目录（如C:\apache-maven）
3. 配置环境变量MAVEN_HOME指向Maven安装目录
4. 将%MAVEN_HOME%\bin添加到PATH环境变量

#### Linux/macOS系统
```bash
# Ubuntu/Debian
sudo apt install maven -y

# CentOS/RHEL
sudo yum install maven -y

# macOS (使用Homebrew)
brew install maven
```

### 3. 验证环境安装

打开命令行工具并执行以下命令：
```bash
java -version
mvn -version
```

### 4. 构建应用程序

#### 方法一：使用命令行构建（推荐）

进入项目根目录（包含pom.xml文件的目录），执行以下命令：

```bash
# 清理项目
mvn clean

# 构建项目并打包
mvn package

# 或者跳过测试构建（如果测试失败但代码可正常运行）
mvn package -DskipTests
```

构建成功后，会在项目目录下生成`target`文件夹，其中包含`api-1.0.0.jar`文件。

#### 方法二：使用构建脚本

##### Windows系统
双击运行[build_jar.bat](file:///H:/ToolManagmentAPP/server-example/build_jar.bat)文件，或在命令行中执行：
```cmd
build_jar.bat
```

##### Linux/macOS系统
在终端中执行以下命令：
```bash
# 添加执行权限
chmod +x build_jar.sh

# 运行构建脚本
./build_jar.sh
```

## 构建输出

构建成功后，将在以下位置生成JAR文件：
```
target/
└── api-1.0.0.jar
```

## 部署JAR文件

构建完成后，可以将生成的JAR文件部署到服务器：

### 使用SCP上传到服务器
```bash
scp target/api-1.0.0.jar root@your-server-ip:/opt/
```

### 在服务器上运行
```bash
# 直接运行
java -jar target/api-1.0.0.jar

# 或在后台运行
nohup java -jar target/api-1.0.0.jar > app.log 2>&1 &
```

## 常见问题解决

### 1. Maven构建失败
```bash
# 清理项目并重新构建
mvn clean compile

# 查看详细错误信息
mvn package -X
```

### 2. 缺少依赖
```bash
# 下载并安装所有依赖
mvn dependency:resolve

# 强制更新依赖
mvn clean install -U
```

### 3. 测试失败导致构建中断
```bash
# 跳过测试构建
mvn package -DskipTests

# 或者只运行编译阶段
mvn compile
```

### 4. 内存不足
```bash
# 增加Maven内存
export MAVEN_OPTS="-Xmx1024m"
mvn package
```

通过以上步骤，您可以成功构建Spring Boot API应用程序并生成JAR文件用于部署。