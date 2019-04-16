package com.higgschain.trust.slave.common.enums;

import com.higgschain.trust.common.exception.ErrorInfo;

/**
 * The enum Slave error enum.
 *
 * @Description:
 * @author: pengdi
 */
public enum SlaveErrorEnum implements ErrorInfo {
    //@formatter:off
    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//
    //                      公共类错误码[000-099,999]                           //
    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//

    /**
     * Slave unknown exception slave error enum.
     */
SLAVE_UNKNOWN_EXCEPTION("999", "其它未知异常", true),

    /**
     * Slave configuration error slave error enum.
     */
SLAVE_CONFIGURATION_ERROR("000", "配置错误", true),

    /**
     * The Slave param validate error.
     */
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//
    //                         请求校检[200-299]                                //
    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//
    SLAVE_PARAM_VALIDATE_ERROR("200", "param validate error", false),
    /**
     * The Slave idempotent.
     */
SLAVE_IDEMPOTENT("201", "request idempotent", false),
    /**
     * The Slave tx verify signature failed.
     */
SLAVE_TX_VERIFY_SIGNATURE_FAILED("202", "transaction verify signature failed", false),
    /**
     * The Slave package verify signature failed.
     */
SLAVE_PACKAGE_VERIFY_SIGNATURE_FAILED("203", "package verify master node signature failed", false),
    /**
     * The Slave package sign signature failed.
     */
SLAVE_PACKAGE_SIGN_SIGNATURE_FAILED("204", "package sign signature failed", false),
    /**
     * The Slave tx verify signature pub key not exist.
     */
SLAVE_TX_VERIFY_SIGNATURE_PUB_KEY_NOT_EXIST("205", "transaction verify signature cannot acquire public key", false),
    /**
     * The Slave consensus get result failed.
     */
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//
    //                         查询相关[300-399]                                //
    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//
    SLAVE_CONSENSUS_GET_RESULT_FAILED("301", "get the consensus result failed.", true),



    /**
     * The Slave tx out not exists error.
     */
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//
    //                         内部处理相关[500-699]                            //
    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//
    SLAVE_TX_OUT_NOT_EXISTS_ERROR("501", "txOut is not exists", false),
    /**
     * The Slave data identity not exists error.
     */
SLAVE_DATA_IDENTITY_NOT_EXISTS_ERROR("502", "dataidentity is not exists", false),
    /**
     * The Slave utxo is double spend error.
     */
SLAVE_UTXO_IS_DOUBLE_SPEND_ERROR("503", "utxo is double spend", false),
    /**
     * The Slave utxo contract process fail error.
     */
SLAVE_UTXO_CONTRACT_PROCESS_FAIL_ERROR("504", "utxo contract process fail", false),
    /**
     * The Slave policy exists error.
     */
SLAVE_POLICY_EXISTS_ERROR("505", "policy is already exists", false),
    /**
     * The Slave rs exists error.
     */
SLAVE_RS_EXISTS_ERROR("506", "RS is already exists", false),
    /**
     * The Slave data not update exception.
     */
SLAVE_DATA_NOT_UPDATE_EXCEPTION("507", "data not update  exception", false),
    /**
     * The Slave policy is not exists exception.
     */
SLAVE_POLICY_IS_NOT_EXISTS_EXCEPTION("508", "policy is not exists exception", false),
    /**
     * The Slave action handler is not exists exception.
     */
SLAVE_ACTION_HANDLER_IS_NOT_EXISTS_EXCEPTION("509", "action handler is not exists exception", false),
    /**
     * The Slave snapshot biz key not existed exception.
     */
SLAVE_SNAPSHOT_BIZ_KEY_NOT_EXISTED_EXCEPTION("510", "snapshot core key not existed exception", false),
    /**
     * The Slave snapshot not init exception.
     */
SLAVE_SNAPSHOT_NOT_INIT_EXCEPTION("511", "snapshot  not init exception", false),
    /**
     * The Slave snapshot transaction not started exception.
     */
SLAVE_SNAPSHOT_TRANSACTION_NOT_STARTED_EXCEPTION("512", "snapshot transaction not started exception", false),
    /**
     * The Slave snapshot transaction has started exception.
     */
SLAVE_SNAPSHOT_TRANSACTION_HAS_STARTED_EXCEPTION("513", "snapshot transaction has started exception", false),
    /**
     * The Slave snapshot null pointed exception.
     */
SLAVE_SNAPSHOT_NULL_POINTED_EXCEPTION("514", "snapshot  null point  exception", false),
    /**
     * The Slave snapshot query exception.
     */
SLAVE_SNAPSHOT_QUERY_EXCEPTION("515", "snapshot  query exception", false),
    /**
     * The Slave s tx out not exists error.
     */
SLAVE_S_TX_OUT_NOT_EXISTS_ERROR("516", "S txOut is not exists", false),
    /**
     * The Slave action not exists exception.
     */
SLAVE_ACTION_NOT_EXISTS_EXCEPTION("517", "action not exists exception", false),
    /**
     * The Slave snapshot cache size not enough exception.
     */
SLAVE_SNAPSHOT_CACHE_SIZE_NOT_ENOUGH_EXCEPTION("518", "snapshot cache size not enough exception", false),
    /**
     * The Slave snapshot get no lock exception.
     */
SLAVE_SNAPSHOT_GET_NO_LOCK_EXCEPTION("519", "snapshot get no lock exception", false),
    /**
     * The Slave snapshot data not exist exception.
     */
SLAVE_SNAPSHOT_DATA_NOT_EXIST_EXCEPTION("520", "update data not exist exception", false),
    /**
     * The Slave snapshot data exist exception.
     */
SLAVE_SNAPSHOT_DATA_EXIST_EXCEPTION("521", "insert data is existed exception", false),
    /**
     * The Slave snapshot data type error exception.
     */
SLAVE_SNAPSHOT_DATA_TYPE_ERROR_EXCEPTION("522", "insert data type error exception", false),
    /**
     * The Slave data not insert exception.
     */
SLAVE_DATA_NOT_INSERT_EXCEPTION("523", "data not insert  exception", false),
    /**
     * The Slave snapshot flush data exception.
     */
SLAVE_SNAPSHOT_FLUSH_DATA_EXCEPTION("524", "snapshot flush data exception", false),
    /**
     * The Slave rs not exists error.
     */
SLAVE_RS_NOT_EXISTS_ERROR("525", "RS is not exist", false),
    /**
     * The Slave rs already canceled error.
     */
SLAVE_RS_ALREADY_CANCELED_ERROR("526", "RS is already canceled exception", false),
    /**
     * The Slave tx not only one contract action exception.
     */
SLAVE_TX_NOT_ONLY_ONE_CONTRACT_ACTION_EXCEPTION("527", "tx not only one contract action", false),


