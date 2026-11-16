package com.capgemini.test.code.infrastructure.clients;

import com.capgemini.test.code.domain.model.Role;
import com.capgemini.test.code.domain.model.User;
import com.capgemini.test.code.domain.service.NotificationPort;
import org.springframework.stereotype.Component;

/**
 * Implementación del puerto NotificationPort.
 * Regla del cliente:
 *  - ADMIN      → email a /email con mensaje "usuario guardado".
 *  - SUPERADMIN → SMS   a /sms   con mensaje "usuario guardado".
 * Así validamos también que MockServer recibe la notificación correcta.
 */
@Component
public class NotificationAdapter implements NotificationPort {

    private final NotificationClient notificationClient;

    public NotificationAdapter(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @Override
    public void notifyUserCreated(User user) {
        String message = "usuario guardado";
        if (user.getRole() == Role.ADMIN) {
            notificationClient.sendEmail(
                    new EmailNotificationRequest(user.getEmail(), message));
        } else if (user.getRole() == Role.SUPERADMIN) {
            notificationClient.sendSms(
                    new SmsNotificationRequest(user.getPhone(), message));
        }
    }

    @Override
    public void notifyUserDeleted(User user) {
        // No requerido por el cliente; se deja vacío para no romper el contrato del puerto.
    }
}