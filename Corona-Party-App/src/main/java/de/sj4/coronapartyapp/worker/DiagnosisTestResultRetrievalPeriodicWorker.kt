package de.sj4.coronapartyapp.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import de.sj4.coronapartyapp.CoronaWarnApplication
import de.sj4.coronapartyapp.R
import de.sj4.coronapartyapp.notification.NotificationHelper
import de.sj4.coronapartyapp.service.submission.SubmissionService
import de.sj4.coronapartyapp.storage.LocalData
import de.sj4.coronapartyapp.util.TimeAndDateExtensions
import de.sj4.coronapartyapp.util.formatter.TestResult
import de.sj4.coronapartyapp.worker.BackgroundWorkScheduler.stop
import timber.log.Timber

/**
 * Diagnosis Test Result Periodic retrieavl
 *
 * @see BackgroundWorkScheduler
 */
class DiagnosisTestResultRetrievalPeriodicWorker(
    val context: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams) {

    companion object {
        private val TAG: String? = DiagnosisTestResultRetrievalPeriodicWorker::class.simpleName
    }

    /**
     * Work execution
     *
     * If background job is running for less than 21 days, testResult is checked.
     * If the job is running for more than 21 days, the job will be stopped
     *
     * @return Result
     *
     * @see LocalData.isTestResultNotificationSent
     * @see LocalData.initialPollingForTestResultTimeStamp
     */
    override suspend fun doWork(): Result {

        Timber.d("Background job started. Run attempt: $runAttemptCount")

        if (runAttemptCount > BackgroundConstants.WORKER_RETRY_COUNT_THRESHOLD) {
            Timber.d("Background job failed after $runAttemptCount attempts. Rescheduling")
            BackgroundWorkScheduler.scheduleDiagnosisKeyPeriodicWork()
            return Result.failure()
        }
        var result = Result.success()
        try {
            if (TimeAndDateExtensions.calculateDays(
                    LocalData.initialPollingForTestResultTimeStamp(),
                    System.currentTimeMillis()
                ) < BackgroundConstants.POLLING_VALIDITY_MAX_DAYS
            ) {
                val testResult = SubmissionService.asyncRequestTestResult()
                initiateNotification(testResult)
            } else {
                stopWorker()
            }
        } catch (e: Exception) {
            result = Result.retry()
        }
        return result
    }

    /**
     * Notification Initiation
     *
     * If the returned Test Result is Negative, Positive or Invalid
     * The Background polling  will be stopped
     * and a notification is shown, but only if the App is not in foreground
     *
     * @see LocalData.isTestResultNotificationSent
     * @see LocalData.initialPollingForTestResultTimeStamp
     * @see TestResult
     */
    private fun initiateNotification(testResult: TestResult) {
        if (testResult == TestResult.NEGATIVE || testResult == TestResult.POSITIVE ||
            testResult == TestResult.INVALID
        ) {
            if (!CoronaWarnApplication.isAppInForeground) {
                NotificationHelper.sendNotification(
                    CoronaWarnApplication.getAppContext()
                        .getString(R.string.notification_name), CoronaWarnApplication.getAppContext()
                        .getString(R.string.notification_body),
                    NotificationCompat.PRIORITY_HIGH
                )
            }
            LocalData.isTestResultNotificationSent(true)
            stopWorker()
        }
    }

    /**
     * Stops the Background Polling worker
     *
     * @see LocalData.initialPollingForTestResultTimeStamp
     * @see BackgroundWorkScheduler.stop

     */
    private fun stopWorker() {
        LocalData.initialPollingForTestResultTimeStamp(0L)
        BackgroundWorkScheduler.WorkType.DIAGNOSIS_TEST_RESULT_PERIODIC_WORKER.stop()
    }
}
