package com.displaynone.acss.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.displaynone.acss.R
import com.displaynone.acss.databinding.FragmentAuthBinding
import com.displaynone.acss.util.collectWithLifecycle
import com.displaynone.acss.util.navigateTo
import com.displaynone.acss.ui.auth.AuthViewModel.Action
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.math.log

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
//        viewModel.action.collectWithLifecycle(this) { action ->
//            if (action is AuthViewModel.Action.GotoProfile){
//
//            }
//        }
    }


    private fun setupLoginButton() {
        binding.login.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.error.visibility = View.GONE
                val username = s.toString()
                binding.login.isEnabled = isUsernameValid(username)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isUsernameValid(username: String): Boolean {
        val alf = "^[a-zA-Z0-9]+$".toRegex()
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