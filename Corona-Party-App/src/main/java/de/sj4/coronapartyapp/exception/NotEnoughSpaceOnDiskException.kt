package de.sj4.coronapartyapp.exception

import de.sj4.coronapartyapp.R
import de.sj4.coronapartyapp.exception.reporting.ErrorCodes
import de.sj4.coronapartyapp.exception.reporting.ReportedException

class NotEnoughSpaceOnDiskException(cause: Throwable? = null) : ReportedException(
    ErrorCodes.NOT_ENOUGH_AVAILABLE_SPACE_ON_DISK.code,
    "the app detected that not enough storage space is available for the required operation",
    cause,
    R.string.errors_not_enough_device_storage
)
