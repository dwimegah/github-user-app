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
import com.belajar.submissionfundamental1.databinding.FragmentFollowersBinding
import com.belajar.submissionfundamental1.ui.ViewModelFactory

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersBinding.inflate(layoutInflater)

        val viewModel = obtainDetailViewModel(requireActivity())

        val username = requireActivity().intent.getStringExtra("username")
        viewModel.getFollowers(username.toString())

        viewModel.followers.observe(viewLifecycleOwner) { followers ->
            setFollowers(followers)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvViewFollowers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvViewFollowers.addItemDecoration(itemDecoration)

        viewModel.isLoadingFollowers.observe(viewLifecycleOwner) {
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

    private fun setFollowers(followers: List<ItemsItem>) {
        val adapter = FollowersAdapter()
        adapter.submitList(followers)
        binding.rvViewFollowers.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        binding.progressBarFollowers.visibility = if (state) View.VISIBLE else View.GONE
    }
}