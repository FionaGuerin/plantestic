package de.unia.se.mdd

import com.google.common.io.Resources
import java.io.StringWriter
import java.io.IOException
import org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.URIConverter.WriteableOutputStream
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.util.BasicExtendedMetaData
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl
import org.eclipse.m2m.qvt.oml.TransformationExecutor
import plantuml.PumlRuntimeModule
import org.eclipse.core.runtime.IStatus
import org.eclipse.emf.common.util.*
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic
import org.eclipse.m2m.qvt.oml.BasicModelExtent
import org.eclipse.m2m.qvt.oml.ModelExtent
import plantuml.PumlStandaloneSetup
import plantuml.PumlStandaloneSetupGenerated
import plantuml.puml.UmlDiagram
import java.util.*
import kotlin.collections.HashMap
import org.eclipse.emf.common.util.URI.createFileURI
import org.eclipse.emf.common.util.URI.createFileURI
import org.eclipse.emf.ecore.impl.BasicEObjectImpl
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl
import org.eclipse.emf.ecore.impl.EObjectImpl
import org.eclipse.emf.ecore.resource.URIConverter
import org.eclipse.emf.ecore.util.EContentsEList
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil.createResource
import org.eclipse.m2m.qvt.oml.util.Log
import org.eclipse.m2m.qvt.oml.util.WriterLog
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.resource.UMLResource
import plantuml.puml.PumlPackage
import java.io.File
import java.io.OutputStreamWriter
import java.lang.IllegalArgumentException


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
    }


    fun transformPuml2ReqRes() {
        // sources:
        // - https://github.com/mrcalvin/qvto-cli/blob/master/qvto-app/src/main/java/at/ac/wu/nm/qvto/App.java
        // - https://wiki.eclipse.org/QVTOML/Examples/InvokeInJava


        val rs = ResourceSetImpl()


        // TODO register factories
        PumlStandaloneSetup.doSetup()
        //rs.resourceFactoryRegistry.extensionToFactoryMap[packageInstance.nsURI] = packageInstance
        //rs.resourceFactoryRegistry.extensionToFactoryMap["xmi"] = XMIResourceFactoryImpl()
        //rs.resourceFactoryRegistry.extensionToFactoryMap["ecore"] = EcoreResourceFactoryImpl()

        // TODO load input model
        val INPUT_URI = createFileURI(Resources.getResource("minimal_hello.puml").path)
        val inputModel = rs.getResource(INPUT_URI, true)

        val rs_temp = ResourceSetImpl()
        val inputResource = rs_temp.createResource(URI.createURI("dummy:/test.ecore"))
        //inputResource.contents.add(inputModel.contents[0])


        //inputModel.uri = URI.createURI("dummy:/test.ecore")


        //val test1 = inputModel.getEObject("dummy:test.ecore")

        /*val stringWriter = StringWriter()
        val outputStream = URIConverter.WriteableOutputStream(
            stringWriter,
            "UTF-8"
        )
        val options = HashMap<String, String>()
        inputResource.save(outputStream, options)

        println(stringWriter.toString())
*/



        // FIXME Not sure why we need to do this?
        EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE)
        Resource.Factory.Registry.INSTANCE.
            getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE)

        // TODO load source (=PUML) metamodel
        val mmResSrc = rs.getResource(PUML_METAMODEL_URI, true)
        val eObjectSrc = mmResSrc.contents[0]
        if (eObjectSrc is EPackage) {
            EPackage.Registry.INSTANCE[eObjectSrc.nsURI] = eObjectSrc
        }

        // TODO load target (=RESTASSURED) metamodel
        val mmResTarget = rs.getResource(REST_ASSURED_METAMODEL_URI, true)
        val eObjectTarget = mmResTarget.contents[0]
        if (eObjectTarget is EPackage) {
            EPackage.Registry.INSTANCE[eObjectTarget.nsURI] = eObjectTarget
        }

        // TODO Refer to an existing transformation via URI
        val transformationURI = QVT_PUML2REQRES_TRANFORMATION_URI
        // create executor for the given transformation
        val executor = TransformationExecutor(transformationURI)
        val validationDiagnostic = executor.loadTransformation()
        require(validationDiagnostic.message == "OK")

        val examle_xmi = createFileURI(Resources.getResource("diagram.xmi").path)
        val input_xmi = rs.getResource(examle_xmi, true)

        //val inObjects = EContentsEList<EObject>(diagram)
        //inObjects.add(diagram)



        // create the input extent with its initial contents
        val input_1 = BasicModelExtent(inputModel.contents.map { d -> DynamicEObjectImpl(d.eClass()) })
        val input_2 = BasicModelExtent(input_xmi.contents)
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
        val result = executor.execute(context, input_2, output)



        // check the result for success
        if (result.severity == Diagnostic.OK) {
            // the output objects got captured in the output extent
            val outObjects = output.contents
            // let's persist them using a resource
            val outputLocationString = Resources.getResource("qvt").path + "/result.uml"
            val outputPathUri = URI.createURI("file:$outputLocationString")
            val file = File(outputLocationString)
            file.createNewFile()



            EPackage.Registry.INSTANCE[UMLPackage.eNS_URI] = UMLPackage.eINSTANCE
            Resource.Factory.Registry.INSTANCE.extensionToFactoryMap[UMLResource.FILE_EXTENSION] =
                UMLResource.Factory.INSTANCE

            val outResource = rs.createResource(outputPathUri) as UMLResource
            outResource.contents.addAll(outObjects)
            try {
                outResource.save(null)
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

    fun getEcoreLectureExample(): String {
        val ecoreFactory: EcoreFactory = EcoreFactory.eINSTANCE
        val aPackage: EPackage = ecoreFactory.createEPackage()

        aPackage.name = "somePackage"
        aPackage.nsPrefix = "pkg"
        aPackage.nsURI = "urn:www.pst.ifi.lmu.de/knapp/pkg"
        val aClass: EClass = ecoreFactory.createEClass()
        aClass.name = "SomeClass"
        aPackage.eClassifiers.add(aClass)
        val anAttribute: EAttribute = ecoreFactory.createEAttribute()
        anAttribute.name = "someAttribute"
        anAttribute.eType = ecoreFactory.ecorePackage.eString
        aClass.eStructuralFeatures.add(anAttribute)
        val aReference: EReference = ecoreFactory.createEReference()
        aReference.name = "someReference"
        aReference.eType = aClass
        aClass.eStructuralFeatures.add(aReference)

        val stringWriter = StringWriter()
        try {
            Resource.Factory.Registry.INSTANCE.extensionToFactoryMap["ecore"] = EcoreResourceFactoryImpl()
            val resourceSet: ResourceSet = ResourceSetImpl()
            val resource: Resource = resourceSet.createResource(URI.createFileURI("test.ecore"))
            resource.contents.add(aPackage)

            val outputStream = WriteableOutputStream(
                stringWriter,
                "UTF-8"
            )
            val options: Map<String, String> = HashMap()
            resource.save(outputStream, options)
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }

        return stringWriter.toString()
    }
}
