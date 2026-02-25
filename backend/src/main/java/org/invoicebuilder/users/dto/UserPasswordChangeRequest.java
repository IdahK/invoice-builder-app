package org.invoicebuilder.users.dto;

import jakarta.validation.constraints.NotBlank;

public  record UserPasswordChangeRequest(
    @NotBlank(message = "Current password is required")
    String currentPassword,

    @NotBlank(message = "New password is required")
    String newPassword
    ){
}