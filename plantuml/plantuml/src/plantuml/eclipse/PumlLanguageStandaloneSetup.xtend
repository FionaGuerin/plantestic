/*
 * generated by Xtext 2.18.0
 */
package plantuml.eclipse


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class PumlLanguageStandaloneSetup extends PumlLanguageStandaloneSetupGenerated {

	def static void doSetup() {
		new PumlLanguageStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
