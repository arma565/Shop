package com.auth.login.view.fragment.setting.help

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.auth.login.R
import com.auth.login.databinding.FragmentHelpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpFragment : Fragment() {
    private lateinit var binding: FragmentHelpBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.devEmailAutoComplete.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(resources.getString(R.string.rezagorji68_gmail_com))
            )
            i.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.help))
            try {
                startActivity(Intent.createChooser(i, getString(R.string.send_mail)))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    activity,
                    getString(R.string.there_are_no_email_clients_installed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        try {
            val packageInfo: PackageInfo =
                requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            binding.appVersionAutoComplete.setText(packageInfo.versionName)

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}