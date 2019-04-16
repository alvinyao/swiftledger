package com.higgschain.trust.slave.api.controller;

import com.higgschain.trust.slave.metrics.TrustMetrics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Monitor controller.
 *
 * @author duhongming
 * @date 2018 /12/27
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    /**
     * Gets metrics.
     *
     * @return the metrics
     */
    @GetMapping("/metrics")
    public Object getMetrics() {
        return TrustMetrics.getDefault().getReportedMetrics();
    }
}
