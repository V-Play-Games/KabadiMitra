package com.kabadimitra.collector.core.ml

data class MaterialClassificationResult(
    val topLabel: String,
    val hindiLabel: String,
    val confidence: Float,
    val isConfident: Boolean, // >= 0.85
    val alternateSuggestions: List<String>
)

class MaterialClassifier {

    private val supportedMaterials = listOf(
        "PET Plastic (बोतलें)" to "पीईटी प्लास्टिक",
        "Cardboard / गत्ता" to "गत्ता / रद्दी पुट्ठा",
        "Iron / Loha (कबाड़ लोहा)" to "कबाड़ लोहा",
        "Copper Wire (तांबा)" to "तांबा वायर",
        "Aluminium (एल्युमिनियम)" to "एल्युमिनियम",
        "HDPE Drums (कड़क प्लास्टिक)" to "एचडीपीई प्लास्टिक"
    )

    fun classifyImage(imagePath: String? = null): MaterialClassificationResult {
        // High-confidence MobileNetV3 classifier inference simulator
        val defaultMaterial = supportedMaterials[0]
        val confidence = 0.94f

        return MaterialClassificationResult(
            topLabel = defaultMaterial.first,
            hindiLabel = defaultMaterial.second,
            confidence = confidence,
            isConfident = confidence >= 0.85f,
            alternateSuggestions = listOf(
                "HDPE Drums (कड़क प्लास्टिक)",
                "Cardboard / गत्ता"
            )
        )
    }

    fun classifyWeightOcr(extractedText: String): Double? {
        // ML Kit Scale OCR reader logic - regex extracting decimal or integer weight numbers
        val weightRegex = Regex("""(\d+(\.\d+)?)""")
        val match = weightRegex.find(extractedText)
        return match?.value?.toDoubleOrNull()
    }
}
