package de.unia.se.mdd

import com.google.common.io.Resources
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import plantuml.PumlStandaloneSetup

object MetaModelSetup {

    private val REQUEST_RESPONSE_PAIRS_METAMODEL_URI =
        URI.createFileURI(Resources.getResource("metamodels/reqrespairs/RequestResponsePairs.ecore").path)
    private val REST_ASSURED_METAMODEL_URI =
        URI.createFileURI(Resources.getResource("metamodels/restassured/RestAssured.ecore").path)

    fun doSetup() {
        PumlStandaloneSetup.doSetup()
        registerMetamodelFromEcoreFile(REQUEST_RESPONSE_PAIRS_METAMODEL_URI)
        registerMetamodelFromEcoreFile(REST_ASSURED_METAMODEL_URI)
    }

    private fun registerMetamodelFromEcoreFile(uri: URI) {
        val resourceSet = ResourceSetImpl()

        val metaModelResource = resourceSet.getResource(uri, true)
        val metaModelEPackage = metaModelResource.contents[0]
        require(metaModelEPackage is EPackage) { "Metamodel for URI $uri wasn't loaded properly!" }
        EPackage.Registry.INSTANCE[metaModelEPackage.nsURI] = metaModelEPackage
    }
}
