package com.displaynone.acss.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.displaynone.acss.R
import com.displaynone.acss.components.auth.models.user.UserServiceST
import com.displaynone.acss.databinding.FragmentProfileBinding
import com.displaynone.acss.ui.fragment.ProfileViewModel.Action
import com.displaynone.acss.util.collectWithLifecycle
import com.displaynone.acss.util.navigateTo
import kotlinx.coroutines.launch

class ProfileFragment: Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }
        binding.logout.setOnClickListener{
            logout()
        }
    }

    private fun refreshData() {
        Log.d("ProfileFragment", "Refreshed")
    }
    fun subscribe2Logout() {
        viewModel.action.collectWithLifecycle(this) { action ->
            if (action is Action.GoToAuth) {
                view?.let { navigateTo(it, R.id.action_profileFragment_to_authFragment) } ?: throw IllegalStateException("View is null")
            }
        }
    }
    private fun logout() {
        viewModel.logout()
        viewModel.openAuth()
        subscribe2Logout()
        Toast.makeText(activity, "LOGOUT", Toast.LENGTH_SHORT).show()
    }

    private fun subscribe(){
//        viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}