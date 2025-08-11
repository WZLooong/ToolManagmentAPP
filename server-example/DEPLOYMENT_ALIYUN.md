# 阿里云服务器部署Spring Boot API指南

本文档专门针对阿里云服务器（镜像：aliyun_3_x64_20G_alibase_20250117.vhd）部署Spring Boot API应用程序的详细说明。

## 服务器环境信息

- 镜像：aliyun_3_x64_20G_alibase_20250117.vhd
- 操作系统：Alibaba Cloud Linux 3（基于CentOS/RHEL）
- 架构：x86_64
- 磁盘：20G

## 连接服务器

使用SSH连接到阿里云服务器：
```bash
ssh root@your-server-ip
```

## 部署步骤

### 1. 更新系统和安装必要软件

```bash
# 更新系统包
yum update -y

# 安装必要的工具
yum install -y wget curl vim git unzip tar

# 安装Java 11
yum install -y java-11-openjdk-devel

# 验证Java安装
java -version
```

### 2. 安装MySQL数据库

```bash
# 下载MySQL官方仓库
wget https://dev.mysql.com/get/mysql80-community-release-el8-1.noarch.rpm

# 安装仓库
rpm -ivh mysql80-community-release-el8-1.noarch.rpm

# 安装MySQL服务器
yum install -y mysql-server

# 启动MySQL服务
systemctl start mysqld
systemctl enable mysqld

# 获取临时root密码
grep 'temporary password' /var/log/mysqld.log

# 运行安全配置脚本
mysql_secure_installation
```

### 3. 配置MySQL数据库

```bash
# 登录MySQL（使用设置的root密码）
mysql -u root -p

# 在MySQL命令行中执行以下命令
CREATE DATABASE tool_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 创建专用用户（请替换'your_password'为强密码）
CREATE USER 'tooluser'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON tool_management.* TO 'tooluser'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 4. 创建数据表

将项目中的`ToolManagementSql.sql`文件上传到服务器，并执行以下命令：
```bash
# 上传文件后执行
mysql -u tooluser -p tool_management < ToolManagementSql.sql
```

### 5. 安装Maven（用于构建项目）

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

### 6. 上传并配置应用程序

#### 方法一：使用Git克隆项目（如果代码在仓库中）
```bash
# 安装Git（如果尚未安装）
yum install -y git

# 克隆项目
cd /opt
git clone your-repository-url tool-management-api
cd tool-management-api
```

#### 方法二：上传项目文件
使用SCP或SFTP将项目文件上传到服务器的`/opt/tool-management-api`目录。

### 7. 配置应用程序

编辑`src/main/resources/application.properties`文件：
```properties
# 服务器配置
server.port=8080
server.address=0.0.0.0

# 数据库配置（请替换'your_password'为实际密码）
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
```

### 8. 构建应用程序

```bash
# 进入项目目录
cd /opt/tool-management-api

# 构建项目
mvn clean package
```

### 9. 配置防火墙和安全组

#### 配置系统防火墙
```bash
# 启动firewalld
systemctl start firewalld
systemctl enable firewalld

# 开放8080端口
firewall-cmd --permanent --add-port=8080/tcp
firewall-cmd --reload

# 查看开放的端口
firewall-cmd --list-all
```

#### 配置阿里云安全组
1. 登录阿里云控制台
2. 进入ECS实例管理页面
3. 找到您的服务器实例，点击"更多" -> "网络和安全组" -> "安全组配置"
4. 添加安全组规则：
   - 授权策略：允许
   - 协议类型：TCP
   - 端口范围：8080/8080
   - 授权对象：0.0.0.0/0（或指定IP范围）

### 10. 创建系统服务

创建系统服务文件`/etc/systemd/system/tool-api.service`：
```ini
[Unit]
Description=Tool Management API
After=network.target mysqld.service

[Service]
Type=simple
User=root
WorkingDirectory=/opt/tool-management-api
ExecStart=/usr/bin/java -jar /opt/tool-management-api/target/api-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

