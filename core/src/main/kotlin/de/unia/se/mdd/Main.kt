package de.unia.se.mdd

import com.google.common.io.Resources
import org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.URIConverter.WriteableOutputStream
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl
import org.eclipse.m2m.qvt.oml.TransformationExecutor
import org.eclipse.emf.common.util.*
import org.eclipse.m2m.qvt.oml.BasicModelExtent
import plantuml.PumlStandaloneSetup
import plantuml.PumlStandaloneSetupGenerated
import plantuml.puml.UmlDiagram
import kotlin.collections.HashMap
import org.eclipse.emf.common.util.URI.createFileURI
import org.eclipse.m2m.qvt.oml.util.WriterLog
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.resource.UMLResource
import plantuml.puml.PumlPackage
import java.io.*
import java.lang.IllegalArgumentException
import java.io.IOException


object Main {

    val PUML_METAMODEL_URI = createFileURI("../plantuml/model/generated/Puml.ecore")
    val REQUEST_RESPONSE_PAIRS_METAMODEL_URI = createFileURI(Resources.getResource("request-response-pairs/RequestResponsePairs.ecore").path)
    val REST_ASSURED_METAMODEL_URI = createFileURI(Resources.getResource("abstract-syntax-rest-assured/abstractsyntaxrestassured.ecore").path)
    val QVT_PUML2REQRES_TRANFORMATION_URI = createFileURI(Resources.getResource("qvt/puml2reqres.qvto").path)
    val QVT_REQRES2RESTASSURED_TRANFORMATION_URI = createFileURI(Resources.getResource("qvt/reqres2restassured.qvto").path)

    @JvmStatic
    fun main(args: Array<String>) {
        // Parse PlantUML input file
        // http://www.davehofmann.de/different-ways-of-parsing-with-xtext/

        // Transform PlantUML AST to RequestResponse-Pairs
        //transformPuml2ReqRes()

        // TODO

        // Transform RequestResponse-Pairs to RestAssured AST
        // TODO

        // Generate test files from RestAssured AST
        // TODO
        doMetamodelSetup()
    }


    fun transformPuml2ReqRes() {
        // sources:
        // - https://github.com/mrcalvin/qvto-cli/blob/master/qvto-app/src/main/java/at/ac/wu/nm/qvto/App.java
        // - https://wiki.eclipse.org/QVTOML/Examples/InvokeInJava


        val rs = ResourceSetImpl()

        // TODO Refer to an existing transformation via URI
        val transformationURI = QVT_PUML2REQRES_TRANFORMATION_URI
        // create executor for the given transformation
        val executor = TransformationExecutor(transformationURI)
        val validationDiagnostic = executor.loadTransformation()
        require(validationDiagnostic.message == "OK")


        // load input model and parse it
        val INPUT_URI = createFileURI(Resources.getResource("minimal_hello.puml").path)
        val inputModel = rs.getResource(INPUT_URI, true)

        // create the input extent with its initial contents
        val input = BasicModelExtent(inputModel.contents)
        // create an empty extent to catch the output
        val output = BasicModelExtent()


        // setup the execution environment details ->
        // configuration properties, logger, monitor object etc.
        val context = ExecutionContextImpl()
        context.setConfigProperty("keepModeling", true)
        val outStream = OutputStreamWriter(System.out)
	    val log = WriterLog(outStream)
        context.log = log


        // run the transformation assigned to the executor with the given
        // input and output and execution context -> ChangeTheWorld(in, out)
        // Remark: variable arguments count is supported
        val result = executor.execute(context, input, output)



        // check the result for success
        if (result.severity == Diagnostic.OK) {
            val fileOutput = FileOutputStream("out.uml")
            val options = HashMap<String, String>()
            val outResource = rs.createResource(URI.createURI("dummy:/out.uml")) as UMLResource
            outResource.contents.addAll(output.contents)
            try {
                outResource.save(fileOutput, options)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        } else {
            // turn the result diagnostic into status and send it to error log
            val status = BasicDiagnostic.toIStatus(result)
            throw IllegalArgumentException(result.toString())
            //TODO Activator.getDefault().getLog().log(status)
        }
    }

    fun doMetamodelSetup() {

        // register encoding factories
        PumlStandaloneSetup.doSetup()


        val rs = ResourceSetImpl()

        // FIXME Not sure why we need to do this?
        EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE)
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap[UMLResource.FILE_EXTENSION] =
            UMLResource.Factory.INSTANCE

        // TODO load target (=RESTASSURED) metamodel
        val mmResTarget = rs.getResource(REST_ASSURED_METAMODEL_URI, true)
        val eObjectTarget = mmResTarget.contents[0]
        if (eObjectTarget is EPackage) {
            EPackage.Registry.INSTANCE[eObjectTarget.nsURI] = eObjectTarget
        }
    }
}
