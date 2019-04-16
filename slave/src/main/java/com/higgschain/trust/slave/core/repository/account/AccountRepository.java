package com.higgschain.trust.slave.core.repository.account;

import com.higgschain.trust.common.utils.BeanConvertor;
import com.higgschain.trust.common.utils.Profiler;
import com.higgschain.trust.slave.api.enums.account.AccountStateEnum;
import com.higgschain.trust.slave.api.vo.AccountInfoVO;
import com.higgschain.trust.slave.common.config.InitConfig;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.repository.DataIdentityRepository;
import com.higgschain.trust.slave.dao.mysql.account.AccountDcRecordDao;
import com.higgschain.trust.slave.dao.mysql.account.AccountDetailDao;
import com.higgschain.trust.slave.dao.mysql.account.AccountInfoDao;
import com.higgschain.trust.slave.dao.mysql.account.AccountJDBCDao;
import com.higgschain.trust.slave.dao.po.account.AccountDcRecordPO;
import com.higgschain.trust.slave.dao.po.account.AccountDetailPO;
import com.higgschain.trust.slave.dao.po.account.AccountInfoPO;
import com.higgschain.trust.slave.dao.po.account.AccountInfoWithOwnerPO;
import com.higgschain.trust.slave.dao.rocks.account.AccountDcRecordRocksDao;
import com.higgschain.trust.slave.dao.rocks.account.AccountDetailRocksDao;
import com.higgschain.trust.slave.dao.rocks.account.AccountInfoRocksDao;
import com.higgschain.trust.slave.model.bo.account.AccountDcRecord;
import com.higgschain.trust.slave.model.bo.account.AccountDetail;
import com.higgschain.trust.slave.model.bo.account.AccountInfo;
import com.higgschain.trust.slave.model.bo.account.OpenAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * The type Account repository.
 *
 * @author liuyu
 * @description
 * @date 2018 -04-10
 */
@Repository @Slf4j public class AccountRepository {
    /**
     * The Account info dao.
     */
    @Autowired
    AccountInfoDao accountInfoDao;
    /**
     * The Account info rocks dao.
     */
    @Autowired
    AccountInfoRocksDao accountInfoRocksDao;
    /**
     * The Account detail dao.
     */
    @Autowired AccountDetailDao accountDetailDao;
    /**
     * The Account detail rocks dao.
     */
    @Autowired AccountDetailRocksDao accountDetailRocksDao;
    /**
     * The Account dc record dao.
     */
    @Autowired
    AccountDcRecordDao accountDcRecordDao;
    /**
     * The Account dc record rocks dao.
     */
    @Autowired
    AccountDcRecordRocksDao accountDcRecordRocksDao;
    /**
     * The Data identity repository.
     */
    @Autowired
    DataIdentityRepository dataIdentityRepository;
    /**
     * The Account jdbc dao.
     */
    @Autowired
    AccountJDBCDao accountJDBCDao;
    /**
     * The Init config.
     */
    @Autowired
    InitConfig initConfig;

    /**
     * query account info by account no
     *
     * @param accountNo the account no
     * @param forUpdate the for update
     * @return account info
     */
    public AccountInfo queryAccountInfo(String accountNo, boolean forUpdate) {
        AccountInfoPO accountInfo;
        if (initConfig.isUseMySQL()) {
            accountInfo = accountInfoDao.queryByAccountNo(accountNo, forUpdate);
        } else {
            //for update 参数未使用
            accountInfo = accountInfoRocksDao.get(accountNo);
        }
        return BeanConvertor.convertBean(accountInfo, AccountInfo.class);
    }

    /**
     * batch query the account info
     *
     * @param accountNos the account nos
     * @return list
     */
    public List<AccountInfoVO> queryByAccountNos(List<String> accountNos) {
        List<AccountInfoPO> list;
        if (initConfig.isUseMySQL()) {
            list = accountInfoDao.queryByAccountNos(accountNos);
        } else {
            list = accountInfoRocksDao.queryByAccountNos(accountNos);
        }
        return BeanConvertor.convertList(list, AccountInfoVO.class);
    }

