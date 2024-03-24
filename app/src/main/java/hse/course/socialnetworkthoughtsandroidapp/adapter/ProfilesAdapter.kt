package hse.course.socialnetworkthoughtsandroidapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hse.course.socialnetworkthoughtsandroidapp.R
import hse.course.socialnetworkthoughtsandroidapp.model.SearchProfile
import java.util.UUID

class ProfilesAdapter(
    private val profiles: List<SearchProfile>,
    private val subscribe: (id: UUID) -> Unit,
    private val unsubscribe: (id: UUID) -> Unit
) :

    RecyclerView.Adapter<ProfilesAdapter.ProfilesViewHolder>() {

    class ProfilesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subscribeImageButton: ImageButton
        val nickname: TextView

        init {
            nickname = view.findViewById(R.id.nickname_text_view)
            subscribeImageButton = view.findViewById(R.id.subscribe_image_button)
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ProfilesViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.profile_card, viewGroup, false)

        return ProfilesViewHolder(view)
    }

    override fun onBindViewHolder(profilesViewHolder: ProfilesViewHolder, position: Int) {
        profilesViewHolder.nickname.text = profiles[position].nickname
        if (profiles[position].isSubscribed == true) {
            profilesViewHolder.subscribeImageButton.setImageResource(R.drawable.account_minus)
        } else {
            profilesViewHolder.subscribeImageButton.setImageResource(R.drawable.account_plus)
        }

        profilesViewHolder.subscribeImageButton.setOnClickListener {
            if (profiles[position].isSubscribed == false) {
                profiles[position].id?.let { it1 -> subscribe(it1) }
                profiles[position].isSubscribed = true
                profilesViewHolder.subscribeImageButton.setImageResource(R.drawable.account_minus)
            } else {
                profiles[position].id?.let { it1 -> unsubscribe(it1) }
                profiles[position].isSubscribed = false
                profilesViewHolder.subscribeImageButton.setImageResource(R.drawable.account_plus)
            }
        }
    }

    override fun getItemCount() = profiles.size

}