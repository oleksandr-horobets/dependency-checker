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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vafer.jdependency.Clazz;
import org.vafer.jdependency.Clazzpath;
import org.vafer.jdependency.ClazzpathUnit;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Checker {
    private static final Logger LOG = LoggerFactory.getLogger(Checker.class);

    private final Clazzpath clazzpath;
    private ClazzpathUnit inspectedUnit;

    public Checker() {
        clazzpath = new Clazzpath();
    }

    public void setInspectedJar(File inspectedJar) throws IOException {
        LOG.debug("Added jar for inspection: {}", inspectedJar);
        inspectedUnit = clazzpath.addClazzpathUnit(inspectedJar);
    }

    public void addClasspathJars(Set<File> classpathJars) throws IOException {
        for (File file : classpathJars) {
            LOG.debug("Added classpath unit: {}", file);
            clazzpath.addClazzpathUnit(file);
        }
    }

    public Set<File> getRedundantDependencies() {
        LOG.debug("Started new redundant dependencies inspection");
        Set<Clazz> dependencies = new HashSet<>();

        dependencies.addAll(inspectedUnit.getDependencies());
        dependencies.addAll(inspectedUnit.getTransitiveDependencies());

        Set<ClazzpathUnit> used = new HashSet<>();

        for (ClazzpathUnit unit : clazzpath.getUnits()) {
            for (Clazz dependency : dependencies) {
                if (unit.getClazzes().contains(dependency)) {
                    LOG.debug("Found source of {}, it's: {}", dependency, unit);
                    used.add(unit);
                }
            }
        }

        LOG.debug("Used dependencies: {}", used);

        Set<File> notUsed = new HashSet<>();

        for (ClazzpathUnit unit : clazzpath.getUnits()) {
            if (!used.contains(unit) && !inspectedUnit.toString().equals(unit.toString())) {
                notUsed.add(new File(unit.toString()));
            }
        }

        LOG.debug("Not used dependencies: {}", notUsed);

        return notUsed;
    }
}
