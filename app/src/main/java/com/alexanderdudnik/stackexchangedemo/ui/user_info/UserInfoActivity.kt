package com.alexanderdudnik.stackexchangedemo.ui.user_info

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import com.alexanderdudnik.stackexchangedemo.R
import com.alexanderdudnik.stackexchangedemo.databinding.ActivityUserInfoBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat


/**
 * Main activity
 *
 * Contains initialisation of main activity class
 */

class UserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoBinding
    private val viewModel by viewModels<StackUsersInfoModel>()
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            this.finish()
        }

        supportActionBar?.let{
            it.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }

        intent.extras?.getInt(USER_ID_PARAM)?.let{
            userId = it
            viewModel.loadUserInfo(userId)
        } ?: finish()

        title = intent.extras?.getString(USER_NAME_PARAM)?:getString(R.string.loading)

        with (binding) {
            viewModel.userLiveData.observe(this@UserInfoActivity){ user ->
                user?.let { mUser ->
                    Glide.with(this@UserInfoActivity)
                        .load(mUser.profileImage)
                        .centerCrop()
                        .into(binding.avatarIv)

                    title = mUser.displayName
                    userNameTv.text = mUser.displayName
                    userReputationTv.text = mUser.reputation.toString()
                    userLocationTv.text = mUser.location
                    userAgeTv.text = mUser.age?.toString()?:""
                    userCreatedTv.text = SimpleDateFormat("MM-dd-yyyy").format(mUser.creationDate)

                    with(userBadgesRv) {
                        adapter = BadgesListAdapter(mUser.badges.map { "${it.key}: ${it.value}" })
                        setHasFixedSize(true)
                        isEnabled = false
                    }
                }
            }
            viewModel.stateLiveData.observe(this@UserInfoActivity){
                when (it){
                    StackUsersInfoModel.LoadingState.LOADING, null -> {
                        progressbar.isVisible = true
                        errorLayout.isVisible = false
                        userInfoBlock.isVisible = false
                    }
                    StackUsersInfoModel.LoadingState.IDLE -> {
                        progressbar.isVisible = false
                        errorLayout.isVisible = false
                        userInfoBlock.isVisible = true
                    }
                    StackUsersInfoModel.LoadingState.ERROR -> {
                        progressbar.isVisible = false
                        errorLayout.isVisible = true
                        userInfoBlock.isVisible = false
                    }
                }
            }
            retryBtn.setOnClickListener{
                viewModel.loadUserInfo(userId)
            }
        }
    }

    companion object{
        const val USER_ID_PARAM = "USER_ID_PARAM"
        const val USER_NAME_PARAM = "USER_NAME_PARAM"
    }
}