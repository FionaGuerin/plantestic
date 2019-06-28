package de.unia.se.mdd

import de.unia.se.mdd.Main.doMetamodelSetup
import de.unia.se.mdd.Main.transformPuml2ReqRes
import io.kotlintest.specs.StringSpec

class MainTest : StringSpec({

    "Bullshit transformation throws error" {
        //val parser = PumlParser()
        //val umlDiagram = parser.parse(Resources.getResource("minimal_hello.puml").openStream())
        doMetamodelSetup()
        transformPuml2ReqRes()
    }

})
