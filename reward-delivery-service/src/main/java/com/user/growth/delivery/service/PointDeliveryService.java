package com.user.growth.delivery.service;

import com.user.growth.delivery.domain.PointAccount;
import com.user.growth.delivery.repository.PointAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service responsible for persisting granted points into the account
 * repository and sending notifications to users.
 */
@Service
public class PointDeliveryService {

    private final PointAccountRepository accountRepository;

    @Autowired
    public PointDeliveryService(PointAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Grant points to user based on the delivery message. In production this
     * operation must be idempotent and transactional. The repository should
     * ensure idempotency via unique keys or DB constraints.
     */
    public void grantPoints(Map<String, Object> message) {
        Long userId = ((Number) message.getOrDefault("userId", 0)).longValue();
        Integer points = ((Number) message.getOrDefault("points", 0)).intValue();

        if (userId == null || userId == 0) {
            return;
        }

        // Fetch or create user account
        PointAccount account = accountRepository.findByUserId(userId);
        if (account == null) {
            account = new PointAccount();
            account.setUserId(userId);
            account.setPoints(0L);
        }

        account.setPoints(account.getPoints() + points);
        accountRepository.save(account);

        // Notify user via push/sms etc. (stubbed)
    }
}
