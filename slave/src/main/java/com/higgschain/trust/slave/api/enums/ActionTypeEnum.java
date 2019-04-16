package com.higgschain.trust.slave.api.enums;

/**
 * The enum Action type enum.
 *
 * @author WangQuanzhou
 * @desc action type enum class
 * @date 2018 /3/26 17:26
 */
public enum ActionTypeEnum {

    /**
     * The Open account.
     */
    OPEN_ACCOUNT("OPEN_ACCOUNT", "open account action"),
    /**
     * The Accounting.
     */
    ACCOUNTING("ACCOUNTING", "accounting action"),
    /**
     * The Freeze.
     */
    FREEZE("FREEZE", "freeze capital action"),
    /**
     * The Unfreeze.
     */
    UNFREEZE("UNFREEZE", "unfreeze capital action"),
    /**
     * The Utxo.
     */
    UTXO("UTXO", "UTXO action"),
    /**
     * The Register contract.
     */
    REGISTER_CONTRACT("REGISTER_CONTRACT", "register contract action"),
    /**
     * The Bind contract.
     */
    BIND_CONTRACT("BIND_CONTRACT", "bind contract action"),
    /**
     * The Trigger contract.
     */
    TRIGGER_CONTRACT("TRIGGER_CONTRACT", "trigger contract action"),
    /**
     * The Contract state migration.
     */
    CONTRACT_STATE_MIGRATION("CONTRACT_STATE_MIGRATION", "migration contract state"),
    /**
     * The Register rs.
     */
    REGISTER_RS("REGISTER_RS", "register RS action"),
    /**
     * The Register policy.
     */
    REGISTER_POLICY("REGISTER_POLICY", "register policy action"),
    /**
     * The Create data identity.
     */
    CREATE_DATA_IDENTITY("CREATE_DATA_IDENTITY", "create data identity"),
    /**
     * The Issue currency.
     */
    ISSUE_CURRENCY("ISSUE_CURRENCY", "issue new currency"),
    /**
     * The Ca auth.
     */
    CA_AUTH("CA_AUTH", "ca auth"),
    /**
     * The Ca init.
     */
    CA_INIT("CA_INIT", "ca init"),
    /**
     * The Ca cancel.
     */
    CA_CANCEL("CA_CANCEL", "ca cancel"),
    /**
     * The Ca update.
     */
    CA_UPDATE("CA_UPDATE", "ca update"),
    /**
     * The Node join.
     */
    NODE_JOIN("NODE_JOIN", "node join"),
    /**
     * The Node leave.
     */
    NODE_LEAVE("NODE_LEAVE", "node leave"),
    /**
     * The Rs cancel.
     */
    RS_CANCEL("RS_CANCEL", "cancel rs"),
    /**
     * The Contract creation.
     */
    CONTRACT_CREATION("CONTRACT_CREATION", "creation contract"),
    /**
     * The Contract invoked.
     */
    CONTRACT_INVOKED("CONTRACT_INVOKED", "invoked contract"),

    ;

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    ActionTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets action type enum bycode.
     *
     * @param code the code
     * @return the action type enum bycode
     */
    public static ActionTypeEnum getActionTypeEnumBycode(String code) {
        for (ActionTypeEnum actionTypeEnum : ActionTypeEnum.values()) {
            if (actionTypeEnum.getCode().equals(code)) {
                return actionTypeEnum;
            }
        }
        return null;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
}
