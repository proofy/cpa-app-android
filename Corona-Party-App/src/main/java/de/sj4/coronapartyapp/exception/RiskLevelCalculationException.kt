package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedException

class RiskLevelCalculationException(cause: Throwable) :
    ReportedException(
        ErrorCodes.RISK_LEVEL_CALCULATION_PROBLEM.code,
        "an exception occurred during risk level calculation", cause
    )
