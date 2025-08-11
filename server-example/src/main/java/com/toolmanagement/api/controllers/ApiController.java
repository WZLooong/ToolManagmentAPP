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

            // 使用BCrypt验证密码
            System.out.println("Checking password for user: " + user.getUsername());
            boolean passwordMatches = SecurityUtils.matchesPassword(password, user.getPassword());
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

            // 创建新用户并加密密码
            User newUser = new User(
                request.getPhone(),
                request.getEmail(),
                SecurityUtils.encodePassword(request.getPassword()),
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

    // 获取工具列表
    @GetMapping("/tools")
    public ResponseEntity<Map<String, Object>> getTools(@RequestParam(required = false) String keyword) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Tool> tools;
            if (keyword != null && !keyword.isEmpty()) {
                tools = toolRepository.findByNameContainingIgnoreCase(keyword);
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

    // 添加工具
    @PostMapping("/tools")
    public ResponseEntity<Map<String, Object>> addTool(@RequestBody Tool tool) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查工具是否已存在
            Optional<Tool> existingTool = toolRepository.findByBarcodeOrName(tool.getBarcode(), tool.getName());
            if (existingTool.isPresent()) {
                response.put("success", false);
                response.put("message", "工具条码或名称已存在");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Tool savedTool = toolRepository.save(tool);
            
            response.put("success", true);
            response.put("message", "工具添加成功");
            response.put("tool", savedTool);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 借阅工具
    @PostMapping("/tools/borrow/{toolId}")
    public ResponseEntity<Map<String, Object>> borrowTool(@PathVariable Long toolId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 获取当前登录用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long userId = userDetails.getId();

            // 检查工具是否存在
            Optional<Tool> toolOptional = toolRepository.findById(toolId);
            if (!toolOptional.isPresent()) {
                response.put("success", false);
                response.put("message", "工具不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Tool tool = toolOptional.get();

            // 检查工具是否可借阅
            if (tool.getBorrowerId() != null) {
                response.put("success", false);
                response.put("message", "工具已被借阅");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // 更新工具状态
            tool.setBorrowerId(userId);
            tool.setBorrowDate(LocalDateTime.now());
            tool.setStatus("BORROWED");

            Tool updatedTool = toolRepository.save(tool);

            response.put("success", true);
            response.put("message", "工具借阅成功");
            response.put("tool", updatedTool);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 归还工具
    @PostMapping("/tools/return/{toolId}")
    public ResponseEntity<Map<String, Object>> returnTool(@PathVariable Long toolId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 获取当前登录用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long userId = userDetails.getId();

            // 检查工具是否存在
            Optional<Tool> toolOptional = toolRepository.findById(toolId);
            if (!toolOptional.isPresent()) {
                response.put("success", false);
                response.put("message", "工具不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Tool tool = toolOptional.get();

            // 检查工具是否被当前用户借阅
            if (tool.getBorrowerId() == null || !tool.getBorrowerId().equals(userId)) {
                response.put("success", false);
                response.put("message", "该工具未被您借阅");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // 更新工具状态
            tool.setReturnDate(LocalDateTime.now());
            tool.setBorrowerId(null);
            tool.setStatus("AVAILABLE");

            Tool updatedTool = toolRepository.save(tool);

            response.put("success", true);
            response.put("message", "工具归还成功");
            response.put("tool", updatedTool);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 更新工具
    @PutMapping("/tools")
    public ResponseEntity<Map<String, Object>> updateTool(@RequestBody Tool tool) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (tool.getId() == null) {
                response.put("success", false);
                response.put("message", "工具ID不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Optional<Tool> existingTool = toolRepository.findById(tool.getId());
            if (!existingTool.isPresent()) {
                response.put("success", false);
                response.put("message", "工具未找到");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Tool updatedTool = toolRepository.save(tool);
            
            response.put("success", true);
            response.put("message", "工具更新成功");
            response.put("tool", updatedTool);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 删除工具
    @DeleteMapping("/tools")
    public ResponseEntity<Map<String, Object>> deleteTool(@RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Tool> existingTool = toolRepository.findById(id);
            if (!existingTool.isPresent()) {
                response.put("success", false);
                response.put("message", "工具未找到");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            toolRepository.deleteById(id);
            
            response.put("success", true);
            response.put("message", "工具删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 批量导入工具
    @PostMapping("/tools/batch")
    public ResponseEntity<Map<String, Object>> batchImportTools(@RequestBody List<Tool> tools) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Tool> savedTools = toolRepository.saveAll(tools);
            
            response.put("success", true);
            response.put("message", "工具批量导入成功");
            response.put("tools", savedTools);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}