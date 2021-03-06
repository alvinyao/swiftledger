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
import java.util.HashMap;
import java.util.Map;

/**
 * The type Sources.
 */
public class Sources {

    private final Map<String, SourceArtifact> artifacts = new HashMap<>();
    private String targetArtifact;

    /**
     * Instantiates a new Sources.
     *
     * @param files the files
     */
    public Sources(File[] files) {
        for (File file : files) {
            artifacts.put(file.getName(), new SourceArtifact(file));
        }
    }

    /**
     * Resolve dependencies.
     */
    public void resolveDependencies() {
        for (String srcName : artifacts.keySet()) {
            SourceArtifact src = artifacts.get(srcName);
            for (String dep : src.getDependencies()) {
                SourceArtifact depArtifact = artifacts.get(dep);
                if (depArtifact == null) {
                    throw ContractException.assembleError("can't resolve dependency: dependency '%s' not found.", dep);
                }
                src.injectDependency(depArtifact);
            };
        }

        for (SourceArtifact artifact : artifacts.values()) {
            if (!artifact.hasDependentArtifacts()) {
                targetArtifact = artifact.getName();
            }
        }
    }

    /**
     * Plain source string.
     *
     * @return the string
     */
    public String plainSource() {
        return artifacts.get(targetArtifact).plainSource();
    }
}
