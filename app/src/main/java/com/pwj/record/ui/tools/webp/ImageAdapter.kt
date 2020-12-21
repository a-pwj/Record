package com.pwj.record.ui.tools.webp

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation
import com.bumptech.glide.load.Transformation
import com.pwj.record.R
import com.pwj.record.utils.GlideApp

/**
 * @Author:          pwj
 * @Date:            2020/7/3 11:36
 * @FileName:        ImageAdapter
 * @Description:     description
 */
class ImageAdapter(val mContext: Context, urls: List<String>?) : RecyclerView.Adapter<ImageAdapter.ImageHolder?>() {
    private val mImageUrls: MutableList<String> = ArrayList()
    private var mBitmapTrans: Transformation<Bitmap>? = null

    fun setBitmapTransformation(bitmapTrans: Transformation<Bitmap>?) {
        mBitmapTrans = bitmapTrans
    }

    fun updateData(urls: List<String>?) {
        mImageUrls.clear()
        mImageUrls.addAll(urls!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val itemView: View = LayoutInflater.from(mContext).inflate(R.layout.webp_image_item, parent, false)
        return ImageHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val size = mImageUrls.size.toLong()
        if (position < 0 || position >= size) {
            return
        }
        val url = mImageUrls[position]
        if (holder.imageView is AspectRatioImageView) {
            val view: AspectRatioImageView = holder.imageView as AspectRatioImageView
            view.setAspectRatio(720.0f / 1268.0f)
        }
//        if (holder.imageView is SimpleDraweeView) {
//            val view: SimpleDraweeView = holder.imageView as SimpleDraweeView
//            loadImageWithFresco(view, url)
//        } else
        if (mBitmapTrans != null) {
            loadImageWithTransformation(holder.imageView, url)
        } else {
            loadImage(holder.imageView, url)
        }
        holder.textView.text = url
    }

    override fun getItemCount(): Int {
        return mImageUrls.size
    }

//    private fun loadImageWithFresco(draweeView: SimpleDraweeView, url: String) {
//        val imageRequest: ImageRequest = ImageRequest.fromUri(url)
//        val controller: DraweeController = Fresco.newDraweeControllerBuilder()
//            .setImageRequest(imageRequest)
//            .setAutoPlayAnimations(true)
//            .setOldController(draweeView.getController())
//            .build()
//        draweeView.setController(controller)
//    }

    private fun loadImage(imageView: ImageView, url: String) {
        GlideApp.with(mContext) //.asBitmap()
            .load(url)
//            .placeholder(R.drawable.image_loading)
//            .error(R.drawable.image_error) //.set(WebpFrameLoader.FRAME_CACHE_STRATEGY, WebpFrameCacheStrategy.AUTO)
            .into(imageView)
    }

    private fun loadImageWithTransformation(imageView: ImageView, url: String) {
        GlideApp.with(mContext) //.asBitmap()
            .load(url)
//            .placeholder(R.drawable.image_loading)
//            .error(R.drawable.image_error)
            .optionalTransform(mBitmapTrans!!)
            .optionalTransform(WebpDrawable::class.java, WebpDrawableTransformation(mBitmapTrans)) //.set(WebpFrameLoader.FRAME_CACHE_STRATEGY, WebpFrameCacheStrategy.AUTO)
            .into(imageView)
    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById<ImageView>(R.id.webp_image)
        val textView: TextView = itemView.findViewById<TextView>(R.id.webp_text)

    }


    init {
        mImageUrls.addAll(urls!!)
    }
}
