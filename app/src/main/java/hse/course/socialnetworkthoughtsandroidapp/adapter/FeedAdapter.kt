package hse.course.socialnetworkthoughtsandroidapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.internal.managers.ViewComponentManager
import hse.course.socialnetworkthoughtsandroidapp.R
import hse.course.socialnetworkthoughtsandroidapp.model.Feed
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.CommentsFragment
import hse.course.socialnetworkthoughtsandroidapp.utils.ImagesUtils
import hse.course.socialnetworkthoughtsandroidapp.utils.TimeUtils
import java.util.UUID

class FeedAdapter(
    private val feed: List<Feed>,
    private val likePost: (id: UUID) -> Unit,
    private val unlikePost: (id: UUID) -> Unit,
    private val viewPost: (id: UUID) -> Unit
) :

    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView = view.findViewById(R.id.nickname_text_view)
        val image: ImageView = view.findViewById(R.id.profile_picture_imageview)
        val postedTime: TextView = view.findViewById(R.id.posted_time_textview)
        val theme: TextView = view.findViewById(R.id.theme)
        val content: TextView = view.findViewById(R.id.content)
        val imagesPager: ViewPager2 = view.findViewById(R.id.images_pager)
        val likes: TextView = view.findViewById(R.id.like_textview)
        val comments: TextView = view.findViewById(R.id.comment_textview)
        val reposts: TextView = view.findViewById(R.id.repost_textview)
        val views: TextView = view.findViewById(R.id.views_textview)
        val likeButton: ImageButton = view.findViewById(R.id.like_button)
        val commentButton: ImageButton = view.findViewById(R.id.comment_button)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): FeedViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_card, viewGroup, false)

        return FeedViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
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
        feedViewHolder.views.text = feed[position].views.toString()

        feedViewHolder.imagesPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        feedViewHolder.imagesPager.adapter = ImagesPagerAdapter(ArrayList())

        if (feed[position].profileImage != null) {
            val bitmap = feed[position].profileImage?.let { ImagesUtils.base64ToBitmap(it) }
            if (bitmap != null) {
                feedViewHolder.image.setImageBitmap(bitmap)
            }
        }

        if (feed[position].images != null) {
            val images = feed[position].images?.let { ImagesUtils.base64ListToBitmapList(it) }
            if (!images.isNullOrEmpty()) {
                feedViewHolder.imagesPager.adapter = ImagesPagerAdapter(images)
                feedViewHolder.imagesPager.layoutParams.height = 720
            }

        }

        if (feed[position].isLiked) {
            feedViewHolder.likeButton.setImageResource(R.drawable.heart)
        } else {
            feedViewHolder.likeButton.setImageResource(R.drawable.heart_outline)
        }
        if (feed[position].createdAt.equals(feed[position].editedAt)) {
            feedViewHolder.postedTime.text = TimeUtils.formatTime(feed[position].createdAt)
        } else {
            feedViewHolder.postedTime.text = TimeUtils.formatTime(feed[position].editedAt)
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

        feedViewHolder.itemView.setOnTouchListener { _, _ ->
            viewPost(feed[position].postId)
            return@setOnTouchListener true
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