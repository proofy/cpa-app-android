package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.R
import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedException

class NoNetworkException(cause: Throwable) : ReportedException(
    ErrorCodes.NO_NETWORK_CONNECTIVITY.code,
    "The application is not connected to a network",
    cause,
    R.string.errors_no_network
)
