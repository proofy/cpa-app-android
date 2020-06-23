package de.sj4.coronapartyapp.ui.submission

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import de.sj4.coronapartyapp.storage.SubmissionRepository
import de.sj4.coronapartyapp.util.TanHelper
import timber.log.Timber

class SubmissionTanViewModel : ViewModel() {

    companion object {
        private val TAG: String? = SubmissionTanViewModel::class.simpleName
    }

    val tan = MutableLiveData<String?>(null)

    val isValidTanFormat =
        Transformations.map(tan) {
            it != null &&
                    it.length == TanConstants.MAX_LENGTH &&
                    TanHelper.isChecksumValid(it) &&
                    TanHelper.allCharactersValid(it)
        }

    fun storeTeletan() {
        val teletan = tan.value!!
        Timber.d("Storing teletan $teletan")
        SubmissionRepository.setTeletan(teletan)
    }
}
