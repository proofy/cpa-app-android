package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedIOException

class CwaWebSecurityException(cause: Throwable) : ReportedIOException(
    ErrorCodes.CWA_WEB_SECURITY_PROBLEM.code,
    "an error occurred while trying to establish a secure connection to the server",
    cause
)
