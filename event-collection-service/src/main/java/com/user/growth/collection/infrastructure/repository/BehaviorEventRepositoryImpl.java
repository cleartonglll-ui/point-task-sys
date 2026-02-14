package com.user.growth.collection.infrastructure.repository;

import com.user.growth.collection.domain.aggregate.BehaviorEvent;
import com.user.growth.collection.domain.repository.IBehaviorEventRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 行为事件仓储实现
 * 基础设施层：MongoDB实现
 * 
 * 该实现类提供了行为事件仓储接口的具体实现，使用MongoDB作为数据存储，
 * 通过MongoTemplate执行数据库操作，确保领域层与具体技术实现的解耦。
 */
@Repository
public class BehaviorEventRepositoryImpl implements IBehaviorEventRepository {

    private final MongoTemplate mongoTemplate;

    public BehaviorEventRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public BehaviorEvent save(BehaviorEvent event) {
        return mongoTemplate.save(event);
    }

    @Override
    public BehaviorEvent findById(String id) {
        return mongoTemplate.findById(id, BehaviorEvent.class);
    }

    @Override
    public List<BehaviorEvent> findPendingEvents(int limit) {
        Query query = new Query(Criteria.where("status").is(0)).limit(limit);
        return mongoTemplate.find(query, BehaviorEvent.class);
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("status", status);
        mongoTemplate.updateFirst(query, update, BehaviorEvent.class);
    }
}