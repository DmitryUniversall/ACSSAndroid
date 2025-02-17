package com.displaynone.acss.ui.result

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.displaynone.acss.R
import com.displaynone.acss.databinding.FragmentQrResultBinding
import com.displaynone.acss.util.collectWithLifecycle
import com.displaynone.acss.util.navigateTo


class QrResultFragment : Fragment(R.layout.fragment_qr_result) {
    private var _binding: FragmentQrResultBinding? = null
    private val binding: FragmentQrResultBinding get() = _binding!!
    private val viewModel: QrResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("QrResultFragment", getQrCode())
        _binding = FragmentQrResultBinding.bind(view)
        binding.close.setOnClickListener(this::closeQrScanFragment)

        this.openDoor()

        viewModel.state.collectWithLifecycle(this){ state ->
            if (state is QrResultViewModel.State.Result){
                if (state.resultCode == 200) {
                    setResult(getString(R.string.success))
                } else if (state.resultCode == 400) {
                    setResult(getString(R.string.wrong))
                } else if (state.resultCode == 401) {
                    setResult(getString(R.string.cancel))
                }
            }
            if (state is QrResultViewModel.State.Error){
                setResult(state.errorMessage)
            }
        }
    }

    private fun openDoor() {

        val qrCodeValueLong: Long

        try {
            qrCodeValueLong = getQrCode().toLong()
        } catch (exception: Exception) {
            when (exception) {
                is NumberFormatException, is IllegalArgumentException -> setResult(getString(R.string.wrong))
                else -> throw exception
            }
            return
        }

        viewModel.openDoor(qrCodeValueLong)
    }

    private fun getQrCode(): String {
        return arguments?.getString("qrCode") ?: "No QR Code Provided"
    }

    private fun closeQrScanFragment(view: View) {
        navigateTo(view, R.id.action_qrResultFragment_to_profileFragment)
    }

    private fun setResult(result: String) {
        binding.result.text = result
    }
}