    /**
     * build an new account info
     *
     * @param openAccount the open account
     * @return account info
     */
    public AccountInfo buildAccountInfo(OpenAccount openAccount) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountNo(openAccount.getAccountNo());
        accountInfo.setCurrency(openAccount.getCurrency());
        accountInfo.setBalance(BigDecimal.ZERO);
        accountInfo.setFreezeAmount(BigDecimal.ZERO);
        accountInfo.setFundDirection(openAccount.getFundDirection().getCode());
        accountInfo.setDetailNo(0L);
        accountInfo.setDetailFreezeNo(0L);
        accountInfo.setStatus(AccountStateEnum.NORMAL.getCode());
        return accountInfo;
    }

    /**
     * for explorer
     *
     * @param accountNo the account no
     * @param dataOwner the data owner
     * @param pageNo    the page no
     * @param pageSize  the page size
     * @return list
     */
    public List<AccountInfoVO> queryAccountInfoWithOwner(String accountNo, String dataOwner, Integer pageNo,
        Integer pageSize) {
        if (null != accountNo) {
            accountNo = accountNo.trim();
        }

        if (null != dataOwner) {
            dataOwner = dataOwner.trim();
        }

        List<AccountInfoWithOwnerPO> list =
            accountInfoDao.queryAccountInfoWithOwner(accountNo, dataOwner, (pageNo - 1) * pageSize, pageSize);
        return BeanConvertor.convertList(list, AccountInfoVO.class);
    }

    /**
     * for explorer
     *
     * @param accountNo the account no
     * @param dataOwner the data owner
     * @return long
     */
    public long countAccountInfoWithOwner(String accountNo, String dataOwner) {
        if (null != accountNo) {
            accountNo = accountNo.trim();
        }

        if (null != dataOwner) {
            dataOwner = dataOwner.trim();
        }

        return accountInfoDao.countAccountInfoWithOwner(accountNo, dataOwner);
    }

    /**
     * batch insert
     *
     * @param accountInfos the account infos
     */
    public void batchInsert(List<AccountInfo> accountInfos) {
        if (CollectionUtils.isEmpty(accountInfos)) {
            return;
        }
        try {
            Profiler.enter("[batchInsert accountInfo]");
            if (initConfig.isUseMySQL()) {
                int r = accountJDBCDao.batchInsertAccount(accountInfos);
                if (r != accountInfos.size()) {
                    log.info("[batchInsert]the number of update rows is different from the original number");
                    throw new SlaveException(SlaveErrorEnum.SLAVE_BATCH_INSERT_ROWS_DIFFERENT_ERROR);
                }
            } else {
                accountInfoRocksDao.batchInsert(
                    BeanConvertor.convertList(accountInfos, AccountInfoPO.class));
            }
        } catch (DuplicateKeyException e) {
            log.error("[batchInsert] has idempotent for accountInfos:{}", accountInfos);
            throw new SlaveException(SlaveErrorEnum.SLAVE_IDEMPOTENT);
        } finally {
            Profiler.release();
        }
    }

    /**
     * batch update
     *
     * @param accountInfos the account infos
     */
    public void batchUpdate(List<AccountInfo> accountInfos) {
        if (CollectionUtils.isEmpty(accountInfos)) {
            return;
        }
        try {
            Profiler.enter("[batchUpdate accountInfo]");
            if (initConfig.isUseMySQL()) {
                int r = accountJDBCDao.batchUpdateAccount(accountInfos);
                if (r != accountInfos.size()) {
                    log.info("[batchUpdate]the number of update rows is different from the original number");
                    throw new SlaveException(SlaveErrorEnum.SLAVE_BATCH_INSERT_ROWS_DIFFERENT_ERROR);
                }
            } else {
                accountInfoRocksDao.batchInsert(
                    BeanConvertor.convertList(accountInfos, AccountInfoPO.class));
            }
        } finally {
            Profiler.release();
        }
    }

    /**
     * batch insert account detail
     *
     * @param accountDetails the account details
     */
    public void batchInsertAccountDetail(List<AccountDetail> accountDetails) {
        if (CollectionUtils.isEmpty(accountDetails)) {
            return;
        }
        try {
            Profiler.enter("[batchInsert accountDetail]");
            if (initConfig.isUseMySQL()) {
                int r = accountJDBCDao.batchInsertAccountDetail(accountDetails);
                if (r != accountDetails.size()) {
                    log.info("[batchInsertAccountDetail]the number of update rows is different from the original number");
                    throw new SlaveException(SlaveErrorEnum.SLAVE_BATCH_INSERT_ROWS_DIFFERENT_ERROR);
                }
            } else {
                accountDetailRocksDao.batchInsert(
                    BeanConvertor.convertList(accountDetails, AccountDetailPO.class));
            }
        } catch (DuplicateKeyException e) {
            log.error("[batchInsertAccountDetail] has idempotent for accountDetails:{}", accountDetails);
            throw new SlaveException(SlaveErrorEnum.SLAVE_IDEMPOTENT);
        } finally {
            Profiler.release();
        }
    }

    /**
     * batch insert DC record
     *
     * @param dcRecords the dc records
     */
    public void batchInsertDcRecords(List<AccountDcRecord> dcRecords) {
        if (CollectionUtils.isEmpty(dcRecords)) {
            return;
        }
        try {
            Profiler.enter("[batchInsert accountDcRecords]");
            if (initConfig.isUseMySQL()) {
                int r = accountJDBCDao.batchInsertDcRecords(dcRecords);
                if (r != dcRecords.size()) {
                    log.info("[batchInsertDcRecords]the number of update rows is different from the original number");
                    throw new SlaveException(SlaveErrorEnum.SLAVE_BATCH_INSERT_ROWS_DIFFERENT_ERROR);
                }
            } else {
                accountDcRecordRocksDao.batchInsert(
                    BeanConvertor.convertList(dcRecords, AccountDcRecordPO.class));
            }
        } catch (DuplicateKeyException e) {
            log.error("[batchInsertDcRecords] has idempotent for dcRecords:{}", dcRecords);
            throw new SlaveException(SlaveErrorEnum.SLAVE_IDEMPOTENT);
        } finally {
            Profiler.release();
        }
    }
}
