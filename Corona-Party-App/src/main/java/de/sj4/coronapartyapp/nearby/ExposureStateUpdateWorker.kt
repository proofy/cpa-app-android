package de.sj4.coronapartyapp.nearby

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.nearby.exposurenotification.ExposureNotificationClient
import de.sj4.coronapartyapp.exception.ExceptionCategory
import de.sj4.coronapartyapp.exception.NoTokenException
import de.sj4.coronapartyapp.exception.TransactionException
import de.sj4.coronapartyapp.exception.reporting.report
import de.sj4.coronapartyapp.storage.ExposureSummaryRepository
import de.sj4.coronapartyapp.transaction.RiskLevelTransaction
import timber.log.Timber

class ExposureStateUpdateWorker(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    companion object {
        private val TAG = ExposureStateUpdateWorker::class.simpleName
    }

    override suspend fun doWork(): Result {
        try {
            Timber.v("worker to persist exposure summary started")
            val token = inputData.getString(ExposureNotificationClient.EXTRA_TOKEN)
                ?: throw NoTokenException(IllegalArgumentException("no token was found in the intent"))

            Timber.v("valid token $token retrieved")

            val exposureSummary = InternalExposureNotificationClient
                .asyncGetExposureSummary(token)

            ExposureSummaryRepository.getExposureSummaryRepository()
                .insertExposureSummaryEntity(exposureSummary)
            Timber.v("exposure summary state updated: $exposureSummary")

            RiskLevelTransaction.start()
            Timber.v("risk level calculation triggered")
        } catch (e: ApiException) {
            e.report(ExceptionCategory.EXPOSURENOTIFICATION)
        } catch (e: TransactionException) {
            e.report(ExceptionCategory.INTERNAL)
        }

        return Result.success()
    }
}
