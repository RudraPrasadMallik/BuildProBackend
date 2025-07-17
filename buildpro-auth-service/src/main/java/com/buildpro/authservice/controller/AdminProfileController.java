package com.buildpro.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard")
public class AdminProfileController {
	 @GetMapping
	    public ResponseEntity<String> dashboard() {
	        String email = SecurityContextHolder.getContext().getAuthentication().getName();
	        return ResponseEntity.ok("Welcome Admin: " + email);
	    }
}
