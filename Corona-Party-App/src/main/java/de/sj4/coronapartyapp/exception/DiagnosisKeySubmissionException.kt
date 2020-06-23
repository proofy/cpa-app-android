package de.sj4.coronapartyapp.exception

/**
 * An Exception thrown when an error occurs during Key Reporting to the Server
 *
 * @param cause the cause of the error
 *
 * @see DiagnosisKeyServiceException
 */
class DiagnosisKeySubmissionException(cause: Throwable) :
    DiagnosisKeyServiceException("exception occurred during reporting of diagnosis keys", cause)
