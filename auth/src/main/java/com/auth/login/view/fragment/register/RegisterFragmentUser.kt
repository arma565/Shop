package com.auth.login.view.fragment.register

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.auth.login.data.model.GlobalFunctions
import com.auth.login.data.model.IProgressbarState
import com.auth.login.data.model.User
import com.auth.login.databinding.FragmentRegisterBinding
import com.auth.login.viewmodel.AuthNetworkViewModel
import com.auth.login.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragmentUser : Fragment() , IProgressbarState {
    private lateinit var binding: FragmentRegisterBinding
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
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalFunctions.layoutEdtEndIconMode(binding.layoutUserEmailRegister, binding.edtEmail)
        GlobalFunctions.layoutEdtEndIconMode(
            binding.layoutUserPasswordRegister,
            binding.edtPassword
        )
        GlobalFunctions.layoutEdtEndIconMode(binding.layoutConfirmRegister, binding.edtConfirm)

        navController = GlobalFunctions.getNavControllerFragmentAuth(requireActivity())

        binding.txtLogin.setOnClickListener {
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            val user = User(
                email = binding.edtEmail.text.toString(),
                password = binding.edtPassword.text.toString(),
                confirm = binding.edtConfirm.text.toString(),
                recoveryCode = (1548..4781345).random().toString()
            )
            if (validateUserRegister(user)) {
                CoroutineScope(IO).launch {
                    viewModel.getUserList().collect { userList ->
                        this.launch(Main) LaunchMain@{
                            if (userList.any { it.email == user.email }) {
                                Toast.makeText(
                                    activity,
                                    getString(R.string.user_already_exist),
                                    Toast.LENGTH_LONG
                                ).show()
                                return@LaunchMain
                            }
                            this@RegisterFragmentUser.onShowProgressBar()
                            try {
                                networkViewModel.register(user.email!!, user.password!!)
                                    .observe(owner) {
                                        if (it == 0.0) throw Error()
                                        this@RegisterFragmentUser.onHideProgressBar()
                                        viewModel.upsertUser(user)
                                        val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                                        alert.setTitle(requireContext().getString(R.string.your_registration_recovery_code))
                                        alert.setIcon(android.R.drawable.ic_dialog_info)
                                        alert.setMessage(
                                            requireContext().getString(R.string.your_registration_recovery_code_is)
                                                .plus(user.recoveryCode)
                                                .plus("\n")
                                                .plus(requireContext().getString(R.string.please_keep_it_for_recovery_assistance))
                                        )
                                        alert.setPositiveButton(requireContext().getString(R.string.ok)) { _, _ ->
                                            navController.navigate(R.id.action_registerFragment_to_loginFragment)
                                        }
                                        alert.show()
                                    }
                            }catch (e : Error){
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
    }

    private fun validateUserRegister(user: User): Boolean {
        when (true) {
            user.email!!.isEmpty() -> {
                binding.layoutUserEmailRegister.error = getString(R.string.email_require)
                return false
            }

            !PatternsCompat.EMAIL_ADDRESS.matcher(user.email!!).matches() -> {
                binding.layoutUserEmailRegister.error = getString(R.string.email_not_valid)
                return false
            }

            user.password!!.isEmpty() -> {
                binding.layoutUserPasswordRegister.error = getString(R.string.password_require)
                return false
            }

            user.confirm!!.isEmpty() -> {
                binding.layoutConfirmRegister.error = getString(R.string.confirm_password_require)
                return false
            }

            (user.password != user.confirm) -> {
                binding.layoutUserPasswordRegister.error =
                    getString(R.string.password_and_confirm_password_are_not_match)
                binding.layoutConfirmRegister.error =
                    getString(R.string.password_and_confirm_password_are_not_match)
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