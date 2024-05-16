package com.auth.login.view.fragment.setting

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.auth.login.R
import com.auth.login.data.local.config.UserInfoConfig
import com.auth.login.data.model.GlobalFunctions
import com.auth.login.data.model.User
import com.auth.login.databinding.FragmentSettingBinding
import com.auth.login.viewmodel.AuthenticationViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = GlobalFunctions.getNavControllerFragmentSetting(requireActivity())

        val userInfoConfig = UserInfoConfig(requireActivity())
        val email = userInfoConfig.getEmail()
        CoroutineScope(IO).launch LaunchIO@{
            viewModel.getUserList().collect { userList ->
                if (userList.none { it.email == email }) return@collect
                val foundedUser: User = userList.first { it.email == email }
                if (!validateUpdatedInfo(foundedUser)) {
                    this.launch(Main) {
                        binding.txtUserFirstNameLastName.text =
                            resources.getString(R.string.no_name)
                        binding.txtUserName.text = resources.getString(R.string.personal_info)
                    }
                    return@collect
                }
                this@LaunchIO.launch(Main) {
                    binding.txtUserFirstNameLastName.text =
                        foundedUser.firstName.plus(" ").plus(foundedUser.lastName)
                    binding.txtUserName.text = foundedUser.username
                    val multi = MultiTransformation(
                        CropCircleWithBorderTransformation(
                            2,
                            resources.getColor(R.color.pink_color, resources.newTheme())
                        )
                    )
                    Glide.with(activity as Context).load(foundedUser.profilePhoto)
                        .apply(RequestOptions.bitmapTransform(multi))
                        .into(binding.imageUserProfile)
                }
            }
        }

        val nightModeFlags =
            view.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            binding.btnDarkSwitch.isChecked = true
            binding.txtDarkState.text = getString(R.string.on)
        }

        binding.consInfo.setOnClickListener {
            navController.navigate(R.id.action_settingFragment_to_accountFragment)
        }

        binding.btnDarkSwitch.setOnClickListener {
            if (binding.btnDarkSwitch.isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.consHelp.setOnClickListener {
            navController.navigate(R.id.action_settingFragment_to_helpFragment)
        }

        binding.btnLogout.setOnClickListener {
            GlobalFunctions.logOut(requireActivity() as AppCompatActivity)
        }

        val backCallBackBtn = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finish()
                val intentPropertyActivity = Intent(activity, Class.forName("com.store.shop.view.activity.MainActivity"))
                intentPropertyActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                requireActivity().startActivity(intentPropertyActivity)
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,backCallBackBtn)
    }




    private fun validateUpdatedInfo(user: User): Boolean {
        when (true) {
            user.username!!.isEmpty() -> {
                return false
            }

            user.firstName!!.isEmpty() -> {
                return false
            }

            user.lastName!!.isEmpty() -> {
                return false
            }

            user.phoneNumber!!.isEmpty() -> {
                return false
            }

            else -> {
                return true
            }
        }
    }
}