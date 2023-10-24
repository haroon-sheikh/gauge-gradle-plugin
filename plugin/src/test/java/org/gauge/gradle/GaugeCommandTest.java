package org.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GaugeCommandTest {


    private Project project;
    private GaugeExtensionNew extension;

    @BeforeEach
    void setUp() {
        project = ProjectBuilder.builder().build();
        project.getPlugins().apply(GaugeConstants.GAUGE_PLUGIN_ID);
        extension = project.getExtensions().findByType(GaugeExtensionNew.class);
        assertNotNull(extension, "extension not found");
    }

    @Test
    void testSpecsDirCommand() {
        assertEquals(List.of("specs"), new GaugeCommand(extension, project).getSpecsDir());
        extension.getSpecsDir().set("ext specs");
        assertEquals(List.of("ext", "specs"), new GaugeCommand(extension, project).getSpecsDir());
        project.getExtensions().getExtraProperties().set(GaugeProperty.SPECS_DIR.getKey(), "project ");
        assertEquals(List.of("project"), new GaugeCommand(extension, project).getSpecsDir());
    }

    @Test
    void testEnvProperty() {
        assertEquals(List.of(GaugeProperty.ENV.getFlag(), "default"), new GaugeCommand(extension, project).getEnvironment());
        extension.getEnv().set("ext");
        assertEquals(List.of(GaugeProperty.ENV.getFlag(), "ext"), new GaugeCommand(extension, project).getEnvironment());
        project.getExtensions().getExtraProperties().set(GaugeProperty.ENV.getKey(), "project ");
        assertEquals(List.of(GaugeProperty.ENV.getFlag(), "project"), new GaugeCommand(extension, project).getEnvironment());
    }

    @Test
    void testTagsProperty() {
        assertEquals(Collections.emptyList(), new GaugeCommand(extension, project).getTags());
        extension.getTags().set("ext");
        assertEquals(List.of(GaugeProperty.TAGS.getFlag(), "ext"), new GaugeCommand(extension, project).getTags());
        // TODO do we need quotes around tags
        project.getExtensions().getExtraProperties().set(GaugeProperty.TAGS.getKey(), "tag1 & tag2");
        assertEquals(List.of(GaugeProperty.TAGS.getFlag(), "tag1 & tag2"), new GaugeCommand(extension, project).getTags());
    }

    @Test
    void testProjectDir() {
        assertEquals(List.of(GaugeProperty.PROJECT_DIR.getFlag(), project.getProjectDir().getAbsolutePath()),
                new GaugeCommand(extension, project).getProjectDir());
    }

    @Test
    void testFlagsWithAdditionalFlagsProperty() {
        assertEquals(Collections.emptyList(), new GaugeCommand(extension, project).getFlags());
        extension.getAdditionalFlags().set("--ext");
        assertEquals(List.of("--ext"), new GaugeCommand(extension, project).getFlags());
        project.getExtensions().getExtraProperties().set(GaugeProperty.ADDITIONAL_FLAGS.getKey(), "--simple-console -v ");
        assertEquals(List.of("--simple-console", "-v"), new GaugeCommand(extension, project).getFlags());
    }

    @Test
    void testFlagWithInParallelAndNodesProperty() {
        assertEquals(Collections.emptyList(), new GaugeCommand(extension, project).getFlags());
        extension.getInParallel().set(true);
        extension.getNodes().set(0);
        assertEquals(List.of(GaugeProperty.IN_PARALLEL.getFlag()), new GaugeCommand(extension, project).getFlags());
        extension.getNodes().set(2);
        assertEquals(List.of(GaugeProperty.IN_PARALLEL.getFlag(), GaugeProperty.NODES.getFlag(), 2),
                new GaugeCommand(extension, project).getFlags());
        project.getExtensions().getExtraProperties().set(GaugeProperty.IN_PARALLEL.getKey(), false);
        assertEquals(Collections.emptyList(), new GaugeCommand(extension, project).getFlags());
        project.getExtensions().getExtraProperties().set(GaugeProperty.IN_PARALLEL.getKey(), "true");
        project.getExtensions().getExtraProperties().set(GaugeProperty.NODES.getKey(), 3);
        assertEquals(List.of(GaugeProperty.IN_PARALLEL.getFlag(), GaugeProperty.NODES.getFlag(), 3),
                new GaugeCommand(extension, project).getFlags());
    }

    @Test
    void testRepeatAndFailedFlagsWithAdditionalFlagsProperty() {
        extension.getInParallel().set(true);
        extension.getNodes().set(2);
        extension.getAdditionalFlags().set("--ext");
        assertEquals(List.of("--ext", GaugeProperty.IN_PARALLEL.getFlag(), GaugeProperty.NODES.getFlag(), 2),
                new GaugeCommand(extension, project).getFlags());
        // when --repeat or --failed flag is provided
        extension.getAdditionalFlags().set("--failed --ext");
        // then it should exclude --parallel and --n flags
        assertEquals(List.of("--failed", "--ext"), new GaugeCommand(extension, project).getFlags());
        project.getExtensions().getExtraProperties().set(GaugeProperty.ADDITIONAL_FLAGS.getKey(), "-v --repeat");
        assertEquals(List.of("-v", "--repeat"), new GaugeCommand(extension, project).getFlags());
    }

}
