package hse.course.socialnetworkthoughtsandroidapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hse.course.socialnetworkthoughtsandroidapp.R
import hse.course.socialnetworkthoughtsandroidapp.model.Comment

class CommentsAdapter (
    private val comments: List<Comment>
) :

    RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    class CommentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView
        val content: TextView

        init {
            nickname = view.findViewById(R.id.nickname_text_view)
            content = view.findViewById(R.id.content_text_view)
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): CommentsViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.comment_card, viewGroup, false)

        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(commentsViewHolder: CommentsViewHolder, position: Int) {
        commentsViewHolder.nickname.text = comments[position].nickname
        commentsViewHolder.content.text = comments[position].content
    }

    override fun getItemCount() = comments.size

}