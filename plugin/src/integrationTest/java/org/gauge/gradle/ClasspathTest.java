package org.gauge.gradle;

import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClasspathTest extends Base {

    @Test
    void testCanRunGaugeClasspathTaskWithDefaultConfigurations() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied without gauge extension
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the classpath task
        BuildResult result = defaultGradleRunner().withArguments("classpath").build();
        assertEquals(SUCCESS, result.task(":classpath").getOutcome());
        System.out.println(result.getOutput());
        assertThat(result.getOutput(), containsString("com/thoughtworks/gauge/gauge-java"));
        assertThat(result.getOutput(), containsString("org/assertj/assertj-core"));
        assertThat(result.getOutput(), containsString("org/json/json"));
    }

}
