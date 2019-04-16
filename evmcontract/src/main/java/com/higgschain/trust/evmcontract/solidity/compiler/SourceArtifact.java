/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package com.higgschain.trust.evmcontract.solidity.compiler;


import java.io.File;
import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.collections4.CollectionUtils.disjunction;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.substringsBetween;

/**
 * The type Source artifact.
 */
public class SourceArtifact {

    private String name;
    private List<String> dependencies;
    private String source;

    private final Set<SourceArtifact> injectedDependencies = new HashSet<>();
    private final Set<SourceArtifact> dependentArtifacts = new HashSet<>();

    /**
     * Instantiates a new Source artifact.
     *
     * @param name   the name
     * @param source the source
     */
    public SourceArtifact(String name, String source) {
        this.name = name;
        this.dependencies = extractDependencies(source);
        this.source = source.replaceAll("import\\s\"\\.*?\\.sol\";", "");
    }

    /**
     * Instantiates a new Source artifact.
     *
     * @param f the f
     */
    public SourceArtifact(File f) {

    }

    private static List<String> extractDependencies(String source) {
        String[] deps = substringsBetween(source, "import \"", "\";");
        return deps == null ? Collections.<String>emptyList() : asList(deps);
    }

//    public SourceArtifact(MultipartFile srcFile) throws IOException {
//        this(srcFile.getOriginalFilename(), new String(srcFile.getBytes(), "UTF-8"));
//    }

    /**
     * Inject dependency.
     *
     * @param srcArtifact the src artifact
     */
    public void injectDependency(SourceArtifact srcArtifact) {
        injectedDependencies.add(srcArtifact);
        srcArtifact.addDependentArtifact(this);
    }

    private void addDependentArtifact(SourceArtifact srcArtifact) {
        dependentArtifacts.add(srcArtifact);
    }

    /**
     * Has dependent artifacts boolean.
     *
     * @return the boolean
     */
    public boolean hasDependentArtifacts() {
        return !dependentArtifacts.isEmpty();
    }

    private Collection<String> getUnresolvedDependencies() {
        Set<String> ret = new HashSet<>();
        for (SourceArtifact injectedDependency : injectedDependencies) {
            ret.add(injectedDependency.getName());
        }

        return disjunction(dependencies, ret);
    }

    /**
     * Plain source string.
     *
     * @return the string
     */
    public String plainSource() {
        Collection<String> unresolvedDeps = getUnresolvedDependencies();
        if (isNotEmpty(unresolvedDeps)) {
            throw ContractException.assembleError("Followed dependencies aren't resolved: %s", unresolvedDeps);
        }

        String result = this.source;
        for (SourceArtifact dependencyArtifact : injectedDependencies) {
            String importDefinition = format("import \"%s\";", dependencyArtifact.getName());
            String dependencySrc = format("// %s\n%s", importDefinition, dependencyArtifact.plainSource());

            result = result.replace(importDefinition, dependencySrc);
        }

        return result;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets dependencies.
     *
     * @return the dependencies
     */
    public List<String> getDependencies() {
        return dependencies;
    }
}
