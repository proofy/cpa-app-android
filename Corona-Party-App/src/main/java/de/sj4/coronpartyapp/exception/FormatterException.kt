package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedException

class FormatterException(cause: Throwable?) :
    ReportedException(
        ErrorCodes.FORMATTER_PROBLEM.code,
        "exception occurred during formatting",
        cause
    ) {
    constructor() : this(null)
}
