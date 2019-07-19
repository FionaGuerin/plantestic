package de.unia.se.mdd

import com.google.common.io.Resources
import de.unia.se.mdd.Main.runTransformationPipeline
import io.kotlintest.specs.StringSpec
import org.joor.Reflect
import java.io.File

interface CompiledTest {
    fun test()
}

class End2EndTest : StringSpec({

    "End2End test does not fail" {
        runTransformationPipeline(INPUT_PATH, OUTPUT_PATH)

        // Now compile the resulting code and execute it
        val compiledTest : CompiledTest = Reflect.compile("com.mdd.test.Test", File(OUTPUT_PATH + "/TestName.java").readText()).create().get()
        println(compiledTest.test())
    }
}) {
    companion object {
        private val INPUT_PATH = Resources.getResource("minimal_hello.puml").path
        private val OUTPUT_PATH = Resources.getResource("code-generation").path + "/generatedCode"
    }
}
