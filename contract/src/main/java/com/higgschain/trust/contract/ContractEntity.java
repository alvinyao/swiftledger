package com.higgschain.trust.contract;

/**
 * The type Contract entity.
 */
public class ContractEntity {

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets language.
     *
     * @return the language
     */
    public ContractLanguageEnum getLanguage() {
        return language;
    }

    /**
     * Sets language.
     *
     * @param language the language
     */
    public void setLanguage(ContractLanguageEnum language) {
        this.language = language;
    }

    /**
     * Gets script.
     *
     * @return the script
     */
    public String getScript() {
        return script;
    }

    /**
     * Sets script.
     *
     * @param script the script
     */
    public void setScript(String script) {
        this.script = script;
    }

    private String address;
    private ContractLanguageEnum language;
    private String script;
}
