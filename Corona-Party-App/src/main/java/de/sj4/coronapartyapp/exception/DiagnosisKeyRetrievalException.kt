package de.sj4.coronapartyapp.exception

/**
 * An Exception thrown when an error occurs during Key Retrieval from the Server
 *
 * @param cause the cause of the error
 *
 * @see DiagnosisKeyServiceException
 */
class DiagnosisKeyRetrievalException(cause: Throwable) :
    DiagnosisKeyServiceException("exception occurred during retrieval of diagnosis keys", cause)
