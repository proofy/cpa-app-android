package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedException

class ENPermissionException :
    ReportedException(
        ErrorCodes.EN_PERMISSION_PROBLEM.code,
        "user did not grant the exposure notification permission"
    )
