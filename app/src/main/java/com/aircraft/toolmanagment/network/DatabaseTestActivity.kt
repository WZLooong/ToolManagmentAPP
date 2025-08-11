package com.aircraft.toolmanagment.network

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aircraft.toolmanagment.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseTestActivity : AppCompatActivity() {
    private val TAG = "DatabaseTestActivity"
    private lateinit var databaseConnectionTest: DatabaseConnectionTest
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // 使用现有的布局文件

        databaseConnectionTest = DatabaseConnectionTest()
        resultTextView = TextView(this).apply {
            text = "数据库连接测试结果将显示在这里"
        }

        val testDatabaseButton = Button(this).apply {
            text = "测试数据库连接"
        }

        testDatabaseButton.setOnClickListener {
            testDatabaseConnection()
        }
    }

    private fun testDatabaseConnection() {
        CoroutineScope(Dispatchers.Main).launch {
            resultTextView.text = "正在测试数据库连接..."
            Log.d(TAG, "开始测试数据库连接")
            
            val isConnected = databaseConnectionTest.testDatabaseConnection()
            
            if (isConnected) {
                resultTextView.text = "数据库连接成功！"
                Toast.makeText(this@DatabaseTestActivity, "数据库连接成功", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "数据库连接成功")
            } else {
                resultTextView.text = "数据库连接失败！"
                Toast.makeText(this@DatabaseTestActivity, "数据库连接失败", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "数据库连接失败")
            }
        }
    }
}