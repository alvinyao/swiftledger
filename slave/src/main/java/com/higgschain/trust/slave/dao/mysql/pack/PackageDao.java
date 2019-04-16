package com.higgschain.trust.slave.dao.mysql.pack;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.pack.PackagePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * The interface Package dao.
 */
@Mapper public interface PackageDao extends BaseDao<PackagePO> {

    /**
     * query package by block height
     *
     * @param height the height
     * @return package po
     */
    PackagePO queryByHeight(@Param("height") Long height);

    /**
     * query package list by block height
     *
     * @param height the height
     * @return list
     */
    List<Long> queryHeightListByHeight(@Param("height") Long height);

    /**
     * query packagePO by height for update
     *
     * @param height the height
     * @return package po
     */
    PackagePO queryByHeightForUpdate(@Param("height") Long height);

    /**
     * update status by block height
     *
     * @param height the height
     * @param from   the from
     * @param to     the to
     * @return int
     */
    int updateStatus(@Param("height") Long height, @Param("from") String from, @Param("to") String to);

    /**
     * get max height of package
     *
     * @return max height
     */
    Long getMaxHeight();

    /**
     * query package list by package status
     *
     * @param status the status
     * @return list
     */
    List<PackagePO> queryByStatus(@Param("status") String status);

    /**
     * query min package height with status and start height
     *
     * @param startHeight the start height
     * @param statusSet   the status set
     * @return min height with height and status
     */
    Long getMinHeightWithHeightAndStatus(@Param("startHeight") Long startHeight,
        @Param("statusSet") Set<String> statusSet);

    /**
     * count how much package
     *
     * @param statusSet      the status set
     * @param maxBlockHeight the max block height
     * @return long
     */
    Long countWithStatus(@Param("statusSet") Set<String> statusSet, @Param("maxBlockHeight") Long maxBlockHeight);

    /**
     * query max height by status
     *
     * @param status the status
     * @return max height by status
     */
    Long getMaxHeightByStatus(@Param("status")String status);

    /**
     * query min height by status
     *
     * @param status the status
     * @return min height by status
     */

    Long getMinHeightByStatus(@Param("status")String status);

    /**
     * delete by less than height
     *
     * @param height the height
     * @param status the status
     * @return int
     */
    int deleteLessThanHeightAndStatus(@Param("height")Long height,@Param("status")String status);

    /**
     * get height list by status
     *
     * @param status the status
     * @return block heights by status
     */
    List<Long> getBlockHeightsByStatus(@Param("status")String status);
}
