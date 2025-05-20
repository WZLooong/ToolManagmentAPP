package com.aircraft.toolmanagment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var userManagement: UserManagement
    private lateinit var identifierEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login);

        userManagement = UserManagement(this);
        identifierEditText = findViewById(R.id.et_username_login);
        passwordEditText = findViewById(R.id.et_password_login);
        loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener {
            // 输入验证
            if (identifierEditText.text.toString().isBlank() || passwordEditText.text.toString().isBlank()) {
                Toast.makeText(this@LoginActivity, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            val identifier = identifierEditText.text.toString();
            val password = passwordEditText.text.toString();

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // 假设 userManagement 有检查用户是否注册的方法
val isRegistered = userManagement.checkUserRegistered(identifier, password);
if (isRegistered) {
    val isLoggedIn = userManagement.login(identifier, password);
} else {
    runOnUiThread {
        Toast.makeText(this@LoginActivity, "用户未注册，请先注册", Toast.LENGTH_SHORT).show();
    }
    return@launch
}
                    runOnUiThread {
                        if (::isLoggedIn.isInitialized && isLoggedIn) {
                            Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show();
                            // 跳转到主界面
                            val intent = Intent(this@LoginActivity, MainActivity::class.java);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this@LoginActivity, "登录失败，请检查用户名和密码", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "登录时发生错误: ${e.message}", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}