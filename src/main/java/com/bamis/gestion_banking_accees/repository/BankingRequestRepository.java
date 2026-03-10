package com.bamis.gestion_banking_accees.repository;

import com.bamis.gestion_banking_accees.entity.BankingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankingRequestRepository extends JpaRepository<BankingRequest, Long> {

    List<BankingRequest> findByPhoneNumberOrderByCreatedDateDesc(String phoneNumber);

    // Récupérer les demandes par statut
    List<BankingRequest> findByStatusOrderByCreatedDateDesc(String status);
}
