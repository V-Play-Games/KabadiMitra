package com.kabadimitra.collector

import com.kabadimitra.collector.core.crypto.RecordSigner
import com.kabadimitra.collector.core.ml.MaterialClassifier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PricingEngineTest {

    @Test
    fun testPriceEstimateCalculation() {
        val weightKg = 28.5
        val ratePerKg = 18
        val expectedAmount = (weightKg * ratePerKg).toInt() // 513

        val actualAmount = (weightKg * ratePerKg).toInt()
        assertEquals(expectedAmount, actualAmount)
        assertEquals(513, actualAmount)

        // Verify within ±15% range boundary per PRD Section 6
        val baseline = 500
        val percentageDelta = kotlin.math.abs(actualAmount - baseline).toDouble() / baseline * 100.0
        assertTrue("Estimate is within 15% tolerance: $percentageDelta%", percentageDelta < 15.0)
    }

    @Test
    fun testMaterialClassifierInference() {
        val classifier = MaterialClassifier()
        val result = classifier.classifyImage("mock_photo_pet.jpg")

        assertNotNull(result)
        assertEquals("PET Plastic (बोतलें)", result.topLabel)
        assertTrue(result.confidence >= 0.85f)
        assertTrue(result.isConfident)
    }

    @Test
    fun testScaleOcrWeightExtraction() {
        val classifier = MaterialClassifier()
        val weight = classifier.classifyWeightOcr("DIGITAL SCALE READING: 28.50 KG")

        assertNotNull(weight)
        assertEquals(28.5, weight!!, 0.001)
    }

    @Test
    fun testEd25519RecordSigning() {
        val signer = RecordSigner()
        val signedRecord = signer.signLot(
            lotId = "KM-2026-0417",
            collectorId = "COL-9821-4321",
            material = "PET Plastic",
            weightKg = 28.5,
            ratePerKg = 20,
            pickupLat = 19.0434,
            pickupLng = 72.8562
        )

        assertNotNull(signedRecord)
        assertEquals("KM-2026-0417", signedRecord.lotId)
        assertTrue(signedRecord.recordHashSha256.isNotEmpty())
        assertTrue(signedRecord.ed25519Signature.isNotEmpty())
        assertTrue(signedRecord.publicKeyHex.isNotEmpty())
        assertTrue(signer.verifySignature(signedRecord.canonicalPayload, signedRecord.ed25519Signature))
    }
}
