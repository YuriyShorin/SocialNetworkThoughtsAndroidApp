package hse.course.socialnetworkthoughtsandroidapp.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hse.course.socialnetworkthoughtsandroidapp.R
import hse.course.socialnetworkthoughtsandroidapp.model.Post
import hse.course.socialnetworkthoughtsandroidapp.model.Profile
import java.util.UUID

class CurrentProfilePostsAdapter(
    private val posts: MutableList<Post>,
    private val profile: Profile,
    private val likePost: (id: UUID) -> Unit,
    private val unlikePost: (id: UUID) -> Unit,
    private val deletePost: (id: UUID) -> Unit
) :

    RecyclerView.Adapter<CurrentProfilePostsAdapter.CurrentProfilePostsViewHolder>() {

    class CurrentProfilePostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView
        val postedTime: TextView
        val theme: TextView
        val content: TextView
        val likes: TextView
        val comments: TextView
        val reposts: TextView
        val views: TextView
        val likeButton: ImageButton
        val menuButton: ImageButton
        val view: View

        init {
            this.view = view
            nickname = view.findViewById(R.id.nickname_text_view)
            postedTime = view.findViewById(R.id.posted_time_textview)
            theme = view.findViewById(R.id.theme)
            content = view.findViewById(R.id.content)
            likes = view.findViewById(R.id.like_textview)
            comments = view.findViewById(R.id.comment_textview)
            reposts = view.findViewById(R.id.repost_textview)
            views = view.findViewById(R.id.views_textview)
            likeButton = view.findViewById(R.id.like_button)
            menuButton = view.findViewById(R.id.menu_button)
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): CurrentProfilePostsViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_card, viewGroup, false)

        return CurrentProfilePostsViewHolder(view)
    }

    override fun onBindViewHolder(
        currentProfilePostsViewHolder: CurrentProfilePostsViewHolder,
        position: Int
    ) {
        currentProfilePostsViewHolder.nickname.text = profile.nickname
        currentProfilePostsViewHolder.theme.text = posts[position].theme
        currentProfilePostsViewHolder.content.text = posts[position].content
        currentProfilePostsViewHolder.likes.text = posts[position].likes.toString()
        currentProfilePostsViewHolder.comments.text = posts[position].comments.toString()
        currentProfilePostsViewHolder.reposts.text = posts[position].reposts.toString()
        currentProfilePostsViewHolder.views.text = posts[position].reposts.toString()

        if (posts[position].createdAt.equals(posts[position].editedAt)) {
            currentProfilePostsViewHolder.postedTime.text = posts[position].createdAt.toString()
        } else {
            currentProfilePostsViewHolder.postedTime.text = posts[position].editedAt.toString()
        }

        currentProfilePostsViewHolder.likeButton.setOnClickListener {
            if (posts[position].isLiked) {
                unlikePost(posts[position].id)
                posts[position].isLiked = false
                posts[position].likes--
                currentProfilePostsViewHolder.likeButton.setImageResource(R.drawable.heart_outline)
            } else {
                likePost(posts[position].id)
                posts[position].isLiked = true
                posts[position].likes++
                currentProfilePostsViewHolder.likeButton.setImageResource(R.drawable.heart)
            }
            currentProfilePostsViewHolder.likes.text = posts[position].likes.toString()
        }

        currentProfilePostsViewHolder.menuButton.setOnClickListener {
            val popup = PopupMenu(
                currentProfilePostsViewHolder.view.context,
                currentProfilePostsViewHolder.menuButton
            )
            popup.inflate(R.menu.post_card)

            popup.setOnMenuItemClickListener { item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.remove -> {
                        deletePost(posts[position].id)
                        removeItem(position)
                    }
                }
                true
            }

            popup.show()
        }
    }

    override fun getItemCount() = posts.size

    private fun removeItem(position: Int) {
        posts.removeAt(position)
        notifyItemRemoved(position)
    }
}