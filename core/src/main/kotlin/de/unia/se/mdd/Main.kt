package de.unia.se.mdd

// import com.google.common.io.Resources

// import java.io.File

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        runTransformationPipeline("/home/andi/Projects/SE-Master/mdd/core/src/test/resources/minimal_hello.puml", "/home/andi/Projects/SE-Master/mdd/core/src/test/resources/code-generation/generatedCode")
    }

    fun runTransformationPipeline(inputUriString: String, outputPath: String) {
        MetaModelSetup.doSetup()

        val pumlDiagramModel = PumlParser.parse(inputUriString)

        val requestResponsePairsModel = M2MTransformer.transformPuml2ReqRes(pumlDiagramModel)
        val restAssuredModel = M2MTransformer.transformReqRes2RestAssured(requestResponsePairsModel)

        // AcceleoCodeGenerator.generateCode(restAssuredModel, File(outputPath))
    }
}
