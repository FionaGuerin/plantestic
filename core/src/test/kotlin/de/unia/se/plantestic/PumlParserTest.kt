package de.unia.se.plantestic

import com.google.common.io.Resources
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import plantuml.puml.Activate
import plantuml.puml.Alternative
import plantuml.puml.SequenceUml
import plantuml.puml.UseLeft
import plantuml.puml.Participant

class PumlParserTest : StringSpec({

    "Parsing works for the minimal example" {
        MetaModelSetup.doSetup()

        val umlDiagram = PumlParser.parse(MINIMAL_HELLO_INPUT_PATH)
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

    "Parsing works for the complex hello example" {
        MetaModelSetup.doSetup()

        val umlDiagram = PumlParser.parse(COMPLEX_HELLO_INPUT_PATH)
        printModel(umlDiagram)

        (umlDiagram.umlDiagrams[0] is SequenceUml) shouldBe true
        val sequenceDiagram = umlDiagram.umlDiagrams[0] as SequenceUml
        sequenceDiagram.umlElements.size shouldBe 3
        (sequenceDiagram.umlElements[0] as Participant).name shouldBe "A"
        (sequenceDiagram.umlElements[1] as Participant).name shouldBe "B"

        val alternative = (sequenceDiagram.umlElements[2] as Alternative)

        alternative.text shouldBe "\${testCondition} == 'SomeValue'"
        (alternative.umlElements[0] as UseLeft).userOne shouldBe sequenceDiagram.umlElements[0]
        (alternative.umlElements[0] as UseLeft).userTwo shouldBe sequenceDiagram.umlElements[1]
        ((alternative.umlElements[1] as Activate).umlElements[0] as UseLeft).userOne shouldBe
                sequenceDiagram.umlElements[1]
        ((alternative.umlElements[1] as Activate).umlElements[0] as UseLeft).userTwo shouldBe
                sequenceDiagram.umlElements[0]
    }

    "Parsing works for the rerouting example" {
        MetaModelSetup.doSetup()

        val umlDiagram = PumlParser.parse(REROUTE_INPUT_PATH)
        printModel(umlDiagram)

        (umlDiagram.umlDiagrams[0] is SequenceUml) shouldBe true
        val sequenceDiagram = umlDiagram.umlDiagrams[0] as SequenceUml
        sequenceDiagram.umlElements.filterIsInstance<Participant>().size shouldBe 3
        val alternative = ((sequenceDiagram.umlElements[5] as Activate)
            .umlElements[2] as Activate).umlElements[0] as Alternative

        alternative.text shouldBe "\${voiceEstablished} == true"
        (alternative.umlElements[0] as UseLeft).userOne.name shouldBe "VM"
    }

    "Parsing works for the xcall example" {
        MetaModelSetup.doSetup()

        val umlDiagram = PumlParser.parse(XCALL_INPUT_PATH)
        printModel(umlDiagram)

        (umlDiagram.umlDiagrams[0] is SequenceUml) shouldBe true
        val sequenceDiagram = umlDiagram.umlDiagrams[0] as SequenceUml
        sequenceDiagram.umlElements.filterIsInstance<Participant>().size shouldBe 7
        val alternative = (sequenceDiagram.umlElements[7] as Activate).umlElements[4] as Alternative

        alternative.text shouldBe "\${xcsServiceType} == 'ACall'"
        (alternative.umlElements[0] as UseLeft).userOne.name shouldBe "XCS"
        ((alternative.umlElements[1] as Activate).umlElements[0] as UseLeft).userOne.name shouldBe "EventNotifier"
    }
}) {
    companion object {
        private val MINIMAL_HELLO_INPUT_PATH = Resources.getResource("minimal_hello.puml").path
        private val COMPLEX_HELLO_INPUT_PATH = Resources.getResource("complex_hello.puml").path
        private val REROUTE_INPUT_PATH = Resources.getResource("rerouting.puml").path
        private val XCALL_INPUT_PATH = Resources.getResource("xcall.puml").path

        fun printModel(model: EObject) {
            val resource = ResourceSetImpl().createResource(URI.createURI("dummy:/test.ecore"))
            resource.contents.add(model)

            resource.save(System.out, null)
        }
    }
}
