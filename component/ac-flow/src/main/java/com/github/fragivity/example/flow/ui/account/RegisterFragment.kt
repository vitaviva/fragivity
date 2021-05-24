package com.github.fragivity.example.flow.ui.account

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.fragivity.example.base.hideSoftInput
import com.github.fragivity.example.base.initToolbarNav
import com.github.fragivity.example.base.showToast
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.flow.databinding.FlowFragmentRegisterBinding
import com.github.fragivity.example.flow.listener.OnLoginSuccessListener
import com.github.fragivity.example.viewbinding.viewBinding
import com.github.fragivity.navigator
import com.github.fragivity.popTo

class RegisterFragment : Fragment(R.layout.flow_fragment_register) {

    private val binding by viewBinding(FlowFragmentRegisterBinding::bind)

    private val mToolbar get() = binding.content.toolbar
    private val mEtAccount get() = binding.etAccount
    private val mEtPassword get() = binding.etPassword
    private val mEtPasswordConfirm get() = binding.etPasswordConfirm
    private val mBtnRegister get() = binding.btnRegister

    private var mOnLoginSuccessListener: OnLoginSuccessListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginSuccessListener) {
            mOnLoginSuccessListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mToolbar.setTitle(R.string.register)
        initToolbarNav(mToolbar)

        mBtnRegister.setOnClickListener {
            val account = mEtAccount.text.toString()
            if (account.isEmpty()) {
                showToast(R.string.error_username)
                return@setOnClickListener
            }

            val password = mEtPassword.text.toString()
            val passwordConfirm = mEtPasswordConfirm.text.toString()
            if (password.isEmpty() && passwordConfirm.isEmpty()) {
                showToast(R.string.error_pwd)
                return@setOnClickListener
            }

            mOnLoginSuccessListener?.onLoginSuccess(account)
            navigator.popTo(LoginFragment::class, true)
        }
    }

    override fun onPause() {
        super.onPause()
        hideSoftInput()
    }

    override fun onDetach() {
        super.onDetach()
        mOnLoginSuccessListener = null
    }
}