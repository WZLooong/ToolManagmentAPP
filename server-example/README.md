# Tool Management API 服务器部署指南

## 技术栈
- Node.js + Express
- MySQL (生产环境)
- bcryptjs (密码加密)
- jsonwebtoken (JWT认证)

## 部署步骤

### 1. 安装Node.js环境

```bash
# Ubuntu/Debian系统
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
sudo apt-get install -y nodejs

# CentOS/RHEL系统
curl -fsSL https://rpm.nodesource.com/setup_16.x | sudo bash -
sudo yum install -y nodejs
```

### 2. 上传代码到服务器

使用SCP命令上传代码：
```bash
scp -r server-example user@39.106.150.70:/home/user/
```

或者使用Git克隆：
```bash
git clone https://github.com/yourusername/tool-management-api.git
```

### 3. 安装依赖

```bash
cd server-example
npm install
```

### 4. 安装PM2并启动服务

```bash
# 安装PM2
sudo npm install -g pm2

# 使用PM2启动API服务
pm2 start server.js --name "tool-api"

# 设置开机自启
pm2 startup
pm2 save
```

### 5. 配置防火墙

```bash
# 允许8080端口
sudo ufw allow 8080

# 检查防火墙状态
sudo ufw status
```

### 6. 配置Nginx反向代理（可选）

创建Nginx配置文件：
```bash
sudo nano /etc/nginx/sites-available/tool-api
```

添加以下配置：
```nginx
server {
    listen 80;
    server_name 39.106.150.70;

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

启用配置并重启Nginx：
```bash
sudo ln -s /etc/nginx/sites-available/tool-api /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

## API端点

### 认证相关
- POST `/api/login` - 用户登录
- POST `/api/register` - 用户注册

### 工具管理
- GET `/api/tools` - 获取工具列表
- POST `/api/tools` - 添加工具
- PUT `/api/tools/:id` - 更新工具
- DELETE `/api/tools/:id` - 删除工具

## 测试API

```bash
# 测试健康检查
curl http://39.106.150.70:8080/api/health

# 测试登录
curl -X POST http://39.106.150.70:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"identifier":"admin","password":"admin123"}'

# 测试注册
curl -X POST http://39.106.150.70:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138001","email":"user@example.com","password":"user123","name":"张三","employeeId":"EMP002","team":"技术团队","role":"user"}'
```

## 默认测试账户

- 用户名: admin
- 密码: admin123

## 生产环境注意事项

1. 更换JWT_SECRET为强随机字符串
2. 配置真实的MySQL数据库
3. 使用HTTPS（配置SSL证书）
4. 设置适当的日志记录
5. 配置备份策略
6. 设置监控和告警