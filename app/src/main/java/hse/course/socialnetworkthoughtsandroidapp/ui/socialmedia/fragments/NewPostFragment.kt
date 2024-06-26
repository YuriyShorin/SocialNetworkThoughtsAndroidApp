package hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import hse.course.socialnetworkthoughtsandroidapp.R
import hse.course.socialnetworkthoughtsandroidapp.databinding.NewPostFragmentLayoutBinding
import hse.course.socialnetworkthoughtsandroidapp.viewmodel.SocialMediaViewModel
import kotlinx.coroutines.launch

private const val MAX_IMAGES = 10

@AndroidEntryPoint
class NewPostFragment : Fragment() {

    private lateinit var binding: NewPostFragmentLayoutBinding

    private val socialMediaViewModel: SocialMediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewPostFragmentLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(MAX_IMAGES)) { uris ->
                val images = buildList {
                    uris.forEach { uri ->
                        val file = createTempFile()
                        requireContext().contentResolver.openInputStream(uri).use { input ->
                            file.outputStream().use { output ->
                                input?.copyTo(output)
                            }
                        }
                        add(file)
                    }
                }
                if (images.isNotEmpty()) {
                    binding.filesNumberTextField.text =
                        getString(R.string.files_attached, images.size)
                }

                socialMediaViewModel.setPostImages(images)
            }

        binding.galleryButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.newPostAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.check -> {
                    val theme = binding.themeInputLayout.editText?.text.toString()
                    val content = binding.contentInputLayout.editText?.text.toString()
                    socialMediaViewModel.createPost(theme, content)
                    true
                }

                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                socialMediaViewModel.code.collect { code ->
                    if (code == 201) {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.fragment_holder, CurrentProfileFragment())
                            ?.addToBackStack(null)
                            ?.commit()
                    }
                }
            }
        }
    }
}