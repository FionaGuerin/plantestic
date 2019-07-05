package de.unia.se.mdd

import org.eclipse.emf.common.util.BasicMonitor
import java.io.File
import java.util.ArrayList

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        // runTransformationPipeline()
    }

    fun runTransformationPipeline(inputUriString: String) {
        MetaModelSetup.doSetup()

        val pumlDiagramModel = PumlParser.parse(inputUriString)

        val requestResponsePairsModel = M2MTransformer.transformPuml2ReqRes(pumlDiagramModel)
        val restAssuredModel = M2MTransformer.transformReqRes2RestAssured(requestResponsePairsModel)

        val arguments = ArrayList<String>()
        val targetFolder = File("generatedCode")
        //val generator = GenerateAcceleo()
        GenerateAcceleo.init(restAssuredModel, targetFolder, arguments)
        //val generator = GenerateAcceleo(restAssuredModel, targetFolder, arguments)

        GenerateAcceleo.doGenerate(BasicMonitor())
    }
}
