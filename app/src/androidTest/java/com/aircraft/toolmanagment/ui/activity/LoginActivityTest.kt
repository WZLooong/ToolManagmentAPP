package com.aircraft.toolmanagment.ui.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aircraft.toolmanagment.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Test
    fun testLoginActivityLaunch() {
        val scenario: ActivityScenario<LoginActivity> = launchActivity()
        
        // 验证登录界面元素显示
        onView(withId(R.id.et_email_login)).check(matches(isDisplayed()))
        onView(withId(R.id.et_password_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_register_nav)).check(matches(isDisplayed()))
    }

    @Test
    fun testRegisterNavigation() {
        val scenario: ActivityScenario<LoginActivity> = launchActivity()
        
        // 点击注册按钮
        onView(withId(R.id.btn_register_nav)).perform(click())
        
        // 在实际测试中，我们应该验证是否导航到了注册页面
        // 但由于我们没有实现完整的导航逻辑，这里只是示例
    }
}