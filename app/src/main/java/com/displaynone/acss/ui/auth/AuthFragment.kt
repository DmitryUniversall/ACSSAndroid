package com.displaynone.acss.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.displaynone.acss.R
import com.displaynone.acss.databinding.FragmentAuthBinding
import com.displaynone.acss.util.collectWithLifecycle
import com.displaynone.acss.util.navigateTo
import com.displaynone.acss.ui.auth.AuthViewModel.Action

class AuthFragment: Fragment(R.layout.fragment_auth) {
    private var _binding: FragmentAuthBinding? = null
    private val binding: FragmentAuthBinding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAuthBinding.bind(view)
        setupLoginButton()

        viewModel.action.collectWithLifecycle(this) { action ->
            when (action) {
                Action.GotoProfile -> navigateTo(view, R.id.action_authFragment_to_profileFragment)
            }
        }
        viewModel.errorState.collectWithLifecycle(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                binding.errorTitle.text = errorMessage
                binding.errorTitle.visibility = View.VISIBLE
            }
        }
        binding.next.setOnClickListener{
            onLoginButtonClicked(view)
        }
    }


    private fun setupLoginButton() {
        binding.login.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            @SuppressLint("ResourceAsColor")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.error.visibility = View.GONE
                val username = s.toString()
                val valid = isUsernameValid(username)
                binding.hint.visibility = if(valid) View.INVISIBLE else View.VISIBLE
                binding.next.isEnabled = valid
                val color = if (valid) R.color.primary else R.color.secondary
                binding.next.backgroundTintList = ContextCompat.getColorStateList(requireContext(), color)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isUsernameValid(username: String): Boolean {
        val alf = "^[a-zA-Z0-9_]+$".toRegex()
        return username.isNotEmpty() &&
                username.length >= 3 &&
                !username[0].isDigit() &&
                alf.matches(username)
    }
//    private fun subscribe() {
//        viewModel.state.collectWhenStarted(this) { state ->
//            binding.login.setOnClickListener(this::onLoginButtonClicked)
//        }
//    }

    private fun onLoginButtonClicked(view: View) {
        val login = binding.login.text.toString()
        if (login.isEmpty()) return

        viewModel.login(login)
    }


}