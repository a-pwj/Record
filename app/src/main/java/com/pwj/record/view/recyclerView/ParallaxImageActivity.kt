package com.pwj.record.view.recyclerView

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.pwj.parallaximage.ParallaxImageView
import com.pwj.parallaximage.controller.GlideImageController
import com.pwj.parallaximage.controller.LocalImageController
import com.pwj.parallaximage.controller.PicassoImageController
import com.pwj.parallaximage.controller.ResImageController
import com.pwj.record.R
import com.pwj.record.ext.showToast
import kotlinx.android.synthetic.main.activity_parallax_image.*
import java.io.File

/**
 * @Author:           pwj
 * @Date:             2020-4-21 11:33:06
 * @FileName:         DSLAnimActivity
 * @Description:      用在recyclerview的item中，它可以随着recyclerview进行视差效果的移动，进而可以在一个小区域的item中展示一个完整的图片
 */
class ParallaxImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parallax_image)

        /**请求权限**/
        prepareCall(ActivityResultContracts.RequestPermissions()) { result ->
            var grant: Boolean = true
            for ((key, value) in result) {
                if (!value) {
                    grant = false
                    break
                }
            }
            if (grant) {
                initRecyclerView()
            }
        }.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    private fun initRecyclerView() {
        showToast("copy assets/a0.jpg to you sdcard")
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = MyAdapter(recyclerView)
    }
}

class MyAdapter(private val recyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val pathPrefix = Environment.getExternalStorageDirectory().absolutePath + File.separator

    override fun getItemViewType(position: Int): Int {
        return if (position != 0 && position % 5 == 0) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_image, parent, false)
            ImageViewViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
            MyViewHolder(view)
        }
    }

    //todo 测试用
    override fun getItemCount(): Int = 25

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            5 -> {      // 资源图
                (holder as ImageViewViewHolder).parallaxImageView.apply {
                    bindRecyclerView(recyclerView)
                    setController(ResImageController(context, R.mipmap.girl))
                }
                holder.tvTitle.text = "加载资源图:R.mipmap.girl"
            }
            10 -> {     // 本地图
                val imagePath = pathPrefix + "a0.jpg";
                (holder as ImageViewViewHolder).parallaxImageView.apply {
                    bindRecyclerView(recyclerView)
                    setController(LocalImageController(imagePath))
                }
                holder.tvTitle.text = "加载本地图: /sdcard/a0.jpg"
            }
            15 -> {     // Glide加载
                val imageUrl = "https://ae01.alicdn.com/kf/Ufc3308bab50840f3a64d44a89cd1dc4fN.jpg"
                (holder as ImageViewViewHolder).parallaxImageView.apply {
                    bindRecyclerView(recyclerView)
                    setController(GlideImageController(context, imageUrl))
                }
                holder.tvTitle.text = "Glide加载网络图: $imageUrl"
            }
            20 -> {     // picasso加载
                val imageUrl = "https://ae01.alicdn.com/kf/U724ab429b1454ee68d00f7fdeb630d929.jpg"
                (holder as ImageViewViewHolder).parallaxImageView.apply {
                    bindRecyclerView(recyclerView)
                    setController(PicassoImageController(context, imageUrl))
                }
                holder.tvTitle.text = "Picasso加载网络图: $imageUrl"
            }
            else -> {
                (holder as MyViewHolder).tvTitle.text = "position:$position"
            }
        }

    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById<TextView>(R.id.tvTitle)
    }

    class ImageViewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parallaxImageView = view.findViewById<ParallaxImageView>(R.id.parallaxImageView)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
    }
}
