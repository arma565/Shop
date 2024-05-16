package com.auth.login.view.fragment.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.auth.login.R
import com.auth.login.data.local.config.UserAutoLoginConfig
import com.auth.login.data.local.config.UserInfoConfig
import com.auth.login.data.model.GlobalFunctions
import com.auth.login.data.model.IProgressbarState
import com.auth.login.data.model.User
import com.auth.login.databinding.FragmentLoginBinding
import com.auth.login.viewmodel.AuthNetworkViewModel
import com.auth.login.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragmentUser : Fragment(), IProgressbarState {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController
    private lateinit var owner: LifecycleOwner
    private val viewModel: AuthenticationViewModel by viewModels()
    private val networkViewModel: AuthNetworkViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        owner = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        GlobalFunctions.layoutEdtEndIconMode(binding.layoutUserEmailLogin, binding.edtEmail)
        GlobalFunctions.layoutEdtEndIconMode(binding.layoutPassword, binding.edtPassword)

        navController = GlobalFunctions.getNavControllerFragmentAuth(requireActivity())

        binding.txtSignUp.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            val user = User(
                email = binding.edtEmail.text.toString(),
                password = binding.edtPassword.text.toString()
            )
            if (validateUserLogin(user)) {
                CoroutineScope(IO).launch {
                    viewModel.getUserList().collect { userList ->
                        if (userList.none { it.email == user.email && it.password == user.password }) {
                            this.launch(Main) {
                                Toast.makeText(
                                    activity,
                                    getString(R.string.user_not_found),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            return@collect
                        }
                        val foundUser: User =
                            userList.first { it.email == user.email && it.password == user.password }
                        this.launch(Main) {
                            this@LoginFragmentUser.onShowProgressBar()
                            try {
                                networkViewModel.login(user.email!!, user.password!!)
                                    .observe(owner) {
                                        if (it == "0") throw Error()
                                        this@LoginFragmentUser.onHideProgressBar()
                                        val userAutoLoginConfig =
                                            UserAutoLoginConfig(requireActivity())
                                        val userInfoConfig = UserInfoConfig(requireActivity())
                                        if (binding.checkBoxRemember.isChecked) {
                                            userAutoLoginConfig.save(foundUser)
                                            userInfoConfig.save(foundUser)
                                        }
                                        userInfoConfig.save(foundUser)
                                        GlobalFunctions.logIn(requireActivity())
                                    }
                            } catch (e: Error) {
                                Toast.makeText(
                                    activity,
                                    getString(R.string.un_success),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        binding.txtForgot.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_forgotFragment)
        }
    }

    private fun validateUserLogin(user: User): Boolean {
        when (true) {
            user.email!!.isEmpty() -> {
                binding.layoutUserEmailLogin.error = getString(R.string.email_require)
                return false
            }

            !PatternsCompat.EMAIL_ADDRESS.matcher(user.email!!).matches() -> {
                binding.layoutUserEmailLogin.error = getString(R.string.email_not_valid)
                return false
            }

            user.password!!.isEmpty() -> {
                binding.layoutPassword.error = getString(R.string.password_require)
                return false
            }

            else -> {
                return true
            }
        }
    }

    override fun onShowProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onHideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}