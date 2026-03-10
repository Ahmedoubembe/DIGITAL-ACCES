package com.bamis.gestion_banking_accees.service.impl;

import com.bamis.gestion_banking_accees.entity.BankingRequest;
import com.bamis.gestion_banking_accees.repository.BankingRequestRepository;
import com.bamis.gestion_banking_accees.service.BankingRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankingRequestServiceImpl implements BankingRequestService {

    private final BankingRequestRepository bankingRequestRepository;

    @Override
    @Transactional
    public BankingRequest createRequest(BankingRequest request) {
        if (request.getStatus() == null) {
            request.setStatus("PENDING");
        }
        return bankingRequestRepository.save(request);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BankingRequest> getAllRequests() {
        return bankingRequestRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankingRequest> getRequestsByPhoneNumber(String phoneNumber) {
        return bankingRequestRepository.findByPhoneNumberOrderByCreatedDateDesc(phoneNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankingRequest> getRequestsByStatus(String status) {
        return bankingRequestRepository.findByStatusOrderByCreatedDateDesc(status);
    }
}