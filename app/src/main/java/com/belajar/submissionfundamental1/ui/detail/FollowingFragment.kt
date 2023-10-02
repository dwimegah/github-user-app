package com.belajar.submissionfundamental1.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.belajar.submissionfundamental1.data.response.ItemsItem
import com.belajar.submissionfundamental1.databinding.FragmentFollowingBinding
import com.belajar.submissionfundamental1.ui.ViewModelFactory

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFollowingBinding.inflate(layoutInflater)

        val viewModel = obtainDetailViewModel(requireActivity())

        val username = requireActivity().intent.getStringExtra("username")
        viewModel.getFollowing(username.toString())

        viewModel.following.observe(viewLifecycleOwner) { following ->
            setFollowing(following)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvViewFollowing.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvViewFollowing.addItemDecoration(itemDecoration)

        viewModel.isLoadingFollowing.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun obtainDetailViewModel(activity: FragmentActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(requireActivity(), factory)[UserViewModel::class.java]
    }

    private fun setFollowing(following: List<ItemsItem>) {
        val adapter = FollowingAdapter()
        adapter.submitList(following)
        binding.rvViewFollowing.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        binding.progressBarFollowing.visibility = if (state) View.VISIBLE else View.GONE
    }
}