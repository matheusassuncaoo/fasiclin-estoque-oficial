# ============================================
# GUIA DE DEPLOY NO RENDER.COM
# ============================================

## 1. CRIAR BANCO DE DADOS NO RENDER

1. Acesse: https://dashboard.render.com
2. Clique em "New +" → "MySQL"
3. Preencha:
   - Name: fasiclin-estoque-db
   - Database: estoque_fasiclin
   - User: estoque_user
   - Region: Ohio (US East)
   - Instance Type: Free

4. Clique em "Create Database"
5. Copie as credenciais:
   - Internal Database URL
   - Username
   - Password

## 2. CRIAR OS 3 USUÁRIOS NO MYSQL

Conecte no banco via MySQL Workbench ou terminal usando as credenciais do Render:

```sql
-- Criar usuários
CREATE USER 'matheus_prod'@'%' IDENTIFIED BY 'senha_forte_matheus';
CREATE USER 'yuri_prod'@'%' IDENTIFIED BY 'senha_forte_yuri';
CREATE USER 'erasmo_prod'@'%' IDENTIFIED BY 'senha_forte_erasmo';

-- Dar permissões
GRANT ALL PRIVILEGES ON estoque_fasiclin.* TO 'matheus_prod'@'%';
GRANT ALL PRIVILEGES ON estoque_fasiclin.* TO 'yuri_prod'@'%';
GRANT ALL PRIVILEGES ON estoque_fasiclin.* TO 'erasmo_prod'@'%';

FLUSH PRIVILEGES;
```

## 3. FAZER BUILD DO PROJETO

```bash
# Limpar e buildar
mvn clean package -DskipTests

# O arquivo JAR estará em:
# target/estoque-0.0.1-SNAPSHOT.jar
```

## 4. CRIAR WEB SERVICE NO RENDER

1. Clique em "New +" → "Web Service"
2. Conecte seu repositório GitHub
3. Preencha:
   - Name: fasiclin-estoque-api
   - Environment: Docker OU Java
   - Build Command: `mvn clean package -DskipTests`
   - Start Command: `java -jar target/estoque-0.0.1-SNAPSHOT.jar`
   - Instance Type: Free

## 5. CONFIGURAR VARIÁVEIS DE AMBIENTE

No painel do Render, vá em "Environment" e adicione:

```
DB_URL=jdbc:mysql://dpg-xxxxx.render.com:3306/estoque_fasiclin
DB_USERNAME=estoque_user
DB_PASSWORD=sua_senha_do_render

DB_URL_MATHEUS=jdbc:mysql://dpg-xxxxx.render.com:3306/estoque_fasiclin
DB_USER_MATHEUS=matheus_prod
DB_PASS_MATHEUS=senha_forte_matheus

DB_URL_YURI=jdbc:mysql://dpg-xxxxx.render.com:3306/estoque_fasiclin
DB_USER_YURI=yuri_prod
DB_PASS_YURI=senha_forte_yuri

DB_URL_ERASMO=jdbc:mysql://dpg-xxxxx.render.com:3306/estoque_fasiclin
DB_USER_ERASMO=erasmo_prod
DB_PASS_ERASMO=senha_forte_erasmo

SPRING_PROFILES_ACTIVE=prod
LOG_LEVEL=INFO
SERVER_PORT=8080
```

## 6. DEPLOY

Clique em "Create Web Service" e aguarde o deploy!

## 7. ACESSAR A APLICAÇÃO

Sua aplicação estará disponível em:
```
https://fasiclin-estoque-api.onrender.com
```

Endpoints:
- Interface: https://fasiclin-estoque-api.onrender.com/ordemcompra
- API: https://fasiclin-estoque-api.onrender.com/api/produtos
- Swagger: https://fasiclin-estoque-api.onrender.com/swagger-ui.html

## TROUBLESHOOTING

### Erro de conexão com banco:
- Verifique se o Internal Database URL está correto
- Certifique-se de usar a porta 3306
- Confirme que os usuários foram criados no MySQL

### Aplicação não inicia:
- Verifique os logs no Render Dashboard
- Confirme que todas as variáveis de ambiente estão definidas
- Teste o build local antes: `mvn clean package`

### Timeout no Free Plan:
- O Render Free desliga após 15 min de inatividade
- A primeira requisição pode demorar ~30s para "acordar"
- Considere upgrade para Starter ($7/mês) se precisar 24/7

## CUSTO ESTIMADO

- MySQL Database (Free): $0/mês
- Web Service (Free): $0/mês
- Total: **GRÁTIS** 🎉

(Com limitações: 750h/mês, dorme após inatividade)
