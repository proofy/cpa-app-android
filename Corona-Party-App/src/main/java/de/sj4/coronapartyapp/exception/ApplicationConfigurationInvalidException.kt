package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedException

class ApplicationConfigurationInvalidException : ReportedException(
    ErrorCodes.APPLICATION_CONFIGURATION_INVALID.code, "the application configuration is invalid"
)
