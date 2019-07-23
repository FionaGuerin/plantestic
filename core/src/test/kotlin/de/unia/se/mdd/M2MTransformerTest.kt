package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl

class M2MTransformerTest : StringSpec({

    "Transform a simple Puml input to Request Response Pairs" {
        MetaModelSetup.doSetup()

        val pumlInputModel = ResourceSetImpl().getResource(URI.createFileURI(MINIMAL_HELLO_PUML_INPUT_PATH), true).contents[0]

        val reqRespOutputModel = M2MTransformer.transformPuml2ReqRes(pumlInputModel)
        reqRespOutputModel shouldNotBe null

        printModel(reqRespOutputModel)

        reqRespOutputModel.eClass().name shouldBe "Scenario"
        reqRespOutputModel.eClass().eAllStructuralFeatures.filter { f -> f.name == "roundtrip" }.size shouldBe 1
    }

    "Transform the rerouting Puml input to Request Response Pairs" {
        MetaModelSetup.doSetup()

        val pumlInputModel = ResourceSetImpl().getResource(URI.createFileURI(REROUTING_PUML_INPUT_PATH), true).contents[0]

        val reqRespOutputModel = M2MTransformer.transformPuml2ReqRes(pumlInputModel)
        reqRespOutputModel shouldNotBe null

        printModel(reqRespOutputModel)

        reqRespOutputModel.eClass().name shouldBe "Scenario"
//        reqRespOutputModel.eClass().eAllStructuralFeatures.filter { f -> f.name == "roundtrip" }.size shouldBe 3
    }

    "Transform the xcall Puml input to Request Response Pairs" {
        MetaModelSetup.doSetup()

        val pumlInputModel = ResourceSetImpl().getResource(URI.createFileURI(XCALL_PUML_INPUT_PATH), true).contents[0]

        val reqRespOutputModel = M2MTransformer.transformPuml2ReqRes(pumlInputModel)
        reqRespOutputModel shouldNotBe null

        printModel(reqRespOutputModel)

        reqRespOutputModel.eClass().name shouldBe "Scenario"
//        reqRespOutputModel.eClass().eAllStructuralFeatures.filter { f -> f.name == "roundtrip" }.size shouldBe 6
    }

    "Transform a simple Request Response Pair input to a Rest Assured EObject" {
        MetaModelSetup.doSetup()

        val reqresInputModel =
            ResourceSetImpl().getResource(URI.createFileURI(MINIMAL_HELLO_REQRES_INPUT_PATH), true).contents[0]

        val restAssuredOutputModel = M2MTransformer.transformReqRes2RestAssured(reqresInputModel)
        restAssuredOutputModel shouldNotBe null

        printModel(restAssuredOutputModel)
    }

    "Transform complex Request Response Pair input to a Rest Assured EObject" {
        MetaModelSetup.doSetup()

        val reqresInputModel =
            ResourceSetImpl().getResource(URI.createFileURI(COMPLEX_REQRES_INPUT_PATH), true).contents[0]

        val restAssuredOutputModel = M2MTransformer.transformReqRes2RestAssured(reqresInputModel)
        restAssuredOutputModel shouldNotBe null

        printModel(restAssuredOutputModel)
    }
}) {
    companion object {
        private val MINIMAL_HELLO_PUML_INPUT_PATH = Resources.getResource("minimal_hello_puml.xmi").path
        private val REROUTING_PUML_INPUT_PATH = Resources.getResource("rerouting_puml.xmi").path
        private val XCALL_PUML_INPUT_PATH = Resources.getResource("xcall_puml.xmi").path

        private val MINIMAL_HELLO_REQRES_INPUT_PATH = Resources.getResource("minimal_hello_reqres.xmi").path // TODO
        private val COMPLEX_REQRES_INPUT_PATH = Resources.getResource("complex_hello_reqres.xmi").path

        fun printModel(model: EObject) {
            val resource = ResourceSetImpl().createResource(URI.createURI("dummy:/test.ecore"))
            resource.contents.add(model)

            resource.save(System.out, null)
        }
    }
}
