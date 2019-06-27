package de.unia.se.mdd

import com.google.common.io.Resources
import de.unia.se.mdd.Main.getEcoreLectureExample
import de.unia.se.mdd.Main.transformPuml2ReqRes
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
import io.kotlintest.matchers.startWith
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.should
import io.kotlintest.specs.StringSpec

class MainTest : StringSpec({

    "Lecture example should contain expected xml tags" {
        val lectureExample = getEcoreLectureExample()
        lectureExample should startWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        lectureExample.shouldContain("ecore:EPackage")
    }

    "Lecture example should be longer than a threshold" {
        getEcoreLectureExample().length.shouldBeGreaterThan(500)
    }

    "Bullshit transformation throws error" {
        //val parser = PumlParser()
        //val umlDiagram = parser.parse(Resources.getResource("minimal_hello.puml").openStream())

        transformPuml2ReqRes()
    }
})
