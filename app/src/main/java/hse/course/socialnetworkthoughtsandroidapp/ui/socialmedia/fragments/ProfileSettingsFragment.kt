package hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import hse.course.socialnetworkthoughtsandroidapp.R

import hse.course.socialnetworkthoughtsandroidapp.databinding.ProfileSettingsFragmentLayoutBinding
import hse.course.socialnetworkthoughtsandroidapp.ui.authentication.LoginActivity
import hse.course.socialnetworkthoughtsandroidapp.viewmodel.SocialMediaViewModel
import java.io.File

@AndroidEntryPoint
class ProfileSettingsFragment(
    private val profileNickname: String?,
    private val profileStatus: String?
) : Fragment() {

    private lateinit var binding: ProfileSettingsFragmentLayoutBinding

    private val socialMediaViewModel: SocialMediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileSettingsFragmentLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.profileSettingsNicknameTextInputLayout.editText?.setText(profileNickname)
        binding.profileSettingsStatusInputLayout.editText?.setText(profileStatus)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                var image: File? = null
                if (uri != null) {
                    val file = createTempFile()
                    requireContext().contentResolver.openInputStream(uri).use { input ->
                        file.outputStream().use { output ->
                            input?.copyTo(output)
                        }
                    }
                    image = file
                }

                socialMediaViewModel.setProfileImage(image)
            }

        binding.newProfilePictureButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.profileSettingsAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.check -> {
                    val nickname = binding.profileSettingsNicknameTextInputLayout.editText?.text.toString()
                    val status = binding.profileSettingsStatusInputLayout.editText?.text.toString()
                    socialMediaViewModel.updateProfile(nickname, status)
                    true
                }

                else -> false
            }
        }

        binding.logoutButton.setOnClickListener {
            socialMediaViewModel.logout()
            val intent = Intent(this.activity?.applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}