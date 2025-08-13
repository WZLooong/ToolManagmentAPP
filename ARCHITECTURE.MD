# ToolManagementAPP 架构说明

## 概述

本文档描述了ToolManagementAPP项目的架构设计和组织结构。项目采用分层架构，遵循MVVM设计模式，以提高代码的可维护性、可测试性和可扩展性。

## 包结构

```
com.aircraft.toolmanagment
├── ui/                     # UI层
│   ├── activity/           # Activity类
│   ├── fragment/           # Fragment类
│   ├── adapter/            # RecyclerView适配器等
│   └── custom/             # 自定义UI组件
├── data/                   # 数据层
│   ├── local/              # 本地数据相关
│   │   ├── dao/            # DAO接口
│   │   ├── entity/         # Room实体类
│   │   └── database/       # 数据库相关
│   ├── remote/             # 远程数据相关
│   │   ├── api/            # API接口和网络相关类
│   │   ├── dto/            # 数据传输对象
│   │   └── model/          # 网络数据模型
│   ├── repository/         # 数据仓库
│   └── di/                 # 依赖注入模块
├── domain/                 # 业务逻辑层
│   ├── model/              # 领域模型
│   ├── usecase/            # 业务用例
│   └── viewmodel/          # ViewModel类
└── util/                   # 工具类
```

## 架构层次

### 1. UI层 (View Layer)
- **BaseActivity**: 所有Activity的基类，提供通用功能
- **具体Activity**: 继承BaseActivity，负责UI展示和用户交互
- **Adapter**: RecyclerView适配器，负责列表数据展示
- **Fragment**: UI片段（将来添加）

### 2. 数据层 (Data Layer)
- **本地数据**: 使用Room数据库进行本地数据持久化
- **远程数据**: 通过Retrofit与后端API通信
- **Repository**: 数据仓库，整合本地和远程数据源，提供统一的数据访问接口
- **依赖注入**: 使用Hilt管理依赖关系

### 3. 业务逻辑层 (Domain Layer)
- **ViewModel**: 处理UI相关的业务逻辑，暴露状态给View层
- **UseCase**: 独立的业务用例
- **Model**: 领域模型

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

1. **Application类**: [ToolManagementApplication](file://i:\AndroidStudioProject\ToolManagmentAPP\app\src\main\java\com\aircraft\toolmanagment\ToolManagementApplication.kt) 使用@HiltAndroidApp注解
2. **模块**: [RepositoryModule](file://i:\AndroidStudioProject\ToolManagmentAPP\app\src\main\java\com\aircraft\toolmanagment\data\di\RepositoryModule.kt) 提供Repository依赖
3. **注入**: ViewModel使用@HiltViewModel注解，Activity使用@AndroidEntryPoint注解

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

## 未来改进方向

1. 使用Navigation组件管理Fragment导航
2. 实现数据缓存策略
3. 添加数据同步机制
4. 实现离线支持
5. 增加更多的单元测试和UI测试
6. 优化UI/UX设计