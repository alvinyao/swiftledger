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
package com.higgschain.trust.evmcontract.datasource;

/**
 * Defines configurable database settings
 *
 * @author Mikhail Kalinin
 * @since 26.04.2018
 */
public class DbSettings {

    /**
     * The constant DEFAULT.
     */
    public static final DbSettings DEFAULT = new DbSettings()
            .withMaxThreads(1)
            .withMaxOpenFiles(32);

    /**
     * The Max open files.
     */
    int maxOpenFiles;
    /**
     * The Max threads.
     */
    int maxThreads;

    private DbSettings() {
    }

    /**
     * New instance db settings.
     *
     * @return the db settings
     */
    public static DbSettings newInstance() {
        DbSettings settings = new DbSettings();
        settings.maxOpenFiles = DEFAULT.maxOpenFiles;
        settings.maxThreads = DEFAULT.maxThreads;
        return settings;
    }

    /**
     * Gets max open files.
     *
     * @return the max open files
     */
    public int getMaxOpenFiles() {
        return maxOpenFiles;
    }

    /**
     * With max open files db settings.
     *
     * @param maxOpenFiles the max open files
     * @return the db settings
     */
    public DbSettings withMaxOpenFiles(int maxOpenFiles) {
        this.maxOpenFiles = maxOpenFiles;
        return this;
    }

    /**
     * Gets max threads.
     *
     * @return the max threads
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * With max threads db settings.
     *
     * @param maxThreads the max threads
     * @return the db settings
     */
    public DbSettings withMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
        return this;
    }
}
