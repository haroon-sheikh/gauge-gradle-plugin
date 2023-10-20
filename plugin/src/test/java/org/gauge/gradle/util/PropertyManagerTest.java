package org.gauge.gradle.util;

import org.gauge.gradle.GaugeExtension;
import org.gauge.gradle.util.PropertyManager;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.gradle.internal.impldep.org.hamcrest.CoreMatchers.containsString;
import static org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PropertyManagerTest {


    @Mock
    private Project project;

    @Mock
    private SourceSetContainer sourceSetContainer;

    private GaugeExtension extension;

    @BeforeEach
    public void setUp() {
        extension = new GaugeExtension();
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("sourceSets", sourceSetContainer);
        doReturn(properties).when(project).getProperties();
    }

    @Test
    public void classpathShouldBeEmptyIfNoTesRuntimeDependencies() {
        SourceSet config = mock(SourceSet.class, RETURNS_DEEP_STUBS);
        when(config.getRuntimeClasspath().getAsPath()).thenReturn("");
        when(sourceSetContainer.getByName("test")).thenReturn(config);
        PropertyManager manager = new PropertyManager(project, extension);

        manager.setProperties();

        assertThat(extension.getClasspath(), containsString(""));
    }

    @Test
    public void classpathShouldIncludeTestRuntimeClasspathConfigurations() {
        SourceSet config = mock(SourceSet.class, RETURNS_DEEP_STUBS);
        when(config.getRuntimeClasspath().getAsPath())
                .thenReturn(String.join(File.pathSeparator, "blah.jar", "blah2.jar"));
        when(sourceSetContainer.getByName("test")).thenReturn(config);
        PropertyManager manager = new PropertyManager(project, extension);

        manager.setProperties();

        assertThat(extension.getClasspath(), containsString(String.join(File.pathSeparator, "blah.jar", "blah2.jar")));
    }


}