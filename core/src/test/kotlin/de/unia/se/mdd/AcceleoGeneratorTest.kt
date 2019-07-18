package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import java.io.File

class AcceleoGeneratorTest : StringSpec({
    "Transform a  Rest Assured EObject input to Java Code" {
        MetaModelSetup.doSetup()

        val pumlInputModel = ResourceSetImpl().getResource(URI.createFileURI(RESTASSURED_INPUT_URI_STRING), true).contents[0]
        val output = File(Resources.getResource("code-generation").path + "/generatedCode")

        AcceleoCodeGenerator.generateCode(pumlInputModel, output)
        output shouldNotBe null

        printCode(output)
    }
}) {
    companion object {
        private val RESTASSURED_INPUT_URI_STRING = Resources.getResource("minimal_hello_restassured.xmi").path

        fun printCode(folder: File) {
            folder.listFiles().forEach { file ->
                val lines = file.readLines()
                lines.forEach { line -> println(line) }
            }
        }
    }
}
