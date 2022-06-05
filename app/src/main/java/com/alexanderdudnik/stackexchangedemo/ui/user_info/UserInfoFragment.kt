package com.alexanderdudnik.stackexchangedemo.ui.user_info

import android.os.Bundle
import android.os.LocaleList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alexanderdudnik.stackexchangedemo.R
import com.alexanderdudnik.stackexchangedemo.data.StackUserEntity
import com.alexanderdudnik.stackexchangedemo.databinding.FragmentUserInfoBinding
import com.alexanderdudnik.stackexchangedemo.ui.StackUsersViewModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class UserInfoFragment : Fragment(R.layout.fragment_user_info) {
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!

    private val navArgs:UserInfoFragmentArgs by navArgs()
    private val viewModel:StackUsersViewModel by activityViewModels()

    private var user: StackUserEntity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        user = viewModel.getUserData( navArgs.accountId ) ?.also {
            requireActivity().title = it.displayName
        }
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (user == null){
            Toast.makeText(requireActivity(), getString(R.string.error_user_info_not_available), Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
            return
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserInfoBinding.bind(view)

        with (binding) {
            user?.let { mUser ->
                Glide.with(this@UserInfoFragment)
                    .load(mUser.profileImage)
                    .centerCrop()
                    .into(binding.avatarIv)

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}