    /**
     * The Slave merkle param not valid exception.
     */
SLAVE_MERKLE_PARAM_NOT_VALID_EXCEPTION("600", "slave merkle param not valid exception", false),
    /**
     * The Slave merkle non exist exception.
     */
SLAVE_MERKLE_NON_EXIST_EXCEPTION("601", "slave merkle non exist exception", false),
    /**
     * The Slave merkle update parent exception.
     */
SLAVE_MERKLE_UPDATE_PARENT_EXCEPTION("602", "slave merkle update parent exception", false),
    /**
     * The Slave merkle calculate index exception.
     */
SLAVE_MERKLE_CALCULATE_INDEX_EXCEPTION("603", "slave merkle calculate index exception", false),
    /**
     * The Slave merkle calculate hash exception.
     */
SLAVE_MERKLE_CALCULATE_HASH_EXCEPTION("604", "slave merkle calculate hash exception", false),
    /**
     * The Slave merkle node non exist exception.
     */
SLAVE_MERKLE_NODE_NON_EXIST_EXCEPTION("605", "slave merkle node non exist exception", false),
    /**
     * The Slave merkle node add idempotent exception.
     */
SLAVE_MERKLE_NODE_ADD_IDEMPOTENT_EXCEPTION("606", "slave merkle node add idempotent exception", false),
    /**
     * The Slave merkle node add exception.
     */
SLAVE_MERKLE_NODE_ADD_EXCEPTION("607", "slave merkle node add exception", false),
    /**
     * The Slave merkle node update exception.
     */
SLAVE_MERKLE_NODE_UPDATE_EXCEPTION("608", "slave merkle node update exception", false),
    /**
     * The Slave merkle already exist exception.
     */
SLAVE_MERKLE_ALREADY_EXIST_EXCEPTION("609", "slave merkle already exist exception", false),
    /**
     * The Slave merkle node build duplicate exception.
     */
SLAVE_MERKLE_NODE_BUILD_DUPLICATE_EXCEPTION("610", "slave merkle node build duplicate exception", false),

