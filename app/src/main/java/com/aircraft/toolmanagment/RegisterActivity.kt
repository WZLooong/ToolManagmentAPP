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

class RegisterActivity : AppCompatActivity() {
    private lateinit var userManagement: UserManagement
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var employeeIdEditText: EditText
    private lateinit var teamEditText: EditText
    private lateinit var roleEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userManagement = UserManagement(this)
        phoneEditText = findViewById(R.id.phoneEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        nameEditText = findViewById(R.id.nameEditText)
        employeeIdEditText = findViewById(R.id.employeeIdEditText)
        teamEditText = findViewById(R.id.teamEditText)
        roleEditText = findViewById(R.id.roleEditText)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            // 输入验证
            if (passwordEditText.text.toString().isBlank() || nameEditText.text.toString().isBlank() || employeeIdEditText.text.toString().isBlank()) {
                Toast.makeText(this@RegisterActivity, "密码、姓名和员工 ID 不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val phone = phoneEditText.text.toString().takeIf { it.isNotBlank() }
            val email = emailEditText.text.toString().takeIf { it.isNotBlank() }
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()
            val employeeId = employeeIdEditText.text.toString()
            val team = teamEditText.text.toString()
            val role = roleEditText.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val isRegistered = userManagement.register(phone, email, password, name, employeeId, team, role)
                    runOnUiThread {
                        if (isRegistered) {
                            Toast.makeText(this@RegisterActivity, "注册成功", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "用户已存在，注册失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "注册时发生错误: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}