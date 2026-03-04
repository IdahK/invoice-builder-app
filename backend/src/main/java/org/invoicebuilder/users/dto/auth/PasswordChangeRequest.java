package org.invoicebuilder.users.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public  record PasswordChangeRequest(
    @NotBlank(message = "Email is required")
    @Email(message = "Provide a valid email")
    String email,

    @NotBlank(message = "Current password is required")
    String currentPassword,

    @NotBlank(message = "New password is required")
    String newPassword
    ){
}