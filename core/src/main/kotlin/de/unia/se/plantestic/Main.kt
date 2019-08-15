package de.unia.se.plantestic

import java.io.File
import java.nio.file.Paths

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        runTransformationPipeline(args[0], File(args[0] + "/generated-code"))
    }

    fun runTransformationPipeline(inputUriString: String, outputFolder: File) {
        MetaModelSetup.doSetup()

        val pumlDiagramModel = PumlParser.parse(inputUriString)

        val requestResponsePairsModel = M2MTransformer.transformPuml2ReqRes(pumlDiagramModel)
        val restAssuredModel = M2MTransformer.transformReqRes2RestAssured(requestResponsePairsModel)

        println("Generating code into $outputFolder")

        AcceleoCodeGenerator.generateCode(restAssuredModel, outputFolder)
    }
}
