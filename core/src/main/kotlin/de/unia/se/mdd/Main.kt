package de.unia.se.mdd

import java.io.StringWriter
import java.io.IOException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.EPackage.Registry
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.URIConverter.WriteableOutputStream
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.util.BasicExtendedMetaData
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl
import org.eclipse.m2m.qvt.oml.TransformationExecutor
import java.io.ByteArrayInputStream
import java.io.InputStream
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.XtextResourceSet
import com.google.inject.Injector
import plantuml.PumlStandaloneSetup
import plantuml.puml.UmlDiagram


object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        // Parse PlantUML input file
        // http://www.davehofmann.de/different-ways-of-parsing-with-xtext/

        // Transform PlantUML AST to RequestResponse-Pairs
        transformPuml2ReqRes()

        // TODO

        // Transform RequestResponse-Pairs to RestAssured AST
        // TODO

        // Generate test files from RestAssured AST
        // TODO
    }

    fun loadInputFile() {
        org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../")
        val injector = PumlStandaloneSetup.doSetup()
        val resourceSet = injector.getInstance(XtextResourceSet::class.java)
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, java.lang.Boolean.TRUE)
        val resource = resourceSet.createResource(URI.createURI("dummy:/example.mydsl"))
        val `in` = ByteArrayInputStream("type foo type bar".toByteArray())
        resource.load(`in`, resourceSet.getLoadOptions())
        val model = resource.getContents().get(0) as UmlDiagram
    }

    fun transformPuml2ReqRes() {
        val resourceSet: ResourceSet

        val ecoreModelUri = URI.createFileURI("../plantuml/model/generated/Puml.ecore")
        val qvtTransformationUri = URI.createFileURI(".qvto") // TODO



        // taken from https://stackoverflow.com/questions/9386348/register-ecore-meta-model-programmatically

        // register globally the Ecore Resource Factory to the ".ecore" extension
        // weird that we need to do this, but well...
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap["ecore"] = EcoreResourceFactoryImpl()

        val rs = ResourceSetImpl()
        // enable extended metadata
        val extendedMetaData = BasicExtendedMetaData(rs.packageRegistry)
        rs.loadOptions[XMLResource.OPTION_EXTENDED_META_DATA] = extendedMetaData

        val r = rs.getResource(ecoreModelUri, true)
        val eObject = r.contents[0]
        if (eObject is EPackage) {
            rs.packageRegistry[eObject.nsURI] = eObject

            val transformationExecutor = TransformationExecutor(qvtTransformationUri, rs.packageRegistry)
            val executionContext = ExecutionContextImpl()
            val diagnostics = transformationExecutor.execute(executionContext)
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
