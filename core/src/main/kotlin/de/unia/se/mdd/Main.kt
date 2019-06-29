package de.unia.se.mdd

import com.google.common.io.Resources
import org.eclipse.emf.common.util.URI

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        //runTransformationPipeline()
    }

    fun runTransformationPipeline(input: URI) {
        MetaModelSetup.doSetup()

        val parser = PumlParser()
        val pumlDiagramModel = parser.parse(input)

        val requestResponsePairsModel = M2MTransformer.transformPuml2ReqRes(pumlDiagramModel)
        val restAssuredModel = M2MTransformer.transformReqRes2RestAssured(requestResponsePairsModel)

        // TODO: generate output
    }
}
