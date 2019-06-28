package de.unia.se.mdd

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        MetaModelSetup.doSetup()

        // TODO: Parse PlantUML input file
        // private val INPUT_URI = URI.createFileURI(Resources.getResource("minimal_hello.puml").path)
        // val pumlInputModel = resultSet.getResource(INPUT_URI, true)

        // TODO: Transform PlantUML AST to RequestResponse-Pairs
        // M2MTransformer.transformPuml2ReqRes()

        // TODO: Transform RequestResponse-Pairs to RestAssured AST
        // M2MTransformer.transformReqRes2RestAssured()

        // TODO: Generate test files from RestAssured AST with Accelio
    }
}