    /**
     * The Slave account is already exists error.
     */
SLAVE_ACCOUNT_IS_ALREADY_EXISTS_ERROR("800", "account is already exists", false),
    /**
     * The Slave account currency not exists error.
     */
SLAVE_ACCOUNT_CURRENCY_NOT_EXISTS_ERROR("801", "currency is not exists", false),
    /**
     * The Slave account trial balance error.
     */
SLAVE_ACCOUNT_TRIAL_BALANCE_ERROR("802", "trial balance check error", false),
    /**
     * The Slave account is not exists error.
     */
SLAVE_ACCOUNT_IS_NOT_EXISTS_ERROR("803", "account is not exists error", false),
    /**
     * The Slave account change balance error.
     */
SLAVE_ACCOUNT_CHANGE_BALANCE_ERROR("804", "change account balance is error", false),
    /**
     * The Slave account balance is not enough error.
     */
SLAVE_ACCOUNT_BALANCE_IS_NOT_ENOUGH_ERROR("805", "account balance is not enough error", false),
    /**
     * The Slave account status is destroy error.
     */
SLAVE_ACCOUNT_STATUS_IS_DESTROY_ERROR("806", "account status is destroy error", false),
    /**
     * The Slave account fund direction is null error.
     */
SLAVE_ACCOUNT_FUND_DIRECTION_IS_NULL_ERROR("807", "account fund direction is null error", false),
    /**
     * The Slave account currency is not consistent error.
     */
SLAVE_ACCOUNT_CURRENCY_IS_NOT_CONSISTENT_ERROR("808", "account currency is not consistent error", false),
    /**
     * The Slave account check data owner error.
     */
SLAVE_ACCOUNT_CHECK_DATA_OWNER_ERROR("809", "account check data owner error", false),
    /**
     * The Slave account freeze amount error.
     */
SLAVE_ACCOUNT_FREEZE_AMOUNT_ERROR("810", "account check freeze amount error", false),
    /**
     * The Slave account freeze error.
     */
SLAVE_ACCOUNT_FREEZE_ERROR("811", "account freeze error", false),
    /**
     * The Slave account freeze record is not exists error.
     */
SLAVE_ACCOUNT_FREEZE_RECORD_IS_NOT_EXISTS_ERROR("812", "account freeze record is not exists error", false),
    /**
     * The Slave account unfreeze amount error.
     */
SLAVE_ACCOUNT_UNFREEZE_AMOUNT_ERROR("813", "account check unfreeze amount error", false),
    /**
     * The Slave account unfreeze error.
     */
SLAVE_ACCOUNT_UNFREEZE_ERROR("814", "account unfreeze error", false),
    /**
     * The Slave account merkle tree not exist error.
     */
SLAVE_ACCOUNT_MERKLE_TREE_NOT_EXIST_ERROR("815", "account merkletree not exist error", false),
    /**
     * The Slave package build tx root hash error.
     */
SLAVE_PACKAGE_BUILD_TX_ROOT_HASH_ERROR("816", "package build root hash error", false),
    /**
     * The Slave package build tx receipt root hash error.
     */
SLAVE_PACKAGE_BUILD_TX_RECEIPT_ROOT_HASH_ERROR("817", "package build receipt root hash error", false),
    /**
     * The Slave package get block error.
     */
SLAVE_PACKAGE_GET_BLOCK_ERROR("818", "package get block error", false),
    /**
     * The Slave package block height unequal error.
     */
SLAVE_PACKAGE_BLOCK_HEIGHT_UNEQUAL_ERROR("819", "package block height unequal error", false),
    /**
     * The Slave package txs is empty error.
     */
SLAVE_PACKAGE_TXS_IS_EMPTY_ERROR("820", "package txs is empty error", false),
    /**
     * The Slave package validating error.
     */
SLAVE_PACKAGE_VALIDATING_ERROR("821", "package validating error", false),
    /**
     * The Slave package update status error.
     */
SLAVE_PACKAGE_UPDATE_STATUS_ERROR("822", "package update status error", false),
    /**
     * The Slave package header is null error.
     */
SLAVE_PACKAGE_HEADER_IS_NULL_ERROR("823", "package block hash is null error", false),
    /**
     * The Slave package header is unequal error.
     */
SLAVE_PACKAGE_HEADER_IS_UNEQUAL_ERROR("824", "package hash is unequal error", false),
    /**
     * The Slave package persisting error.
     */
SLAVE_PACKAGE_PERSISTING_ERROR("825", "package persisting error", false),
    /**
     * The Slave package two header unequal error.
     */
SLAVE_PACKAGE_TWO_HEADER_UNEQUAL_ERROR("826", "package consensus header unequal tempHeader error", false),
    /**
     * The Slave package update pending tx error.
     */
SLAVE_PACKAGE_UPDATE_PENDING_TX_ERROR("827", "package pending tx update error", false),
    /**
     * The Slave package no such status.
     */
SLAVE_PACKAGE_NO_SUCH_STATUS("828", "package status is invalid", false),
    /**
     * The Slave package is not exist.
     */
SLAVE_PACKAGE_IS_NOT_EXIST("829", "package is not exist", false),
    /**
     * The Slave package not suitable height.
     */
SLAVE_PACKAGE_NOT_SUITABLE_HEIGHT("830", "current package height is not suitable", false),
    /**
     * The Slave amount illegal.
     */
SLAVE_AMOUNT_ILLEGAL("831", "amount is illegal", false),
    /**
     * The Slave account currency already exists error.
     */
SLAVE_ACCOUNT_CURRENCY_ALREADY_EXISTS_ERROR("832", "currency is already exists", false),
    /**
     * The Slave contract not exist error.
     */
SLAVE_CONTRACT_NOT_EXIST_ERROR("833", "contract is not exist", false),
    /**
     * The Slave last package not finish.
     */
SLAVE_LAST_PACKAGE_NOT_FINISH("834", "last package is not finished, just waiting", false),
    /**
     * The Slave package replicate failed.
     */
SLAVE_PACKAGE_REPLICATE_FAILED("835", "package replicated to consensus failed", false),
    /**
     * The Slave package callback error.
     */
SLAVE_PACKAGE_CALLBACK_ERROR("836", "package callback rs has error", false),
    /**
     * The Slave rs callback not register error.
     */
SLAVE_RS_CALLBACK_NOT_REGISTER_ERROR("837", "rs callback not register error", false),
    /**
     * The Slave batch insert rows different error.
     */
SLAVE_BATCH_INSERT_ROWS_DIFFERENT_ERROR("838", "slave batch insert rows different error", false),
    /**
     * The Slave package received invalid node state.
     */
SLAVE_PACKAGE_RECEIVED_INVALID_NODE_STATE("839", "the node state is not running", false),
    /**
     * The Slave account freeze record is already exists error.
     */
SLAVE_ACCOUNT_FREEZE_RECORD_IS_ALREADY_EXISTS_ERROR("840", "account freeze record is already exists error", false),
    /**
     * The Slave rocks key already exist.
     */
SLAVE_ROCKS_KEY_ALREADY_EXIST("841", "rocks key is exist", false),
    /**
     * The Slave rocks key is not exist.
     */
SLAVE_ROCKS_KEY_IS_NOT_EXIST("842", "rocks key is not exist", false),
    /**
     * The Slave rocks transaction is null.
     */
SLAVE_ROCKS_TRANSACTION_IS_NULL("843", "rocks transaction is null", false),
    /**
     * The Slave block is not exist.
     */
SLAVE_BLOCK_IS_NOT_EXIST("844", "the block is not exist", false),


