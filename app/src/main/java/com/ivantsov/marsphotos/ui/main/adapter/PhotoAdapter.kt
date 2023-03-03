package com.ivantsov.marsphotos.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.ivantsov.marsphotos.R
import com.ivantsov.marsphotos.data.model.PhotoItem
import com.ivantsov.marsphotos.databinding.PhotoItemBinding
import com.ivantsov.marsphotos.util.glide.GlideImageLoader


class PhotoAdapter(private val photoList: MutableList<PhotoItem> = ArrayList()) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = PhotoItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int = photoList.size

    fun addData(list: List<PhotoItem>) {
        photoList.addAll(list)
    }

    inner class PhotoViewHolder(val binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photoItem: PhotoItem) {

            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.ic_baseline_error_24)
                .priority(Priority.HIGH)

            GlideImageLoader(
                binding.imageViewPhoto,
                binding.progressBar
            ).load(photoItem.imgSrc, options)
        }
    }
}