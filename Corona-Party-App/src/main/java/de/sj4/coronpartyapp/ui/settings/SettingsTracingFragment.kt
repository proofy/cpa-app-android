package de.sj4.coronapartyapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import de.sj4.coronapartyapp.R
import de.sj4.coronapartyapp.databinding.FragmentSettingsTracingBinding
import de.sj4.coronapartyapp.exception.ExceptionCategory
import de.sj4.coronapartyapp.exception.reporting.report
import de.sj4.coronapartyapp.nearby.InternalExposureNotificationClient
import de.sj4.coronapartyapp.nearby.InternalExposureNotificationPermissionHelper
import de.sj4.coronapartyapp.storage.LocalData
import de.sj4.coronapartyapp.ui.main.MainActivity
import de.sj4.coronapartyapp.ui.viewmodel.SettingsViewModel
import de.sj4.coronapartyapp.ui.viewmodel.TracingViewModel
import de.sj4.coronapartyapp.util.DialogHelper
import de.sj4.coronapartyapp.util.ExternalActionHelper
import de.sj4.coronapartyapp.worker.BackgroundWorkScheduler
import kotlinx.coroutines.launch

/**
 * The user can start/stop tracing and is informed about tracing.
 *
 * @see TracingViewModel
 * @see SettingsViewModel
 * @see InternalExposureNotificationClient
 * @see InternalExposureNotificationPermissionHelper
 */
class SettingsTracingFragment : Fragment(),
    InternalExposureNotificationPermissionHelper.Callback {

    companion object {
        private val TAG: String? = SettingsTracingFragment::class.simpleName
    }

    private val tracingViewModel: TracingViewModel by activityViewModels()
    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private var _binding: FragmentSettingsTracingBinding? = null
    private val binding: FragmentSettingsTracingBinding get() = _binding!!

    private lateinit var internalExposureNotificationPermissionHelper: InternalExposureNotificationPermissionHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsTracingBinding.inflate(inflater)
        binding.tracingViewModel = tracingViewModel
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
        // refresh required data
        tracingViewModel.refreshIsTracingEnabled()
        binding.settingsTracingContainer.sendAccessibilityEvent(AccessibilityEvent.TYPE_ANNOUNCEMENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        internalExposureNotificationPermissionHelper.onResolutionComplete(
            requestCode,
            resultCode
        )
    }

    override fun onStartPermissionGranted() {
        tracingViewModel.refreshIsTracingEnabled()
        BackgroundWorkScheduler.startWorkScheduler()
    }

    override fun onFailure(exception: Exception?) {
        tracingViewModel.refreshIsTracingEnabled()
    }

    private fun setButtonOnClickListener() {
        val switch = binding.settingsTracingSwitchRow.settingsSwitchRowSwitch
        val back = binding.settingsTracingHeader.headerButtonBack.buttonIcon
        val bluetooth = binding.settingsTracingStatusBluetooth.tracingStatusCardButton
        val connection = binding.settingsTracingStatusConnection.tracingStatusCardButton
        internalExposureNotificationPermissionHelper =
            InternalExposureNotificationPermissionHelper(this, this)
        switch.setOnClickListener {
            startStopTracing()
        }
        back.setOnClickListener {
            (activity as MainActivity).goBack()
        }
        bluetooth.setOnClickListener {
            ExternalActionHelper.toMainSettings(requireContext())
        }
        connection.setOnClickListener {
            ExternalActionHelper.toConnections(requireContext())
        }
    }

    private fun startStopTracing() {
        // if tracing is enabled when listener is activated it should be disabled
        lifecycleScope.launch {
            try {
                if (InternalExposureNotificationClient.asyncIsEnabled()) {
                    InternalExposureNotificationClient.asyncStop()
                    tracingViewModel.refreshIsTracingEnabled()
                    BackgroundWorkScheduler.stopWorkScheduler()
                } else {
                    // tracing was already activated
                    if (LocalData.initialTracingActivationTimestamp() != null) {
                        internalExposureNotificationPermissionHelper.requestPermissionToStartTracing()
                    } else {
                        // tracing was never activated
                        // ask for consent via dialog for initial tracing activation when tracing was not
                        // activated during onboarding
                        showConsentDialog()
                    }
                }
            } catch (exception: Exception) {
                tracingViewModel.refreshIsTracingEnabled()
                exception.report(
                    ExceptionCategory.EXPOSURENOTIFICATION,
                    TAG,
                    null
                )
            }
        }
    }

    private fun showConsentDialog() {
        val dialog = DialogHelper.DialogInstance(
            requireActivity(),
            R.string.onboarding_tracing_headline_consent,
            R.string.onboarding_tracing_body_consent,
            R.string.onboarding_button_enable,
            R.string.onboarding_button_cancel,
            true,
            {
                internalExposureNotificationPermissionHelper.requestPermissionToStartTracing()
            }, {
                tracingViewModel.refreshIsTracingEnabled()
            })
        DialogHelper.showDialog(dialog)
    }
}
