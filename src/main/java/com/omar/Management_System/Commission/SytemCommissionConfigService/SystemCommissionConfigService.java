package com.omar.Management_System.Commission.SytemCommissionConfigService;

import com.omar.Management_System.Commission.SystemCommissionConfig.SystemCommissionConfig;
import com.omar.Management_System.Commission.SystemCommissionConfigRepository.SystemCommissionConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SystemCommissionConfigService {

    @Autowired
    private SystemCommissionConfigRepository configRepository;

    public double getCurrentRate() {
        SystemCommissionConfig config = configRepository.findTopByOrderByIdDesc();
        return config != null ? config.getRate() : 0.10;
    }

    public SystemCommissionConfig updateRate(double newRate) {
        SystemCommissionConfig config = configRepository.findTopByOrderByIdDesc();
        if (config == null) config = new SystemCommissionConfig();
        config.setRate(newRate);
        return configRepository.save(config);
    }

    public BigDecimal calculateCommission(BigDecimal amount) {
        BigDecimal commissionRate = BigDecimal.valueOf(getCurrentRate());
        return amount.multiply(commissionRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
