package de.unia.se.mdd

import org.eclipse.acceleo.engine.service.AbstractAcceleoGenerator
import org.eclipse.emf.common.util.BasicMonitor
import org.eclipse.emf.common.util.Monitor
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import java.io.File
import java.io.IOException
import java.util.ArrayList
import com.google.common.io.Resources

object GenerateAcceleo : AbstractAcceleoGenerator() {

    // The name of the module.
    // val MODULE_FILE_NAME = "de/unia/se/mdd/generate"
    //val MODULE_FILE_NAME = "code-generation/generateCode"
    val MODULE_FILE_NAME = Resources.getResource("code-generation/generateCode.mtl").path

    val TEMPLATE_NAMES = arrayOf("generateTestScenario")

    /*@Throws(IOException::class)
    fun GenerateAcceleo(){}

    @Throws(IOException::class)
    fun GenerateAcceleo(
        model: EObject,
        targetFolder: File,
        arguments: List<Any>
    ){
        initialize(model, targetFolder, arguments)
    }*/

    fun init(
        model: EObject,
        targetFolder: File,
        arguments: List<Any>
    ) {
        initialize(model, targetFolder, arguments)
    }

    override fun getModuleName(): String {
        return MODULE_FILE_NAME
    }

    override fun getTemplateNames(): Array<String> {
        return TEMPLATE_NAMES
    }

    @Throws(IOException::class)
    override fun doGenerate(monitor: Monitor) {
        println("Generating code from Rest assured Model")
        super.doGenerate(monitor)
    }
}
