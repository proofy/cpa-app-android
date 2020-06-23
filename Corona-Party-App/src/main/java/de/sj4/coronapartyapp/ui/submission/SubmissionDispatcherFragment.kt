package de.sj4.coronapartyapp.ui.submission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import de.sj4.coronapartyapp.R
import de.sj4.coronapartyapp.databinding.FragmentSubmissionDispatcherBinding
import de.sj4.coronapartyapp.ui.doNavigate
import de.sj4.coronapartyapp.ui.main.MainActivity
import de.sj4.coronapartyapp.util.DialogHelper

class SubmissionDispatcherFragment : Fragment() {

    companion object {
        private val TAG: String? = SubmissionDispatcherFragment::class.simpleName
    }

    private var _binding: FragmentSubmissionDispatcherBinding? = null
    private val binding: FragmentSubmissionDispatcherBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubmissionDispatcherBinding.inflate(inflater)
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
        binding.submissionDispatcherRoot.sendAccessibilityEvent(AccessibilityEvent.TYPE_ANNOUNCEMENT)
    }

    private fun setButtonOnClickListener() {
        binding.submissionDispatcherHeader.headerButtonBack.buttonIcon.setOnClickListener {
            (activity as MainActivity).goBack()
        }
        binding.submissionDispatcherContent.submissionDispatcherQr.dispatcherCard.setOnClickListener {
            checkForDataPrivacyPermission()
        }
        binding.submissionDispatcherContent.submissionDispatcherTanCode.dispatcherCard.setOnClickListener {
            findNavController().doNavigate(
                SubmissionDispatcherFragmentDirections
                    .actionSubmissionDispatcherFragmentToSubmissionTanFragment()
            )
        }
        binding.submissionDispatcherContent.submissionDispatcherTanTele.dispatcherCard.setOnClickListener {
            findNavController().doNavigate(
                SubmissionDispatcherFragmentDirections
                    .actionSubmissionDispatcherFragmentToSubmissionContactFragment()
            )
        }
    }

    private fun checkForDataPrivacyPermission() {
        val cameraPermissionRationaleDialogInstance = DialogHelper.DialogInstance(
            requireActivity(),
            R.string.submission_dispatcher_qr_privacy_dialog_headline,
            R.string.submission_dispatcher_qr_privacy_dialog_body,
            R.string.submission_dispatcher_qr_privacy_dialog_button_positive,
            R.string.submission_dispatcher_qr_privacy_dialog_button_negative,
            true,
            {
                privacyPermissionIsGranted()
            }
        )

        DialogHelper.showDialog(cameraPermissionRationaleDialogInstance)
    }

    private fun privacyPermissionIsGranted() {
        findNavController().doNavigate(
            SubmissionDispatcherFragmentDirections
                .actionSubmissionDispatcherFragmentToSubmissionQRCodeScanFragment()
        )
    }
}
