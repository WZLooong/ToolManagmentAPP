package com.aircraft.toolmanagment.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle

/**
 * Activity基类，提供通用功能
 */
abstract class BaseActivity : ComponentActivity() {
    protected abstract fun getLayoutRes(): Int
    protected abstract fun initViews()
    protected abstract fun initData()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        initViews()
        initData()
    }
    
    /**
     * 安全显示Toast，避免在Activity销毁后显示
     */
    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        if (!isFinishing && !isDestroyed && lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            Toast.makeText(this, message, duration).show()
        }
    }
    
    /**
     * 检查Activity是否处于活跃状态
     */
    protected fun isActivityActive(): Boolean {
        return !isFinishing && !isDestroyed && lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    }
}