package com.auth.login.view.fragment.setting.account

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.PatternsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.auth.login.R
import com.auth.login.data.local.config.UserInfoConfig
import com.auth.login.data.model.GlobalFunctions
import com.auth.login.data.model.User
import com.auth.login.databinding.FragmentAccountDataBinding
import com.auth.login.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountDataFragment : Fragment(R.layout.fragment_account_data) {
    private lateinit var binding: FragmentAccountDataBinding
    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountDataBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalFunctions.layoutEdtEndIconMode(
            binding.layoutCurrentPassword,
            binding.edtCurrentPassword
        )
        GlobalFunctions.layoutEdtEndIconMode(binding.layoutNewPassword, binding.edtNewPassword)
        GlobalFunctions.layoutEdtEndIconMode(binding.layoutConfirm, binding.edtConfirm)
        GlobalFunctions.layoutEdtEndIconMode(binding.layoutEmailAddress, binding.edtEmailAddress)
        GlobalFunctions.layoutEdtEndIconMode(binding.layoutPassword, binding.edtPassword)

        binding.btnChangePassword.setOnClickListener {
            val currentPassword = binding.edtCurrentPassword.text.toString()
            val newPassword = binding.edtNewPassword.text.toString()
            val confirmPassword = binding.edtConfirm.text.toString()
            val userInfoConfig = UserInfoConfig(requireActivity())
            val emailAddress = userInfoConfig.getEmail()

            CoroutineScope(IO).launch {
                viewModel.getUserList().collect { userList ->
                    if (userList.none { it.email == emailAddress }) {
                        this.launch(Main) {
                            Toast.makeText(activity, R.string.user_not_found, Toast.LENGTH_SHORT)
                                .show()
                        }
                        return@collect
                    }
                    val foundedUser: User = userList.first { it.email == emailAddress }
                    this.launch(Main) {
                        if (validatePassword(
                                currentPassword,
                                foundedUser.password,
                                newPassword,
                                confirmPassword
                            )
                        ) {
                            val newUser = User(
                                foundedUser.id,
                                foundedUser.username,
                                foundedUser.firstName,
                                foundedUser.lastName,
                                foundedUser.phoneNumber,
                                foundedUser.email,
                                newPassword,
                                foundedUser.recoveryCode,
                                confirmPassword,
                                foundedUser.profilePhoto
                            )
                            viewModel.upsertUser(newUser)
                            GlobalFunctions.logOut(requireActivity() as AppCompatActivity)
                        }
                    }
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            val email: String = binding.edtEmailAddress.text.toString()
            val password: String = binding.edtPassword.text.toString()
            val user = User(email = email, password = password)

            if (validateUserLogin(user)) {
                CoroutineScope(IO).launch {
                    viewModel.getUserList().collect { userList ->
                        if (userList.none { it.email == user.email && it.password == user.password }) {
                            this.launch(Main) {
                                Toast.makeText(
                                    activity,
                                    R.string.user_not_found,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            return@collect
                        }
                        val foundUser: User =
                            userList.first { it.email == user.email && it.password == user.password }
                        this.launch(Main) {
                            val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                            alert.setTitle(R.string.delete_user)
                            alert.setMessage(R.string.user_will_permanently_deleted_are_you_sure)
                            alert.setIcon(android.R.drawable.ic_delete)
                            alert.setPositiveButton(R.string.yes) { _, _ ->
                                viewModel.deleteUser(foundUser)
                                GlobalFunctions.logOut(requireActivity() as AppCompatActivity)
                            }
                            alert.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                            alert.show()
                        }
                    }
                }
            }
        }
    }

    private fun validatePassword(
        currentPassword: String?,
        dbPassword: String?,
        newPassword: String?,
        confirmPassword: String?
    ): Boolean {
        when (true) {
            (currentPassword.isNullOrBlank()) -> {
                binding.layoutCurrentPassword.error = getString(R.string.password_require)
                return false
            }

            (currentPassword != dbPassword) -> {
                binding.layoutCurrentPassword.error =
                    getString(R.string.currentpassword_is_not_correct)
                return false
            }

            (newPassword.isNullOrBlank()) -> {
                binding.layoutNewPassword.error = getString(R.string.password_require)
                return false
            }

            (confirmPassword.isNullOrBlank()) -> {
                binding.layoutConfirm.error = getString(R.string.confirm_password_require)
                return false
            }

            (newPassword != confirmPassword) -> {
                binding.layoutConfirm.error =
                    getString(R.string.password_and_confirm_password_are_not_match)
                binding.layoutNewPassword.error =
                    getString(R.string.password_and_confirm_password_are_not_match)
                return false
            }

            else -> {
                return true
            }
        }
    }

    private fun validateUserLogin(user: User): Boolean {
        when (true) {
            user.email!!.isEmpty() -> {
                binding.layoutEmailAddress.error = getString(R.string.email_require)
                return false
            }

            !PatternsCompat.EMAIL_ADDRESS.matcher(user.email!!).matches() -> {
                binding.layoutEmailAddress.error = getString(R.string.email_not_valid)
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
}