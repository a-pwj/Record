package com.pwj.record.ui.file

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pwj.record.R

class FileListActivity : AppCompatActivity() {

    private var fragmentCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_list)
        if (supportFragmentManager.findFragmentById(R.id.fragment_container_view) == null) {
            val fileListFragment = FileListFragment()
            this.showFragment(fileListFragment)
        }
    }

    fun showFragment(fragment: Fragment) {
        ++fragmentCount
        supportFragmentManager.beginTransaction()
            .addToBackStack(this.fragmentCount.toString() + "")
            .replace(R.id.fragment_container_view, fragment)
            .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        if (--fragmentCount == 0) {
            val fm = supportFragmentManager
            for (i in 0 until fm.backStackEntryCount) {
                val entry = fm.getBackStackEntryAt(i)
                val fragment = fm.findFragmentByTag(entry.name)
                fragment?.onDestroy()
            }
            finish()
        } else {
            super.onBackPressed()
        }
    }

}