    /**
     * The Slave ca init error.
     */
SLAVE_CA_INIT_ERROR("900", "slave ca init error", false),
    /**
     * The Slave ca validate error.
     */
SLAVE_CA_VALIDATE_ERROR("901", "slave ca validate error", false),
    /**
     * The Slave ca write file error.
     */
SLAVE_CA_WRITE_FILE_ERROR("902", "slave ca write file error", false),
    /**
     * The Slave generate key error.
     */
SLAVE_GENERATE_KEY_ERROR("903", "slave generate key error", false),
    /**
     * The Slave smart contract error.
     */
SLAVE_SMART_CONTRACT_ERROR("904", "has SmartContractException", false),
    /**
     * The Slave leave consensus error.
     */
SLAVE_LEAVE_CONSENSUS_ERROR("905", "slave leave consensus error", false),
    /**
     * The Slave join consensus error.
     */
SLAVE_JOIN_CONSENSUS_ERROR("906", "join leave consensus error", false),
    /**
     * The Slave ca batch update error.
     */
SLAVE_CA_BATCH_UPDATE_ERROR("907", "slave ca batch update error", false),
    /**
     * The Slave ca update error.
     */
SLAVE_CA_UPDATE_ERROR("908", "slave ca update error", false),
    ;
    //@formatter:on

    /**
     * 枚举编码
     */
    private final String code;

    /**
     * 描述说明
     */
    private final String description;

    /**
     * 是否需要重试
     */
    private final boolean needRetry;

    /**
     * 私有构造函数。
     *
     * @param code        枚举编码
     * @param description 描述说明
     */
    private SlaveErrorEnum(String code, String description, boolean needRetry) {
        this.code = code;
        this.description = description;
        this.needRetry = needRetry;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public boolean isNeedRetry() {
        return needRetry;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code 枚举编码
     * @return 错误场景枚举 by code
     */
    public static SlaveErrorEnum getByCode(String code) {
        for (SlaveErrorEnum scenario : values()) {
            if (scenario.getCode().equals(code)) {

                return scenario;
            }
        }
        return null;
    }
}