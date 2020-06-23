package de.sj4.coronapartyapp.util

import KeyExportFormat.SignatureInfo

object SignatureHelper {
    val clientSig: SignatureInfo = SignatureInfo.newBuilder()
        .setAndroidPackage("de.sj4.coronapartyapp")
        .setAppBundleId("de.sj4.coronapartyapp")
        .setSignatureAlgorithm("ECDSA")
        .build()
}
