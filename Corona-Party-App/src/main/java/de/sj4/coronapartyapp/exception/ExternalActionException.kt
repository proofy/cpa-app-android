package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.R
import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedException

class ExternalActionException(cause: Throwable) : ReportedException(
    ErrorCodes.EXTERNAL_NAVIGATION.code,
    "Error during external navigation, likely due to bad target / action not available",
    cause,
    R.string.errors_external_action
)
