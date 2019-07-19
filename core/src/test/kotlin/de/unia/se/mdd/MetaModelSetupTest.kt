package de.unia.se.mdd

import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import kotlin.reflect.KCallable
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible

class MetaModelSetupTest : StringSpec() {

    init {
        "doSetup should work" {
            MetaModelSetup.doSetup()

            EPackage.Registry.INSTANCE["http://www.example.org/RestAssured"] shouldNotBe null
            EPackage.Registry.INSTANCE["http://www.example.org/RequestResponsePairs"] shouldNotBe null
            EPackage.Registry.INSTANCE["http://www.eclipse.plantuml/Puml"] shouldNotBe null
        }

        "RegisterMetaModelFromEcoreFile should fail if a Resource URI is invalid" {
            shouldThrow<Throwable> {
                val testUri = URI.createURI("invalid_uri")
                callRegisterMetaModelFromEcoreFile(testUri)
            }
        }
    }

    private fun callRegisterMetaModelFromEcoreFile(testUri: URI?): URI {
        val registerMetaModelFromEcoreFile = MetaModelSetup::class.declaredFunctions
            .single { it.name == "registerMetamodelFromEcoreFile" }
        registerMetaModelFromEcoreFile.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return (registerMetaModelFromEcoreFile as KCallable<URI>).call(MetaModelSetup, testUri)
    }
}
