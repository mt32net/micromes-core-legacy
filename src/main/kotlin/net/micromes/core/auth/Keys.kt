package net.micromes.core.auth

import java.io.File
import java.io.FileInputStream
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

fun loadKey() : PublicKey {

    val dateiPubK = File("keys/public.key")

    var fis = FileInputStream(dateiPubK)
    val encodedPublicKey = ByteArray(dateiPubK.length().toInt())
    fis.read(encodedPublicKey)
    fis.close()

    var keyFactory = KeyFactory.getInstance("RSA")
    val publicKeySpec = X509EncodedKeySpec(encodedPublicKey)
    val publicKey = keyFactory.generatePublic(publicKeySpec)

    return publicKey
}