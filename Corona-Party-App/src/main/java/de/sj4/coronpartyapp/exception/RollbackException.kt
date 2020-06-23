package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedException
import java.util.UUID

/**
 * An Exception thrown when an error occurs inside the Rollback of a Transaction.
 *
 * @param transactionId the atomic Transaction ID
 * @param state the atomic Transaction state (defined in the Transaction) with a valid ToString
 * @param cause the cause of the error
 *
 * @see de.sj4.coronapartyapp.transaction.Transaction
 */
class RollbackException(transactionId: UUID, state: String, cause: Throwable?) :
    ReportedException(
        ErrorCodes.ROLLBACK_PROBLEM.code,
        "An error occurred during rollback of transaction $transactionId, State $state",
        IllegalStateException(
            "the state before the transaction state could not be restored.",
            cause
        )
    )
