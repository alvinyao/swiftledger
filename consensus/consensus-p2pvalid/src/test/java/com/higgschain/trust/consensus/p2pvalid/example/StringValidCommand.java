package com.higgschain.trust.consensus.p2pvalid.example;

import com.higgschain.trust.consensus.p2pvalid.core.ValidCommand;
import lombok.Getter;
import lombok.Setter;

/**
 * The type String valid command.
 *
 * @author cwy
 */
@Getter
@Setter
public class StringValidCommand extends ValidCommand<String> {
    private static final long serialVersionUID = -1L;

    /**
     * Instantiates a new String valid command.
     */
    public StringValidCommand() {
        super();
    }

    /**
     * Instantiates a new String valid command.
     *
     * @param load the load
     */
    public StringValidCommand(String load) {
        super(load, -1);
    }

    @Override
    public String messageDigest() {
        return get();
    }
}
