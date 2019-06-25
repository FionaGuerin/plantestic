package de.unia.se.mdd

import java.io.StringWriter
import java.io.IOException
import org.eclipse.emf.ecore.EcoreFactory
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.URIConverter.WriteableOutputStream
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl
import plantuml.puml.UmlDiagram

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val lectureExample = getEcoreLectureExample()
        System.out.println(lectureExample)
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
