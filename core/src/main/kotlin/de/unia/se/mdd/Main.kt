package de.unia.se.mdd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt

class Pipeliner : CliktCommand() {

    val inputPath: String? by option(help = "Input path")

    override fun run() {
       runTransformationPipeline(inputPath.toString())
    }

    fun runTransformationPipeline(inputUriString: String) {
        MetaModelSetup.doSetup()

        val pumlDiagramModel = PumlParser.parse(inputUriString)

        val requestResponsePairsModel = M2MTransformer.transformPuml2ReqRes(pumlDiagramModel)
        val restAssuredModel = M2MTransformer.transformReqRes2RestAssured(requestResponsePairsModel)

        // TODO: generate output
    }
}

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        Pipeliner().main(args)
    }
}
