# 🎨 Frontend Thymeleaf - Sistema Fasiclin

## ✅ Estrutura Implementada

### **Arquitetura Híbrida: Thymeleaf + JavaScript**

```
Backend (Spring Boot)
    ↓
Thymeleaf (Templates HTML)
    ↓
JavaScript (OrdemCompraManager.js)
    ↓
API REST (Controllers)
    ↓
Services (Lógica de Negócio)
    ↓
Database (MySQL - aluno4)
```

---

## 📁 Estrutura de Arquivos

### **Templates Thymeleaf**
```
src/main/resources/templates/
├── ordemcompra-simple.html    ← VERSÃO NOVA (Simplificada)
├── ordemcompra.html            ← VERSÃO ORIGINAL (Complexa)
├── movimentacaoestoque.html
└── validacaoalmoxarifado.html
```

### **Assets Estáticos**
```
src/main/resources/static/assets/
├── css/
│   ├── global.css              ← CSS original
│   ├── global-simple.css       ← CSS novo (limpo)
│   ├── ordemcompra.css
│   └── ordemcompra-responsive.css
├── js/
│   ├── ApiManager.js           ← Comunicação com backend
│   ├── OrdemCompraManager.js   ← Lógica do frontend
│   └── OrdemCompraComponentsManager.js
├── logo/
│   └── FasicomercioLogo.png
└── icon/
    └── OrdemCompraIcone.ico
```

---

## 🔗 Rotas Configuradas

### **ViewController.java**
```java
@GetMapping("/ordemcompra")          → ordemcompra-simple.html
@GetMapping("/ordemcompra/full")     → ordemcompra.html (original)
@GetMapping("/movimentacaoestoque")  → movimentacaoestoque.html
@GetMapping("/validacaoalmoxarifado")→ validacaoalmoxarifado.html
@GetMapping("/")                     → redirect:/ordemcompra
```

---

## 🚀 Como Funciona

### **1. Servidor Spring Boot Inicia**
```bash
.\mvnw.cmd spring-boot:run
```

### **2. Usuário Acessa URL**
```
http://localhost:8080/ordemcompra
```

### **3. Thymeleaf Processa Template**
```html
<!-- ordemcompra-simple.html -->
<html xmlns:th="http://www.thymeleaf.org">
  <link rel="stylesheet" th:href="@{/assets/css/global-simple.css}">
  <script th:src="@{/assets/js/ApiManager.js}"></script>
</html>
```

**Thymeleaf converte `th:href` e `th:src` para:**
```html
<link rel="stylesheet" href="/assets/css/global-simple.css">
<script src="/assets/js/ApiManager.js"></script>
```

### **4. JavaScript Carrega e Faz Requisições**
```javascript
// ApiManager.js
class ApiManager {
  constructor() {
    this.baseURL = "http://localhost:8080/api";
  }
  
  async getOrdens() {
    const response = await fetch(`${this.baseURL}/ordens-compra`);
    return response.json();
  }
}
```

### **5. Backend Processa e Retorna JSON**
```java
@GetMapping("/ordens-compra")
public ResponseEntity<ApiResponseDTO<Page<OrdemCompraDTO>>> findAll() {
    // Usa datasource do Matheus (aluno4)
    Page<OrdemCompraDTO> ordens = ordemCompraService.findAll(pageable);
    return ResponseEntity.ok(ApiResponseDTO.success(ordens));
}
```

---

## 📝 Exemplo Completo de Fluxo

### **1. Usuário clica em "Nova Ordem"**
```javascript
// OrdemCompraManager.js
document.getElementById('btnNovaOrdem').addEventListener('click', () => {
    document.getElementById('modalOrdem').classList.add('active');
});
```

### **2. Usuário preenche formulário e clica "Salvar"**
```javascript
const formData = {
    status: 'PEND',
    valor: 1500.00,
    dataOrdem: '2025-11-23',
    dataPrevisao: '2025-12-01',
    dataEntrega: '2025-12-01'
};

const response = await apiManager.createOrdem(formData);
```

### **3. ApiManager envia POST para backend**
```javascript
async createOrdem(data) {
    const response = await fetch(`${this.baseURL}/ordens-compra`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    return response.json();
}
```

### **4. Controller recebe e valida**
```java
@PostMapping
public ResponseEntity<ApiResponseDTO<OrdemCompraDTO>> create(@Valid @RequestBody OrdemCompraDTO dto) {
    // Validações Jakarta automáticas
    OrdemCompraDTO created = ordemCompraService.create(dto);
    return ResponseEntity.status(CREATED).body(ApiResponseDTO.success(created));
}
```

