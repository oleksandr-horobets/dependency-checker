/*
 * Copyright 2015 Oleksandr Horobets.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.iwtp.tools.dependency.checker;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckerTest {
    private final File inspectedJar = new File("build/libs/test-set1-inspected.jar");
    private final File annotation = new File("build/libs/test-set1-annotation.jar");
    private final File dependency1 = new File("build/libs/test-set1-dependency1.jar");
    private final File dependency2 = new File("build/libs/test-set1-dependency2.jar");

    @Test
    public void testIntegration() throws IOException {
        Set<File> classpathJars = new HashSet<>();
        classpathJars.add(dependency1);
        classpathJars.add(dependency2);

        Checker checker = new Checker();

        checker.setInspectedJar(inspectedJar);
        checker.addClasspathJars(classpathJars);

        assertTrue(checker.getRedundantDependencies().contains(dependency2.getAbsoluteFile()));
    }

    @Test
    public void testDoNotCountInspectedArtifactItself() throws IOException {
        Set<File> classpathJars = new HashSet<>();
        classpathJars.add(inspectedJar);
        classpathJars.add(dependency1);

        Checker checker = new Checker();

        checker.setInspectedJar(inspectedJar);
        checker.addClasspathJars(classpathJars);

        assertFalse(checker.getRedundantDependencies().contains(inspectedJar.getAbsoluteFile()));
    }

    @Test
    public void testAnnotation() throws IOException {
        Set<File> classpathJars = new HashSet<>();
        classpathJars.add(annotation);

        Checker checker = new Checker();

        checker.setInspectedJar(inspectedJar);
        checker.addClasspathJars(classpathJars);

        assertFalse(checker.getRedundantDependencies().contains(annotation.getAbsoluteFile()));
    }
}
