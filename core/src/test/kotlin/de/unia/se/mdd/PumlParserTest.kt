package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import plantuml.puml.Participant
import plantuml.puml.SequenceUml

class PumlParserTest : StringSpec({

    "Minimal example can be parsed from URI" {
        val parser = PumlParser()
        val umlDiagram = parser.parse(URI.createFileURI(Resources.getResource("minimal_hello.puml").path))

        (umlDiagram.umlDiagrams[0] is SequenceUml) shouldBe true
        val sequenceDiagram = umlDiagram.umlDiagrams[0] as SequenceUml
        sequenceDiagram.umlElements.size shouldBe 4
        (sequenceDiagram.umlElements[0] as Participant).name shouldBe "A"
        (sequenceDiagram.umlElements[1] as Participant).name shouldBe "B"
    }
})
