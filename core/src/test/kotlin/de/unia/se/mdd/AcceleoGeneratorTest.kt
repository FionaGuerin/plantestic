package de.unia.se.mdd

import com.google.common.io.Resources
// import io.kotlintest.matchers.numerics.shouldBeGreaterThanOrEqual
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
// import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import java.io.File

class AcceleoGeneratorTest : StringSpec({
    "Transform a  Rest Assured EObject input to Java Code" {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(RESTASSURED_INPUT_URI_STRING)
        // val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        // val outputFolder = File(Resources.getResource("code-generation").path + "/generatedCode")

        // AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        // outputFolder.listFiles().size.shouldBeGreaterThanOrEqual(1)

        // printCode(outputFolder)
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
