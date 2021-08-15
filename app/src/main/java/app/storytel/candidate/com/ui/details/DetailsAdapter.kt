package app.storytel.candidate.com.ui.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.databinding.DetailsItemBinding
import app.storytel.candidate.com.model.Comment
import app.storytel.candidate.com.model.Post

class DetailsAdapter : ListAdapter<Post, DetailsAdapter.CommentsViewHolder>(PostComparator()) {

    private val itemsComment = ArrayList<Comment>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(comments: List<Comment>) {
        this.itemsComment.clear()
        this.itemsComment.addAll(comments)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val binding: DetailsItemBinding =
            DetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsViewHolder(binding)
    }

    override fun getItemCount(): Int = itemsComment.size

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) =
        holder.bind(itemsComment[position])


    class PostComparator : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }

    class CommentsViewHolder(
        private val itemBinding: DetailsItemBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private lateinit var comment: Comment

        @SuppressLint("SetTextI18n")
        fun bind(comment: Comment) {
            this.comment = comment
            itemBinding.apply {

                detailsItemNameTextview.text = comment.name
                detailsItemEmailTextview.text = comment.email
                detailsItemBodyTextview.text = comment.body
            }


        }

    }
}


