package com.higgschain.trust.rs.core.controller;

import com.higgschain.trust.rs.core.api.RsManageService;
import com.higgschain.trust.rs.core.vo.manage.CancelRsVO;
import com.higgschain.trust.rs.core.vo.manage.RegisterPolicyVO;
import com.higgschain.trust.rs.core.vo.manage.RegisterRsVO;
import com.higgschain.trust.slave.api.enums.RespCodeEnum;
import com.higgschain.trust.slave.api.vo.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author tangfashuang
 * @date 2018/05/18 11:26
 * @desc manage controller
 */
@RequestMapping("/v1/manage")
@RestController
@Slf4j
public class ManageController {

    @Autowired
    private RsManageService rsManageService;

    /**
     * @param registerRsVO
     * @return
     */
    @RequestMapping("/rs/register")
    public RespData registerRs(@RequestBody @Valid RegisterRsVO registerRsVO, BindingResult result) {
        log.info("[ManageController.registerRs] register rs request receive. {}", registerRsVO);

        if (result.hasErrors()) {
            log.error("[ManageController.registerRs] register rs request param is invalid, errMsg={}", result.getAllErrors());
            return new RespData(RespCodeEnum.PARAM_NOT_VALID.getRespCode(),RespCodeEnum.PARAM_NOT_VALID.getMsg());
        }

        return rsManageService.registerRs(registerRsVO);
    }

    /**
     * @param cancelRsVO
     * @return
     */
    @RequestMapping("/rs/cancel")
    public RespData cancelRs(@RequestBody @Valid CancelRsVO cancelRsVO, BindingResult result) {
        log.info("[ManageController.cancelRs] cancel rs request receive. {}", cancelRsVO);

        if (result.hasErrors()) {
            log.error("[ManageController.cancelRs] cancel rs request param is invalid, errMsg={}", result.getAllErrors());
            return new RespData(RespCodeEnum.PARAM_NOT_VALID.getRespCode(),RespCodeEnum.PARAM_NOT_VALID.getMsg());
        }

        return rsManageService.cancelRs(cancelRsVO);
    }

    @RequestMapping("/policy/register")
    public RespData registerPolicy(@RequestBody @Valid RegisterPolicyVO registerPolicyVO, BindingResult result) {
        log.info("[ManageController.registerPolicy] register policy request receive. {}", registerPolicyVO);

        if (result.hasErrors()) {
            log.error("[ManageController.registerPolicy] register policy request param is invalid, errMsg={}", result.getAllErrors());
            return new RespData(RespCodeEnum.PARAM_NOT_VALID.getRespCode(),RespCodeEnum.PARAM_NOT_VALID.getMsg());
        }

        return rsManageService.registerPolicy(registerPolicyVO);
    }
}
