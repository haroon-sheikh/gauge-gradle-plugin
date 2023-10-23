package org.gauge.gradle;

import org.gauge.gradle.GaugeConstants;
import org.gauge.gradle.GaugeExtensionNew;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GaugeExtensionTest {

    @Test
    public void shouldLoadDefaultProperties() {
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply(GaugeConstants.GAUGE_PLUGIN_ID);
        GaugeExtensionNew extension = project.getExtensions().findByType(GaugeExtensionNew.class);
        assertNotNull(extension, "extension should not be null");
        assertEquals("default", extension.getEnv().get());
        assertFalse(extension.getTags().isPresent());
        assertEquals("specs", extension.getSpecsDir().get());
        assertFalse(extension.getInParallel().get());
        assertFalse(extension.getNodes().isPresent());
        assertTrue(extension.getEnvironmentVariables().get().isEmpty());
        assertFalse(extension.getAdditionalFlags().isPresent());
        assertFalse(extension.getGaugeRoot().isPresent());
    }
}
