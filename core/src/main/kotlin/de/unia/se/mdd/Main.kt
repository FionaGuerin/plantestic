package de.unia.se.mdd

import com.google.common.io.Resources

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        runTransformationPipeline("/home/andi/Projects/SE-Master/mdd/core/src/test/resources/minimal_hello.puml")
    }

    fun runTransformationPipeline(inputUriString: String) {
        MetaModelSetup.doSetup()

        val pumlDiagramModel = PumlParser.parse(inputUriString)

        val requestResponsePairsModel = M2MTransformer.transformPuml2ReqRes(pumlDiagramModel)
        val restAssuredModel = M2MTransformer.transformReqRes2RestAssured(requestResponsePairsModel)

        // TODO: generate output
    }
}
