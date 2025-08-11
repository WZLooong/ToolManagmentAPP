# Spring Boot API服务器部署指南

本文档详细说明了如何将Spring Boot API服务器部署到远程服务器上。

## 服务器要求

1. 操作系统：Linux (推荐Ubuntu 20.04+) 或 Windows Server
2. Java版本：Java 11 或更高版本
3. 数据库：MySQL 5.7 或更高版本
4. 内存：至少2GB RAM
5. 磁盘空间：至少10GB可用空间

## 部署步骤

### 1. 准备服务器环境

#### 安装Java 11
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-11-jdk -y

# CentOS/RHEL
sudo yum install java-11-openjdk-devel -y

# 验证安装
java -version
```

#### 安装Maven
```bash
# Ubuntu/Debian
sudo apt install maven -y

# CentOS/RHEL
sudo yum install maven -y

# 验证安装
mvn -version
```

#### 安装MySQL数据库
```bash
# Ubuntu/Debian
sudo apt install mysql-server -y

# CentOS/RHEL
sudo yum install mysql-server -y

# 启动MySQL服务
sudo systemctl start mysql
sudo systemctl enable mysql
```

### 2. 配置数据库

#### 登录MySQL并创建数据库
```bash
# 登录MySQL
mysql -u root -p

# 在MySQL命令行中执行以下命令
CREATE DATABASE tool_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'tooluser'@'localhost' IDENTIFIED BY 'toolpassword';
GRANT ALL PRIVILEGES ON tool_management.* TO 'tooluser'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

#### 创建数据表
将项目中的`ToolManagementSql.sql`文件上传到服务器，并执行以下命令：
```bash
mysql -u tooluser -p tool_management < ToolManagementSql.sql
```

### 3. 配置应用程序

#### 修改数据库配置
编辑`src/main/resources/application.properties`文件：
```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/tool_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=tooluser
spring.datasource.password=toolpassword
```

#### 配置服务器地址和端口
在同一文件中修改：
```properties
# 服务器配置
server.port=8080
server.address=0.0.0.0
```

### 4. 构建和部署应用程序

#### 方法一：直接在服务器上构建（推荐）

1. 将项目源代码上传到服务器
2. 进入项目根目录
3. 构建项目：
   ```bash
   mvn clean install
   ```
4. 运行应用程序：
   ```bash
   mvn spring-boot:run
   ```

#### 方法二：本地构建后上传JAR文件

1. 在本地开发环境中构建项目：
   ```bash
   mvn clean package
   ```
2. 找到生成的JAR文件（通常在`target`目录下）
3. 将JAR文件上传到服务器
4. 在服务器上运行：
   ```bash
   java -jar api-1.0.0.jar
   ```

### 5. 配置防火墙

确保服务器防火墙允许8080端口的访问：

```bash
# Ubuntu/Debian (UFW)
sudo ufw allow 8080

# CentOS/RHEL (firewalld)
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
```

### 6. 配置反向代理（可选）

为了更好地管理和保护API，建议使用Nginx作为反向代理：

#### 安装Nginx
```bash
# Ubuntu/Debian
sudo apt install nginx -y

# CentOS/RHEL
sudo yum install nginx -y
```

#### 配置Nginx
创建配置文件`/etc/nginx/sites-available/tool-api`：
```nginx
server {
    listen 80;
    server_name your-server-ip;  # 替换为实际的服务器IP或域名

    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

启用配置：
```bash
sudo ln -s /etc/nginx/sites-available/tool-api /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### 7. 设置系统服务（生产环境推荐）

创建系统服务文件`/etc/systemd/system/tool-api.service`：
```ini
[Unit]
Description=Tool Management API
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/path/to/your/app  # 替换为实际的应用路径
ExecStart=/usr/bin/java -jar /path/to/your/app/api-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启动并启用服务：
```bash
sudo systemctl daemon-reload
sudo systemctl start tool-api
sudo systemctl enable tool-api
sudo systemctl status tool-api
```

## 测试部署

部署完成后，可以通过以下方式测试API：

```bash
# 健康检查
curl http://your-server-ip:8080/api/health

# 如果配置了Nginx反向代理
curl http://your-server-ip/api/health
```

## 常见问题解决

### 1. 端口被占用
```bash
# 查看端口占用情况
netstat -tuln | grep 8080

# 杀死占用端口的进程
kill -9 <PID>
```

### 2. 数据库连接失败
检查以下几点：
- 数据库服务是否运行：`sudo systemctl status mysql`
- 数据库配置是否正确
- 用户权限是否正确设置
- 防火墙是否阻止了连接

### 3. 内存不足
增加JVM内存限制：
```bash
java -Xmx1024m -jar api-1.0.0.jar
```

## 日志查看

```bash
# 查看应用程序日志
tail -f /var/log/tool-api.log

# 或者如果使用systemd服务
sudo journalctl -u tool-api -f
```