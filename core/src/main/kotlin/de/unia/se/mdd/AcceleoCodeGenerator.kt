package de.unia.se.mdd

import org.eclipse.emf.common.util.BasicMonitor
import org.eclipse.emf.ecore.EObject
import java.io.File

object AcceleoCodeGenerator {

    /**
     * Parses a resource specified by an URI and returns the resulting object tree root element.
     * @param fileUriString URI of resource to be parsed as String
     * @return Root model object
     */
    fun generateCode(inputModel: EObject, targetFolder: File) {
        val restAssuredAcceleoGenerator = RestAssuredAcceleoGenerator()
        restAssuredAcceleoGenerator.initialize(inputModel, targetFolder, emptyList())
        restAssuredAcceleoGenerator.doGenerate(BasicMonitor())
    }
}
