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

import com.higgschain.trust.evmcontract.config.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * The type Solidity compiler.
 */
@Component
public class SolidityCompiler {

    private Solc solc;

    private static SolidityCompiler INSTANCE;

    /**
     * Instantiates a new Solidity compiler.
     *
     * @param config the config
     */
    @Autowired
    public SolidityCompiler(SystemProperties config) {
        solc = new Solc(config);
    }

    /**
     * Compile result.
     *
     * @param sourceDirectory the source directory
     * @param combinedJson    the combined json
     * @param options         the options
     * @return the result
     * @throws IOException the io exception
     */
    public static Result compile(File sourceDirectory, boolean combinedJson, Option... options) throws IOException {
        return getInstance().compileSrc(sourceDirectory, false, combinedJson, options);
    }

    /**
     * This class is mainly here for backwards compatibility; however we are now reusing it making it the solely public
     * interface listing all the supported options.
     */
    public static final class Options {
        /**
         * The constant AST.
         */
        public static final OutputOption AST = OutputOption.AST;
        /**
         * The constant BIN.
         */
        public static final OutputOption BIN = OutputOption.BIN;
        /**
         * The constant INTERFACE.
         */
        public static final OutputOption INTERFACE = OutputOption.INTERFACE;
        /**
         * The constant ABI.
         */
        public static final OutputOption ABI = OutputOption.ABI;
        /**
         * The constant METADATA.
         */
        public static final OutputOption METADATA = OutputOption.METADATA;
        /**
         * The constant ASTJSON.
         */
        public static final OutputOption ASTJSON = OutputOption.ASTJSON;

        private static final NameOnlyOption OPTIMIZE = NameOnlyOption.OPTIMIZE;
        private static final NameOnlyOption VERSION = NameOnlyOption.VERSION;

        private static class CombinedJson extends ListOption {
            private CombinedJson(List values) {
                super("combined-json", values);
            }
        }

        /**
         * The type Allow paths.
         */
        public static class AllowPaths extends ListOption {
            /**
             * Instantiates a new Allow paths.
             *
             * @param values the values
             */
            public AllowPaths(List values) {
                super("allow-paths", values);
            }
        }
    }

    /**
     * The interface Option.
     */
    public interface Option extends Serializable {
        /**
         * Gets value.
         *
         * @return the value
         */
        String getValue();

        /**
         * Gets name.
         *
         * @return the name
         */
        String getName();
    }

    private static class ListOption implements Option {
        private String name;
        private List values;

       private ListOption(String name, List values) {
            this.name = name;
            this.values = values;
        }

        @Override public String getValue() {
            StringBuilder result = new StringBuilder();
            for (Object value : values) {
                if (OutputOption.class.isAssignableFrom(value.getClass())) {
                    result.append((result.length() == 0) ? ((OutputOption) value).getName() : ',' + ((OutputOption) value).getName());
                } else if (Path.class.isAssignableFrom(value.getClass())) {
                    result.append((result.length() == 0) ? ((Path) value).toAbsolutePath().toString() : ',' + ((Path) value).toAbsolutePath().toString());
                } else if (File.class.isAssignableFrom(value.getClass())) {
                    result.append((result.length() == 0) ? ((File) value).getAbsolutePath() : ',' + ((File) value).getAbsolutePath());
                } else if (String.class.isAssignableFrom(value.getClass())) {
                    result.append((result.length() == 0) ? value : "," + value);
                } else {
                    throw new UnsupportedOperationException("Unexpected type, value '" + value + "' cannot be retrieved.");
                }
            }
            return result.toString();
        }
        @Override public String getName() { return name; }
        @Override public String toString() { return name; }
    }

    private enum NameOnlyOption implements Option {
        /**
         * Optimize name only option.
         */
        OPTIMIZE("optimize"),
        /**
         * Version name only option.
         */
        VERSION("version");

        private String name;

        NameOnlyOption(String name) {
            this.name = name;
        }

        @Override public String getValue() { return ""; }
        @Override public String getName() { return name; }
        @Override public String toString() {
            return name;
        }
    }

    private enum OutputOption implements Option {
        /**
         * Ast output option.
         */
        AST("ast"),
        /**
         * Bin output option.
         */
        BIN("bin"),
        /**
         * Interface output option.
         */
        INTERFACE("interface"),
        /**
         * Abi output option.
         */
        ABI("abi"),
        /**
         * Metadata output option.
         */
        METADATA("metadata"),
        /**
         * Astjson output option.
         */
        ASTJSON("ast-json");

        private String name;

        OutputOption(String name) {
            this.name = name;
        }

        @Override public String getValue() { return ""; }
        @Override public String getName() { return name; }
        @Override public String toString() {
            return name;
        }
    }

    /**
     * The type Custom option.
     */
    public static class CustomOption implements Option {
        private String name;
        private String value;

        /**
         * Instantiates a new Custom option.
         *
         * @param name the name
         */
        public CustomOption(String name) {
            if (name.startsWith("--")) {
                this.name = name.substring(2);
            } else {
                this.name = name;
            }
        }

