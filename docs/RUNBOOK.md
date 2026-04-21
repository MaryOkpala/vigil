# Vigil — Operations Runbook

## Prerequisites

| Tool | Version |
|------|---------|
| kubectl | >= 1.28 |
| helm | >= 3.0 |
| docker | >= 24.0 |
| k3s | >= 1.28 |

## Cluster setup

```bash
# Install k3s
curl -sfL https://get.k3s.io | sh -s - \
  --write-kubeconfig-mode 644 \
  --disable traefik \
  --node-name vigil-node

# Configure kubectl
mkdir -p ~/.kube
cp /etc/rancher/k3s/k3s.yaml ~/.kube/config
```

## Install ArgoCD

```bash
helm repo add argo https://argoproj.github.io/argo-helm
helm install argocd argo/argo-cd --namespace argocd --create-namespace
kubectl apply -f argocd/vigil-app.yaml
```

## Install observability stack

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm install prometheus prometheus-community/kube-prometheus-stack \
  --namespace monitoring --create-namespace
kubectl apply -f monitoring/
```

## Deploy application manually

```bash
kubectl apply -f k8s/
```

## Trigger GitOps deployment

```bash
# Simply push to main — ArgoCD handles the rest
git push origin main
```

## Check cluster health

```bash
kubectl get nodes
kubectl get pods -A
kubectl get application -n argocd
```

## Access services

| Service | URL |
|---------|-----|
| App | http://NODE_IP:30080 |
| ArgoCD | http://NODE_IP:31088 |
| Grafana | http://NODE_IP:31300 |
| Prometheus | http://NODE_IP:31090 |

## Destroy

```bash
kubectl delete -f k8s/
kubectl delete -f argocd/vigil-app.yaml
helm uninstall prometheus -n monitoring
helm uninstall argocd -n argocd
```
