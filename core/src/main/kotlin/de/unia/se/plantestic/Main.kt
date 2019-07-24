package de.unia.se.plantestic

import java.io.File
import java.nio.file.Paths

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

        val IS_WINDOWS = System.getProperty( "os.name" ).contains( "indow" );
        val osAppropriatePath = if (IS_WINDOWS) inputUriString.substring(1) else inputUriString
        val outputFolder = Paths.get(osAppropriatePath).toAbsolutePath().parent.toString() + "/generatedCode"
        println("Generating code into $outputFolder")

        AcceleoCodeGenerator.generateCode(restAssuredModel, File(outputFolder))
    }
}
