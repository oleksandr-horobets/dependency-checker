Dependency Checker
==================

This is a small utility for checking compilation classpath of your jars and identifying redundant dependencies.
It's based on [jdependency library](https://github.com/tcurdt/jdependency).

Usage
=====

You can use core class `net.iwtp.tools.dependency.checker.Checker` in your custom Java classes.
Adapter for Ant build system is also available.
 
Ant Adapter Example
===================

Firstly you need to download Dependency Checker and it's dependencies.
Dependency Checker itself could be downloaded from [Releases page](https://github.com/oleksandr-horobets/dependency-checker/releases).
`jdependency` library should be downloaded as well. Preferred way is to use Apache Ivy:

    <dependencies>
        <dependency org="org.vafer" name="jdependency" rev="1.0"/>
    </dependencies>
    
After putting all dependencies together you can define and use Dependency Checker:

        <taskdef name="dependency-checker"
                 classname="net.iwtp.tools.dependency.checker.extension.ant.DependencyCheckerTask"
                 classpathref="task.path"/>
                 
        <dependency-checker inspectedArtifact="inspected.jar">
            <path>
                <fileset dir="${lib.dir}" includes="dependencies-*.jar"/>
            </path>
        </dependency-checker>
        
If there are some unused jars in your classpath Dependency Checker will show warnings:

    [WARNING] Redundant dependency: /home/user/libs/redundant-dependency.jar

License
=======

Dependency Checker source code and binaries are released under the Apache License 2.0.