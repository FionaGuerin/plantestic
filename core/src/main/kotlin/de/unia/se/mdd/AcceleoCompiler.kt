package de.unia.se.mdd

import org.eclipse.acceleo.model.mtl.MtlPackage
import org.eclipse.acceleo.model.mtl.resource.EMtlResourceFactoryImpl
import org.eclipse.acceleo.common.IAcceleoConstants
import org.eclipse.acceleo.parser.compiler.AcceleoCompiler
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.URIConverter
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl
import java.lang.Exception


class AcceleoStandaloneCompiler : AcceleoCompiler() {
    @Throws(Exception::class)
    override fun execute() {
        registerResourceFactories()
        registerPackages()
        registerLibraries()

        super.execute()
    }

    private fun registerResourceFactories() {
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap["ecore"] = EcoreResourceFactoryImpl()
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap[IAcceleoConstants.EMTL_FILE_EXTENSION] =
            EMtlResourceFactoryImpl()

        // Uncomment the following if you need to use UML models
        // Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
    }

    private fun registerPackages() {
        // Uncomment if you need to use UML models
        // EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
        // Uncomment if you need to use UML models saved with on old version of MDT/UML (you might need to change the URI's version number)
        // EPackage.Registry.INSTANCE.put("http://www.eclipse.org/uml2/2.1.0/UML", UMLPackage.eINSTANCE);
    }

    private fun registerLibraries() {
        val acceleoModel = MtlPackage::class.java.protectionDomain.codeSource
        if (acceleoModel != null) {
            var libraryLocation = acceleoModel.location.toString()
            if (libraryLocation.endsWith(".jar")) {
                libraryLocation = "jar:$libraryLocation!"
            }

            URIConverter.URI_MAP[URI.createURI("http://www.eclipse.org/acceleo/mtl/3.0/mtlstdlib.ecore")] = URI.createURI("$libraryLocation/model/mtlstdlib.ecore")
            URIConverter.URI_MAP[URI.createURI("http://www.eclipse.org/acceleo/mtl/3.0/mtlnonstdlib.ecore")] = URI.createURI("$libraryLocation/model/mtlnonstdlib.ecore")
        } else {
            System.err.println("Coudln't retrieve location of plugin 'org.eclipse.acceleo.model'.")
        }
    }
}