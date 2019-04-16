package com.higgschain.trust.slave.model.convert;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.consensus.util.DeflateUtil;
import com.higgschain.trust.slave.api.vo.PackageVO;
import com.higgschain.trust.slave.model.bo.Package;

/**
 * The type Package convert.
 *
 * @author tangfashuang
 * @date 2018 /04/11 19:57
 * @desc package convert util
 */
public class PackageConvert {
    /**
     * Convert pack vo to pack package.
     *
     * @param packageVO the package vo
     * @return the package
     */
    public static Package convertPackVOToPack(PackageVO packageVO) {
        Package pack = new Package();
        pack.setPackageTime(packageVO.getPackageTime());
        pack.setHeight(packageVO.getHeight());
        pack.setSignedTxList(packageVO.getSignedTxList());
        return pack;
    }

    /**
     * Convert pack to pack vo package vo.
     *
     * @param pack the pack
     * @return the package vo
     */
    public static PackageVO convertPackToPackVO(Package pack) {
        PackageVO packVO = new PackageVO();
        packVO.setPackageTime(pack.getPackageTime());
        packVO.setHeight(pack.getHeight());
        packVO.setSignedTxList(pack.getSignedTxList());
        return packVO;
    }

    /**
     * Convert pack to pack vo to bytes byte [ ].
     *
     * @param pack the pack
     * @return the byte [ ]
     */
    public static byte[] convertPackToPackVOToBytes(Package pack){
        String result = JSON.toJSONString(pack);
        return DeflateUtil.compress(result.getBytes());
    }
}