### **5. Service processa com lógica de negócio**
```java
@Transactional
public OrdemCompraDTO create(OrdemCompraDTO dto) {
    validateOrdemCompra(dto); // Validações customizadas
    OrdemCompra ordem = convertToEntity(dto);
    ordem.setDataOrdem(LocalDate.now());
    ordem.setStatus(StatusOrdemCompra.PEND);
    
    OrdemCompra saved = ordemCompraRepository.save(ordem); // Salva com datasource do Matheus
    return convertToDTO(saved);
}
```

### **6. Resposta volta para JavaScript**
```javascript
if (response.success) {
    showToast('Ordem criada com sucesso!', 'success');
    loadOrdens(); // Recarrega tabela
    closeModal();
}
```

---

## 🎯 Versões Disponíveis

### **Versão Simplificada (Recomendada)**
- **URL**: `http://localhost:8080/ordemcompra`
- **Template**: `ordemcompra-simple.html`
- **CSS**: `global-simple.css`
- **Características**:
  - ✅ Interface limpa e moderna
  - ✅ CSS próprio sem dependências externas
  - ✅ Totalmente funcional
  - ✅ Responsivo
  - ✅ Feather Icons integrado

### **Versão Original (Completa)**
- **URL**: `http://localhost:8080/ordemcompra/full`
- **Template**: `ordemcompra.html`
- **CSS**: `global.css`, `ordemcompra.css`
- **Características**:
  - ✅ Todas as funcionalidades avançadas
  - ✅ Modal de informações
  - ✅ Estatísticas detalhadas
  - ✅ Exportação Excel

---

## 🔧 Configuração do Thymeleaf

### **application.properties**
```properties
# Thymeleaf Configuration (já configurado automaticamente pelo Spring Boot)
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.cache=false  # Desabilitado em dev para hot reload
```

### **Recursos Estáticos**
```properties
# Spring Boot serve automaticamente de:
spring.web.resources.static-locations=classpath:/static/
```

---

## 🧪 Testar Localmente

### **1. Iniciar Backend**
```bash
cd c:\Users\Matheus\Documents\estoque\estoque
.\mvnw.cmd spring-boot:run
```

### **2. Aguardar Mensagem**
```
Started EstoqueApplication in X.XXX seconds
```

### **3. Acessar no Navegador**
```
http://localhost:8080/ordemcompra
```

### **4. Verificar Console do Navegador (F12)**
```
🚀 Aplicação Ordem de Compra iniciada
📡 API Base URL: http://localhost:8080
✅ ApiManager inicializado
```

### **5. Testar CRUD**
- ✅ Criar nova ordem
- ✅ Listar ordens
- ✅ Editar ordem
- ✅ Excluir ordem
- ✅ Filtrar por status

---

## 📊 Recursos Thymeleaf Utilizados

### **1. Namespace**
```html
<html xmlns:th="http://www.thymeleaf.org">
```

### **2. Recursos Estáticos (`th:href`, `th:src`)**
```html
<link rel="stylesheet" th:href="@{/assets/css/global-simple.css}">
<script th:src="@{/assets/js/ApiManager.js}"></script>
<img th:src="@{/assets/logo/FasicomercioLogo.png}">
```

### **3. Variáveis do Spring (Se Necessário)**
```html
<!-- Exemplo: Injetar porta do servidor -->
<script>
  const API_BASE_URL = /*[[${@environment.getProperty('server.port')}]]*/ 'http://localhost:8080';
</script>
```

### **4. Condicionais (Futuro)**
```html
<div th:if="${user.isAdmin}">Admin Panel</div>
<span th:text="${user.name}">Nome do Usuário</span>
```

---

## ✅ Checklist de Implementação

### Frontend
- [x] Template Thymeleaf criado (`ordemcompra-simple.html`)
- [x] CSS customizado (`global-simple.css`)
- [x] JavaScript funcionando (`ApiManager.js`)
- [x] ViewController mapeando rotas
- [x] Feather Icons integrado
- [x] Modal de formulário
- [x] Tabela de listagem
- [x] Cards de estatísticas
- [x] Sistema de toast/notificações
- [x] Loading overlay
- [x] Paginação

### Backend
- [x] DataSource do Matheus configurado
- [x] Controllers REST completos
- [x] Services com validações
- [x] CORS configurado
- [x] Swagger UI disponível

### Integração
- [x] Thymeleaf servindo templates
- [x] JavaScript consumindo API
- [x] Validações frontend + backend
- [ ] Dados de teste no banco (executar test-data.sql)

---

## 🎯 Próximos Passos

1. **Iniciar backend**: `.\mvnw.cmd spring-boot:run`
2. **Acessar**: http://localhost:8080/ordemcompra
3. **Popular banco**: Executar `test-data.sql`
4. **Testar CRUD**: Criar, editar, listar ordens
5. **Verificar logs**: Console do navegador + terminal Spring Boot

**Frontend Thymeleaf pronto e integrado com backend! 🎉**
