package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl

class ModelToModelTransformationsTest : StringSpec({

    "Transform simple puml input to Request Response Pairs " {
        MetaModelSetup.doSetup()

        val resultSet = ResourceSetImpl()
        val pumlInputModel = resultSet.getResource(INPUT_URI, true)

        val reqRespOutputModel = ModelToModelTransformations.transformPuml2ReqRes(pumlInputModel)
        reqRespOutputModel shouldNotBe null
    }
}) {
    companion object {
        private val INPUT_URI = URI.createFileURI(Resources.getResource("minimal_hello.puml").path)
    }
}
