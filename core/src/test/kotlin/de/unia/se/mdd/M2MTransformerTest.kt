package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl

class M2MTransformerTest : StringSpec({

    "Transform a simple puml input to Request Response Pairs " {
        MetaModelSetup.doSetup()

        val pumlInputModel = ResourceSetImpl().getResource(URI.createURI(PUML_INPUT_URI_STRING), true).contents[0]

        val reqRespOutputModel = M2MTransformer.transformPuml2ReqRes(pumlInputModel)
        reqRespOutputModel shouldNotBe null

        printModel(reqRespOutputModel)
    }

    "Transform a simple Request Response Pair input to a Rest Assured EObject" {
        MetaModelSetup.doSetup()

        val reqresInputModel = ResourceSetImpl().getResource(URI.createURI(REQRES_INPUT_URI_STRING), true).contents[0]

        val restAssuredOutputModel = M2MTransformer.transformReqRes2RestAssured(reqresInputModel)
        restAssuredOutputModel shouldNotBe null

        printModel(restAssuredOutputModel)
    }
}) {
    companion object {
        private val PUML_INPUT_URI_STRING = Resources.getResource("minimal_hello_puml.xmi").toExternalForm()
        private val REQRES_INPUT_URI_STRING = Resources.getResource("minimal_hello_reqres.xmi").toExternalForm() // TODO

        fun printModel(model: EObject) {
            val resource = ResourceSetImpl().createResource(URI.createURI("dummy:/test.ecore"))
            resource.contents.add(model)

            resource.save(System.out, null)
        }
    }
}
