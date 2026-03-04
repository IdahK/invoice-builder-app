package org.invoicebuilder.users.dto.user;

import lombok.Builder;
import org.invoicebuilder.users.domain.Account;

@Builder
public record CreateUserRequest(
        String email,
        String password,
        String displayName,
        Account account
) {
}
