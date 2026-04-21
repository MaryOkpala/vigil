# Vigil

> Cloud-native observability and GitOps-driven deployments on Kubernetes — every commit auto-deploys, every metric is observable.

## What this project does

Vigil runs a Spring Boot application on Kubernetes with full GitOps and observability:

- **ArgoCD** watches this GitHub repository and automatically deploys every change to the cluster — no manual `kubectl apply` ever
- **Prometheus** scrapes metrics from all pods, nodes, and the application's `/actuator/prometheus` endpoint
- **Grafana** visualises everything — cluster health, pod metrics, application performance
- **AlertManager** fires alerts when pods crash, memory spikes, or the app goes down

## Live endpoints

| Service | URL |
|---------|-----|
| Application | http://44.216.162.230:30080 |
| ArgoCD UI | http://44.216.162.230:31088 |
| Grafana | http://44.216.162.230:31300 |
| Prometheus | http://44.216.162.230:31090 |

## Architecture

![Vigil Architecture](docs/architecture.png)

## Stack

| Tool | Role |
|------|------|
| k3s | Lightweight Kubernetes distribution |
| ArgoCD | GitOps continuous delivery |
| Helm | Kubernetes package management |
| Prometheus | Metrics collection and alerting |
| Grafana | Observability dashboards |
| AlertManager | Alert routing |
| Spring Boot | Application framework |
| Docker | Container runtime |
| AWS EC2 | Cloud compute |

## How GitOps works here

1. Developer pushes code to this repository
2. ArgoCD detects the change within 3 minutes
3. ArgoCD compares the desired state (Git) with the actual state (cluster)
4. ArgoCD applies the diff — rolling update with zero downtime
5. Prometheus scrapes the new pods immediately
6. Grafana dashboards update in real time

## Repository structure
vigil/
├── app/                    # Spring Boot application source
├── k8s/                    # Kubernetes manifests (ArgoCD syncs this)
│   ├── namespace.yaml
│   ├── deployment.yaml
│   ├── service.yaml
│   └── ingress.yaml
├── argocd/                 # ArgoCD Application CRD
│   └── vigil-app.yaml
├── monitoring/             # Prometheus rules and ServiceMonitor
│   ├── vigil-servicemonitor.yaml
│   └── vigil-alerts.yaml
└── docs/
└── architecture.png
## Quick start

```bash
# Install k3s
curl -sfL https://get.k3s.io | sh -s - --write-kubeconfig-mode 644 --disable traefik

# Install ArgoCD
helm install argocd argo/argo-cd --namespace argocd --create-namespace

# Apply ArgoCD application — this deploys everything else automatically
kubectl apply -f argocd/vigil-app.yaml

# Install observability stack
helm install prometheus prometheus-community/kube-prometheus-stack \
  --namespace monitoring --create-namespace
```
