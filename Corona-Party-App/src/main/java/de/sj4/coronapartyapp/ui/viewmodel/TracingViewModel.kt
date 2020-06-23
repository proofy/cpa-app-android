package de.sj4.coronapartyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.sj4.coronapartyapp.exception.ExceptionCategory.INTERNAL
import de.sj4.coronapartyapp.exception.TransactionException
import de.sj4.coronapartyapp.exception.reporting.report
import de.sj4.coronapartyapp.storage.ExposureSummaryRepository
import de.sj4.coronapartyapp.storage.RiskLevelRepository
import de.sj4.coronapartyapp.storage.TracingRepository
import de.sj4.coronapartyapp.timer.TimerHelper
import de.sj4.coronapartyapp.transaction.RiskLevelTransaction
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date

/**
 * Provides all the relevant data for tracing relevant topics and settings.
 * The public variables consume MutableLiveDate from two different repositories.
 * This variables are consumed in different views all over the application via bindings, e.g. "@{tracingViewModel.isTracingEnabled}"
 * Most of the values are used in formatters due to the different states the application can have.
 *
 * @see TracingRepository
 * @see RiskLevelRepository
 */
class TracingViewModel : ViewModel() {

    companion object {
        val TAG: String? = TracingViewModel::class.simpleName
    }

    // Values from RiskLevelRepository
    val riskLevel: LiveData<Int> = RiskLevelRepository.riskLevelScore
    val riskLevelScoreLastSuccessfulCalculated =
        RiskLevelRepository.riskLevelScoreLastSuccessfulCalculated

    // Values from ExposureSummaryRepository
    val daysSinceLastExposure: LiveData<Int?> = ExposureSummaryRepository.daysSinceLastExposure
    val matchedKeyCount: LiveData<Int?> = ExposureSummaryRepository.matchedKeyCount

    // Values from TracingRepository
    val lastTimeDiagnosisKeysFetched: LiveData<Date> =
        TracingRepository.lastTimeDiagnosisKeysFetched
    val isTracingEnabled: LiveData<Boolean?> = TracingRepository.isTracingEnabled
    val activeTracingDaysInRetentionPeriod = TracingRepository.activeTracingDaysInRetentionPeriod
    var isRefreshing: LiveData<Boolean> = TracingRepository.isRefreshing

    /**
     * Launches the RiskLevelTransaction in the viewModel scope
     *
     * @see RiskLevelTransaction
     * @see RiskLevelRepository
     */
    fun refreshRiskLevel() {
        viewModelScope.launch {
            try {
                RiskLevelTransaction.start()
            } catch (e: TransactionException) {
                e.cause?.report(INTERNAL)
            }
        }
    }

    /**
     * Refreshes the time when the diagnosis key was fetched the last time
     *
     * @see TracingRepository
     */
    fun refreshLastTimeDiagnosisKeysFetchedDate() {
        TracingRepository.refreshLastTimeDiagnosisKeysFetchedDate()
    }

    /**
     * Refreshes the diagnosis keys
     *
     * @see TracingRepository
     */
    fun refreshDiagnosisKeys() {
        this.viewModelScope.launch {
            TracingRepository.refreshDiagnosisKeys()
            TimerHelper.startManualKeyRetrievalTimer()
        }
    }

    /**
     * Refreshes is tracing enabled
     *
     * @see TracingRepository
     */
    fun refreshIsTracingEnabled() {
        viewModelScope.launch {
            TracingRepository.refreshIsTracingEnabled()
        }
    }

    /**
     * Exposure summary
     * Refresh the following variables in TracingRepository
     * - daysSinceLastExposure
     * - matchedKeysCount
     *
     * @see TracingRepository
     */
    fun refreshExposureSummary() {
        viewModelScope.launch {
            try {
                ExposureSummaryRepository.getExposureSummaryRepository()
                    .getLatestExposureSummary()
                Timber.v("retrieved latest exposure summary from db")
            } catch (e: Exception) {
                e.report(
                    de.sj4.coronapartyapp.exception.ExceptionCategory.EXPOSURENOTIFICATION,
                    TAG,
                    null
                )
            }
        }
    }

    /**
     * Refresh the activeTracingDaysInRetentionPeriod in the viewModel scope
     *
     * @see TracingRepository
     */
    fun refreshActiveTracingDaysInRetentionPeriod() {
        viewModelScope.launch {
            TracingRepository.refreshActiveTracingDaysInRetentionPeriod()
        }
    }

    fun refreshLastSuccessfullyCalculatedScore() {
        RiskLevelRepository.refreshLastSuccessfullyCalculatedScore()
    }
}
