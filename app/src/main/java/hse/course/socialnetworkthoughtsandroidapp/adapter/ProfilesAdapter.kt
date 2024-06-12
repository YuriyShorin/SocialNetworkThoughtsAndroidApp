package hse.course.socialnetworkthoughtsandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.internal.managers.ViewComponentManager
import hse.course.socialnetworkthoughtsandroidapp.R
import hse.course.socialnetworkthoughtsandroidapp.model.SearchProfile
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments.ProfileFragment
import hse.course.socialnetworkthoughtsandroidapp.utils.ImagesUtils
import java.util.UUID

class ProfilesAdapter(
    private val profiles: List<SearchProfile>,
    private val subscribe: (id: UUID) -> Unit,
    private val unsubscribe: (id: UUID) -> Unit,
) :

    RecyclerView.Adapter<ProfilesAdapter.ProfilesViewHolder>() {

    class ProfilesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val subscribeImageButton: ImageButton = view.findViewById(R.id.subscribe_image_button)
        val nickname: TextView = view.findViewById(R.id.nickname_text_view)
        val image: ImageView = view.findViewById(R.id.profile_picture_imageview)
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
        profilesViewHolder.view.setOnClickListener {
            val activity = activityContext(profilesViewHolder) as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, ProfileFragment(profiles[position].id))
                .addToBackStack(null)
                .commit()
        }

        profilesViewHolder.nickname.text = profiles[position].nickname
        if (profiles[position].isSubscribed == true) {
            profilesViewHolder.subscribeImageButton.setImageResource(R.drawable.account_minus)
        } else {
            profilesViewHolder.subscribeImageButton.setImageResource(R.drawable.account_plus)
        }

        if (profiles[position].profileImage != null) {
            val bitmap = profiles[position].profileImage?.let { ImagesUtils.base64ToBitmap(it) }
            if (bitmap != null) {
                profilesViewHolder.image.setImageBitmap(bitmap)
            }
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

    private fun activityContext(profilesViewHolder: ProfilesViewHolder): Context? {
        val context = profilesViewHolder.itemView.context
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context
    }

    override fun getItemCount() = profiles.size

}