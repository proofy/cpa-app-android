package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedException

class ApplicationConfigurationCorruptException : ReportedException(
    ErrorCodes.APPLICATION_CONFIGURATION_CORRUPT.code, "the application configuration is corrupt"
)
