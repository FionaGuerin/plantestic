package de.unia.se.mdd

import org.eclipse.acceleo.model.mtl.MtlPackage
import org.eclipse.acceleo.model.mtl.resource.EMtlResourceFactoryImpl
import org.eclipse.acceleo.common.IAcceleoConstants
import org.eclipse.acceleo.parser.compiler.AbstractAcceleoCompiler
import org.eclipse.acceleo.parser.compiler.AcceleoCompiler
import org.eclipse.emf.common.util.Monitor
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.URIConverter
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl
import org.eclipse.uml2.uml.UMLPackage
import java.lang.Exception


class AcceleoStandaloneCompiler : AbstractAcceleoCompiler() {

    /**
     * Launches the compilation of the mtl files in the generator.
     *
     */
    override fun doCompile(monitor: Monitor) {
        super.doCompile(monitor)
    }

    /**
     * Registers the packages of the metamodels used in the generator.
     *
     */
    override fun registerPackages() {

        super.registerPackages()
        /*
         * If you want to add the other packages used by your generator, for example UML:
         * org.eclipse.emf.ecore.EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
         **/
        //val metaModelResource = ResourceSetImpl().getResource(MetaModelSetup.REST_ASSURED_METAMODEL_URI, true)
        //val metaModelEPackage = metaModelResource.contents[0]
        //require(metaModelEPackage is EPackage) { "Metamodel for URI ${MetaModelSetup.REST_ASSURED_METAMODEL_URI} " +
        //        "wasn't loaded properly!" }
        //org.eclipse.emf.ecore.EPackage.Registry.INSTANCE[metaModelEPackage.nsURI] = metaModelEPackage
    }

    /**
     * Registers the resource factories.
     *
     */
    override fun registerResourceFactories() {
        super.registerResourceFactories()
        /*
         * If you want to add other resource factories, for example if your metamodel uses a specific serialization and it is not contained in a ".ecore" file:
         * org.eclipse.emf.ecore.resource.Resource.Factory.Registry.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
         **/
    }
}