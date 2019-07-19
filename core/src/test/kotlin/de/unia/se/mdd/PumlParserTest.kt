package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import plantuml.puml.SequenceUml

class PumlParserTest : StringSpec({

    "Minimal example can be parsed from URI" {
        MetaModelSetup.doSetup()

        val umlDiagram = PumlParser.parse(PUML_INPUT_URI_STRING)

        printModel(umlDiagram)

        (umlDiagram.umlDiagrams[0] is SequenceUml) shouldBe true
        val sequenceDiagram = umlDiagram.umlDiagrams[0] as SequenceUml
    }
}) {
    companion object {
        private val PUML_INPUT_URI_STRING = Resources.getResource("minimal_hello.puml").path

        fun printModel(model: EObject) {
            val resource = ResourceSetImpl().createResource(URI.createURI("dummy:/test.ecore"))
            resource.contents.add(model)

            resource.save(System.out, null)
        }
    }
}
