package de.sj4.coronapartyapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import de.sj4.coronapartyapp.databinding.FragmentSettingsNotificationsBinding
import de.sj4.coronapartyapp.ui.main.MainActivity
import de.sj4.coronapartyapp.ui.viewmodel.SettingsViewModel
import de.sj4.coronapartyapp.ui.viewmodel.TracingViewModel
import de.sj4.coronapartyapp.util.ExternalActionHelper

/**
 * This is the setting notification page. Here the user sees his os notifications settings status.
 * If os notifications are disabled he can navigate to them with one click. And if the os is enabled
 * the user can decide which notifications he wants to get: risk updates and/or test results.
 *
 * @see TracingViewModel
 * @see SettingsViewModel
 */
class SettingsNotificationFragment : Fragment() {
    companion object {
        private val TAG: String? = SettingsNotificationFragment::class.simpleName
    }

    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private var _binding: FragmentSettingsNotificationsBinding? = null
    private val binding: FragmentSettingsNotificationsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsNotificationsBinding.inflate(inflater)
        binding.settingsViewModel = settingsViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        binding.settingsNotificationsContainer.sendAccessibilityEvent(AccessibilityEvent.TYPE_ANNOUNCEMENT)
        // refresh required data
        settingsViewModel.refreshNotificationsEnabled(requireContext())
        settingsViewModel.refreshNotificationsRiskEnabled()
        settingsViewModel.refreshNotificationsTestEnabled()
    }

    private fun setButtonOnClickListener() {
        // Notifications about risk status
        val updateRiskNotificationSwitch =
            binding.settingsSwitchRowNotificationsRisk.settingsSwitchRowSwitch
        // Notifications about test status
        val updateTestNotificationSwitch =
            binding.settingsSwitchRowNotificationsTest.settingsSwitchRowSwitch
        // Settings
        val settingsRow = binding.settingsNotificationsCard.tracingStatusCardButton
        val goBack =
            binding.settingsNotificationsHeader.headerButtonBack.buttonIcon
        // Update Risk
        updateRiskNotificationSwitch.setOnClickListener {
            settingsViewModel.toggleNotificationsRiskEnabled()
        }
        // Update Test
        updateTestNotificationSwitch.setOnClickListener {
            settingsViewModel.toggleNotificationsTestEnabled()
        }
        goBack.setOnClickListener {
            (activity as MainActivity).goBack()
        }
        // System Settings
        settingsRow.setOnClickListener {
            ExternalActionHelper.toNotifications(requireContext())
        }
    }
}
