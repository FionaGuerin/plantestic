package de.unia.se.mdd

import com.google.inject.Guice
import com.google.inject.Inject
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.XtextResourceSet
import plantuml.PumlRuntimeModule
import plantuml.PumlStandaloneSetup
import plantuml.puml.UmlDiagram

class PumlParser {

    @Inject
    private val resourceSet: XtextResourceSet? = null

    init {
        setupParser()
    }

    private fun setupParser() {
        PumlStandaloneSetup.doSetup()
        val injector = Guice.createInjector(PumlRuntimeModule())
        injector.injectMembers(this)
        resourceSet!!.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, java.lang.Boolean.TRUE)
    }

    /**
     * Parses a resource specified by an URI and returns the resulting object tree root element.
     * @param uri URI of resource to be parsed
     * @return Root model object
     */
    fun parse(uri: URI): UmlDiagram {
        val resource = resourceSet!!.getResource(uri, true)

        require(resource.contents.size > 0) { "File should contain something meaningful." }
        require(resource.contents[0] is UmlDiagram) { "File should contain a diagram." }
        return resource.contents[0] as UmlDiagram
    }
}
