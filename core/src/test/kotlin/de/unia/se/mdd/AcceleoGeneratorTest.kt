package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.matchers.numerics.shouldBeGreaterThanOrEqual
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.joor.Reflect
import java.io.File

class AcceleoGeneratorTest : StringSpec({
    "Transform a Rest Assured EObject input to Java Code".config(enabled = true) {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(RESTASSURED_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)
        outputFolder.listFiles().size.shouldBeGreaterThanOrEqual(1)

        printCode(outputFolder)
    }

    "Acceleo generation produces valid Java code".config(enabled = true) {
        MetaModelSetup.doSetup()

        val pumlInputModelURI = URI.createFileURI(RESTASSURED_INPUT_PATH)
        val pumlInputModel = ResourceSetImpl().getResource(pumlInputModelURI, true).contents[0]
        val outputFolder = File(OUTPUT_PATH)

        AcceleoCodeGenerator.generateCode(pumlInputModel, outputFolder)

        // Now compile the resulting code
        Reflect.compile("com.mdd.test.Test", File(OUTPUT_PATH + "/scenario.java").readText()).create("")
    }
}) {
    companion object {
        private val RESTASSURED_INPUT_PATH = Resources.getResource("minimal_hello_restassured.xmi").path
        private val OUTPUT_PATH = Resources.getResource("code-generation").path + "/generatedCode"

        fun printCode(folder: File) {
            folder.listFiles().forEach { file ->
                val lines = file.readLines()
                lines.forEach { line -> println(line) }
            }
        }
    }
}
