package de.sj4.coronapartyapp.exception

class NoRegistrationTokenSetException :
    Exception("there is no valid registration token set in local storage")
