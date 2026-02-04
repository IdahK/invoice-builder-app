package org.invoicebuilder.invoices.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "senders")
@Data
public class Sender {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

}
