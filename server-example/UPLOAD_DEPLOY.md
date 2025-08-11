# Spring Boot应用程序上传和部署指南

本文档详细说明如何将本地开发的Spring Boot应用程序上传到服务器并进行部署。

## 前提条件

1. 服务器上已安装JDK 11或更高版本
2. 服务器上已安装MySQL数据库并完成配置
3. 本地开发环境已完成Spring Boot应用程序开发

## 方法一：本地构建后上传JAR文件（推荐）

### 1. 在本地构建Spring Boot应用程序

进入项目根目录，执行Maven构建命令：
```bash
# 清理并构建项目
mvn clean package

# 或者只构建而不运行测试（如果测试失败但代码可以正常运行）
mvn clean package -DskipTests
```

构建成功后，会在项目目录的`target`文件夹下生成一个JAR文件，例如`api-1.0.0.jar`。

### 2. 将JAR文件上传到服务器

#### 使用SCP命令（Linux/Mac）
```bash
# 上传JAR文件到服务器
scp target/api-1.0.0.jar root@your-server-ip:/opt/

# 如果需要上传数据库SQL文件
scp ToolManagementSql.sql root@your-server-ip:/opt/
```

#### 使用WinSCP（Windows）
1. 下载并安装WinSCP
2. 创建新会话，输入服务器IP、用户名（root）和密码
3. 连接成功后，将本地的`target/api-1.0.0.jar`文件拖拽到服务器的`/opt/`目录

#### 使用SFTP命令行工具
```bash
# 启动SFTP会话
sftp root@your-server-ip

# 在SFTP会话中执行以下命令
sftp> cd /opt
sftp> put target/api-1.0.0.jar
sftp> put ToolManagementSql.sql
sftp> quit
```

### 3. 在服务器上配置应用程序

#### 创建应用程序目录
```bash
# 创建应用程序目录
mkdir -p /opt/tool-management-api

# 移动JAR文件到应用程序目录
mv /opt/api-1.0.0.jar /opt/tool-management-api/
```

#### 创建配置文件
创建`/opt/tool-management-api/application.properties`文件：
```bash
cat > /opt/tool-management-api/application.properties << 'EOF'
# 服务器配置
server.port=8080
server.address=0.0.0.0

# 数据库配置（请根据实际情况修改）
spring.datasource.url=jdbc:mysql://localhost:3306/tool_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=tooluser
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA配置
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# 日志配置
logging.level.com.toolmanagement.api=INFO
logging.level.org.springframework.web=INFO
EOF
```

### 4. 导入数据库结构

如果还没有在服务器上创建数据库表结构：
```bash
# 登录MySQL并执行SQL脚本
mysql -u tooluser -p tool_management < /opt/ToolManagementSql.sql
```

### 5. 创建系统服务

创建系统服务文件`/etc/systemd/system/tool-api.service`：
```bash
cat > /etc/systemd/system/tool-api.service << 'EOF'
[Unit]
Description=Tool Management API
After=network.target mysqld.service

[Service]
Type=simple
User=root
WorkingDirectory=/opt/tool-management-api
ExecStart=/usr/bin/java -jar /opt/tool-management-api/api-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF
```

### 6. 启动应用程序

```bash
# 重新加载systemd配置
systemctl daemon-reload

# 启动服务
systemctl start tool-api

# 设置开机自启
systemctl enable tool-api

# 检查服务状态
systemctl status tool-api
```

## 方法二：上传源代码并在服务器上构建

### 1. 将源代码上传到服务器

#### 使用SCP命令上传整个项目目录
```bash
# 在本地执行，将整个项目目录上传到服务器
scp -r server-example root@your-server-ip:/opt/
```

#### 或者使用Git（如果代码在仓库中）
```bash
# 在服务器上执行
cd /opt
git clone your-repository-url tool-management-api
```

### 2. 在服务器上安装Maven（如果尚未安装）

```bash
# 下载Maven
cd /opt
wget https://archive.apache.org/dist/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz

# 解压
tar -xzf apache-maven-3.8.4-bin.tar.gz
ln -s apache-maven-3.8.4 maven

# 设置环境变量
echo 'export M2_HOME=/opt/maven' >> /etc/profile
echo 'export PATH=${M2_HOME}/bin:${PATH}' >> /etc/profile

# 重新加载环境变量
source /etc/profile

# 验证安装
mvn -version
```

### 3. 在服务器上构建项目

```bash
# 进入项目目录
cd /opt/tool-management-api

# 构建项目
mvn clean package

# 或者跳过测试构建
mvn clean package -DskipTests
```

### 4. 启动应用程序

按照方法一中的步骤5-6配置系统服务并启动应用程序。

## 测试部署

部署完成后，可以通过以下方式测试API：

```bash
# 直接访问API
curl http://your-server-ip:8080/api/health

# 测试登录接口
curl -X POST http://your-server-ip:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"identifier":"admin","password":"admin123"}'
```

## 常见问题解决

### 1. 端口被占用
```bash
# 查看端口占用情况
netstat -tuln | grep 8080

# 杀死占用端口的进程（替换<PID>为实际进程ID）
kill -9 <PID>
```

### 2. 权限问题
```bash
# 确保JAR文件具有执行权限
chmod +x /opt/tool-management-api/api-1.0.0.jar
```

### 3. 内存不足
```bash
# 增加JVM内存限制
ExecStart=/usr/bin/java -Xmx1024m -jar /opt/tool-management-api/api-1.0.0.jar
```

### 4. 数据库连接失败
检查以下几点：
- 数据库服务是否运行：`systemctl status mysqld`
- 数据库配置是否正确
- 用户权限是否正确设置
- 防火墙是否阻止了连接

### 5. 查看应用程序日志
```bash
# 查看服务日志
journalctl -u tool-api -f

# 查看特定时间范围的日志
journalctl -u tool-api --since "10 minutes ago"
```

## 自动化部署脚本（可选）

创建部署脚本`/opt/tool-management-api/deploy.sh`：
```bash
cat > /opt/tool-management-api/deploy.sh << 'EOF'
#!/bin/bash

echo "停止服务..."
systemctl stop tool-api

echo "备份当前版本..."
cp /opt/tool-management-api/api-1.0.0.jar /opt/tool-management-api/api-1.0.0.jar.backup

echo "上传新版本..."
# 这里可以添加从远程仓库拉取最新代码的命令

echo "重新构建项目..."
cd /opt/tool-management-api
mvn clean package -DskipTests

echo "启动服务..."
systemctl start tool-api

echo "检查服务状态..."
systemctl status tool-api

echo "部署完成！"
EOF

# 设置执行权限
chmod +x /opt/tool-management-api/deploy.sh
```

通过以上步骤，您可以成功将Spring Boot应用程序上传到服务器并进行部署。