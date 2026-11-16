package com.capgemini.test.code.domain.service;

import com.capgemini.test.code.domain.model.User;

public interface NotificationPort {
    void notifyUserCreated(User user);
    void notifyUserDeleted(User user);
}