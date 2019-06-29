package de.unia.se.mdd

import com.google.common.io.Resources
import de.unia.se.mdd.Main.runTransformationPipeline
import io.kotlintest.specs.StringSpec
import org.eclipse.emf.common.util.URI


class End2EndTest : StringSpec({

    "End2End test does not fail" {
        runTransformationPipeline(URI.createFileURI(Resources.getResource("minimal_hello.puml").path))
    }
})