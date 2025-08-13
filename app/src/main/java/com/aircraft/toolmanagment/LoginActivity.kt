<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#FFFFFF"/>
    <corners android:radius="8dp"/>
    <stroke
        android:width="1dp"
        android:color="#CCCCCC"/>
    <padding
        android:left="8dp"
        android:top="8dp"
        android:right="8dp"
        android:bottom="8dp"/>
</shape>
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#F5F5F5"
    android:padding="24dp"
    tools:context=".LoginActivity">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:gravity="center_horizontal">

        <!-- Logo and title -->
        <View
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_marginTop="60dp"
            android:layout_marginBottom="16dp"
            android:background="@drawable/ic_launcher_background" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="工具管理系统"
            android:textSize="24sp"
            android:textStyle="bold"
            android:layout_marginBottom="32dp"/>

        <!-- Form area -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:gravity="center_horizontal"
            android:padding="16dp">

            <!-- Username/Employee ID field -->
            <EditText
                android:id="@+id/et_email_login"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="16dp"
                android:hint="用户名或员工ID"
                android:background="@drawable/edittext_background"
                android:padding="12dp"
                android:textSize="16sp"
                android:textColor="#000000"
                android:textColorHint="#888888"
                android:layout_marginTop="8dp"
                android:layout_marginBottom="8dp"
                android:inputType="text"/>

            <!-- Password field -->
            <EditText
                android:id="@+id/et_password_login"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="密码"
                android:background="@drawable/edittext_background"
                android:padding="12dp"
                android:textSize="16sp"
                android:textColor="#000000"
                android:textColorHint="#888888"
                android:layout_marginTop="8dp"
                android:layout_marginBottom="8dp"
                android:inputType="textPassword"/>

            <!-- Login button -->
            <Button
                android:id="@+id/btn_login"
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:text="登录"
                android:textSize="16sp"
                android:textColor="#FFFFFF"
                android:layout_marginTop="24dp"
                android:layout_marginBottom="8dp"
                android:backgroundTint="#2196F3"
                android:cornerRadius="8dp"/>

            <!-- Register navigation button -->
            <Button
                android:id="@+id/btn_register_nav"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="没有账户？立即注册"
                android:textSize="16sp"
                android:textColor="#2196F3"
                android:layout_marginTop="8dp"
                android:layout_marginBottom="24dp"
                android:backgroundTint="android:color/transparent"/>
        </LinearLayout>
    </LinearLayout>
</ScrollView>
package com.aircraft.toolmanagment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.UserManagement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    private lateinit var userManagement: UserManagement
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegisterNav: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        userManagement = UserManagement(this)
        
        // 初始化视图组件
        initViews()
        
        // 设置点击事件监听器
        setupClickListeners()
    }

    private fun initViews() {
        etEmail = findViewById(R.id.et_email_login)
        etPassword = findViewById(R.id.et_password_login)
        btnLogin = findViewById(R.id.btn_login)
        btnRegisterNav = findViewById(R.id.btn_register_nav)
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val identifier = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            login(identifier, password)
        }

        btnRegisterNav.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(identifier: String, password: String) {
        if (identifier.isBlank() || password.isBlank()) {
            if (!isFinishing && !isDestroyed) {
                Toast.makeText(this, "请输入用户名/员工ID和密码", Toast.LENGTH_SHORT).show()
            }
            return
        }

        // 使用lifecycleScope来自动管理协程生命周期
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val user = userManagement.loginUser(identifier, password)
                // 检查Activity是否仍然处于活跃状态
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    runOnUiThread {
                        // 再次检查Activity是否仍然处于活跃状态
                        if (!isFinishing && !isDestroyed) {
                            if (user != null) {
                                Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                                // Navigate to main activity
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "登录失败，请检查用户名和密码", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // 检查Activity是否仍然处于活跃状态
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    runOnUiThread {
                        // 再次检查Activity是否仍然处于活跃状态
                        if (!isFinishing && !isDestroyed) {
                            val errorMessage = e.message ?: "未知错误"
                            Log.e("LoginActivity", "登录异常", e)
                            Toast.makeText(this@LoginActivity, "登录异常: $errorMessage", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
