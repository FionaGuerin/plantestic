package de.unia.se.mdd

import com.google.common.io.Resources
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.m2m.qvt.oml.BasicModelExtent
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl
import org.eclipse.m2m.qvt.oml.TransformationExecutor
import org.eclipse.m2m.qvt.oml.util.WriterLog
import java.io.OutputStreamWriter

object ModelToModelTransformations {

    private val QVT_PUML2REQRES_TRANFORMATION_URI = URI.createFileURI(Resources.getResource("qvt/puml2reqres.qvto").path)!!

    private val QVT_REQRES2RESTASSURED_TRANFORMATION_URI =
        URI.createFileURI(Resources.getResource("qvt/reqres2restassured.qvto").path)!!

    fun transformPuml2ReqRes(inputModel: Resource): EObject {
        return doQvtoTransformation(inputModel, QVT_PUML2REQRES_TRANFORMATION_URI)
    }

    fun transformReqRes2RestAssured(inputModel: Resource): EObject {
        return doQvtoTransformation(inputModel, QVT_REQRES2RESTASSURED_TRANFORMATION_URI)
    }

    private fun doQvtoTransformation(inputModel: Resource, transformationUri: URI): EObject {
        // sources:
        // - https://github.com/mrcalvin/qvto-cli/blob/master/qvto-app/src/main/java/at/ac/wu/nm/qvto/App.java
        // - https://wiki.eclipse.org/QVTOML/Examples/InvokeInJava

        // create executor for the given transformation
        val executor = TransformationExecutor(transformationUri)
        val validationDiagnostic = executor.loadTransformation()
        require(validationDiagnostic.message == "OK")

        // create the input extent with its initial contents
        val input = BasicModelExtent(inputModel.contents)
        // create an empty extent to catch the output
        val output = BasicModelExtent()

        // setup the execution environment details ->
        // configuration properties, logger, monitor object etc.
        val context = ExecutionContextImpl()
        context.setConfigProperty("keepModeling", true)

        require(System.out != null) { "System.out was null!" }
        val outStream = OutputStreamWriter(System.out!!)
        val log = WriterLog(outStream)
        context.log = log

        // run the transformation assigned to the executor with the given
        // input and output and execution context -> ChangeTheWorld(in, out)
        // Remark: variable arguments count is supported
        val result = executor.execute(context, input, output)

        // check the result for success
        if (result.severity == Diagnostic.OK) {
            require(!output.contents.isNullOrEmpty()) { "No transformation result!" }
            return output.contents[0]
        } else {
            // turn the result diagnostic into status and send it to error log
            throw IllegalArgumentException(result.toString())
        }
    }
}