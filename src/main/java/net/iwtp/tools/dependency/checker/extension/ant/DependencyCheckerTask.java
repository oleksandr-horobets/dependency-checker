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

package net.iwtp.tools.dependency.checker.extension.ant;

import net.iwtp.tools.dependency.checker.Checker;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.resources.FileResource;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DependencyCheckerTask extends Task {
    private FileResource inspectedArtifact;
    private Set<Path> paths = new HashSet<>();

    public void setInspectedArtifact(FileResource inspectedArtifact){
        this.inspectedArtifact = inspectedArtifact;
    }

    public void addPath(Path path) {
        paths.add(path);
    }

    @Override
    public void execute(){
        Checker checker = new Checker();

        try {
            checker.setInspectedJar(inspectedArtifact.getFile());
        } catch (IOException e) {
            log(e, Project.MSG_ERR);
        }

        Set<File> classpathJars = new HashSet<>();

        for(Path path : paths){
            for(String pathElement : path.list()){
                classpathJars.add(new File(pathElement));
            }
        }

        try {
            checker.addClasspathJars(classpathJars);
        } catch (IOException e) {
            log(e, Project.MSG_ERR);
        }

        final Set<File> redundantDependencies = checker.getRedundantDependencies();

        if(redundantDependencies.isEmpty()){
            log("Dependency check finished: no issues found");
        } else {
            for(File dependency : redundantDependencies){
                log("[WARNING] Redundant dependency: " + dependency, Project.MSG_WARN);
            }
        }
    }

}
