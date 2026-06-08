# Resumo do fluxo Gateway + Kubernetes

## 1. Ponto de entrada externo
- O cliente faz a requisição ao `Ingress` do cluster Kubernetes.
- Exemplo: `http://<IP-do-cluster>/api/v1/pacientes`

## 2. Ingress
- O `Ingress` recebe a requisição externa.
- Ele envia a requisição para o serviço interno `gateway-service:8080`.
- Assim, o cliente não precisa conhecer os nomes internos dos microserviços.

## 3. Gateway
- O `gateway-service` é o roteador central.
- Ele usa as rotas definidas em `gateway-service/src/main/resources/application.yml`.
- Rotas principais:
  - `/api/v1/pacientes/**` → `appointment-service:8083`
  - `/api/v1/consultas/**` → `appointment-service:8083`
  - `/api/v1/admin/**` → `administrative-service:8084`
  - `/api/v1/atendimentos/**` → `attendance-service:8085`

## 4. Comunicação interna no Kubernetes
- Cada serviço possui um `Service` Kubernetes que fornece DNS interno.
- O gateway usa esses nomes para encaminhar requisições.
- Exemplo: `http://appointment-service:8083`

## 5. Serviços Java
- Cada serviço (`appointment`, `administrative`, `attendance`) é implantado como um `Deployment`.
- Os pods executam os JARs Java correspondentes.
- O serviço processa a requisição e retorna a resposta ao gateway.

## 6. Bancos de dados
- Cada serviço tem seu próprio MySQL:
  - `mysql-appointment`
  - `mysql-administrative`
  - `mysql-attendance`
- Cada banco é um `Deployment` + `Service` + `PersistentVolumeClaim`.
- Isso mantém o padrão de `Database per Service`.

## 7. Retorno ao cliente
- O serviço de destino responde ao gateway.
- O gateway repassa a resposta ao Ingress.
- O cliente recebe a resposta final do Ingress.

## 8. Benefícios do Kubernetes
- `Deployment`: reinicia pods automaticamente e permite escala.
- `Service`: fornece descoberta de serviço via DNS interno.
- `Ingress`: expõe o gateway para o mundo externo.
- `PVC`: mantém os dados dos bancos persistentes.

## 9. Observação importante
- Para o `Ingress` funcionar, é necessário um Ingress controller instalado no cluster (por exemplo, `nginx-ingress`).
- O Ingress é o caminho que o cliente usa para chegar ao gateway e, por consequência, aos microserviços.
