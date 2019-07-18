package de.unia.se.mdd

import org.eclipse.acceleo.engine.service.AbstractAcceleoGenerator
import org.eclipse.emf.common.util.Monitor
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import java.io.File
import java.io.IOException
import com.google.common.io.Resources
import org.eclipse.emf.common.util.BasicMonitor
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.ResourceSet


object GenerateAcceleo : AbstractAcceleoGenerator() {

    // The name of the module.
    // val MODULE_FILE_NAME = "de/unia/se/mdd/generate"
    //val MODULE_FILE_NAME = "code-generation/generateCode"
    private val MODULE_FILE_NAME = ("generateCode.mtl")

    private val TEMPLATE_NAMES = arrayOf("generateTestScenario")

    /*@Throws(IOException::class)
    fun GenerateAcceleo(){}

    @Throws(IOException::class)
    fun GenerateAcceleo(
        model: EObject,
        targetFolder: File,
        arguments: List<Any>
    ){
        initialize(model, targetFolder, arguments)
    }*/

    fun init(
        model: EObject,
        targetFolder: File,
        arguments: List<Any>
    ) {
        val acceleoCompiler = AcceleoStandaloneCompiler()
        val ioFolder =
            URI.createFileURI(Resources.getResource("code-generation").path)
        val ioFoolderClassLoader = System.getProperty("user.dir") + "/build/classes/kotlin/main/de/unia/se/mdd/"
        acceleoCompiler.setSourceFolder(ioFolder.path())
        acceleoCompiler.setOutputFolder(ioFoolderClassLoader)
        acceleoCompiler.setBinaryResource(false)
        /* if (args.length == 4 && args['[3]'/] != null && !"".equals(args['[3]'/])) { //$NON-NLS-1$
            acceleoCompiler.setDependencies(args['[3]'/])
        } */
        acceleoCompiler.doCompile(BasicMonitor())

        initialize(model, targetFolder, arguments)
    }

    override fun getModuleName(): String {
        return MODULE_FILE_NAME
    }

    override fun getTemplateNames(): Array<String> {
        return TEMPLATE_NAMES
    }

    @Throws(IOException::class)
    override fun doGenerate(monitor: Monitor) {
        println("Generating code from Rest assured Model")
        super.doGenerate(monitor)
    }

    /**
     * This can be used to update the resource set's package registry with all needed EPackages.
     *
     * @param resourceSet
     * The resource set which registry has to be updated.
     * @generated NOT
     */
    override fun registerPackages(resourceSet: ResourceSet) {
        val metaModelResource = resourceSet.getResource(MetaModelSetup.REST_ASSURED_METAMODEL_URI, true)
        val metaModelEPackage = metaModelResource.contents[0]
        require(metaModelEPackage is EPackage) { "Metamodel for URI ${MetaModelSetup.REST_ASSURED_METAMODEL_URI} " +
                "wasn't loaded properly!" }
        resourceSet.packageRegistry[metaModelEPackage.nsURI] = metaModelEPackage
        super.registerPackages(resourceSet)

        /*
         * If you want to change the content of this method, do NOT forget to change the "@generated"
         * tag in the Javadoc of this method to "@generated NOT". Without this new tag, any compilation
         * of the Acceleo module with the main template that has caused the creation of this class will
         * revert your modifications.
         */

        /*
         * If you need additional package registrations, you can register them here. The following line
         * (in comment) is an example of the package registration for UML.
         *
         * You can use the method  "isInWorkspace(Class c)" to check if the package that you are about to
         * register is in the workspace.
         *
         * To register a package properly, please follow the following conventions:
         *
         * If the package is located in another plug-in, already installed in Eclipse. The following content should
         * have been generated at the beginning of this method. Do not register the package using this mechanism if
         * the metamodel is located in the workspace.
         *
         * if (!isInWorkspace(UMLPackage.class)) {
         *     // The normal package registration if your metamodel is in a plugin.
         *     resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
         * }
         *
         * If the package is located in another project in your workspace, the plugin containing the package has not
         * been register by EMF and Acceleo should register it automatically. If you want to use the generator in
         * stand alone, the regular registration (seen a couple lines before) is needed.
         *
         * To learn more about Package Registration, have a look at the Acceleo documentation (Help -> Help Contents).
         */
    }

    /**
     * This can be used to update the resource set's resource factory registry with all needed factories.
     *
     * @param resourceSet
     * The resource set which registry has to be updated.
     * @generated
     */
    override fun registerResourceFactories(resourceSet: ResourceSet) {
        super.registerResourceFactories(resourceSet)
        /*
         * If you want to change the content of this method, do NOT forget to change the "@generated"
         * tag in the Javadoc of this method to "@generated NOT". Without this new tag, any compilation
         * of the Acceleo module with the main template that has caused the creation of this class will
         * revert your modifications.
         */

        /*
         * TODO If you need additional resource factories registrations, you can register them here. the following line
         * (in comment) is an example of the resource factory registration.
         *
         * If you want to use the generator in stand alone, the resource factory registration will be required.
         *
         * To learn more about the registration of Resource Factories, have a look at the Acceleo documentation (Help -> Help Contents).
         */

        // resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(XyzResource.FILE_EXTENSION, XyzResource.Factory.INSTANCE);

        /*
         * Some metamodels require a very complex setup for standalone usage. For example, if you want to use a generator
         * targetting UML models in standalone, you NEED to use the following:
         */
        // UMLResourcesUtil.init(resourceSet)
    }
}
