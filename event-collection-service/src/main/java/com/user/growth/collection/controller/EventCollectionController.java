package com.user.growth.collection.controller;

import com.user.growth.collection.domain.BehaviorEvent;
import com.user.growth.collection.service.EventCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for collecting user behavior events.
 *
 * Exposes endpoints used by client SDKs to submit single or batched events.
 */
@RestController
@RequestMapping("/api/v1/events")
public class EventCollectionController {

    private final EventCollectionService collectionService;

    @Autowired
    public EventCollectionController(EventCollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping("/collect")
    public ResponseEntity<String> collectEvent(@RequestBody BehaviorEvent event) {
        collectionService.collectEvent(event);
        return ResponseEntity.ok("accepted");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }
}
