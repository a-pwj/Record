package com.pwj.record.ui.tools.webp

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.*
import com.pwj.record.R
import com.pwj.record.utils.GlideApp
import java.util.*


class WebpActivity : AppCompatActivity(R.layout.activity_webp) {


    private val SIMPLE_WEBP = arrayOf(
        "http://www.gstatic.com/webp/gallery/1.webp",
        "http://www.gstatic.com/webp/gallery/2.webp",
        "http://www.gstatic.com/webp/gallery/3.webp",
        "http://www.gstatic.com/webp/gallery/4.webp",
        "http://www.gstatic.com/webp/gallery/5.webp"
    )
    private val ALPHA_WEBP = arrayOf(
        "https://www.gstatic.com/webp/gallery3/1_webp_ll.webp",
        "https://www.gstatic.com/webp/gallery3/2_webp_ll.webp",
        "https://www.gstatic.com/webp/gallery3/3_webp_ll.webp",
        "https://www.gstatic.com/webp/gallery3/4_webp_ll.webp",
        "https://www.gstatic.com/webp/gallery3/5_webp_ll.webp",
        "https://www.gstatic.com/webp/gallery3/1_webp_a.webp",
        "https://www.gstatic.com/webp/gallery3/2_webp_a.webp",
        "https://www.gstatic.com/webp/gallery3/3_webp_a.webp",
        "https://www.gstatic.com/webp/gallery3/4_webp_a.webp",
        "https://www.gstatic.com/webp/gallery3/5_webp_a.webp"
    )
    private val ANIM_WEBP = arrayOf(
        //"https://raw.githubusercontent.com/1290846731/RecordMySelf/master/chect.webp",
        "https://www.gstatic.com/webp/animated/1.webp",
        "https://qidian.qpic.cn/qidian_common/349573/a36f7d7d8a5e15e1cf3c32d05109467a/0",
        "https://mathiasbynens.be/demo/animated-webp-supported.webp",
        "https://isparta.github.io/compare-webp/image/gif_webp/webp/2.webp",
        "http://osscdn.ixingtu.com/musi_file/20181108/a20540641eb7de9a8bf186261a8ccf57.webp"
    )

    private val ANIM_GIF = arrayOf(
        "https://78.media.tumblr.com/a0c1be3183449f0d207a022c28f4bbf7/tumblr_p1p2cduAiA1wmghc4o1_500.gif",
        "https://78.media.tumblr.com/31ff4ea771940d2403323c1416b81064/tumblr_p1ymv2Xghn1qbt8b8o2_500.gif",
        "https://78.media.tumblr.com/45c7b305f0dbdb9a3c941be1d86aceca/tumblr_p202yd8Jz11uashjdo3_500.gif",
        "https://78.media.tumblr.com/167e9c5a0534d2718853a2e3985d64e2/tumblr_p1yth5CHXk1srs2u0o1_500.gif",
        "https://78.media.tumblr.com/e7548bfe04a9fdadcac440a5802fb570/tumblr_p1zj4dyrxN1u4mwxfo1_500.gif"
    )

    private var mTextView: TextView? = null
    private var mRecyclerView: RecyclerView? = null
    private var mWebpAdapter: ImageAdapter? = null

    private var mBitmapTrans: Transformation<Bitmap>? = null

    private var mSpinner: Spinner? = null
    private var mActionMenu: Menu? = null

    private var mImageType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val img  = findViewById<ImageView>(R.id.imgTest) as ImageView
        GlideApp.with(this).load(R.drawable.image_loading).into(img)

        mTextView = findViewById<TextView>(R.id.webp_image_type) as TextView
        mRecyclerView = findViewById<RecyclerView>(R.id.webp_recycler_view) as RecyclerView
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)

        mWebpAdapter = ImageAdapter(this, getAnimatedWebpUrls())
        mRecyclerView!!.adapter = mWebpAdapter

        mSpinner = findViewById<View>(R.id.trans_selector) as Spinner
        mSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mBitmapTrans = when (position) {
                    0 -> null
                    1 -> CenterCrop()
                    2 -> CircleCrop()
                    3 -> RoundedCorners(24)
                    4 -> CenterInside()
                    5 -> FitCenter()
                    else -> null
                }
                refreshImageData(mImageType)
            }

            override  fun onNothingSelected(parent: AdapterView<*>?) {
                mBitmapTrans = null
                refreshImageData(mImageType)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        mActionMenu = menu
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        handleMenuItemCheck(item)
        when (id) {
            R.id.static_webp_action -> {
                mImageType = 1
            }
            R.id.alpha_webp_action -> {
                mImageType = 2
            }
            R.id.animate_webp_action -> {
                mImageType = 0
            }
            R.id.animate_gif_action -> {
                mImageType = 3
            }
        }
        refreshImageData(mImageType)
        return true
    }

    private fun getAnimatedWebpUrls(): List<String>? {
        val webpUrls: MutableList<String> = ANIM_WEBP.toMutableList()
        val resUrl = "android.resource://" + packageName + "/" + R.drawable.broken
        webpUrls.add(resUrl)
        return webpUrls
    }

    private fun refreshImageData(imageType: Int) {
        mWebpAdapter?.setBitmapTransformation(mBitmapTrans)
        when (imageType) {
            0 -> {
                // Animated Webp
                mTextView!!.text = "animated webp"
                mWebpAdapter?.updateData(getAnimatedWebpUrls())
            }
            1 -> {
                // Static lossy webp
                mTextView!!.text = "static lossy webp"
                mWebpAdapter?.updateData(SIMPLE_WEBP.toList())
            }
            2 -> {
                // Static lossless webp
                mTextView!!.text = "static lossless (with alpha) webp"
                mWebpAdapter?.updateData(ALPHA_WEBP.toList())
            }
            3 -> {
                // Gif
                mTextView!!.text = "animated gif"
                mWebpAdapter?.updateData(ANIM_GIF.toList())
            }
            else -> {
            }
        }
    }

    private fun handleMenuItemCheck(menuItem: MenuItem) {
        if (mActionMenu == null) {
            menuItem.isChecked = true
            return
        }
        for (i in 0 until mActionMenu!!.size()) {
            val item: MenuItem = mActionMenu!!.getItem(i)
            item.isChecked = item.itemId == menuItem.itemId
        }
    }
}