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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The type Compilation result.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompilationResult {

    @JsonProperty("contracts") private Map<String, ContractMetadata> contracts;
    /**
     * The Version.
     */
    @JsonProperty("version") public String version;

    /**
     * Parse compilation result.
     *
     * @param rawJson the raw json
     * @return the compilation result
     * @throws IOException the io exception
     */
    @JsonIgnore public static CompilationResult parse(String rawJson) throws IOException {
        if(rawJson == null || rawJson.isEmpty()){
            CompilationResult empty = new CompilationResult();
            empty.contracts = Collections.emptyMap();
            empty.version = "";

            return empty;
        } else {
            return new ObjectMapper().readValue(rawJson, CompilationResult.class);
        }
    }

    /**
     * Gets contract path.
     *
     * @return the contract's path given this compilation result contains exactly one contract
     */
    @JsonIgnore public Path getContractPath() {
        if (contracts.size() > 1) {
            throw new UnsupportedOperationException("Source contains more than 1 contact. Please specify the contract name. Available keys (" + getContractKeys() + ").");
        } else {
            String key = contracts.keySet().iterator().next();
            return Paths.get(key.substring(0, key.lastIndexOf(':')));
        }
    }

    /**
     * Gets contract name.
     *
     * @return the contract's name given this compilation result contains exactly one contract
     */
    @JsonIgnore public String getContractName() {
        if (contracts.size() > 1) {
            throw new UnsupportedOperationException("Source contains more than 1 contact. Please specify the contract name. Available keys (" + getContractKeys() + ").");
        } else {
            String key = contracts.keySet().iterator().next();
            return key.substring(key.lastIndexOf(':') + 1);
        }
    }

    /**
     * Gets contract.
     *
     * @param contractName The contract name
     * @return the first contract found for a given contract name; use {@link #getContract(Path, String)} if this compilation result contains more than one contract with the same name
     */
    @JsonIgnore public ContractMetadata getContract(String contractName) {
        if (contractName == null && contracts.size() == 1) {
            return contracts.values().iterator().next();
        } else if (contractName == null || contractName.isEmpty()) {
            throw new UnsupportedOperationException("Source contains more than 1 contact. Please specify the contract name. Available keys (" + getContractKeys() + ").");
        }
        for (Map.Entry<String, ContractMetadata> entry : contracts.entrySet()) {
            String key = entry.getKey();
            String name = key.substring(key.lastIndexOf(':') + 1);
            if (contractName.equals(name)) {
                return entry.getValue();
            }
        }
        throw new UnsupportedOperationException("No contract found with name '" + contractName + "'. Please specify a valid contract name. Available keys (" + getContractKeys() + ").");
    }

    /**
     * Gets contract.
     *
     * @param contractPath The contract path
     * @param contractName The contract name
     * @return the contract with key {@code contractPath:contractName} if it exists; {@code null} otherwise
     */
    @JsonIgnore public ContractMetadata getContract(Path contractPath, String contractName) {
        return contracts.get(contractPath.toAbsolutePath().toString() + ':' + contractName);
    }

    /**
     * Gets contracts.
     *
     * @return all contracts from this compilation result
     */
    @JsonIgnore public List<ContractMetadata> getContracts() {
        return new ArrayList<>(contracts.values());
    }

    /**
     * Gets contract keys.
     *
     * @return all keys from this compilation result
     */
    @JsonIgnore public List<String> getContractKeys() {
        return new ArrayList<>(contracts.keySet());
    }

    /**
     * The type Contract metadata.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContractMetadata {
        /**
         * The Abi.
         */
        public String abi;
        /**
         * The Bin.
         */
        public String bin;
        /**
         * The Sol interface.
         */
        public String solInterface;
        /**
         * The Metadata.
         */
        public String metadata;

        /**
         * Gets interface.
         *
         * @return the interface
         */
        public String getInterface() {
            return solInterface;
        }

        /**
         * Sets interface.
         *
         * @param solInterface the sol interface
         */
        public void setInterface(String solInterface) {
            this.solInterface = solInterface;
        }
    }
}
