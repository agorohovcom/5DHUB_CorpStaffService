package com.agorohov.cs_service.corp_service.controller;

import com.agorohov.cs_service.corp_service.service.CorpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/corps")
public class CorpController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CorpService userService;

    public CorpController(CorpService userService) {
        this.userService = userService;
    }

    @GetMapping("/by-id/{id}")
    public String getById(@PathVariable(value = "id") long id) {
        return "Вызван getById с id=" + id;
    }

    @GetMapping("/by-name/{name}")
    public String getByName(@PathVariable(value = "name") String name) {
        return "Вызван getByName с name=" + name;
    }
}
