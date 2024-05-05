package com.auth.login.view.fragment.forgot

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.auth.login.R
import com.auth.login.data.model.GlobalFunctions
import com.auth.login.data.model.User
import com.auth.login.databinding.FragmentRecoveryBinding
import com.auth.login.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
class RecoveryUserFragment : Fragment() {
    private lateinit var binding: FragmentRecoveryBinding
    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecoveryBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalFunctions.layoutEdtEndIconMode(
            binding.layoutNewPasswordRecovery,
            binding.edtNewPasswordRecovery
        )
        GlobalFunctions.layoutEdtEndIconMode(
            binding.layoutConfirmNewPasswordRecovery,
            binding.edtConfirmNewPasswordRecovery
        )

        binding.btnResetRecovery.setOnClickListener {
            val foundedUser: User = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                arguments?.getParcelable("user", User::class.java)!!
            else
                arguments?.getParcelable("user")!!

            val newPassword = binding.edtNewPasswordRecovery.text.toString()
            val confirm = binding.edtConfirmNewPasswordRecovery.text.toString()
            val newUserPassword = User(
                foundedUser.id,
                foundedUser.username,
                foundedUser.firstName,
                foundedUser.lastName,
                foundedUser.phoneNumber,
                foundedUser.email,
                newPassword,
                foundedUser.recoveryCode,
                confirm,
                foundedUser.profilePhoto
            )
            if (validateRecoveryPassword(newUserPassword)) {
                CoroutineScope(IO).launch {
                    viewModel.upsertUser(newUserPassword)
                    this@launch.launch(Main) {
                        GlobalFunctions.getNavControllerFragmentAuth(requireActivity())
                            .navigate(R.id.action_recoveryFragment_to_loginFragment)
                    }
                }
            }
        }
    }


    private fun validateRecoveryPassword(user: User): Boolean {
        when (true) {
            (user.password.isNullOrBlank()) -> {
                binding.layoutNewPasswordRecovery.error = getString(R.string.password_require)
                return false
            }

            (user.confirm.isNullOrBlank()) -> {
                binding.layoutConfirmNewPasswordRecovery.error =
                    getString(R.string.confirm_password_require)
                return false
            }

            (user.password != user.confirm) -> {
                binding.layoutNewPasswordRecovery.error =
                    getString(R.string.password_and_confirm_password_are_not_match)
                binding.layoutConfirmNewPasswordRecovery.error =
                    getString(R.string.password_and_confirm_password_are_not_match)
                return false
            }

            else -> {
                return true
            }
        }
    }
}