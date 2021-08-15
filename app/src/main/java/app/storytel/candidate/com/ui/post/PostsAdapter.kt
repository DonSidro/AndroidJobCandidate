package app.storytel.candidate.com.ui.post

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.R
import app.storytel.candidate.com.databinding.PostItemBinding
import app.storytel.candidate.com.model.Photo
import app.storytel.candidate.com.model.Post
import app.storytel.candidate.com.model.PostAndImages
import com.squareup.picasso.Picasso

class PostsAdapter(private val listener: ItemListener) :
    ListAdapter<Post, PostsAdapter.PostsViewHolder>(PostComparator()) {

    interface ItemListener {
        fun onClickedPosts(postID: Int)
    }

    private val itemsPost = ArrayList<Post>()
    private val itemsPhoto = ArrayList<Photo>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: PostAndImages) {
        this.itemsPost.clear()
        this.itemsPhoto.clear()
        this.itemsPost.addAll(items.posts)
        this.itemsPhoto.addAll(items.photos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding: PostItemBinding =
            PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostsViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = itemsPost.size

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) =
        holder.bind(itemsPost[position], itemsPhoto[position])


    class PostComparator : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }

    class PostsViewHolder(

        private val itemBinding: PostItemBinding,
        private val listener: ItemListener
    ) : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {
        private val TAG = "PostsAdapter"

        private lateinit var post: Post
        private lateinit var photo: Photo

        init {
            itemBinding.root.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n", "CheckResult")
        fun bind(postItem: Post, photoItem: Photo) {
            post = postItem
            photo = photoItem
            Log.d(TAG, "bind: " + photoItem.thumbnailUrl)
            itemBinding.apply {
                Picasso.get()
                    .load(photoItem.thumbnailUrl)
                    .placeholder(R.color.colorAccent)
                    .into(postItemImageview)
                postItemTitleTextview.text = post.title

                detailsItemBodyTextview.text = post.body
            }


        }

        override fun onClick(v: View?) {
            listener.onClickedPosts(post.id)
        }
    }
}


