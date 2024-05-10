package hse.course.socialnetworkthoughtsandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.internal.managers.ViewComponentManager
import hse.course.socialnetworkthoughtsandroidapp.R
import hse.course.socialnetworkthoughtsandroidapp.model.Post
import hse.course.socialnetworkthoughtsandroidapp.model.Profile
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.CommentsFragment
import java.util.UUID

class PostsAdapter(
    private val posts: MutableList<Post>,
    private val profile: Profile,
    private val likePost: (id: UUID) -> Unit,
    private val unlikePost: (id: UUID) -> Unit,
) :

    RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    class PostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView
        val postedTime: TextView
        val theme: TextView
        val content: TextView
        val likes: TextView
        val comments: TextView
        val reposts: TextView
        val views: TextView
        val likeButton: ImageButton
        val commentButton: ImageButton


        init {
            nickname = view.findViewById(R.id.nickname_text_view)
            postedTime = view.findViewById(R.id.posted_time_textview)
            theme = view.findViewById(R.id.theme)
            content = view.findViewById(R.id.content)
            likes = view.findViewById(R.id.like_textview)
            comments = view.findViewById(R.id.comment_textview)
            reposts = view.findViewById(R.id.repost_textview)
            views = view.findViewById(R.id.views_textview)
            likeButton = view.findViewById(R.id.like_button)
            commentButton = view.findViewById(R.id.comment_button)
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): PostsViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_card, viewGroup, false)

        return PostsViewHolder(view)
    }

    override fun onBindViewHolder(
        postsViewHolder: PostsViewHolder,
        position: Int
    ) {
        postsViewHolder.nickname.text = profile.nickname
        postsViewHolder.theme.text = posts[position].theme
        postsViewHolder.content.text = posts[position].content
        postsViewHolder.likes.text = posts[position].likes.toString()
        postsViewHolder.comments.text = posts[position].comments.toString()
        postsViewHolder.reposts.text = posts[position].reposts.toString()
        postsViewHolder.views.text = posts[position].reposts.toString()

        if (posts[position].isLiked) {
            postsViewHolder.likeButton.setImageResource(R.drawable.heart)
        } else {
            postsViewHolder.likeButton.setImageResource(R.drawable.heart_outline)
        }

        if (posts[position].createdAt.equals(posts[position].editedAt)) {
            postsViewHolder.postedTime.text = posts[position].createdAt.toString()
        } else {
            postsViewHolder.postedTime.text = posts[position].editedAt.toString()
        }

        postsViewHolder.likeButton.setOnClickListener {
            if (posts[position].isLiked) {
                unlikePost(posts[position].id)
                posts[position].isLiked = false
                posts[position].likes--
                postsViewHolder.likeButton.setImageResource(R.drawable.heart_outline)
            } else {
                likePost(posts[position].id)
                posts[position].isLiked = true
                posts[position].likes++
                postsViewHolder.likeButton.setImageResource(R.drawable.heart)
            }
            postsViewHolder.likes.text = posts[position].likes.toString()
        }

        postsViewHolder.commentButton.setOnClickListener {
            val activity = activityContext(postsViewHolder) as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, CommentsFragment(posts[position].id))
                .addToBackStack(null)
                .commit()
        }
    }

    private fun activityContext(postsViewHolder: PostsViewHolder): Context? {
        val context = postsViewHolder.itemView.context
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context
    }

    override fun getItemCount() = posts.size
}