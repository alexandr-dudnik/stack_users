package com.alexanderdudnik.stackexchangedemo.ui.user_list

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexanderdudnik.stackexchangedemo.R
import com.alexanderdudnik.stackexchangedemo.databinding.ActivityUserListBinding
import com.alexanderdudnik.stackexchangedemo.ui.user_info.UserInfoActivity


/**
 * Main activity
 *
 * Contains initialisation of main activity class
 */

class UserListActivity : AppCompatActivity() {

    private val viewModel: StackUsersViewModel by viewModels()
    private lateinit var binding: ActivityUserListBinding
    private val listAdapter = UserListAdapter{ userId, userName ->
        val intent = Intent(this, UserInfoActivity::class.java).apply {
            putExtra(UserInfoActivity.USER_ID_PARAM, userId)
            putExtra(UserInfoActivity.USER_NAME_PARAM, userName)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        viewModel.loadUsers(initial = true)

        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.app_name)
        setSupportActionBar(binding.toolbar)

        with (binding.userListRv) {
            adapter = listAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
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

        viewModel.stateLiveData.observe(this){
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
        viewModel.usersLiveData.observe(this){
            listAdapter.updateList(it)
        }
    }
}