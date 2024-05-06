package com.auth.login.view.fragment.setting.account

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.auth.login.R
import com.auth.login.data.local.config.UserInfoConfig
import com.auth.login.data.model.GlobalFunctions
import com.auth.login.data.model.User
import com.auth.login.databinding.FragmentPersonalDataBinding
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
class PersonalDataFragment : Fragment() {
    private lateinit var binding: FragmentPersonalDataBinding
    private val viewModel: AuthenticationViewModel by viewModels()
    private var bitmapImage: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonalDataBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Load image selector
         */
        binding.btnUploadImageProfile.setOnClickListener {
            val intentPickProfileImage = Intent()
            intentPickProfileImage.action = Intent.ACTION_PICK
            intentPickProfileImage.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            registerProfilePick.launch(
                Intent.createChooser(
                    intentPickProfileImage,
                    getString(R.string.select_profile_image)
                )
            )
        }

        /**
         * Save settings
         */
        binding.btnSave.setOnClickListener {
            val userInfoConfig = UserInfoConfig(requireActivity())
            val email: String? = userInfoConfig.getEmail()

            CoroutineScope(IO).launch {
                viewModel.getUserList().collect { userList ->
                    if (userList.none { it.email == email }) return@collect
                    val userInDatabase = userList.first { it.email == email }
                    this.launch(Main) {
                        val userName = binding.edtUsernamePersonal.text.toString()
                        val firstName = binding.edtFirstNamePersonal.text.toString()
                        val lastName = binding.edtLastNamePersonal.text.toString()
                        val phoneNumber = binding.edtPhoneNumberPersonal.text.toString()
                        val user = User(
                            userInDatabase.id,
                            userName,
                            firstName,
                            lastName,
                            phoneNumber,
                            userInDatabase.email,
                            userInDatabase.password,
                            userInDatabase.recoveryCode,
                            userInDatabase.confirm,
                            bitmapImage
                        )
                        if (updateInfoValidation(user)) {
                            viewModel.upsertUser(user)
                            GlobalFunctions.logOut(requireActivity() as AppCompatActivity)
                        }
                    }
                }
            }
        }
    }

    /**
     * Set selected image to image view
     */
    @Suppress("DEPRECATION")
    private var registerProfilePick =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val uriImage: Uri = result.data?.data!!

                val contentResolver: ContentResolver = requireContext().contentResolver
                try {
                    if (Build.VERSION.SDK_INT > 28) {
                        bitmapImage = ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                contentResolver,
                                uriImage
                            )
                        ).copy(Bitmap.Config.ARGB_8888, true)
                    }
                    bitmapImage = MediaStore.Images.Media.getBitmap(contentResolver, uriImage)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "${e.printStackTrace()}", Toast.LENGTH_LONG)
                        .show()
                }

                bitmapImage = changeSizeUri(requireContext(), uriImage)

                Glide.with(requireActivity())
                    .load(bitmapImage)
                    .apply(
                        RequestOptions.bitmapTransform(
                            MultiTransformation(
                                CropCircleWithBorderTransformation(
                                    2,
                                    resources.getColor(R.color.pink_color, resources.newTheme())
                                )
                            )
                        ).override(80, 80)
                    )
                    .into(binding.imgUserProfile)
            }
        }

    /**
     * Change image uri size
     */
    private fun changeSizeUri(context: Context, uri: Uri): Bitmap {
        val bitMapFactoryOptions: BitmapFactory.Options = BitmapFactory.Options()
        bitMapFactoryOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(
            context.contentResolver.openInputStream(uri),
            null,
            bitMapFactoryOptions
        )
        var widthTmp: Int = bitMapFactoryOptions.outWidth
        var heightTmp: Int = bitMapFactoryOptions.outHeight
        var scale = 1
        while (true) {
            if (widthTmp.div(2) < 400 || heightTmp.div(2) < 400) {
                break
            }
            widthTmp = widthTmp.div(2)
            heightTmp = heightTmp.div(2)
            scale *= 2
        }
        val bitMapFactoryOptions2: BitmapFactory.Options = BitmapFactory.Options()
        bitMapFactoryOptions2.inSampleSize = scale
        return BitmapFactory.decodeStream(
            context.contentResolver.openInputStream(uri),
            null,
            bitMapFactoryOptions2
        )!!
    }

    private fun updateInfoValidation(user: User): Boolean {
        when (true) {
            user.username!!.isEmpty() -> {
                binding.edtUsernamePersonal.error = getString(R.string.username_require)
                return false
            }

            user.firstName!!.isEmpty() -> {
                binding.edtFirstNamePersonal.error = getString(R.string.firstname_require)
                return false
            }

            user.lastName!!.isEmpty() -> {
                binding.edtLastNamePersonal.error = getString(R.string.lastname_require)
                return false
            }

            user.phoneNumber!!.isEmpty() -> {
                binding.edtPhoneNumberPersonal.error = getString(R.string.phonenumber_require)
                return false
            }

            else -> {
                return true
            }
        }
    }
}