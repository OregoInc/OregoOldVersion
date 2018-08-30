package com.example.anton.oregov1.adapter.oregoGalleryAdapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.anton.oregov1.R
import com.example.anton.oregov1.activity.MainActivity
import com.example.anton.oregov1.activity.face3dActivity.model3D.view.ModelActivity
import com.example.anton.oregov1.activity.oregoPhotoManagement.OregoPhotoManager
import com.example.anton.oregov1.fragment.OregoGalleryFragment

//@ADDED:
class OregoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
        , View.OnLongClickListener, View.OnTouchListener {

    var photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)

    //@ADDED:
    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
        itemView.setOnTouchListener(this)
    }

    override fun onClick(view: View) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            OregoPhotoManager.getSpacePhotos()[position]
            val intent = Intent(OregoGalleryFragment.getTHIS()!!.context, ModelActivity::class.java)
            val b = Bundle()
            b.putInt("countModel", position)
            b.putString("model", "null")
            intent.putExtras(b)
            OregoGalleryFragment.getTHIS()!!.startActivity(intent)
        }
    }

    //@ADDED:
    @SuppressLint("PrivateResource")
    override fun onLongClick(v: View?): Boolean {
        val position = this.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val currentOregoPhoto = MainActivity.THIS
                    .findViewById<ImageView>(R.id.current_orego_photo)
            currentOregoPhoto.visibility = View.VISIBLE
            currentOregoPhoto.setImageDrawable(this.photoImageView.drawable)
            currentOregoPhoto.startAnimation(AnimationUtils.loadAnimation(MainActivity.THIS
                    , R.anim.abc_grow_fade_in_from_bottom))
        }
        return true
    }

    //@ADDED:
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("PrivateResource", "ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val action = event?.action
        val cond = action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
        if (cond){
            val currentPhoto = MainActivity.THIS.findViewById<ImageView>(R.id.current_orego_photo)
            currentPhoto.visibility = View.INVISIBLE
            currentPhoto.startAnimation(AnimationUtils.loadAnimation(MainActivity.THIS
                    , R.anim.abc_shrink_fade_out_from_bottom))
        }
        return event?.action == MotionEvent.ACTION_BUTTON_RELEASE
    }
}