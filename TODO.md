- [x] Restaurar/confirmar `docker-compose.yml` na raiz (recriado manualmente quando necessário)
- [x] Corrigir build para gerar fat jar executável também no `attendance-service`

- [ ] Rebuild com `mvnd ... package -DskipTests`
- [ ] Subir com `docker-compose up --build`
- [ ] Validar que `attendance-service` agora tem `BOOT-INF/` e roda sem “no main manifest attribute”


