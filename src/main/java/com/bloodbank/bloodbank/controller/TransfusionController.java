package com.bloodbank.bloodbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bloodbank.bloodbank.entity.TransfusionRecord;
import com.bloodbank.bloodbank.service.TransfusionService;

@RestController
@RequestMapping("/api/transfusions")
public class TransfusionController {

    @Autowired
    private TransfusionService service;

    @PostMapping
    public TransfusionRecord create(@RequestBody TransfusionRecord record) {
        return service.save(record);
    }

    @GetMapping("/blood-unit/{unitId}")
    public List<TransfusionRecord> getByUnit(@PathVariable Long unitId) {
        return service.getByUnit(unitId);
    }
}