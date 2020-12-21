package com.pwj.record

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

/**
 * @Author:          pwj
 * @Date:            2020/7/24 15:16
 * @FileName:        BaseFragmentActivity
 * @Description:     description
 */
open class BaseFragmentActivity : BaseActivity() {

    /**
     * 根据 tag 获取 fragment
     * 注：别在 activity 的 onCreate 方法中使用该方法，因为此时 fragment 还未完全创建好，会返回为 null
     * @param tag
     * @return
     */
   open fun findFragmentByTag(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }


    /**
     * 获取当前 activity 栈内的 fragment 个数
     * 注：别在 activity 的 onCreate 方法中使用该方法
     */
    open fun getFragmentCount(): Int {
        return supportFragmentManager.fragments.size
    }

    // 隐藏 activity 中的 FragmentManager 栈内的所有已经添加了的 fragment
    open fun hideAllFragment(transaction: FragmentTransaction) {
        val fragments = supportFragmentManager.fragments
        if (fragments != null && fragments.size > 0) {
            for (fragment in fragments) {
                // 加上不为空判断，防止出现异常（hide方法的参数不能为空，否则会报错）
                if (fragment != null) {
                    transaction.hide(fragment)
                }
            }
        }
    }

    /**
     * 获取加入了返回栈的 fragment 个数
     * @return
     */
    open fun getBackStackEntryCount(): Int {
        return supportFragmentManager.backStackEntryCount
    }

}