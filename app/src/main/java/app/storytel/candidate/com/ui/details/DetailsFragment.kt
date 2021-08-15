package app.storytel.candidate.com.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.storytel.candidate.com.R
import app.storytel.candidate.com.databinding.DetailsFragmentBinding
import app.storytel.candidate.com.util.Resource
import app.storytel.candidate.com.util.autoCleared
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.details_fragment.*

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val TAG = "DetailsFragment"
    private var binding: DetailsFragmentBinding by autoCleared()
    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var adapter: DetailsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = DetailsAdapter()
        binding.detailsFragmentRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.detailsFragmentRecyclerview.adapter = adapter
    }

    @SuppressLint("CheckResult")
    private fun setupObservers() {
        viewModel.getCommentLiveData(arguments?.getInt("postID")!!).observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "setupObservers: Loading")
                }
                is Resource.Success -> {
                    adapter.setItems(it.data)
                }
                is Resource.Error -> {
                    displayError(it.error)
                }
            }

        })
        viewModel.getPhotoData().observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "setupObservers: Loading")
                }
                is Resource.Success -> {
                    binding.detailsFragmentErrorImageview.visibility = View.INVISIBLE
                    Picasso.get()
                        .load(it.data.url)
                        .placeholder(R.color.colorAccent)
                        .into(details_fragment_imageview)
                }
                is Resource.Error -> {
                    displayError(it.error)
                }
            }
        })
    }

    private fun displayError(throwable: Throwable?) {
        binding.detailsFragmentErrorImageview.visibility = View.VISIBLE
        Toast.makeText(context, throwable!!.message.toString(), Toast.LENGTH_SHORT).show()
    }


}