        /**
         * Instantiates a new Custom option.
         *
         * @param name  the name
         * @param value the value
         */
        public CustomOption(String name, String value) {
            this(name);
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    /**
     * The type Result.
     */
    public static class Result {
        /**
         * The Errors.
         */
        public String errors;
        /**
         * The Output.
         */
        public String output;
        private boolean success;

        /**
         * Instantiates a new Result.
         *
         * @param errors  the errors
         * @param output  the output
         * @param success the success
         */
        public Result(String errors, String output, boolean success) {
            this.errors = errors;
            this.output = output;
            this.success = success;
        }

        /**
         * Is failed boolean.
         *
         * @return the boolean
         */
        public boolean isFailed() {
            return !success;
        }
    }

    private static class ParallelReader extends Thread {

        private InputStream stream;
        private StringBuilder content = new StringBuilder();

        /**
         * Instantiates a new Parallel reader.
         *
         * @param stream the stream
         */
        ParallelReader(InputStream stream) {
            this.stream = stream;
        }

        /**
         * Gets content.
         *
         * @return the content
         */
        public String getContent() {
            return getContent(true);
        }

        /**
         * Gets content.
         *
         * @param waitForComplete the wait for complete
         * @return the content
         */
        public synchronized String getContent(boolean waitForComplete) {
            if (waitForComplete) {
                while(stream != null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return content.toString();
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                synchronized (this) {
                    stream = null;
                    notifyAll();
                }
            }
        }
    }

    /**
     * Compile result.
     *
     * @param source       the source
     * @param combinedJson the combined json
     * @param options      the options
     * @return the result
     * @throws IOException the io exception
     */
    public static Result compile(byte[] source, boolean combinedJson, Option... options) throws IOException {
        return getInstance().compileSrc(source, false, combinedJson, options);
    }

    /**
     * Compile src result.
     *
     * @param source       the source
     * @param optimize     the optimize
     * @param combinedJson the combined json
     * @param options      the options
     * @return the result
     * @throws IOException the io exception
     */
    public Result compileSrc(File source, boolean optimize, boolean combinedJson, Option... options) throws IOException {
        List<String> commandParts = prepareCommandOptions(optimize, combinedJson, options);

        commandParts.add(source.getAbsolutePath());

        ProcessBuilder processBuilder = new ProcessBuilder(commandParts)
                .directory(solc.getExecutable().getParentFile());
        processBuilder.environment().put("LD_LIBRARY_PATH",
                solc.getExecutable().getParentFile().getCanonicalPath());

        Process process = processBuilder.start();

        ParallelReader error = new ParallelReader(process.getErrorStream());
        ParallelReader output = new ParallelReader(process.getInputStream());
        error.start();
        output.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        boolean success = process.exitValue() == 0;

        return new Result(error.getContent(), output.getContent(), success);
    }

    private List<String> prepareCommandOptions(boolean optimize, boolean combinedJson, Option... options) throws IOException {
        List<String> commandParts = new ArrayList<>();
        commandParts.add(solc.getExecutable().getCanonicalPath());
        if (optimize) {
            commandParts.add("--" + Options.OPTIMIZE.getName());
        }
        if (combinedJson) {
            Option combinedJsonOption = new Options.CombinedJson(getElementsOf(OutputOption.class, options));
            commandParts.add("--" + combinedJsonOption.getName());
            commandParts.add(combinedJsonOption.getValue());
        } else {
            for (Option option : getElementsOf(OutputOption.class, options)) {
                commandParts.add("--" + option.getName());
            }
        }
        for (Option option : getElementsOf(ListOption.class, options)) {
            commandParts.add("--" + option.getName());
            commandParts.add(option.getValue());
        }

        for (Option option : getElementsOf(CustomOption.class, options)) {
            commandParts.add("--" + option.getName());
            if (option.getValue() != null) {
                commandParts.add(option.getValue());
            }
        }

        return commandParts;
    }

    private static <T> List<T> getElementsOf(Class<T> clazz, Option... options) {
        return Arrays.stream(options).filter(clazz::isInstance).map(clazz::cast).collect(toList());
    }

    /**
     * Compile src result.
     *
     * @param source       the source
     * @param optimize     the optimize
     * @param combinedJson the combined json
     * @param options      the options
     * @return the result
     * @throws IOException the io exception
     */
    public Result compileSrc(byte[] source, boolean optimize, boolean combinedJson, Option... options) throws IOException {
        List<String> commandParts = prepareCommandOptions(optimize, combinedJson, options);

        ProcessBuilder processBuilder = new ProcessBuilder(commandParts)
                .directory(solc.getExecutable().getParentFile());
        processBuilder.environment().put("LD_LIBRARY_PATH",
                solc.getExecutable().getParentFile().getCanonicalPath());

        Process process = processBuilder.start();

        try (BufferedOutputStream stream = new BufferedOutputStream(process.getOutputStream())) {
            stream.write(source);
        }

        ParallelReader error = new ParallelReader(process.getErrorStream());
        ParallelReader output = new ParallelReader(process.getInputStream());
        error.start();
        output.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        boolean success = process.exitValue() == 0;

        return new Result(error.getContent(), output.getContent(), success);
    }

    /**
     * Run get version output string.
     *
     * @return the string
     * @throws IOException the io exception
     */
    public static String runGetVersionOutput() throws IOException {
        List<String> commandParts = new ArrayList<>();
        commandParts.add(getInstance().solc.getExecutable().getCanonicalPath());
        commandParts.add("--" + Options.VERSION.getName());

        ProcessBuilder processBuilder = new ProcessBuilder(commandParts)
                .directory(getInstance().solc.getExecutable().getParentFile());
        processBuilder.environment().put("LD_LIBRARY_PATH",
                getInstance().solc.getExecutable().getParentFile().getCanonicalPath());

        Process process = processBuilder.start();

        ParallelReader error = new ParallelReader(process.getErrorStream());
        ParallelReader output = new ParallelReader(process.getInputStream());
        error.start();
        output.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (process.exitValue() == 0) {
            return output.getContent();
        }

        throw new RuntimeException("Problem getting solc version: " + error.getContent());
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static SolidityCompiler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SolidityCompiler(SystemProperties.getDefault());
        }
        return INSTANCE;
    }
}