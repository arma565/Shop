package com.auth.login.view.fragment.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.auth.login.R
import com.auth.login.data.model.GlobalFunctions
import com.auth.login.databinding.FragmentForgotBinding
import com.auth.login.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotFragment : Fragment() {
    private lateinit var binding: FragmentForgotBinding
    private lateinit var navController: NavController
    private val authViewMode: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalFunctions.layoutEdtEndIconMode(binding.layoutRecoveryCode, binding.edtRecoveryCode)

        navController = GlobalFunctions.getNavControllerFragmentAuth(requireActivity())

        binding.txtSignUp.setOnClickListener {
            navController.navigate(R.id.action_forgotFragment_to_registerFragment)
        }

        CoroutineScope(IO).launch {
            authViewMode.getUserList().collect { userList ->
                binding.btnReset.setOnClickListener {
                    val userInputRecoveryCode = binding.edtRecoveryCode.text.toString()
                    if (userInputRecoveryCode.isBlank()) {
                        binding.layoutRecoveryCode.error =
                            getString(R.string.recovery_code_is_require)
                        return@setOnClickListener
                    }
                    try {
                        navController.navigate(
                            R.id.action_forgotFragment_to_recoveryFragment,
                            bundleOf("user" to userList.first { it.recoveryCode == userInputRecoveryCode })
                        )
                    } catch (e: Exception) {
                        binding.layoutRecoveryCode.error =
                            getString(R.string.recovery_code_is_incorrect)
                    }
                }
            }
        }
    }
}