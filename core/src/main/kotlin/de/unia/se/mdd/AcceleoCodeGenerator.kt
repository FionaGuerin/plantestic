package de.unia.se.mdd

import org.eclipse.emf.common.util.BasicMonitor
import org.eclipse.emf.ecore.EObject
import java.io.File

object AcceleoCodeGenerator {

    /**
     * Generates the Rest Assured Code based on a Rest Assured EObject.
     * @param inputModel The Rest Assured EObject
     * @param targetFolder The folder to write the generated code into
     */
    fun generateCode(inputModel: EObject, targetFolder: File) {
        val restAssuredAcceleoGenerator = RestAssuredAcceleoGenerator()
        restAssuredAcceleoGenerator.initialize(inputModel, targetFolder, emptyList())
        restAssuredAcceleoGenerator.doGenerate(BasicMonitor())
    }
}
