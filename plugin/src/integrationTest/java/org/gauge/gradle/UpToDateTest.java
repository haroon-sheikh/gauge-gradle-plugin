package org.gauge.gradle;

import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpToDateTest extends Base {

    @Test
    void testGaugeTaskIsNotCached() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("gauge")
                .withPluginClasspath();
        assertEquals(SUCCESS, runner.build().task(":gauge").getOutcome());
        assertEquals(SUCCESS, runner.build().task(":gauge").getOutcome());
    }

    @Test
    void testGaugeValidateTaskIsNotCached() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        GradleRunner runner = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("gaugeValidate")
                .withPluginClasspath();
        assertEquals(SUCCESS, runner.build().task(":gaugeValidate").getOutcome());
        assertEquals(SUCCESS, runner.build().task(":gaugeValidate").getOutcome());
    }

}
