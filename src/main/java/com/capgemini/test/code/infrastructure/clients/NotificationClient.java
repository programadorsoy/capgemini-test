package com.capgemini.test.code.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationClient", url = "${external.service.url}")
public interface NotificationClient {

    @PostMapping("/email")
    ResponseEntity<Void> sendEmail(@RequestBody EmailNotificationRequest request);

    @PostMapping("/sms")
    ResponseEntity<Void> sendSms(@RequestBody SmsNotificationRequest request);
}