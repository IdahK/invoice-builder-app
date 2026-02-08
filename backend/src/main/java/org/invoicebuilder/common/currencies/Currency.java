package org.invoicebuilder.common.currencies;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "currencies")
@Data
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "currency_code", length = 3, nullable = false)
    private String code;

    @Column(name = "currency_name", nullable = false)
    private String name;
}
