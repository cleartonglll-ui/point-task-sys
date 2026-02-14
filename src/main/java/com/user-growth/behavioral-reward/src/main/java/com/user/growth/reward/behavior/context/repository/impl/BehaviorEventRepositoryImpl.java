package com.user.growth.reward.behavior.context.repository.impl;

import com.user.growth.reward.behavior.context.domain.BehaviorEvent;
import com.user.growth.reward.behavior.context.domain.BehaviorEventDocument;
import com.user.growth.reward.behavior.context.repository.BehaviorEventRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 行为事件仓储实现
 *
 * 基于 MongoDB 的行为事件持久化实现
 *
 * @author system
 * @since 1.0.0
 */
@Repository
public class BehaviorEventRepositoryImpl implements BehaviorEventRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "behavior_events";

    @Override
    public BehaviorEventDocument save(BehaviorEvent event) {
        BehaviorEventDocument document = convertToDocument(event);
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        return mongoTemplate.save(document, COLLECTION_NAME);
    }

    @Override
    public BehaviorEventDocument findByEventId(String eventId) {
        Query query = new Query(Criteria.where("eventId").is(eventId));
        return mongoTemplate.findOne(query, BehaviorEventDocument.class, COLLECTION_NAME);
    }

    @Override
    public List<BehaviorEventDocument> findByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.addCriteria(Criteria.where("eventTime").gte(startTime).lte(endTime));
        return mongoTemplate.find(query, BehaviorEventDocument.class, COLLECTION_NAME);
    }

    @Override
    public boolean updateStatus(String eventId, Integer status, LocalDateTime processTime, String failReason) {
        Query query = new Query(Criteria.where("eventId").is(eventId));
        Update update = new Update();
        update.set("status", status);
        update.set("updatedAt", LocalDateTime.now());
        if (processTime != null) {
            update.set("processTime", processTime);
        }
        if (failReason != null) {
            update.set("failReason", failReason);
        }
        if (status == 1) {
            update.set("pointAwarded", true);
        }
        return mongoTemplate.updateFirst(query, update, COLLECTION_NAME).getModifiedCount() > 0;
    }

    @Override
    public List<BehaviorEventDocument> findPendingEvents(int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(0));
        query.limit(limit);
        return mongoTemplate.find(query, BehaviorEventDocument.class, COLLECTION_NAME);
    }

    @Override
    public List<BehaviorEventDocument> saveAll(List<BehaviorEvent> events) {
        if (events == null || events.isEmpty()) {
            return new ArrayList<>();
        }
        List<BehaviorEventDocument> documents = events.stream()
                .map(this::convertToDocument)
                .peek(doc -> {
                    doc.setCreatedAt(LocalDateTime.now());
                    doc.setUpdatedAt(LocalDateTime.now());
                })
                .collect(Collectors.toList());
        return mongoTemplate.saveAll(documents, COLLECTION_NAME);
    }

    /**
     * 转换领域模型为 MongoDB 文档
     */
    private BehaviorEventDocument convertToDocument(BehaviorEvent event) {
        BehaviorEventDocument document = new BehaviorEventDocument();
        BeanUtils.copyProperties(event, document);
        if (event.getEventType() != null) {
            document.setEventTypeCode(event.getEventType().getCode());
        }
        return document;
    }
}
