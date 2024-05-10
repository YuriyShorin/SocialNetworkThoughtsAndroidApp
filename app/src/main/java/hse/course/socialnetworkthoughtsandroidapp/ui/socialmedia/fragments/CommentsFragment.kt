package hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import hse.course.socialnetworkthoughtsandroidapp.databinding.CommentsFragmentLayoutBinding
import hse.course.socialnetworkthoughtsandroidapp.model.CreateComment
import hse.course.socialnetworkthoughtsandroidapp.viewmodel.SocialMediaViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class CommentsFragment(
    private val postId: UUID
) : Fragment() {

    private lateinit var binding: CommentsFragmentLayoutBinding

    private val socialMediaViewModel: SocialMediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CommentsFragmentLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.commentsRecyclerView.layoutManager = linearLayoutManager

        binding.sendButton.setOnClickListener {
            val commentText = binding.commentInput.text.toString()
            if (commentText.isNotBlank()) {
                socialMediaViewModel.commentPost(CreateComment(postId, commentText))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                socialMediaViewModel.code.collect { code ->
                    if (code == 200) {
                        binding.commentInput.setText("")
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                socialMediaViewModel.commentsAdapter.collect { commentsAdapter ->
                    binding.commentsRecyclerView.adapter = commentsAdapter
                }
            }
        }

        socialMediaViewModel.getComments(postId)
    }
}