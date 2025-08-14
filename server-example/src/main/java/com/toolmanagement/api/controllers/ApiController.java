package com.toolmanagement.api.controllers;

import com.toolmanagement.api.entities.Tool;
import com.toolmanagement.api.entities.User;
import com.toolmanagement.api.repositories.ToolRepository;
import com.toolmanagement.api.repositories.UserRepository;
import com.toolmanagement.api.security.UserDetailsImpl;
import com.toolmanagement.api.utils.JwtUtils;
import com.toolmanagement.api.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

// 定义登录请求类
class UserLoginRequest {
    private String identifier;
    private String password;
    
    public String getIdentifier() {
        return identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}

// 定义登录响应类
class UserLoginResponse {
    private User user;
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081") // 限制为可信来源
public class ApiController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ToolRepository toolRepository;

    // 健康检查端点
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "API服务运行正常");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    // 用户登录
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserLoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String identifier = request.getIdentifier();
            String password = request.getPassword();
            
            // 添加详细日志
            System.out.println("Login attempt with identifier: " + identifier);

            // 查找用户（通过用户名、员工ID、邮箱或电话）
            Optional<User> userOpt = userRepository.findByUsername(identifier);
            if (!userOpt.isPresent()) {
                System.out.println("User not found by username, trying employeeId: " + identifier);
                userOpt = userRepository.findByEmployeeId(identifier);
            }
            if (!userOpt.isPresent()) {
                System.out.println("User not found by employeeId, trying email: " + identifier);
                userOpt = userRepository.findByEmail(identifier);
            }
            if (!userOpt.isPresent()) {
                System.out.println("User not found by email, trying phone: " + identifier);
                userOpt = userRepository.findByPhone(identifier);
            }

            if (!userOpt.isPresent()) {
                System.out.println("User not found with identifier: " + identifier);
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            User user = userOpt.get();
            System.out.println("User found: " + user.getUsername() + ", checking password");

            // 直接比较密码（不使用加密）
            System.out.println("Checking password for user: " + user.getUsername());
            boolean passwordMatches = password.equals(user.getPassword()); // 直接比较明文密码
            System.out.println("Password matches: " + passwordMatches);
            
            if (!passwordMatches) {
                response.put("success", false);
                response.put("message", "密码错误");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // 创建响应对象
            UserLoginResponse loginResponse = new UserLoginResponse();
            loginResponse.setUser(user);
            
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("data", loginResponse);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Exception during login: " + e.getMessage());
            e.printStackTrace(); // 打印异常堆栈以便调试
            response.put("success", false);
            response.put("message", "服务器内部错误: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 用户注册
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查用户是否已存在
            if (userRepository.existsByUsername(request.getName()) ||
                userRepository.existsByEmployeeId(request.getEmployeeId()) ||
                userRepository.existsByEmail(request.getEmail()) ||
                userRepository.existsByPhone(request.getPhone())) {
                
                response.put("success", false);
                response.put("message", "用户名或员工ID已存在");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // 创建新用户（不加密密码）
            User newUser = new User(
                request.getPhone(),
                request.getEmail(),
                request.getPassword(), // 直接存储明文密码
                request.getName(),
                request.getEmployeeId(),
                request.getTeam(),
                request.getRole()
            );

            User savedUser = userRepository.save(newUser);

            response.put("success", true);
            response.put("message", "用户注册成功");
            response.put("user", savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 检查用户是否存在
    @GetMapping("/user/exists/{identifier}")
    public ResponseEntity<Map<String, Object>> checkUserExists(@PathVariable String identifier) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean exists = userRepository.existsByUsername(identifier) ||
                             userRepository.existsByEmployeeId(identifier) ||
                             userRepository.existsByEmail(identifier) ||
                             userRepository.existsByPhone(identifier);
            
            response.put("success", true);
            response.put("data", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 获取所有用户
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userRepository.findAll();
            response.put("success", true);
            response.put("data", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 根据ID获取用户
    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                response.put("success", true);
                response.put("data", userOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 更新用户信息
    @PutMapping("/users")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestParam String username, @RequestBody User updatedUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // 更新用户信息
                user.setPhone(updatedUser.getPhone());
                user.setEmail(updatedUser.getEmail());
                user.setPassword(updatedUser.getPassword()); // 直接更新明文密码
                user.setEmployeeId(updatedUser.getEmployeeId());
                user.setTeam(updatedUser.getTeam());
                user.setRole(updatedUser.getRole());

                User savedUser = userRepository.save(user);
                response.put("success", true);
                response.put("message", "用户信息更新成功");
                response.put("data", savedUser);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 删除用户
    @DeleteMapping("/users")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                userRepository.delete(userOpt.get());
                response.put("success", true);
                response.put("message", "用户删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 工具管理相关API
    @PostMapping("/tools")
    public ResponseEntity<Map<String, Object>> addTool(@RequestBody Tool tool) {
        Map<String, Object> response = new HashMap<>();
        try {
            Tool savedTool = toolRepository.save(tool);
            response.put("success", true);
            response.put("message", "工具添加成功");
            response.put("data", savedTool);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/tools")
    public ResponseEntity<Map<String, Object>> queryTools(@RequestParam(required = false) String keyword) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Tool> tools;
            if (keyword != null && !keyword.isEmpty()) {
                tools = toolRepository.findByNameContainingOrModelContainingOrSpecificationContaining(
                    keyword, keyword, keyword);
            } else {
                tools = toolRepository.findAll();
            }
            response.put("success", true);
            response.put("data", tools);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/tools")
    public ResponseEntity<Map<String, Object>> updateTool(@RequestBody Tool tool) {
        Map<String, Object> response = new HashMap<>();
        try {
            Tool updatedTool = toolRepository.save(tool);
            response.put("success", true);
            response.put("message", "工具信息更新成功");
            response.put("data", updatedTool);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/tools")
    public ResponseEntity<Map<String, Object>> deleteTool(@RequestParam int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Tool> toolOpt = toolRepository.findById((long) id);
            if (toolOpt.isPresent()) {
                toolRepository.delete(toolOpt.get());
                response.put("success", true);
                response.put("message", "工具删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "工具不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/tools/batch")
    public ResponseEntity<Map<String, Object>> batchImportTools(@RequestBody List<Tool> tools) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Tool> savedTools = toolRepository.saveAll(tools);
            response.put("success", true);
            response.put("message", "工具批量导入成功");
            response.put("data", savedTools);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 借还相关API
    // borrow和return方法的实现需要根据实际的业务逻辑来完成
}