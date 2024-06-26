package com.example.modul4.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.modul4.R
import com.example.modul4.room.PostDatabase
import com.google.android.material.imageview.ShapeableImageView

class PostAdapterRoom(private var postList: List<PostDatabase>) :
    RecyclerView.Adapter<PostAdapterRoom.PostViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PostDatabase)
        fun onMoreClicked(data: PostDatabase, position: Int)
    }
    // Menghubungkan variabel dengan komponen di layout.
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postTitle: TextView = itemView.findViewById(R.id.post_title)
        val postDesc: TextView = itemView.findViewById(R.id.post_desc)
        val postImg: ShapeableImageView = itemView.findViewById(R.id.post_img)
        val postLike: TextView = itemView.findViewById(R.id.post_like)
        val postLikeImageView: ImageView = itemView.findViewById(R.id.imageLike)

        //btn
        val btnLike: LinearLayout = itemView.findViewById(R.id.btn_like)
        val btnMore: ImageView = itemView.findViewById(R.id.btn_more)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val data = postList[position]

        holder.postTitle.text = data.name
        holder.postDesc.text = data.description.shorten(500)
        holder.postLike.text = data.like.toString()

        val uri = Uri.fromFile(data.image)
        holder.postImg.setImageURI(uri)
        var isLiked = false // variabel untuk menandai apakah tombol "like" sudah ditekan atau belum

        holder.btnLike.setOnClickListener {
            if (!isLiked) {
                data.like += 1
                holder.postLike.text = data.like.toString()
                holder.postLikeImageView.setImageResource(R.drawable.busselin_suka) // mengubah sumber daya ImageView menjadi versi merah
                isLiked = true // Setelah tombol "like" ditekan, tandai bahwa sudah ditekan
            } else {
                data.like -= 1
                holder.postLike.text = data.like.toString()
                holder.postLikeImageView.setImageResource(R.drawable.hati) // mengubah sumber daya ImageView menjadi versi putih
                isLiked = false // Setelah tombol "like" ditekan kembali, tandai bahwa sudah tidak ditekan
            }
        }


        holder.btnMore.setOnClickListener { onItemClickCallback.onMoreClicked(postList[holder.absoluteAdapterPosition], holder.absoluteAdapterPosition) }
    }

    override fun getItemCount(): Int = postList.size

    fun updatePosts(posts: List<PostDatabase>) {
        postList = posts
        notifyDataSetChanged()
    }
    private fun String.shorten(maxLength: Int): String {
        return if (this.length <= maxLength) this else "${this.substring(0, maxLength)}..."
    }
}
