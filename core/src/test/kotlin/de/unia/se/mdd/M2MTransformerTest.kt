package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl

class M2MTransformerTest : StringSpec({

    "Transform a simple puml input to Request Response Pairs " {
        MetaModelSetup.doSetup()

        val resultSet = ResourceSetImpl()
        val pumlInputModel = resultSet.getResource(INPUT_URI, true)

        val reqRespOutputModel = M2MTransformer.transformPuml2ReqRes(pumlInputModel.contents[0])
        reqRespOutputModel shouldNotBe null
    }

    "Transform a simple Request Response Pair input to a Rest Assured EObject" {
        MetaModelSetup.doSetup()

        val resultSet = ResourceSetImpl()

        // FIXME: Update test data as soon as we have a proper .qvto file for this transformation
        val pumlInputModel = resultSet.getResource(INPUT_URI, true)

        val reqRespOutputModel = M2MTransformer.transformReqRes2RestAssured(pumlInputModel.contents[0])
        reqRespOutputModel shouldNotBe null
    }
}) {
    companion object {
        private val INPUT_URI = URI.createFileURI(Resources.getResource("minimal_hello.puml").path)
    }
}
