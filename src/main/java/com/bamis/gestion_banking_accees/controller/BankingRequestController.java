package com.bamis.gestion_banking_accees.controller;

import com.bamis.gestion_banking_accees.entity.BankingRequest;
import com.bamis.gestion_banking_accees.repository.BankingRequestRepository;
import com.bamis.gestion_banking_accees.repository.ClientInfoRepository;
import com.bamis.gestion_banking_accees.service.BankingRequestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/banking-requests")
@CrossOrigin(origins = {"http://localhost:4202", "http://localhost:4200", "http://172.24.1.20:8080"})
@RequiredArgsConstructor
public class BankingRequestController {

    private final BankingRequestService bankingRequestService;
    private final ClientInfoRepository clientInfoRepository;
    private final BankingRequestRepository bankingRequestRepository;
    private static final Logger logger = LoggerFactory.getLogger(BankingRequestController.class);

    // -------------------------------------------------------------------------
    // ENDPOINT — Soumettre une demande
    // POST /api/banking-requests/submit
    // -------------------------------------------------------------------------

    @PostMapping("/submit")
    public ResponseEntity<?> submitRequest(@RequestBody Map<String, Object> requestBody) {
        try {
            String phoneNumber = (String) requestBody.get("phoneNumber");
            String clientName = (String) requestBody.get("clientName");
            String email = (String) requestBody.get("email");
            String serviceType = (String) requestBody.get("serviceType");
            String modificationType = (String) requestBody.get("modificationType");
            String otherMessage = (String) requestBody.get("otherMessage");

            if (phoneNumber == null || phoneNumber.trim().isEmpty() ||
                    clientName == null || clientName.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    serviceType == null || serviceType.trim().isEmpty() ||
                    modificationType == null || modificationType.trim().isEmpty()) {

                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Tous les champs obligatoires doivent etre renseignes");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            logger.info("Soumission demande banking pour: {} - Service: {} - Type: {}",
                    phoneNumber, serviceType, modificationType);

            BankingRequest request = BankingRequest.builder()
                    .phoneNumber(phoneNumber.trim())
                    .clientName(clientName.trim())
                    .email(email.trim())
                    .serviceType(serviceType.trim())
                    .modificationType(modificationType.trim())
                    .otherMessage(otherMessage != null ? otherMessage.trim() : null)
                    .status("PENDING")
                    .build();

            BankingRequest savedRequest = bankingRequestService.createRequest(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Votre demande a ete soumise avec succes");
            response.put("requestId", savedRequest.getId());
            response.put("reference", savedRequest.getReference());

            logger.info("Demande banking creee avec succes - ID: {}", savedRequest.getId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Erreur lors de la soumission de la demande banking", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Erreur lors de la soumission de la demande: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



    // -------------------------------------------------------------------------
    // ENDPOINT — Recuperer par numero de telephone
    // GET /api/banking-requests/phone/{phoneNumber}
    // -------------------------------------------------------------------------

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<?> getRequestsByPhone(@PathVariable String phoneNumber) {
        try {
            logger.info("Recuperation des demandes pour le numero: {}", phoneNumber);

            List<BankingRequest> requests = bankingRequestService.getRequestsByPhoneNumber(phoneNumber);

            List<Map<String, Object>> simplifiedRequests = requests.stream()
                    .map(request -> {
                        Map<String, Object> data = new HashMap<>();
                        data.put("service", request.getServiceType());
                        data.put("typeModification", request.getModificationType());
                        data.put("dateSubmission", request.getCreatedDate().toString());
                        data.put("status", request.getStatus());
                        data.put("reference", request.getReference());
                        return data;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("total", simplifiedRequests.size());
            response.put("data", simplifiedRequests);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Erreur lors de la recuperation des demandes pour: {}", phoneNumber, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Erreur lors de la recuperation des demandes");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // -------------------------------------------------------------------------
    // ENDPOINT — Recuperer par statut
    // GET /api/banking-requests/status/{status}
    // -------------------------------------------------------------------------

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getRequestsByStatus(@PathVariable String status) {
        try {
            logger.info("Recuperation des demandes avec statut: {}", status);

            List<BankingRequest> requests = bankingRequestService.getRequestsByStatus(status);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("total", requests.size());
            response.put("data", requests);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Erreur lors de la recuperation des demandes par statut: {}", status, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Erreur lors de la recuperation des demandes");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}












//package com.bamis.gestion_banking_accees.controller;
//
//import com.bamis.gestion_banking_accees.entity.BankingRequest;
//import com.bamis.gestion_banking_accees.service.BankingRequestService;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.bamis.gestion_banking_accees.repository.ClientInfoRepository;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/banking-requests")
//@CrossOrigin(origins = {"http://localhost:4202", "http://localhost:4200","http://172.24.1.20:8080" })
//@RequiredArgsConstructor
//public class BankingRequestController {
//
//    private final BankingRequestService bankingRequestService;
//    private final ClientInfoRepository clientInfoRepository;
//    private static final Logger logger = LoggerFactory.getLogger(BankingRequestController.class);
//
//    /**
//     * Endpoint pour soumettre une demande d'accès banking après validation OTP
//     */
//    @PostMapping("/submit")
//    public ResponseEntity<?> submitRequest(@RequestBody Map<String, Object> requestBody) {
//        try {
//            // Récupération des données
//            String phoneNumber = (String) requestBody.get("phoneNumber");
//            String clientName = (String) requestBody.get("clientName");
//            String email = (String) requestBody.get("email");
//            String serviceType = (String) requestBody.get("serviceType");
//            String modificationType = (String) requestBody.get("modificationType");
//            String otherMessage = (String) requestBody.get("otherMessage");
//
//            // Validation des champs obligatoires
//            if (phoneNumber == null || phoneNumber.trim().isEmpty() ||
//                    clientName == null || clientName.trim().isEmpty() ||
//                    email == null || email.trim().isEmpty() ||
//                    serviceType == null || serviceType.trim().isEmpty() ||
//                    modificationType == null || modificationType.trim().isEmpty()) {
//
//                Map<String, Object> errorResponse = new HashMap<>();
//                errorResponse.put("success", false);
//                errorResponse.put("message", "Tous les champs obligatoires doivent être renseignés");
//                return ResponseEntity.badRequest().body(errorResponse);
//            }
//
//            logger.info("Soumission demande banking pour: {} - Service: {} - Type: {}",
//                    phoneNumber, serviceType, modificationType);
//
//            // Créer l'entité
//            BankingRequest request = BankingRequest.builder()
//                    .phoneNumber(phoneNumber.trim())
//                    .clientName(clientName.trim())
//                    .email(email.trim())
//                    .serviceType(serviceType.trim())
//                    .modificationType(modificationType.trim())
//                    .otherMessage(otherMessage != null ? otherMessage.trim() : null)
//                    .status("PENDING")
//                    .build();
//
//            // Sauvegarder
//            BankingRequest savedRequest = bankingRequestService.createRequest(request);
//
//            // Réponse succès
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("message", "Votre demande a été soumise avec succès");
//            response.put("requestId", savedRequest.getId());
//            response.put("reference", savedRequest.getReference());
//
//
//            logger.info("Demande banking créée avec succès - ID: {}", savedRequest.getId());
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            logger.error("Erreur lors de la soumission de la demande banking", e);
//
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Erreur lors de la soumission de la demande: " + e.getMessage());
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
//
//    /**
//     * Récupérer toutes les demandes
//     */
//    @GetMapping("/all")
//    public ResponseEntity<?> getAllRequests() {
//        try {
//            logger.info("Récupération de toutes les demandes banking");
//
//            List<BankingRequest> requests = bankingRequestService.getAllRequests();
//
//            List<Map<String, Object>> enrichedRequests = requests.stream()
//                    .map(request -> {
//                        Map<String, Object> data = new HashMap<>();
//                        data.put("id", request.getId());
//                        data.put("phoneNumber", request.getPhoneNumber());
//                        data.put("clientName", request.getClientName());
//                        data.put("email", request.getEmail());
//                        data.put("serviceType", request.getServiceType());
//                        data.put("modificationType", request.getModificationType());
//                        data.put("otherMessage", request.getOtherMessage());
//                        data.put("status", request.getStatus());
//                        data.put("createdDate", request.getCreatedDate());
//                        data.put("updatedDate", request.getUpdatedDate());
//                        data.put("reference", request.getReference());
//
//                        clientInfoRepository.findByPhoneNumber(request.getPhoneNumber())
//                                .ifPresent(client -> {
//                                    data.put("custIden", client.getCustIden());
//                                    data.put("emailSys", client.getEmail());
//                                    data.put("firstName", client.getFirstName());
//                                    data.put("lastName", client.getLastName());
//                                    data.put("agence", client.getWalletCode().substring(0, 5));
//                                });
//
//                        return data;
//                    })
//                    .collect(Collectors.toList());
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("total", enrichedRequests.size());
//            response.put("data", enrichedRequests);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération des demandes", e);
//
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Erreur lors de la récupération des demandes");
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
//
//    /**
//     * Récupérer les demandes d'un client par numéro de téléphone
//     */
//
//    @GetMapping("/phone/{phoneNumber}")
//    public ResponseEntity<?> getRequestsByPhone(@PathVariable String phoneNumber) {
//        try {
//            logger.info("Récupération des demandes pour le numéro: {}", phoneNumber);
//
//            List<BankingRequest> requests = bankingRequestService.getRequestsByPhoneNumber(phoneNumber);
//
//            // Transformer les données pour ne retourner que les champs voulus
//            List<Map<String, Object>> simplifiedRequests = requests.stream()
//                    .map(request -> {
//                        Map<String, Object> data = new HashMap<>();
//                        data.put("service", request.getServiceType());
//                        data.put("typeModification", request.getModificationType());
//                        data.put("dateSubmission", request.getCreatedDate().toString());
//                        data.put("status", request.getStatus());
//                        data.put("reference", request.getReference());
//                        return data;
//                    })
//                    .collect(Collectors.toList());
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("total", simplifiedRequests.size());
//            response.put("data", simplifiedRequests);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération des demandes pour: {}", phoneNumber, e);
//
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Erreur lors de la récupération des demandes");
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
//
//    /**
//     * Récupérer les demandes par statut
//     */
//    @GetMapping("/status/{status}")
//    public ResponseEntity<?> getRequestsByStatus(@PathVariable String status) {
//        try {
//            logger.info("Récupération des demandes avec statut: {}", status);
//
//            List<BankingRequest> requests = bankingRequestService.getRequestsByStatus(status);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("total", requests.size());
//            response.put("data", requests);
//
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération des demandes par statut: {}", status, e);
//
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Erreur lors de la récupération des demandes");
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
//}