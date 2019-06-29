package de.unia.se.mdd

import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import plantuml.puml.UmlDiagram

object PumlParser {

    /**
     * Parses a resource specified by an URI and returns the resulting object tree root element.
     * @param fileUriString URI of resource to be parsed as String
     * @return Root model object
     */
    fun parse(fileUriString: String): UmlDiagram {
        require(EPackage.Registry.INSTANCE["http://www.eclipse.plantuml/Puml"] != null) {
            "Please run MetaModelSetup.doSetup() first"
        }

        val uri = URI.createFileURI(fileUriString)
        val resource = ResourceSetImpl().getResource(uri, true)

        require(resource.contents.size > 0) { "File should contain something meaningful." }
        require(resource.contents[0] is UmlDiagram) { "File should contain a diagram." }
        return resource.contents[0] as UmlDiagram
    }
}
