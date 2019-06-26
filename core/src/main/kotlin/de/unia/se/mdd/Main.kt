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
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil.createResource
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.resource.UMLResource
import java.io.File
import java.lang.IllegalArgumentException


object Main {

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

    fun transformPuml2ReqRes(diagram: UmlDiagram) {
        PumlStandaloneSetup.doSetup()

        val rs = ResourceSetImpl()

        val mmResSrc = rs.getResource(URI.createURI("file:" + Resources.getResource("qvt/Puml.ecore").path), true)

        // FRom https://github.com/mrcalvin/qvto-cli/blob/master/qvto-app/src/main/java/at/ac/wu/nm/qvto/App.java
        val eObjectSrc = mmResSrc.contents[0]
        if (eObjectSrc is EPackage) {
            val p = eObjectSrc as EPackage
            EPackage.Registry.INSTANCE[p.nsURI] = p
        }

        val mmResTarget = rs.getResource(
            URI.createURI("file:" + Resources.getResource("qvt/abstractsyntaxrestassured.ecore").path),
            true
        )

        // FRom https://github.com/mrcalvin/qvto-cli/blob/master/qvto-app/src/main/java/at/ac/wu/nm/qvto/App.java
        val eObjectTarget = mmResTarget.contents[0]
        if (eObjectTarget is EPackage) {
            val p = eObjectTarget as EPackage
            EPackage.Registry.INSTANCE[p.nsURI] = p
        }

        // Refer to an existing transformation via URI
        val transformationURI = URI.createURI("file:" + Resources.getResource("qvt/puml2reqres.qvto").path)
        // create executor for the given transformation
        val executor = TransformationExecutor(transformationURI)


        val inObjects = BasicEList<EObject>()
        inObjects.add(diagram)

        // create the input extent with its initial contents
        val input = BasicModelExtent(inObjects)
        // create an empty extent to catch the output
        val output = BasicModelExtent()

        // setup the execution environment details ->
        // configuration properties, logger, monitor object etc.
        val context = ExecutionContextImpl()
        context.setConfigProperty("keepModeling", true)

        // run the transformation assigned to the executor with the given
        // input and output and execution context -> ChangeTheWorld(in, out)
        // Remark: variable arguments count is supported
        val result = executor.execute(context, input, output)

        // check the result for success
        if (result.severity == Diagnostic.OK) {
            // the output objects got captured in the output extent
            val outObjects = output.contents
            // let's persist them using a resource
            val outputLocationString = Resources.getResource("qvt").path + "/result.uml"
            val outputPathUri = URI.createURI("file:$outputLocationString")
            val file = File(outputLocationString)
            file.createNewFile()

            rs.resourceFactoryRegistry.extensionToFactoryMap["xmi"] = XMIResourceFactoryImpl();

            rs.resourceFactoryRegistry.extensionToFactoryMap["ecore"] = EcoreResourceFactoryImpl()

            EPackage.Registry.INSTANCE[UMLPackage.eNS_URI] = UMLPackage.eINSTANCE;
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
