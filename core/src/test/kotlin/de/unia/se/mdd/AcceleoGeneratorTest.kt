package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.BasicMonitor
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import java.io.File
import java.util.ArrayList

class AcceleoGeneratorTest : StringSpec({
    "Transform a  Rest Assured EObject input to Java Code" {
        MetaModelSetup.doSetup()

        val pumlInputModel = ResourceSetImpl().getResource(URI.createFileURI(RESTASSURED_INPUT_URI_STRING), true).contents[0]

        val output = File("generatedCode")
        GenerateAcceleo.init(pumlInputModel, output, ArrayList<String>())
        GenerateAcceleo.doGenerate(BasicMonitor())
        output shouldNotBe null

        printCode(output)
    }
}) {
    companion object {
        private val RESTASSURED_INPUT_URI_STRING = Resources.getResource("minimal_hello_restassured.xmi").path

        fun printCode(code: File) {
            val data = code.readLines()
            for(line in data) {
                System.out.println(line)
            }
        }
    }
}
