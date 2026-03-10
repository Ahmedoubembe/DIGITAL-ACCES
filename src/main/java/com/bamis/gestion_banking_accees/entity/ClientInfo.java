package com.bamis.gestion_banking_accees.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "epargne_client_info")
@Data
@NoArgsConstructor
public class ClientInfo {

    @Id
    @Column(name = "cust_iden")
    private String custIden;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "wallet_code")
    private String walletCode;

}