package com.displaynone.acss.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.displaynone.acss.R
import com.displaynone.acss.components.auth.models.user.repository.dto.UserDTO
import com.displaynone.acss.databinding.FragmentProfileBinding
import com.displaynone.acss.ui.profile.ProfileViewModel.Action
import com.displaynone.acss.ui.scan.QrScanDestination
import com.displaynone.acss.util.collectWithLifecycle
import com.displaynone.acss.util.navigateTo

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
        binding.scan.setOnClickListener{
            viewModel.openScan()
        }
        subscribe()
        refreshData()
        waitForQRScanResult()
    }

    private fun refreshData() {
        Log.d("ProfileFragment", "Refreshed")
        viewModel.getInfo()
        subscribeToGetData()
    }
    fun subscribe() {
        viewModel.action.collectWithLifecycle(this) { action ->
            if (action is Action.GoToAuth) {
                view?.let { navigateTo(it, R.id.action_profileFragment_to_authFragment) } ?: throw IllegalStateException("View is null")
            }
            if (action is Action.GoToScan) {
                view?.let { navigateTo(it, R.id.action_profileFragment_to_qrScanFragment) }?: throw IllegalStateException("View is null")
            }
        }
    }
    private fun waitForQRScanResult() {

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            })

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(
            QrScanDestination.REQUEST_KEY
        )?.observe(viewLifecycleOwner) { bundle ->
            val qrCode = bundle.getString("key_qr")
            if (!qrCode.isNullOrEmpty()) view?.let {
                val bundle = Bundle().apply {
                    putString("qrCode", qrCode)
                }
                navigateTo(it, R.id.action_profileFragment_to_qrResultFragment, bundle)
            }
        }
    }
    private fun logout() {
        viewModel.logout()
        viewModel.openAuth()
        Toast.makeText(activity, "LOGOUT", Toast.LENGTH_SHORT).show()
    }

    private fun subscribeToGetData(){
        viewModel.state.collectWithLifecycle(this) { state ->
            if (state is ProfileViewModel.State.Show) {
                val userDto: UserDTO = state.item
                binding.fio.text = userDto.name
                binding.position.text = userDto.position
                binding.lastEntry.text = userDto.lastVisit
                setAvatar(userDto.photo)
            }
        }
    }

    private fun setAvatar(photo: String) {
        Glide.with(requireContext())
            .load(photo)
            .placeholder(R.drawable.ic_photo)
            .error(R.drawable.ic_back)
            .into(binding.avatar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}