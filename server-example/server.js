const express = require('express');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const app = express();
const PORT = 8080;

// Middleware
app.use(express.json());

// Mock database - in a real application, you would use MySQL
const users = [
  {
    id: 1,
    username: 'admin',
    password: '$2a$10$8K1p/a0dhrxiowP.dnkgNORTWgdEDHn5L2/xjpEWuC.QQv4rKO9jO', // password: admin123
    name: '管理员',
    employeeId: 'EMP001',
    phone: '13800138000',
    email: 'admin@example.com',
    team: '管理团队',
    role: 'admin'
  }
];

const tools = [
  {
    id: 1,
    name: '电钻',
    description: '专业电钻',
    quantity: 5,
    available: 3
  },
  {
    id: 2,
    name: '扳手',
    description: '套筒扳手组',
    quantity: 10,
    available: 8
  }
];

// JWT Secret
const JWT_SECRET = 'tool_management_secret_key';

// Routes
// 用户登录
app.post('/api/login', (req, res) => {
  try {
    const { identifier, password } = req.body;
    
    // 查找用户
    const user = users.find(u => u.username === identifier);
    
    if (!user) {
      return res.status(401).json({
        success: false,
        message: '用户不存在'
      });
    }
    
    // 验证密码
    const isPasswordValid = bcrypt.compareSync(password, user.password);
    
    if (!isPasswordValid) {
      return res.status(401).json({
        success: false,
        message: '密码错误'
      });
    }
    
    // 生成JWT token
    const token = jwt.sign({ id: user.id, username: user.username }, JWT_SECRET, {
      expiresIn: 86400 // 24 hours
    });
    
    res.status(200).json({
      success: true,
      message: '登录成功',
      token: token,
      user: {
        id: user.id,
        username: user.username,
        name: user.name,
        employeeId: user.employeeId,
        phone: user.phone,
        email: user.email,
        team: user.team,
        role: user.role
      }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: '服务器内部错误'
    });
  }
});

// 用户注册
app.post('/api/register', (req, res) => {
  try {
    const { phone, email, password, name, employeeId, team, role } = req.body;
    
    // 检查用户是否已存在
    const existingUser = users.find(u => u.username === name || u.employeeId === employeeId);
    
    if (existingUser) {
      return res.status(400).json({
        success: false,
        message: '用户名或员工ID已存在'
      });
    }
    
    // 创建新用户
    const newUser = {
      id: users.length + 1,
      username: name,
      password: bcrypt.hashSync(password, 8),
      name: name,
      employeeId: employeeId,
      phone: phone,
      email: email,
      team: team || '默认团队',
      role: role || 'user'
    };
    
    users.push(newUser);
    
    res.status(201).json({
      success: true,
      message: '用户注册成功',
      user: {
        id: newUser.id,
        username: newUser.username,
        name: newUser.name,
        employeeId: newUser.employeeId,
        phone: newUser.phone,
        email: newUser.email,
        team: newUser.team,
        role: newUser.role
      }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: '服务器内部错误'
    });
  }
});

// 获取工具列表
app.get('/api/tools', (req, res) => {
  try {
    const { keyword } = req.query;
    
    let filteredTools = tools;
    
    if (keyword) {
      filteredTools = tools.filter(tool => 
        tool.name.includes(keyword) || tool.description.includes(keyword)
      );
    }
    
    res.status(200).json(filteredTools);
  } catch (error) {
    res.status(500).json({
      success: false,
      message: '服务器内部错误'
    });
  }
});

// 添加工具
app.post('/api/tools', (req, res) => {
  try {
    const { name, description, quantity } = req.body;
    
    const newTool = {
      id: tools.length + 1,
      name: name,
      description: description,
      quantity: quantity,
      available: quantity
    };
    
    tools.push(newTool);
    
    res.status(201).json({
      success: true,
      message: '工具添加成功',
      tool: newTool
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: '服务器内部错误'
    });
  }
});

// 更新工具
app.put('/api/tools/:id', (req, res) => {
  try {
    const { id } = req.params;
    const { name, description, quantity, available } = req.body;
    
    const toolIndex = tools.findIndex(t => t.id === parseInt(id));
    
    if (toolIndex === -1) {
      return res.status(404).json({
        success: false,
        message: '工具未找到'
      });
    }
    
    tools[toolIndex] = {
      ...tools[toolIndex],
      ...(name && { name }),
      ...(description && { description }),
      ...(quantity !== undefined && { quantity }),
      ...(available !== undefined && { available })
    };
    
    res.status(200).json({
      success: true,
      message: '工具更新成功',
      tool: tools[toolIndex]
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: '服务器内部错误'
    });
  }
});

// 删除工具
app.delete('/api/tools/:id', (req, res) => {
  try {
    const { id } = req.params;
    
    const toolIndex = tools.findIndex(t => t.id === parseInt(id));
    
    if (toolIndex === -1) {
      return res.status(404).json({
        success: false,
        message: '工具未找到'
      });
    }
    
    tools.splice(toolIndex, 1);
    
    res.status(200).json({
      success: true,
      message: '工具删除成功'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: '服务器内部错误'
    });
  }
});

// 健康检查端点
app.get('/api/health', (req, res) => {
  res.status(200).json({
    success: true,
    message: 'API服务运行正常',
    timestamp: new Date().toISOString()
  });
});

// 404处理
app.use((req, res) => {
  res.status(404).json({
    success: false,
    message: '接口不存在'
  });
});

// 错误处理中间件
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({
    success: false,
    message: '服务器内部错误'
  });
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Tool Management API服务器正在运行在端口 ${PORT}`);
  console.log(`健康检查: http://localhost:${PORT}/api/health`);
  console.log(`登录接口: http://localhost:${PORT}/api/login`);
  console.log(`注册接口: http://localhost:${PORT}/api/register`);
});