package org.gauge.gradle;

import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpToDateTest extends Base {

    @Test
    void testGaugeTaskIsNotCached() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner().withArguments("gauge");
        assertEquals(SUCCESS, runner.build().task(":gauge").getOutcome());
        assertEquals(SUCCESS, runner.build().task(":gauge").getOutcome());
    }

    @Test
    void testGaugeValidateTaskIsNotCached() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner().withArguments("gaugeValidate");
        assertEquals(SUCCESS, runner.build().task(":gaugeValidate").getOutcome());
        assertEquals(SUCCESS, runner.build().task(":gaugeValidate").getOutcome());
    }

    @Test
    void testGaugeClasspathTaskIsCached() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = defaultGradleRunner().withArguments("classpath");
        assertEquals(SUCCESS, runner.build().task(":classpath").getOutcome());
        assertEquals(UP_TO_DATE, runner.build().task(":classpath").getOutcome());
    }

}
