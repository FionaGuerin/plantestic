package de.unia.se.mdd

import java.io.File

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        // runTransformationPipeline()
    }

    fun runTransformationPipeline(inputUriString: String, outputPath: String) {
        MetaModelSetup.doSetup()

        val pumlDiagramModel = PumlParser.parse(inputUriString)

        val requestResponsePairsModel = M2MTransformer.transformPuml2ReqRes(pumlDiagramModel)
        val restAssuredModel = M2MTransformer.transformReqRes2RestAssured(requestResponsePairsModel)

        AcceleoCodeGenerator.generateCode(restAssuredModel, File(outputPath))
    }
}
