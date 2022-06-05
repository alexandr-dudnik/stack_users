package com.alexanderdudnik.stackexchangedemo.ui.user_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexanderdudnik.stackexchangedemo.R
import com.alexanderdudnik.stackexchangedemo.databinding.FragmentUserListBinding
import com.alexanderdudnik.stackexchangedemo.ui.StackUsersViewModel

/**
 * Fragment for representing list of users
 */
class UserListFragment : Fragment(R.layout.fragment_user_list) {
    private val viewModel: StackUsersViewModel by activityViewModels()

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val listAdapter = UserListAdapter{ accountId ->
        findNavController().navigate(UserListFragmentDirections.toUserInfoFragment(accountId))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUsers(initial = true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserListBinding.bind(view)

        requireActivity().title = getString(R.string.app_name)

        with (binding.userListRv) {
            adapter = listAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                //Listen to scroll to last item and load next portion
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    (recyclerView.layoutManager as LinearLayoutManager).run {
                        if (childCount + findFirstVisibleItemPosition() >= itemCount) {
                            viewModel.loadUsers()
                        }
                    }
                }

            })
        }
        binding.searchBtn.setOnClickListener {
            viewModel.setFilter(binding.searchQueryEt.text.toString())
        }

        viewModel.stateLiveData.observe(viewLifecycleOwner){
            when (it){
                StackUsersViewModel.LoadingState.LOADING -> {
                    binding.userListRv.isEnabled = false
                    binding.progressbar.isVisible = true
                }
                StackUsersViewModel.LoadingState.IDLE -> {
                    binding.userListRv.isEnabled = true
                    binding.progressbar.isVisible = false
                }
                else ->{

                }
            }
        }
        viewModel.usersLiveData.observe(viewLifecycleOwner){
            listAdapter.updateList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}