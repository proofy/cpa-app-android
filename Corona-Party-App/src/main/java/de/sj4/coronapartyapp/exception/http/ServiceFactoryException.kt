package de.sj4.coronapartyapp.exception.http

class ServiceFactoryException(cause: Throwable) :
    Exception("error inside the service factory", cause)
