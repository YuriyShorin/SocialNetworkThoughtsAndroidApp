package hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import hse.course.socialnetworkthoughtsandroidapp.databinding.NotificationsFragmentLayoutBinding

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var binding: NotificationsFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NotificationsFragmentLayoutBinding.inflate(inflater)
        return binding.root
    }
}