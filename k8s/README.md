# Kubernetes manifests for Clinica Medica

Este diretório contém os manifests Kubernetes para o projeto.

## O que está incluso

- `appointment.yaml` — Deployment + Service para o `appointment-service`
- `administrative.yaml` — Deployment + Service para o `administrative-service`
- `attendance.yaml` — Deployment + Service para o `attendance-service`
- `gateway.yaml` — Deployment + ClusterIP Service para o `gateway-service`
- `ingress.yaml` — Ingress para expor o gateway aos clientes externos
- `mysql-appointment.yaml` — Deployment + Service + PVC para o banco do `appointment-service`
- `mysql-administrative.yaml` — Deployment + Service + PVC para o banco do `administrative-service`
- `mysql-attendance.yaml` — Deployment + Service + PVC para o banco do `attendance-service`

## Como usar

1. Gere os JARs dos módulos:

```bash
mvn clean package -DskipTests
```

2. Construa as imagens Docker:

```bash
docker build -t imepac/appointment-service:1.0.0 appointment-service
docker build -t imepac/administrative-service:1.0.0 administrative-service
docker build -t imepac/attendance-service:1.0.0 attendance-service
docker build -t imepac/gateway-service:1.0.0 gateway-service
```

3. Se estiver usando `kind`, carregue as imagens no cluster:

```bash
kind load docker-image imepac/appointment-service:1.0.0
kind load docker-image imepac/administrative-service:1.0.0
kind load docker-image imepac/attendance-service:1.0.0
kind load docker-image imepac/gateway-service:1.0.0
```

4. Aplique os manifests:

```bash
kubectl apply -f k8s/
```

5. Verifique os recursos:

```bash
kubectl get deployments,services,ingress,pvc
```

6. Acesse o gateway:

- Use o Ingress para chegar ao `gateway-service`.
- Em `minikube`, use: `minikube service gateway-service --url` ou, se usar Ingress, `minikube tunnel` e acesse o host configurado.

## Observações importantes

- O gateway encaminha internamente para os serviços pelos nomes DNS do cluster:
  - `http://appointment-service:8083`
  - `http://administrative-service:8084`
  - `http://attendance-service:8085`
- Isso segue o mesmo princípio do guia: `service discovery` por nome de serviço em vez de `localhost`.
- Cada serviço tem seu próprio banco de dados MySQL, seguindo o padrão de `Database per Service`.
