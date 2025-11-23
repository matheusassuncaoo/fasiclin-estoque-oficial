<div align="center">

# 📦 Sistema de Gestão de Estoque - Fasiclin

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-24-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Sistema completo de gestão de estoque com controle de produtos, ordens de compra, movimentações e validação de almoxarifado.

[Características](#-características) •
[Tecnologias](#-tecnologias) •
[Instalação](#-instalação) •
[Uso](#-uso) •
[API](#-documentação-da-api) •
[Contribuindo](#-contribuindo)

</div>

---

## 📋 Sumário

- [Características](#-características)
- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Configuração](#-configuração)
- [Uso](#-uso)
- [Documentação da API](#-documentação-da-api)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Testes](#-testes)
- [Deploy](#-deploy)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)

---

## ✨ Características

### 🎯 Módulos Principais

- **📋 Ordem de Compra**: Gerenciamento completo do ciclo de vida das ordens
  - Criação, edição e acompanhamento de ordens
  - Controle de status (Pendente → Em Andamento → Concluída)
  - Validação de regras de negócio
  - Histórico de alterações

- **📦 Movimentação de Estoque**: Controle de entradas e saídas
  - Registro de entradas de mercadorias
  - Controle de saídas e baixas
  - Rastreabilidade por lote
  - Auditoria de movimentações

- **✅ Validação de Almoxarifado**: Monitoramento e alertas
  - Dashboard de produtos críticos
  - Alertas de estoque baixo
  - Produtos para reposição urgente
  - Relatórios de estoque

### 🚀 Funcionalidades Técnicas

- ✅ **API REST** completa e documentada (OpenAPI/Swagger)
- ✅ **Validação de dados** com Bean Validation
- ✅ **Tratamento global de exceções** padronizado
- ✅ **DTOs** para separação de responsabilidades
- ✅ **Transações** gerenciadas com Spring Data JPA
- ✅ **Segurança** com Spring Security
- ✅ **CORS** configurado para múltiplos clientes
- ✅ **Paginação** em todas as listagens
- ✅ **Logs estruturados** com SLF4J
- ✅ **Interface web híbrida** (Thymeleaf + JavaScript)

---

## 🛠 Tecnologias

### Backend
- **Java 24** - Linguagem principal
- **Spring Boot 4.0.0** - Framework principal
  - Spring Data JPA - Persistência de dados
  - Spring Security - Autenticação e autorização
  - Spring Validation - Validação de dados
  - Spring Web MVC - APIs REST
- **Hibernate** - ORM
- **MySQL** - Banco de dados relacional
- **Lombok** - Redução de boilerplate
- **SpringDoc OpenAPI** - Documentação automática da API

### Frontend
- **Thymeleaf** - Template engine
- **HTML5/CSS3** - Estrutura e estilo
- **JavaScript (Vanilla)** - Interatividade e consumo de API
- **Fetch API** - Requisições HTTP

### Ferramentas
- **Maven** - Gerenciamento de dependências e build
- **Git** - Controle de versão
- **IntelliJ IDEA / VS Code** - IDEs recomendadas

---

## 🏗 Arquitetura

O projeto segue os princípios de **Clean Architecture** e **MVC**, com separação clara de responsabilidades:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Controllers  │  │  View (HTML) │  │  JavaScript  │      │
│  │   REST API   │  │  Thymeleaf   │  │  Fetch API   │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                     SERVICE LAYER                           │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Business Logic, Validations, Transactions          │   │
│  │  ProdutoService | EstoqueService | OrdemCompraService│  │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                   PERSISTENCE LAYER                         │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Spring Data JPA Repositories                       │   │
│  │  Custom Queries, Pagination, Caching               │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                      DATA LAYER                             │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  MySQL Database                                     │   │
│  │  Tables: PRODUTO, ESTOQUE, ORDEMCOMPRA, etc.       │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Camadas

1. **Controllers**: Recebem requisições HTTP e delegam para Services
2. **Services**: Contêm lógica de negócio, validações e orquestração
3. **Repositories**: Interface com banco de dados via Spring Data JPA
4. **Models**: Entidades JPA mapeadas para tabelas do banco
5. **DTOs**: Objetos de transferência de dados (entrada/saída da API)
6. **Exception Handlers**: Tratamento global e padronizado de erros

---

## 📦 Pré-requisitos

Antes de começar, você precisa ter instalado:

- ☕ **Java JDK 24** ou superior ([Download](https://www.oracle.com/java/technologies/downloads/))
- 🗄️ **MySQL 8.0+** ([Download](https://dev.mysql.com/downloads/))
- 📦 **Maven 3.9+** (opcional, pode usar o wrapper incluído)
- 🔧 **Git** ([Download](https://git-scm.com/))

### Verificar instalações

```bash
java -version    # Deve mostrar Java 24+
mysql --version  # Deve mostrar MySQL 8.0+
mvn --version    # Deve mostrar Maven 3.9+
```

---

## 🚀 Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/estoque-fasiclin.git
cd estoque-fasiclin
```

### 2. Configure o banco de dados

Crie o banco de dados no MySQL:

```sql
CREATE DATABASE estoque_fasiclin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crie um usuário (opcional, mas recomendado)
CREATE USER 'estoque_user'@'localhost' IDENTIFIED BY 'senha_segura';
GRANT ALL PRIVILEGES ON estoque_fasiclin.* TO 'estoque_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure o application.properties

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Configuração do Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/estoque_fasiclin
spring.datasource.username=estoque_user
spring.datasource.password=senha_segura
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Configuração do Servidor
server.port=8080

# Configuração de Logs
logging.level.com.br.fasiclin.estoque=DEBUG
```

### 4. Execute o projeto

**Usando Maven Wrapper (recomendado):**

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

**Ou usando Maven instalado:**

```bash
mvn spring-boot:run
```

### 5. Acesse a aplicação

- 🌐 **Interface Web**: http://localhost:8080
- 📚 **Documentação API (Swagger)**: http://localhost:8080/swagger-ui.html
- 🔍 **API Docs (JSON)**: http://localhost:8080/v3/api-docs

---

## ⚙️ Configuração

### Perfis de Ambiente

O projeto suporta diferentes perfis (dev, staging, prod):

```bash
# Desenvolvimento
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Produção
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Variáveis de Ambiente

Para produção, use variáveis de ambiente:

```bash
export DB_URL=jdbc:mysql://production-server:3306/estoque
export DB_USER=prod_user
export DB_PASSWORD=super_secret_password
export SERVER_PORT=8080
```

### CORS

O CORS está configurado para aceitar requisições de:
- `http://localhost:5500`
- `http://127.0.0.1:5500`

Edite `WebConfig.java` para adicionar novos origins.

---

## 💻 Uso

### Acessando os Módulos

1. **Ordem de Compra**: http://localhost:8080/ordemcompra
   - Criar novas ordens
   - Acompanhar status
   - Gerenciar itens

2. **Movimentação de Estoque**: http://localhost:8080/movimentacaoestoque
   - Registrar entradas
   - Registrar saídas
   - Consultar histórico

3. **Validação de Almoxarifado**: http://localhost:8080/validacaoalmoxarifado
   - Ver produtos críticos
   - Alertas de reposição
   - Dashboard de estoque

### Exemplos de Uso da API

#### Listar Produtos

```bash
curl -X GET "http://localhost:8080/api/produtos?page=0&size=20" \
     -H "Content-Type: application/json"
```

#### Criar Produto

```bash
curl -X POST "http://localhost:8080/api/produtos" \
     -H "Content-Type: application/json" \
     -d '{
       "nome": "Paracetamol 500mg",
       "descricao": "Analgésico e antitérmico",
       "codBarras": "7891234567890",
       "idAlmoxarifado": 1,
       "idUnidadeMedida": 1,
       "stqMax": 1000,
       "stqMin": 100,
       "ptnPedido": 200
     }'
```

#### Registrar Entrada de Estoque

```bash
curl -X POST "http://localhost:8080/api/estoque/entrada?idProduto=1&idLote=1&quantidade=500" \
     -H "Content-Type: application/json"
```

#### Atualizar Status de Ordem

```bash
curl -X PATCH "http://localhost:8080/api/ordens-compra/1/status?novoStatus=ANDA" \
     -H "Content-Type: application/json"
```

---

## 📚 Documentação da API

### Endpoints Principais

#### 🏷️ Produtos (`/api/produtos`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/produtos` | Lista produtos (paginado) |
| GET | `/api/produtos/{id}` | Busca produto por ID |
| GET | `/api/produtos/buscar?nome={nome}` | Busca por nome |
| GET | `/api/produtos/reposicao` | Produtos para reposição |
| GET | `/api/produtos/estoque-baixo` | Produtos com estoque baixo |
| POST | `/api/produtos` | Cria novo produto |
| PUT | `/api/produtos/{id}` | Atualiza produto |
| DELETE | `/api/produtos/{id}` | Remove produto |

#### 📦 Estoque (`/api/estoque`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/estoque` | Lista estoques (paginado) |
| GET | `/api/estoque/{id}` | Busca estoque por ID |
| POST | `/api/estoque/entrada` | Registra entrada |
| POST | `/api/estoque/saida` | Registra saída |
| PATCH | `/api/estoque/{id}/quantidade` | Atualiza quantidade |
| DELETE | `/api/estoque/{id}` | Remove registro |

#### 🛒 Ordens de Compra (`/api/ordens-compra`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/ordens-compra` | Lista ordens (paginado) |
| GET | `/api/ordens-compra/{id}` | Busca ordem por ID |
| GET | `/api/ordens-compra/status/{status}` | Filtra por status |
| POST | `/api/ordens-compra` | Cria nova ordem |
| PUT | `/api/ordens-compra/{id}` | Atualiza ordem |
| PATCH | `/api/ordens-compra/{id}/status` | Atualiza status |
| DELETE | `/api/ordens-compra/{id}` | Remove ordem |

#### 👤 Usuários (`/api/usuarios`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/usuarios` | Lista usuários (paginado) |
| GET | `/api/usuarios/{id}` | Busca usuário por ID |
| GET | `/api/usuarios/buscar?nomeUsuario={nome}` | Busca por nome |
| POST | `/api/usuarios` | Cria novo usuário |
| PUT | `/api/usuarios/{id}` | Atualiza usuário |
| PATCH | `/api/usuarios/{id}/toggle-ativo` | Ativa/desativa |
| DELETE | `/api/usuarios/{id}` | Desativa usuário |

### Formato de Resposta Padrão

Todas as respostas seguem o formato:

```json
{
  "timestamp": "2025-11-23T10:30:00",
  "success": true,
  "message": "Operação realizada com sucesso",
  "data": { ... },
  "pagination": {
    "page": 0,
    "size": 20,
    "totalElements": 100,
    "totalPages": 5,
    "first": true,
    "last": false
  }
}
```

### Códigos de Status HTTP

- `200 OK` - Requisição bem-sucedida
- `201 Created` - Recurso criado com sucesso
- `400 Bad Request` - Erro de validação
- `404 Not Found` - Recurso não encontrado
- `422 Unprocessable Entity` - Erro de regra de negócio
- `500 Internal Server Error` - Erro interno do servidor

---

## 📁 Estrutura do Projeto

```
estoque/
├── src/
│   ├── main/
│   │   ├── java/com/br/fasiclin/estoque/estoque/
│   │   │   ├── config/              # Configurações (Security, CORS)
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/          # Controllers REST e Views
│   │   │   │   ├── ProdutoController.java
│   │   │   │   ├── EstoqueController.java
│   │   │   │   ├── OrdemCompraController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   └── ViewController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── ApiResponseDTO.java
│   │   │   │   ├── ProdutoDTO.java
│   │   │   │   ├── EstoqueDTO.java
│   │   │   │   ├── OrdemCompraDTO.java
│   │   │   │   └── UsuarioDTO.java
│   │   │   ├── exception/           # Tratamento de exceções
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── model/               # Entidades JPA
│   │   │   │   ├── Produto.java
│   │   │   │   ├── Estoque.java
│   │   │   │   ├── OrdemCompra.java
│   │   │   │   ├── ItemOrdemCompra.java
│   │   │   │   ├── Lote.java
│   │   │   │   ├── Fornecedor.java
│   │   │   │   ├── Usuario.java
│   │   │   │   └── ...
│   │   │   ├── repository/          # Repositories Spring Data
│   │   │   │   ├── ProdutoRepository.java
│   │   │   │   ├── EstoqueRepository.java
│   │   │   │   ├── OrdemCompraRepository.java
│   │   │   │   └── UsuarioRepository.java
│   │   │   ├── service/             # Lógica de negócio
│   │   │   │   ├── ProdutoService.java
│   │   │   │   ├── EstoqueService.java
│   │   │   │   ├── OrdemCompraService.java
│   │   │   │   └── UsuarioService.java
│   │   │   └── EstoqueApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       │   └── assets/
│   │       │       ├── css/
│   │       │       │   └── style.css
│   │       │       ├── js/
│   │       │       │   ├── ordemcompra.js
│   │       │       │   ├── movimentacaoestoque.js
│   │       │       │   └── validacaoalmoxarifado.js
│   │       │       ├── images/
│   │       │       └── logo/
│   │       └── templates/
│   │           ├── ordemcompra.html
│   │           ├── movimentacaoestoque.html
│   │           ├── validacaoalmoxarifado.html
│   │           └── pages/
│   └── test/
│       └── java/com/br/fasiclin/estoque/estoque/
│           └── EstoqueApplicationTests.java
├── target/                          # Diretório de build
├── .gitignore
├── mvnw                             # Maven Wrapper (Linux/Mac)
├── mvnw.cmd                         # Maven Wrapper (Windows)
├── pom.xml                          # Configuração Maven
└── README.md                        # Este arquivo
```

---

## 🧪 Testes

### Executar todos os testes

```bash
mvn test
```

### Executar testes com cobertura

```bash
mvn clean test jacoco:report
```

O relatório será gerado em `target/site/jacoco/index.html`

### Testes Manuais

Importe a collection do Postman/Insomnia (em breve) para testar os endpoints.

---

## 🚢 Deploy

### Build para produção

```bash
mvn clean package -DskipTests
```

O arquivo `.jar` será gerado em `target/estoque-0.0.1-SNAPSHOT.jar`

### Executar JAR

```bash
java -jar target/estoque-0.0.1-SNAPSHOT.jar
```

### Deploy com Docker (em breve)

```dockerfile
FROM openjdk:24-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t estoque-fasiclin .
docker run -p 8080:8080 estoque-fasiclin
```

### Deploy em Cloud

- **AWS Elastic Beanstalk**: Upload do `.jar`
- **Heroku**: `heroku deploy:jar target/estoque-0.0.1-SNAPSHOT.jar`
- **Azure App Service**: Deploy via Maven plugin

---

## 🤝 Contribuindo

Contribuições são sempre bem-vindas! Para contribuir:

1. **Fork** o projeto
2. Crie uma **branch** para sua feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** suas mudanças (`git commit -m 'Add: nova funcionalidade incrível'`)
4. **Push** para a branch (`git push origin feature/AmazingFeature`)
5. Abra um **Pull Request**

### Padrões de Commit

Seguimos o [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` Nova funcionalidade
- `fix:` Correção de bug
- `docs:` Documentação
- `style:` Formatação de código
- `refactor:` Refatoração
- `test:` Testes
- `chore:` Tarefas de build/configuração

### Code Review

Todos os PRs passam por revisão. Certifique-se de:
- ✅ Código limpo e comentado
- ✅ Testes passando
- ✅ Documentação atualizada
- ✅ Sem warnings de compilação

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👥 Autores

**Sistema Fasiclin - Equipe de Desenvolvimento**

- 📧 Email: contato@fasiclin.com.br
- 🌐 Website: [www.fasiclin.com.br](https://www.fasiclin.com.br)
- 💼 LinkedIn: [Fasiclin](https://linkedin.com/company/fasiclin)

---

## 🙏 Agradecimentos

- Spring Boot Team pela excelente documentação
- Comunidade Java por todo o suporte
- Todos os contribuidores deste projeto

---

## 📞 Suporte

Encontrou um bug? Tem uma sugestão?

- 🐛 [Abra uma Issue](https://github.com/seu-usuario/estoque-fasiclin/issues)
- 💬 [Discussões](https://github.com/seu-usuario/estoque-fasiclin/discussions)
- 📧 Email: suporte@fasiclin.com.br

---

<div align="center">

**⭐ Se este projeto foi útil, deixe uma estrela!**

Feito com ❤️ pela equipe Fasiclin

</div>
