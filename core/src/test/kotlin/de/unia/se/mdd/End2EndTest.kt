package de.unia.se.mdd

import com.google.common.io.Resources
import io.kotlintest.specs.StringSpec

class End2EndTest : StringSpec({

    "End2End test does not fail" {
        Pipeliner().runTransformationPipeline(Resources.getResource("minimal_hello.puml").path)
    }
})
