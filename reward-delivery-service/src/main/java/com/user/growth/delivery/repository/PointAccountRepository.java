package com.user.growth.delivery.repository;

import com.user.growth.delivery.domain.PointAccount;
import org.springframework.stereotype.Repository;

/**
 * Repository stub for PointAccount persistence. Replace with a real MyBatis-
 * Plus mapper or JPA repository in production.
 */
@Repository
public class PointAccountRepository {

    public PointAccount findByUserId(Long userId) {
        // TODO: query MySQL; returning null in template
        return null;
    }

    public void save(PointAccount account) {
        // TODO: persist to MySQL
    }
}
