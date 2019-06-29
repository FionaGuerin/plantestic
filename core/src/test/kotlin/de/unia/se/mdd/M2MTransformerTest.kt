package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec

class M2MTransformerTest : StringSpec({

    "Transform a simple puml input to Request Response Pairs " {
        MetaModelSetup.doSetup()

        val pumlInputModel = PumlParser.parse(INPUT_URI_STRING)

        val reqRespOutputModel = M2MTransformer.transformPuml2ReqRes(pumlInputModel)
        reqRespOutputModel shouldNotBe null
    }

    "Transform a simple Request Response Pair input to a Rest Assured EObject" {
        MetaModelSetup.doSetup()

        val pumlInputModel = PumlParser.parse(INPUT_URI_STRING)
        val reqResInputModel = M2MTransformer.transformPuml2ReqRes(pumlInputModel)

        val reqRespOutputModel = M2MTransformer.transformReqRes2RestAssured(reqResInputModel)
        reqRespOutputModel shouldNotBe null
    }
}) {
    companion object {
        private val INPUT_URI_STRING = Resources.getResource("minimal_hello.puml").path
    }
}
