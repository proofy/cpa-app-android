package de.sj4.coronapartyapp.exception

class NoTokenException(
    cause: Throwable
) : Exception(
    "An error occurred during BroadcastReceiver onReceive function. No token found",
    cause
)
