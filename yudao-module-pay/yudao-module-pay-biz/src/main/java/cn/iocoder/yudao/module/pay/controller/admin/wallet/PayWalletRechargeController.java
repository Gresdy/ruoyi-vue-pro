package cn.iocoder.yudao.module.pay.controller.admin.wallet;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayRefundNotifyReqDTO;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletRechargeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;

@Tag(name = "管理后台 - 钱包充值")
@RestController
@RequestMapping("/pay/wallet-recharge")
@Validated
@Slf4j
public class PayWalletRechargeController {

    @Resource
    private PayWalletRechargeService walletRechargeService;


    @GetMapping("/refund")
    @Operation(summary = "发起钱包充值退款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> refundWalletRecharge(@RequestParam("id") Long id) {
        walletRechargeService.refundWalletRecharge(id,getClientIP());
        return success(true);
    }

    @PostMapping("/update-refunded")
    @Operation(summary = "更新钱包充值为已退款") // 由 pay-module 支付服务，进行回调，可见 PayNotifyJob
    @PermitAll // 无需登录， 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public CommonResult<Boolean> updateWalletRechargeRefunded(@RequestBody PayRefundNotifyReqDTO notifyReqDTO) {
        walletRechargeService.updateWalletRechargeRefunded(
                Long.valueOf(notifyReqDTO.getMerchantOrderId()), notifyReqDTO.getPayRefundId());
        return success(true);
    }
}