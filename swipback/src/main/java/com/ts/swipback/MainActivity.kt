package com.ts.swipback

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ts.uidemo.toActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseSlideCloseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start.setOnClickListener{
            toActivity<SecondActivity>(this)
        }

        MMKVUtils.encode("bool", true)
        println("bool = " + MMKVUtils.decode("bool",Boolean))

        MMKVUtils.encode("int", Integer.MIN_VALUE)
        println("int: " + MMKVUtils.decode("int",Int))

        MMKVUtils.encode("long", java.lang.Long.MAX_VALUE)
        println("long: " + MMKVUtils.decode("long",Long))

        MMKVUtils.encode("float", -3.14f)
        println("float: " + MMKVUtils.decode("float",Float))

        MMKVUtils.encode("double", java.lang.Double.MIN_VALUE)
        println("double: " + MMKVUtils.decode("double",Double))

        MMKVUtils.encode("string", "Hello from MMKVUtils")
        println("string: " + MMKVUtils.decode("string", String))

//        val bytes = byteArrayOf('m'.toByte(), 'm'.toByte(), 'k'.toByte(), 'v'.toByte())
//        MMKVUtils.encode("bytes", bytes)
//        println("bytes: " + String(MMKVUtils.decode("bytes", ByteArray)))
//
//        println("allKeys: " + Arrays.toString(MMKVUtils.allKeys()))
//        println("count = " + MMKVUtils.count() + ", totalSize = " + MMKVUtils.totalSize())
//        println("containsKey[string]: " + MMKVUtils.containsKey("string"))
    }
}