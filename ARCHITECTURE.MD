# ToolManagementAPP 架构说明

## 概述

本文档描述了ToolManagementAPP项目的架构设计和组织结构。项目采用分层架构，遵循MVVM设计模式，以提高代码的可维护性、可测试性和可扩展性。

## 包结构

```
com.aircraft.toolmanagment
├── config/                 # 配置相关
│   └── Config.kt           # 应用配置常量
├── ui/                     # UI层
│   ├── activity/           # Activity类
│   └── adapter/            # RecyclerView适配器等
├── data/                   # 数据层
│   ├── di/                 # 依赖注入模块
│   ├── entity/             # 实体类和DTO
│   ├── remote/             # 远程数据相关
│   │   └── api/            # API接口和网络相关类
│   ├── repository/         # 数据仓库
│   ├── AppDatabase.kt      # 数据库实例
│   ├── DAOs                # 各种数据访问对象
│   └── ...                 # 其他数据相关
├── domain/                 # 业务逻辑层
│   ├── BaseViewModel.kt    # ViewModel基类
│   ├── ViewModels          # 各种ViewModel实现
├── util/                   # 工具类
├── *.kt                    # 顶层业务管理类
```

## 架构层次

### 1. UI层 (View Layer)
- **BaseActivity**: 所有Activity的基类，提供通用功能
- **具体Activity**: 继承BaseActivity，负责UI展示和用户交互
- **Adapter**: RecyclerView适配器，负责列表数据展示

### 2. 数据层 (Data Layer)
- **本地数据**: 使用Room数据库进行本地数据持久化
- **远程数据**: 通过Retrofit与后端API通信
- **Repository**: 数据仓库，整合本地和远程数据源，提供统一的数据访问接口
- **依赖注入**: 使用Hilt管理依赖关系
- **网络模块**: 统一的网络请求配置和API接口定义

### 3. 业务逻辑层 (Domain Layer)
- **ViewModel**: 处理UI相关的业务逻辑，暴露状态给View层
- **顶层业务管理类**: 封装特定业务逻辑的管理类

### 4. 配置层
- **Config**: 应用配置常量，如API基础URL等

### 5. 工具层
- **ViewState**: UI状态封装类
- **其他工具类**: 辅助工具和扩展函数

## 设计模式

### MVVM (Model-View-ViewModel)
- **Model**: Repository层（本地和远程数据源）
- **View**: Activity/Fragment（只负责UI展示和用户交互）
- **ViewModel**: 处理UI逻辑，暴露状态给View

### 依赖注入 (Dependency Injection)
- 使用Hilt进行依赖注入，提高代码的可测试性和可维护性

### 单例模式
- Repository使用单例模式确保全局唯一实例

### 密封类
- 使用密封类定义网络请求结果和UI状态

## 关键类说明

### BaseActivity
所有Activity的基类，提供以下功能：
- 统一的布局设置
- 视图初始化和数据初始化的抽象方法
- 安全的Toast显示方法
- Activity活跃状态检查

### Repository
数据访问的统一入口，包含：
- UserDao、ToolDao、BorrowReturnDao等本地数据访问对象
- ApiService远程数据访问接口
- UserRepository、ToolRepository、BorrowReturnRepository等具体业务仓库

### NetworkModule
网络模块，提供统一的网络请求配置：
- 配置OkHttpClient（超时、拦截器等）
- 提供Retrofit实例
- 创建ApiService实例

### ApiService
定义与后端通信的API接口：
- 用户认证相关接口
- 工具管理相关接口
- 借还记录相关接口

### NetworkResult
网络请求结果封装类，包含：
- Loading: 加载状态
- Success: 成功状态，携带数据
- Error: 错误状态，携带错误码和消息
- NetworkError: 网络错误状态

### ViewState
UI状态封装类，用于ViewModel向View层暴露状态：
- Loading: 加载状态
- Success: 成功状态，携带数据
- Error: 错误状态，携带错误消息

### 顶层业务管理类
- UserManagement: 用户相关业务逻辑封装
- ToolManagement: 工具相关业务逻辑封装
- BorrowReturnManagement: 借还相关业务逻辑封装

## 数据流向

```
UI (Activity) -> ViewModel -> Repository -> DAO/API -> Repository -> ViewModel -> UI (Activity)
```

1. UI层通过ViewModel发起数据请求
2. ViewModel调用Repository处理业务逻辑
3. Repository根据需要访问本地DAO或远程API
4. 数据处理完成后，Repository将结果返回给ViewModel
5. ViewModel将处理后的状态暴露给UI层
6. UI层根据状态更新界面

## 依赖注入

项目使用Hilt进行依赖注入：

1. **Application类**: [ToolManagementApplication](file://h:\APP\ToolManagmentAPP\app\src\main\java\com\aircraft\toolmanagment\ToolManagementApplication.kt) 使用@HiltAndroidApp注解
2. **模块**: [RepositoryModule](file://h:\APP\ToolManagmentAPP\app\src\main\java\com\aircraft\toolmanagment\data\di\RepositoryModule.kt) 提供Repository依赖
3. **注入**: Activity使用@AndroidEntryPoint注解

*注意：目前ViewModel中尚未完全使用Hilt依赖注入，而是手动获取Repository实例，这是需要改进的地方。*

## 最佳实践

1. **依赖注入**: 使用Hilt管理组件依赖，便于测试
2. **错误处理**: 统一的网络异常处理机制
3. **生命周期管理**: 使用lifecycleScope和viewModelScope自动管理协程生命周期
4. **状态管理**: 使用LiveData管理UI状态
5. **代码复用**: 通过基类和工具类减少重复代码
6. **可测试性**: 分层设计便于单元测试和集成测试

## 测试策略

1. **单元测试**: 使用JUnit和Mockito测试业务逻辑
2. **UI测试**: 使用Espresso测试用户界面交互
3. **集成测试**: 验证各组件之间的协作
4. **网络测试**: 使用专门的测试类验证网络连接和API接口

## 未来改进方向

1. 使用Navigation组件管理Fragment导航
2. 实现数据缓存策略
3. 添加数据同步机制
4. 实现离线支持
5. 增加更多的单元测试和UI测试
6. 优化UI/UX设计
7. 完善Hilt依赖注入的使用，确保ViewModel也通过依赖注入获取Repository
8. 重构顶层业务管理类，明确其在架构中的作用或将其整合到合适的层中