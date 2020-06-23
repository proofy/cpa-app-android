package de.sj4.coronapartyapp.ui.submission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import de.sj4.coronapartyapp.R
import de.sj4.coronapartyapp.databinding.FragmentSubmissionContactBinding
import de.sj4.coronapartyapp.ui.doNavigate
import de.sj4.coronapartyapp.ui.main.MainActivity
import de.sj4.coronapartyapp.util.ExternalActionHelper

/**
 * The [SubmissionContactFragment] allows requesting a teletan via phone
 */
class SubmissionContactFragment : Fragment() {

    private var _binding: FragmentSubmissionContactBinding? = null
    private val binding: FragmentSubmissionContactBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // get the binding reference by inflating it with the current layout
        _binding = FragmentSubmissionContactBinding.inflate(inflater)
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
        binding.submissionContactRoot.sendAccessibilityEvent(AccessibilityEvent.TYPE_ANNOUNCEMENT)
    }

    private fun setButtonOnClickListener() {
        binding.submissionContactHeader.headerButtonBack.buttonIcon.setOnClickListener {
            (activity as MainActivity).goBack()
        }
        binding.submissionContactButtonCall.setOnClickListener {
            dial()
        }
        binding.submissionContactButtonEnter.setOnClickListener {
            findNavController().doNavigate(
                SubmissionContactFragmentDirections.actionSubmissionContactFragmentToSubmissionTanFragment()
            )
        }
    }

    private fun dial() = context?.let {
        val number = getString(R.string.submission_contact_number_dial)
        ExternalActionHelper.call(this, number)
    }
}
