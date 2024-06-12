package hse.course.socialnetworkthoughtsandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.internal.managers.ViewComponentManager
import hse.course.socialnetworkthoughtsandroidapp.R
import hse.course.socialnetworkthoughtsandroidapp.model.Post
import hse.course.socialnetworkthoughtsandroidapp.model.Profile
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.CommentsFragment
import hse.course.socialnetworkthoughtsandroidapp.utils.ImagesUtils
import hse.course.socialnetworkthoughtsandroidapp.utils.TimeUtils
import java.util.UUID

class CurrentProfilePostsAdapter(
    private val posts: MutableList<Post>,
    private val profile: Profile,
    private val likePost: (id: UUID) -> Unit,
    private val unlikePost: (id: UUID) -> Unit,
    private val deletePost: (id: UUID) -> Unit
) :

    RecyclerView.Adapter<CurrentProfilePostsAdapter.CurrentProfilePostsViewHolder>() {

    class CurrentProfilePostsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView = view.findViewById(R.id.nickname_text_view)
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
        val menuButton: ImageButton = view.findViewById(R.id.menu_button)
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

        currentProfilePostsViewHolder.imagesPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        currentProfilePostsViewHolder.imagesPager.adapter = ImagesPagerAdapter(ArrayList())

        if (posts[position].images != null) {
            val images = posts[position].images?.let { ImagesUtils.base64ToBitmap(it) }
            if (!images.isNullOrEmpty()) {
                currentProfilePostsViewHolder.imagesPager.adapter = ImagesPagerAdapter(images)
                currentProfilePostsViewHolder.imagesPager.layoutParams.height = 720
            }

        }

        if (posts[position].isLiked) {
            currentProfilePostsViewHolder.likeButton.setImageResource(R.drawable.heart)
        } else {
            currentProfilePostsViewHolder.likeButton.setImageResource(R.drawable.heart_outline)
        }
        if (posts[position].createdAt.equals(posts[position].editedAt)) {
            currentProfilePostsViewHolder.postedTime.text = TimeUtils.formatTime(posts[position].createdAt)
        } else {
            currentProfilePostsViewHolder.postedTime.text = TimeUtils.formatTime(posts[position].editedAt)
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

        currentProfilePostsViewHolder.commentButton.setOnClickListener {
            val activity = activityContext(currentProfilePostsViewHolder) as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, CommentsFragment(posts[position].id))
                .addToBackStack(null)
                .commit()
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

    private fun activityContext(currentProfilePostsViewHolder: CurrentProfilePostsViewHolder): Context? {
        val context = currentProfilePostsViewHolder.itemView.context
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context
    }

    override fun getItemCount() = posts.size

    private fun removeItem(position: Int) {
        posts.removeAt(position)
        notifyItemRemoved(position)
    }
}