配置并启动服务：
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

### 11. 配置Nginx反向代理（可选但推荐）

#### 安装Nginx
```bash
# 安装Nginx
yum install -y nginx

# 启动Nginx
systemctl start nginx
systemctl enable nginx
```

#### 配置反向代理
创建配置文件`/etc/nginx/conf.d/tool-api.conf`：
```nginx
server {
    listen 80;
    server_name your-domain.com;  # 替换为您的域名或服务器IP

    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
    }
}
```

重新加载Nginx配置：
```bash
nginx -t
systemctl reload nginx
```

## 测试部署

部署完成后，可以通过以下方式测试API：

```bash
# 直接访问API
curl http://your-server-ip:8080/api/health

# 如果配置了Nginx反向代理
curl http://your-server-ip/api/health
```

## 日志查看

```bash
# 查看应用程序日志
journalctl -u tool-api -f

# 查看特定时间范围的日志
journalctl -u tool-api --since "10 minutes ago"

# 查看Nginx访问日志
tail -f /var/log/nginx/access.log

# 查看Nginx错误日志
tail -f /var/log/nginx/error.log
```

## 常见问题解决

### 1. 服务启动失败
```bash
# 检查服务状态和错误信息
systemctl status tool-api

# 查看详细日志
journalctl -u tool-api --no-pager
```

### 2. 数据库连接问题
```bash
# 检查MySQL服务状态
systemctl status mysqld

# 检查MySQL错误日志
tail -f /var/log/mysqld.log

# 测试数据库连接
mysql -u tooluser -p tool_management -e "SELECT 1;"
```

### 3. 端口访问问题
```bash
# 检查端口监听状态
netstat -tuln | grep 8080

# 检查防火墙规则
firewall-cmd --list-all

# 检查阿里云安全组配置（在控制台中确认）
```

### 4. 内存不足问题
```bash
# 查看系统内存使用情况
free -h

# 增加JVM内存限制（在服务文件中修改ExecStart）
# ExecStart=/usr/bin/java -Xmx1024m -jar /opt/tool-management-api/target/api-1.0.0.jar
```

## 性能优化建议

1. **JVM调优**：
   ```bash
   # 在服务文件中添加JVM参数
   ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar /opt/tool-management-api/target/api-1.0.0.jar
   ```

2. **数据库连接池优化**：
   在[application.properties](file:///H:/ToolManagmentAPP/server-example/src/main/resources/application.properties)中添加：
   ```properties
   spring.datasource.hikari.maximum-pool-size=20
   spring.datasource.hikari.minimum-idle=5
   spring.datasource.hikari.connection-timeout=20000
   ```

3. **启用GZIP压缩**：
   在[application.properties](file:///H:/ToolManagmentAPP/server-example/src/main/resources/application.properties)中添加：
   ```properties
   server.compression.enabled=true
   server.compression.mime-types=application/json,application/xml,text/html,text/xml,text/plain
   ```

## 备份策略

1. **数据库备份**：
   ```bash
   # 创建备份脚本
   cat > /opt/backup-db.sh << 'EOF'
   #!/bin/bash
   DATE=$(date +%Y%m%d_%H%M%S)
   mysqldump -u tooluser -pyour_password tool_management > /opt/backups/tool_management_$DATE.sql
   find /opt/backups -name "tool_management_*.sql" -mtime +7 -delete
   EOF

   # 设置执行权限
   chmod +x /opt/backup-db.sh

   # 创建备份目录
   mkdir -p /opt/backups

   # 添加到定时任务（每天凌晨2点执行）
   echo "0 2 * * * /opt/backup-db.sh" >> /var/spool/cron/root
   ```

2. **应用程序备份**：
   定期备份应用程序目录和配置文件。

通过以上步骤，您应该能够成功在阿里云服务器上部署Spring Boot API应用程序。