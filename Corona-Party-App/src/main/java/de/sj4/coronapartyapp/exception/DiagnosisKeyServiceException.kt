package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedException

/**
 * An Exception thrown when an error occurs inside the DiagnosisKeyService
 *
 * @param message an Exception thrown inside the DiagnosisKeyService
 * @param cause the cause of the error
 *
 * @see DiagnosisKeySubmissionException
 * @see DiagnosisKeyRetrievalException
 */
abstract class DiagnosisKeyServiceException(message: String, cause: Throwable) :
    ReportedException(ErrorCodes.DIAGNOSIS_KEY_SERVICE_PROBLEM.code, message, cause)
