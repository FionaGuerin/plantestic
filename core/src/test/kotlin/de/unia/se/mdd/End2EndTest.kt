package de.unia.se.mdd

import com.google.common.io.Resources
import de.unia.se.mdd.Main.runTransformationPipeline
import io.kotlintest.specs.StringSpec

class End2EndTest : StringSpec({

    "End2End works for a minimal example" {
        runTransformationPipeline(PUML_INPUT_URI_STRING)
    }

    "End2End test works for the rerouting example".config(enabled = false) {
        runTransformationPipeline(REROUTE_INPUT_URI_STRING)
    }

    "End2End test works for the xcall example".config(enabled = false) {
        runTransformationPipeline(XCALL_INPUT_URI_STRING)
    }
}) {
    companion object {
        private val PUML_INPUT_URI_STRING = Resources.getResource("minimal_hello.puml").path
        private val REROUTE_INPUT_URI_STRING = Resources.getResource("rerouting.puml").path
        private val XCALL_INPUT_URI_STRING = Resources.getResource("xcall.puml").path
    }
}
