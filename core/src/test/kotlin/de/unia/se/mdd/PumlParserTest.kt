package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import plantuml.puml.Activate
import plantuml.puml.Participant
import plantuml.puml.SequenceUml
import plantuml.puml.UseLeft

class PumlParserTest : StringSpec({

    "Parsing works for the minimal example" {
        MetaModelSetup.doSetup()

        val umlDiagram = PumlParser.parse(PUML_INPUT_URI_STRING)

        printModel(umlDiagram)

        (umlDiagram.umlDiagrams[0] is SequenceUml) shouldBe true
        val sequenceDiagram = umlDiagram.umlDiagrams[0] as SequenceUml
        sequenceDiagram.umlElements.size shouldBe 4
        (sequenceDiagram.umlElements[0] as Participant).name shouldBe "A"
        (sequenceDiagram.umlElements[1] as Participant).name shouldBe "B"
        (sequenceDiagram.umlElements[2] as UseLeft).userOne shouldBe sequenceDiagram.umlElements[0]
        (sequenceDiagram.umlElements[2] as UseLeft).userTwo shouldBe sequenceDiagram.umlElements[1]
        ((sequenceDiagram.umlElements[3] as Activate).umlElements[0] as UseLeft).userOne shouldBe
                sequenceDiagram.umlElements[1]
        ((sequenceDiagram.umlElements[3] as Activate).umlElements[0] as UseLeft).userTwo shouldBe
                sequenceDiagram.umlElements[0]
    }

    "Parsing works for the rerouting example".config(enabled = false) {
        MetaModelSetup.doSetup()

        val umlDiagram = PumlParser.parse(REROUTE_INPUT_URI_STRING)
        printModel(umlDiagram)
    }

    "Parsing works for the xcall example".config(enabled = false) {
        MetaModelSetup.doSetup()

        val umlDiagram = PumlParser.parse(XCALL_INPUT_URI_STRING)
        printModel(umlDiagram)
    }
}) {
    companion object {
        private val PUML_INPUT_URI_STRING = Resources.getResource("minimal_hello.puml").path
        private val REROUTE_INPUT_URI_STRING = Resources.getResource("rerouting.puml").path
        private val XCALL_INPUT_URI_STRING = Resources.getResource("xcall.puml").path

        fun printModel(model: EObject) {
            val resource = ResourceSetImpl().createResource(URI.createURI("dummy:/test.ecore"))
            resource.contents.add(model)

            resource.save(System.out, null)
        }
    }
}
