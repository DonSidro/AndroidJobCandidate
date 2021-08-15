package app.storytel.candidate.com.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import app.storytel.candidate.com.R
import app.storytel.candidate.com.databinding.PostsFragmentBinding
import app.storytel.candidate.com.model.PostAndImages
import app.storytel.candidate.com.util.Resource
import app.storytel.candidate.com.util.Utils
import app.storytel.candidate.com.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment : Fragment(), PostsAdapter.ItemListener {

    private var binding: PostsFragmentBinding by autoCleared()
    private val viewModel: PostsViewModel by viewModels()
    private lateinit var adapter: PostsAdapter


    override fun onClickedPosts(postId: Int) {
        val bundle = Bundle()
        bundle.putInt("postID", postId)
        Navigation.findNavController(requireView()).navigate(R.id.listToPost, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PostsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupRecyclerViewRefresh()
    }

    private fun setupRecyclerViewRefresh() {
        binding.postsFragmentSwipeRefresh.setOnRefreshListener {
            if (Utils.isNetworkAvailable(context)) {
                viewModel.onRefreshWeb()
            } else {
                viewModel.onRefreshLocal()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = PostsAdapter(this)
        binding.postsFragmentRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.postsFragmentRecyclerview.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.getPostsAndImages().observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    displayLoading()
                }
                is Resource.Success -> {
                    displayData(it.data)
                }
                is Resource.Error -> {
                    displayError(it.error!!)
                }
            }

        })
    }

    private fun displayLoading() {
        binding.apply {
            postsFragmentSwipeRefresh.isRefreshing = true
        }
    }

    private fun displayData(data: PostAndImages) {
        binding.apply {
            postsFragmentSwipeRefresh.isRefreshing = false
            postsFragmentTextviewRetry.visibility = View.INVISIBLE
            postsFragmentErrorImageview.visibility = View.INVISIBLE
            adapter.setItems(data)
        }
    }

    private fun displayError(throwable: Throwable) {
        binding.apply {
            postsFragmentSwipeRefresh.isRefreshing = false
            postsFragmentTextviewRetry.visibility = View.VISIBLE
            postsFragmentErrorImageview.visibility = View.VISIBLE
            postsFragmentTextviewRetry.text = getString(R.string.errorMessage)
            Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
        }
    }
}