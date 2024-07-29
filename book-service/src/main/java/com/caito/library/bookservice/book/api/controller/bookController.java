package com.caito.library.bookservice.book.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@Slf4j
public class bookController {

    @GetMapping("/authorized")
    public Map<String, String> authorized(@RequestParam String code){
        return Collections.singletonMap("code", code);
    }
}
