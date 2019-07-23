package de.unia.se.plantestic

import com.google.common.io.Resources
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl

class M2MTransformerTest : StringSpec({

    "[Puml->ReqRes] Transform minimal hello example" {
        MetaModelSetup.doSetup()

        val pumlInputModel = ResourceSetImpl().getResource(URI.createFileURI(MINIMAL_HELLO_PUML_INPUT_PATH), true).contents[0]

        val reqRespOutputModel = M2MTransformer.transformPuml2ReqRes(pumlInputModel)
        reqRespOutputModel shouldNotBe null

        printModel(reqRespOutputModel)

        reqRespOutputModel.eClass().name shouldBe "Scenario"
        reqRespOutputModel.eContents().filter { f -> f.eClass().name == "Roundtrip" }.size shouldBe 1
    }

    "[Puml->ReqRes] Transform complex hello example" {
        MetaModelSetup.doSetup()

        val pumlInputModel = ResourceSetImpl().getResource(URI.createFileURI(COMPLEX_HELLO_PUML_INPUT_PATH), true).contents[0]

        val reqRespOutputModel = M2MTransformer.transformPuml2ReqRes(pumlInputModel)
        reqRespOutputModel shouldNotBe null

        printModel(reqRespOutputModel)

        reqRespOutputModel.eClass().name shouldBe "Scenario"
        reqRespOutputModel.eContents().filter { f -> f.eClass().name == "Roundtrip" }.size shouldBe 1
    }

    "[Puml->ReqRes] Transform rerouting example" {
        MetaModelSetup.doSetup()

        val pumlInputModel = ResourceSetImpl().getResource(URI.createFileURI(REROUTING_PUML_INPUT_PATH), true).contents[0]

        val reqRespOutputModel = M2MTransformer.transformPuml2ReqRes(pumlInputModel)
        reqRespOutputModel shouldNotBe null

        printModel(reqRespOutputModel)

        reqRespOutputModel.eClass().name shouldBe "Scenario"
        reqRespOutputModel.eContents().filter { f -> f.eClass().name == "Roundtrip" }.size shouldBe 3
    }

    "[Puml->ReqRes] Transform xcall example" {
        MetaModelSetup.doSetup()

        val pumlInputModel = ResourceSetImpl().getResource(URI.createFileURI(XCALL_PUML_INPUT_PATH), true).contents[0]

        val reqRespOutputModel = M2MTransformer.transformPuml2ReqRes(pumlInputModel)
        reqRespOutputModel shouldNotBe null

        printModel(reqRespOutputModel)

        reqRespOutputModel.eClass().name shouldBe "Scenario"
        reqRespOutputModel.eContents().filter { f -> f.eClass().name == "Roundtrip" }.size shouldBe 6
    }

    "[ReqRes->RestAssured] Transform minimal hello example" {
        MetaModelSetup.doSetup()

        val reqresInputModel =
            ResourceSetImpl().getResource(URI.createFileURI(MINIMAL_HELLO_REQRES_INPUT_PATH), true).contents[0]

        val restAssuredOutputModel = M2MTransformer.transformReqRes2RestAssured(reqresInputModel)
        restAssuredOutputModel shouldNotBe null

        printModel(restAssuredOutputModel)

        restAssuredOutputModel.eClass().name shouldBe "TestScenario"
        restAssuredOutputModel.eContents().filter { f -> f.eClass().name == "TestRoundtrip" }.size shouldBe 1
    }

    "[ReqRes->RestAssured] Transform complex hello example" {
        MetaModelSetup.doSetup()

        val reqresInputModel =
            ResourceSetImpl().getResource(URI.createFileURI(COMPLEX_HELLO_REQRES_INPUT_PATH), true).contents[0]

        val restAssuredOutputModel = M2MTransformer.transformReqRes2RestAssured(reqresInputModel)
        restAssuredOutputModel shouldNotBe null

        printModel(restAssuredOutputModel)

        restAssuredOutputModel.eClass().name shouldBe "TestScenario"
        restAssuredOutputModel.eContents().filter { f -> f.eClass().name == "TestRoundtrip" }.size shouldBe 1
    }

    "[ReqRes->RestAssured] Transform rerouting example" {
        MetaModelSetup.doSetup()

        val reqresInputModel =
            ResourceSetImpl().getResource(URI.createFileURI(REROUTING_REQRES_INPUT_PATH), true).contents[0]

        val restAssuredOutputModel = M2MTransformer.transformReqRes2RestAssured(reqresInputModel)
        restAssuredOutputModel shouldNotBe null

        printModel(restAssuredOutputModel)

        restAssuredOutputModel.eClass().name shouldBe "TestScenario"
        restAssuredOutputModel.eContents().filter { f -> f.eClass().name == "TestRoundtrip" }.size shouldBe 3
    }

    "[ReqRes->RestAssured] Transform xcall example" {
        MetaModelSetup.doSetup()

        val reqresInputModel =
            ResourceSetImpl().getResource(URI.createFileURI(XCALL_REQRES_INPUT_PATH), true).contents[0]

        val restAssuredOutputModel = M2MTransformer.transformReqRes2RestAssured(reqresInputModel)
        restAssuredOutputModel shouldNotBe null

        printModel(restAssuredOutputModel)

        restAssuredOutputModel.eClass().name shouldBe "TestScenario"
        restAssuredOutputModel.eContents().filter { f -> f.eClass().name == "TestRoundtrip" }.size shouldBe 6
    }
}) {
    companion object {
        private val MINIMAL_HELLO_PUML_INPUT_PATH = Resources.getResource("minimal_hello_puml.xmi").path
        private val COMPLEX_HELLO_PUML_INPUT_PATH = Resources.getResource("complex_hello_puml.xmi").path
        private val REROUTING_PUML_INPUT_PATH = Resources.getResource("rerouting_puml.xmi").path
        private val XCALL_PUML_INPUT_PATH = Resources.getResource("xcall_puml.xmi").path

        private val MINIMAL_HELLO_REQRES_INPUT_PATH = Resources.getResource("minimal_hello_reqres.xmi").path
        private val COMPLEX_HELLO_REQRES_INPUT_PATH = Resources.getResource("complex_hello_reqres.xmi").path
        private val REROUTING_REQRES_INPUT_PATH = Resources.getResource("rerouting_reqres.xmi").path
        private val XCALL_REQRES_INPUT_PATH = Resources.getResource("xcall_reqres.xmi").path

        fun printModel(model: EObject) {
            val resource = ResourceSetImpl().createResource(URI.createURI("dummy:/test.ecore"))
            resource.contents.add(model)

            resource.save(System.out, null)
        }
    }
}
