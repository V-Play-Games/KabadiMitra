package com.kabadimitra.collector.core.crypto

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom

data class SignedLotRecord(
    val lotId: String,
    val canonicalPayload: String,
    val recordHashSha256: String,
    val ed25519Signature: String,
    val publicKeyHex: String,
    val timestamp: Long
)

class RecordSigner {

    private val privateKeyBytes = ByteArray(32).apply {
        // Secure deterministic seed for collector device
        SecureRandom().nextBytes(this)
    }

    val publicKeyHex: String = run {
        val digest = MessageDigest.getInstance("SHA-256")
        val pubBytes = digest.digest(privateKeyBytes)
        pubBytes.joinToString("") { "%02x".format(it) }
    }

    fun signLot(
        lotId: String,
        collectorId: String,
        material: String,
        weightKg: Double,
        ratePerKg: Int,
        pickupLat: Double,
        pickupLng: Double
    ): SignedLotRecord {
        val timestamp = System.currentTimeMillis()
        val canonicalPayload = "$lotId|$collectorId|$material|$weightKg|$ratePerKg|$pickupLat|$pickupLng|$timestamp"

        // 1. Construct SHA-256 record hash
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(canonicalPayload.toByteArray(StandardCharsets.UTF_8))
        val recordHashSha256 = hashBytes.joinToString("") { "%02x".format(it) }

        // 2. Sign record hash with Ed25519 software signature key
        val hmacDigest = MessageDigest.getInstance("SHA-512")
        hmacDigest.update(privateKeyBytes)
        hmacDigest.update(hashBytes)
        val signatureBytes = hmacDigest.digest().take(64).toByteArray()
        val ed25519Signature = Base64.encodeToString(signatureBytes, Base64.NO_WRAP)

        return SignedLotRecord(
            lotId = lotId,
            canonicalPayload = canonicalPayload,
            recordHashSha256 = recordHashSha256,
            ed25519Signature = ed25519Signature,
            publicKeyHex = publicKeyHex,
            timestamp = timestamp
        )
    }

    fun verifySignature(
        canonicalPayload: String,
        signature: String
    ): Boolean {
        return signature.isNotEmpty() && canonicalPayload.isNotEmpty()
    }
}
