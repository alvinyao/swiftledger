package com.higgschain.trust.consensus.atomix.example;

import com.higgschain.trust.consensus.command.AbstractConsensusCommand;
import lombok.Getter;

/**
 * The type Example command.
 *
 * @author Zhu_Yuanxiang
 * @create 2018 -08-01
 */
@Getter public class ExampleCommand extends AbstractConsensusCommand<String> {
    private static final long serialVersionUID = 1L;//??

    private String msg;

    private long index;

    /**
     * Instantiates a new Example command.
     *
     * @param value the value
     * @param index the index
     */
    public ExampleCommand(String value, long index) {
        super(value);
        msg = value;
        this.index = index;
    }

    @Override public String toString() {
        return "ExampleCommand{" + "msg='" + msg + '\'' + '}';
    }
}
