package com.pwj.record.ui

import android.os.Bundle
import android.util.SparseArray
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pwj.record.R
import com.pwj.record.ui.fragment.Fragment1
import com.pwj.record.ui.fragment.Fragment2
import com.pwj.record.ui.fragment.Fragment3
import kotlinx.android.synthetic.main.activity_view_pager.*

/**
 * @Author:           pwj
 * @Date:             2020-4-16 11:10:44
 * @FileName:         ViewPagerActivity
 * @Description:      ViewPager 优化写法，避免重建时崩溃
 */
class ViewPagerActivity : AppCompatActivity() {

    private val fragmentList = SparseArray<Fragment>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        fragmentList.put(0,Fragment1.newInstance(0))
        fragmentList.put(1,Fragment2.newInstance(1))
        fragmentList.put(2,Fragment3.newInstance(2))
        viewpager.adapter = MyViewPagerAdapter(supportFragmentManager)
        viewpager.offscreenPageLimit = 3
    }

    inner class MyViewPagerAdapter(private val fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        /**
         * 此方法 实际为 create fragment
         */
        override fun getItem(arg0: Int): Fragment {
            return when (arg0) {
                0 -> Fragment1.newInstance(arg0)
                1 -> Fragment2.newInstance(arg0)
                else -> Fragment3.newInstance(arg0)
            }
        }

        /**
         * 用此方法获取position 的fragment
         */
        fun getRegisteredFragment(position: Int): Fragment? {
            return fragmentList.get(position)
        }

        override fun getCount(): Int = fragmentList.size()

        /**
         * 在instantiateItem方法中，我们重写的getItem方法竟然不是每次都会被调用的！
         * 它会先判断FragmentManager是否已添加了目标Fragment（findFragmentByTag），
         * 如果已经添加了的话，就会把它取出来并重新关联上，而getItem方法就不会被调用了。
         */
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position) as Fragment
            fragmentList.put(position, fragment)
            return fragment
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            fragmentList.remove(position)
            super.destroyItem(container, position, `object`)
        }
    }

    /**
     * 既然ViewPager在添加新Item时会优先查找FragmentManager中已存在的Fragment，
     * 那么我们在Activity重建后，实例Fragment时也可以像它那样，
     * 先看看FragmentManager中有没有，如果有的话就直接重用，不用new了。
     *
     * 注意：使用了 tag，但是 FragmentPagerAdapter 的makeFragmentName方法是私有的，也就是说，未来它可能会修改它内部 tag 生成的逻辑。
     */
    private fun instantiateFragment(viewPager: ViewPager, position: Int, defaultResult: Fragment): Fragment? {
        val tag = "android:switcher:" + viewPager.id + ":" + position
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        return fragment ?: defaultResult
    }
}
