package de.unia.se.mdd

import com.google.common.io.Resources
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.resource.UMLResource
import plantuml.PumlStandaloneSetup

object MetaModelSetup {

    private val REQUEST_RESPONSE_PAIRS_METAMODEL_URI =
        URI.createFileURI(Resources.getResource("request-response-pairs/RequestResponsePairs.ecore").path)
    private val REST_ASSURED_METAMODEL_URI =
        URI.createFileURI(Resources.getResource("abstract-syntax-rest-assured/abstractsyntaxrestassured.ecore").path)

    fun doSetup() {
        // Uml
        EPackage.Registry.INSTANCE[UMLPackage.eNS_URI] = UMLPackage.eINSTANCE
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap[UMLResource.FILE_EXTENSION] =
            UMLResource.Factory.INSTANCE

        // Our meta models
        PumlStandaloneSetup.doSetup()
        registerMetamodelFromEcoreFile(REST_ASSURED_METAMODEL_URI)
        registerMetamodelFromEcoreFile(REQUEST_RESPONSE_PAIRS_METAMODEL_URI)
    }

    private fun registerMetamodelFromEcoreFile(uri: URI) {
        val resourceSet = ResourceSetImpl()

        val metaModelResource = resourceSet.getResource(uri, true)
        val metaModelEPackage = metaModelResource.contents[0]
        require(metaModelEPackage is EPackage) { "Metamodel for URI $uri wasn't loaded properly!" }
        EPackage.Registry.INSTANCE[metaModelEPackage.nsURI] = metaModelEPackage
    }
}
