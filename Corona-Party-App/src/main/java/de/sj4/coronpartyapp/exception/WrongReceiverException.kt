package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedException

/**
 * An Exception thrown when an error occurs inside the Rollback of a Transaction.
 *
 * @param action the received action in the BroadcastReceiver
 * @param expected the expected action
 * @param cause the cause of the error
 *
 * @see de.sj4.coronapartyapp.receiver.ExposureStateUpdateReceiver
 */
class WrongReceiverException(
    action: String?,
    expected: String,
    cause: Throwable
) : ReportedException(
    ErrorCodes.WRONG_RECEIVER_PROBLEM.code,
    "An error occurred during BroadcastReceiver onReceive function. " +
            "Received action was $action, expected action was $expected",
    cause
)
