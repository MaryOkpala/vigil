package com.vigil.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class VigilController {

    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("project", "Vigil");
        response.put("status", "operational");
        response.put("platform", "Kubernetes · k3s");
        response.put("gitops", "ArgoCD");
        response.put("observability", "Prometheus · Grafana");
        response.put("version", "2.0.0");
        return response;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("cluster", "vigil-node");
        return response;
    }
}
