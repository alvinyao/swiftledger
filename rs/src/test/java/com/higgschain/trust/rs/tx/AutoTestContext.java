package com.higgschain.trust.rs.tx;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Auto test context.
 *
 * @author tangkun
 * @date 2018 -12-20
 */
@Setter
@Getter
public class AutoTestContext {

    /**
     * The Deploy num.
     */
    //部署合约个数
    public int deployNum = 1000;

    /**
     * The Froze.
     */
    public Froze froze;
}

/**
 * The type Froze.
 */
@Setter
@Getter
class Froze {

    private String from;
    private String to;
    private List<Currency> currencyList = new ArrayList<>(10000);

    /**
     * Instantiates a new Froze.
     *
     * @param from the from
     * @param to   the to
     */
    public Froze(String from, String to) {
        this.from = from;
        this.to = to;
    }
}

/**
 * The type Currency.
 */
@Setter
@Getter
class Currency {
    private String from;
    private String to;
    private STO sto;
    private int totalSupply;

    /**
     * Instantiates a new Currency.
     *
     * @param from        the from
     * @param to          the to
     * @param totalSupply the total supply
     */
    public Currency(String from, String to, int totalSupply) {
        this.from = from;
        this.to = to;
        this.totalSupply = totalSupply;
    }
}

/**
 * The type Sto.
 */
@Setter
@Getter
class STO {
    private String from;
    private String to;
    private int totalSupply;

    /**
     * Instantiates a new Sto.
     *
     * @param from        the from
     * @param to          the to
     * @param totalSupply the total supply
     */
    public STO(String from, String to, int totalSupply) {
        this.from = from;
        this.to = to;
        this.totalSupply = totalSupply;
    }
}
