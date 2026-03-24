package com.bamis.gestion_banking_accees.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "banking_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", nullable = false, unique = true, length = 50)
    private String reference;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "agence", length = 6)
    private String agence;

    @Column(name = "service_type", nullable = false, length = 50)
    private String serviceType;

    @Column(name = "modification_type", nullable = false, length = 50)
    private String modificationType;

    @Column(name = "other_message", columnDefinition = "TEXT")
    private String otherMessage;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        if (this.status == null) {
            this.status = "PENDING";
        }
        if (this.reference == null) {
            this.reference = generateReference();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    private String generateReference() {
        String serviceCode;
        if (this.serviceType != null && this.serviceType.toUpperCase().contains("MOBILE")) {
            serviceCode = "MOB";
        } else if (this.serviceType != null && this.serviceType.toUpperCase().contains("WEB")) {
            serviceCode = "WEB";
        } else {
            serviceCode = "XXX";
        }

        String modCode = this.modificationType != null
                ? Normalizer.normalize(
                this.modificationType.substring(0, Math.min(3, this.modificationType.length())),
                Normalizer.Form.NFD
        ).replaceAll("\\p{InCombiningDiacriticalMarks}", "").toUpperCase()
                : "XXX";

        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(this.createdDate);

        return serviceCode + "-" + modCode + "-" + timestamp;
    }
}