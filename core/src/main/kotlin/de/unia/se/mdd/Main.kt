package de.unia.se.mdd

import com.google.common.io.Resources
import java.io.File

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        runTransformationPipeline(args[0])
    }

    fun runTransformationPipeline(inputUriString: String) {
        MetaModelSetup.doSetup()

        val pumlDiagramModel = PumlParser.parse(inputUriString)

        val requestResponsePairsModel = M2MTransformer.transformPuml2ReqRes(pumlDiagramModel)
        val restAssuredModel = M2MTransformer.transformReqRes2RestAssured(requestResponsePairsModel)

        val outputFolder = File(Resources.getResource("code-generation").path + "/generatedCode")
        AcceleoCodeGenerator.generateCode(restAssuredModel, outputFolder)
    }
}
