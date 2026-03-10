package com.bamis.gestion_banking_accees.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/proxy/otp")
@CrossOrigin(origins = {"http://localhost:4202", "https://mobile.bamis.mr:4455"})
public class OtpProxyController {

    @Autowired
    private RestTemplate restTemplate;

    private final String OTP_SERVICE_URL = "http://otp.bamis.mr:8080/otp/api/otp";

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateOtp(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Proxy Banking: Génération OTP demandée");
            System.out.println("URL cible: " + OTP_SERVICE_URL + "/generate");
            System.out.println("Request body reçu: " + request);

            // Transformer les clés pour le service OTP
            Map<String, Object> otpRequest = new HashMap<>();
            otpRequest.put("numeroTelephone", request.get("phoneNumber"));
            otpRequest.put("typeAction", "ACCESS_CREATION"); // Type d'action pour le banking

            System.out.println("Request body transformé: " + otpRequest);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    OTP_SERVICE_URL + "/generate",
                    otpRequest,
                    Map.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            System.out.println("ERREUR DÉTAILLÉE: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur lors de la génération OTP");
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateOtp(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Proxy Banking: Validation OTP demandée");
            System.out.println("Request body reçu: " + request);

            // Transformer les clés pour le service OTP
            Map<String, Object> otpRequest = new HashMap<>();
            otpRequest.put("numeroTelephone", request.get("phoneNumber"));

            // Transformer les clés pour le service OTP
            otpRequest.put("typeAction", "ACCESS_CREATION");

            otpRequest.put("codeOtp", request.get("otpCode"));

            System.out.println("Request body transformé: " + otpRequest);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    OTP_SERVICE_URL + "/validate",
                    otpRequest,
                    Map.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            System.out.println("ERREUR DÉTAILLÉE: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur lors de la validation OTP");
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<Map<String, Object>> resendOtp(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Proxy Banking: Renvoi OTP demandé");
            System.out.println("Request body reçu: " + request);

            // Transformer les clés pour le service OTP
            Map<String, Object> otpRequest = new HashMap<>();
            otpRequest.put("numeroTelephone", request.get("phoneNumber"));
            otpRequest.put("typeAction", "ACCESS_CREATION");

            System.out.println("Request body transformé: " + otpRequest);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    OTP_SERVICE_URL + "/resend",
                    otpRequest,
                    Map.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            System.out.println("ERREUR DÉTAILLÉE: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur lors du renvoi OTP");
            return ResponseEntity.status(500).body(error);
        }
    }
}