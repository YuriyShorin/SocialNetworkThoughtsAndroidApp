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
import hse.course.socialnetworkthoughtsandroidapp.model.Feed
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.CommentsFragment
import java.util.UUID

class FeedAdapter(
    private val feed: List<Feed>,
    private val likePost: (id: UUID) -> Unit,
    private val unlikePost: (id: UUID) -> Unit) :

    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
    ): FeedViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_card, viewGroup, false)

        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(
        feedViewHolder: FeedViewHolder,
        position: Int
    ) {
        feedViewHolder.nickname.text = feed[position].profileNickname
        feedViewHolder.theme.text = feed[position].theme
        feedViewHolder.content.text = feed[position].content
        feedViewHolder.likes.text = feed[position].likes.toString()
        feedViewHolder.comments.text = feed[position].comments.toString()
        feedViewHolder.reposts.text = feed[position].reposts.toString()
        feedViewHolder.views.text = feed[position].reposts.toString()

        if (feed[position].isLiked) {
            feedViewHolder.likeButton.setImageResource(R.drawable.heart)
        } else {
            feedViewHolder.likeButton.setImageResource(R.drawable.heart_outline)
        }
        if (feed[position].createdAt.equals(feed[position].editedAt)) {
            feedViewHolder.postedTime.text = feed[position].createdAt.toString()
        } else {
            feedViewHolder.postedTime.text = feed[position].editedAt.toString()
        }

        feedViewHolder.likeButton.setOnClickListener {
            if (feed[position].isLiked) {
                unlikePost(feed[position].postId)
                feed[position].isLiked = false
                feed[position].likes--
                feedViewHolder.likeButton.setImageResource(R.drawable.heart_outline)
            } else {
                likePost(feed[position].postId)
                feed[position].isLiked = true
                feed[position].likes++
                feedViewHolder.likeButton.setImageResource(R.drawable.heart)
            }
            feedViewHolder.likes.text = feed[position].likes.toString()
        }

        feedViewHolder.commentButton.setOnClickListener {
            val activity = activityContext(feedViewHolder) as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, CommentsFragment(feed[position].postId))
                .addToBackStack(null)
                .commit()
        }
    }

    private fun activityContext(feedViewHolder: FeedViewHolder): Context? {
        val context = feedViewHolder.itemView.context
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context
    }

    override fun getItemCount() = feed.size

}