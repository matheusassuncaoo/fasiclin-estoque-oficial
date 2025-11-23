# 🚀 API Endpoints - Módulo Ordem de Compra

## 📋 Status do Backend
✅ **Compilado com sucesso!**
✅ Services implementados com validações completas
✅ Controllers REST com OpenAPI
✅ Tratamento global de exceções

---

## 🔗 Endpoints Disponíveis

### 📦 **Ordens de Compra** (`/api/ordens-compra`)

#### `GET /api/ordens-compra`
Lista todas as ordens com paginação.
```bash
curl "http://localhost:8080/api/ordens-compra?page=0&size=20"
```

#### `GET /api/ordens-compra/{id}`
Busca ordem específica.
```bash
curl "http://localhost:8080/api/ordens-compra/1"
```

#### `GET /api/ordens-compra/status/{status}`
Filtra por status (PEND, ANDA, CONC).
```bash
curl "http://localhost:8080/api/ordens-compra/status/PEND"
```

#### `POST /api/ordens-compra`
Cria nova ordem.
```bash
curl -X POST "http://localhost:8080/api/ordens-compra" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "PEND",
    "valor": 1500.00,
    "dataPrevisao": "2025-12-01",
    "dataOrdem": "2025-11-23",
    "dataEntrega": "2025-12-01"
  }'
```

#### `PUT /api/ordens-compra/{id}`
Atualiza ordem existente.
```bash
curl -X PUT "http://localhost:8080/api/ordens-compra/1" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ANDA",
    "valor": 2000.00,
    "dataPrevisao": "2025-12-05",
    "dataOrdem": "2025-11-23",
    "dataEntrega": "2025-12-05"
  }'
```

#### `PATCH /api/ordens-compra/{id}/status`
Atualiza apenas o status.
```bash
curl -X PATCH "http://localhost:8080/api/ordens-compra/1/status?novoStatus=CONC"
```

#### `DELETE /api/ordens-compra/{id}`
Remove ordem (apenas pendentes).
```bash
curl -X DELETE "http://localhost:8080/api/ordens-compra/1"
```

---

### 🛒 **Itens de Ordem** (`/api/itens-ordem-compra`)

#### `GET /api/itens-ordem-compra/{id}`
Busca item específico.
```bash
curl "http://localhost:8080/api/itens-ordem-compra/1"
```

#### `GET /api/itens-ordem-compra/ordem/{idOrdem}`
Lista itens de uma ordem.
```bash
curl "http://localhost:8080/api/itens-ordem-compra/ordem/1"
```

#### `GET /api/itens-ordem-compra/produto/{idProduto}`
Lista itens de um produto.
```bash
curl "http://localhost:8080/api/itens-ordem-compra/produto/5"
```

#### `GET /api/itens-ordem-compra/vencidos`
Lista itens vencidos.
```bash
curl "http://localhost:8080/api/itens-ordem-compra/vencidos"
```

#### `GET /api/itens-ordem-compra/proximos-vencimento`
Lista itens próximos ao vencimento.
```bash
curl "http://localhost:8080/api/itens-ordem-compra/proximos-vencimento?dias=30"
```

#### `POST /api/itens-ordem-compra`
Adiciona item à ordem.
```bash
curl -X POST "http://localhost:8080/api/itens-ordem-compra" \
  -H "Content-Type: application/json" \
  -d '{
    "idOrdemCompra": 1,
    "idProduto": 5,
    "quantidade": 10,
    "valorUnitario": 50.00
  }'
```

#### `PUT /api/itens-ordem-compra/{id}`
Atualiza item.
```bash
curl -X PUT "http://localhost:8080/api/itens-ordem-compra/1" \
  -H "Content-Type: application/json" \
  -d '{
    "idOrdemCompra": 1,
    "idProduto": 5,
    "quantidade": 15,
    "valorUnitario": 55.00
  }'
```

#### `DELETE /api/itens-ordem-compra/{id}`
Remove item.
```bash
curl -X DELETE "http://localhost:8080/api/itens-ordem-compra/1"
```

---

### 🏭 **Fornecedores** (`/api/fornecedores`)

#### `GET /api/fornecedores`
Lista todos com paginação.
```bash
curl "http://localhost:8080/api/fornecedores?page=0&size=20"
```

#### `GET /api/fornecedores/{id}`
Busca fornecedor específico.
```bash
curl "http://localhost:8080/api/fornecedores/1"
```

#### `GET /api/fornecedores/buscar`
Busca por representante.
```bash
curl "http://localhost:8080/api/fornecedores/buscar?representante=João"
```

#### `POST /api/fornecedores`
Cria novo fornecedor.
```bash
curl -X POST "http://localhost:8080/api/fornecedores" \
  -H "Content-Type: application/json" \
  -d '{
    "idPessoaJuridica": 1,
    "representante": "João Silva",
    "contatoRepresentante": "11999887766",
    "condicoesPagamento": "30/60/90 dias"
  }'
```

#### `PUT /api/fornecedores/{id}`
Atualiza fornecedor.
```bash
curl -X PUT "http://localhost:8080/api/fornecedores/1" \
  -H "Content-Type: application/json" \
  -d '{
    "idPessoaJuridica": 1,
    "representante": "João Silva Jr.",
    "contatoRepresentante": "11999887766",
    "condicoesPagamento": "30/60 dias"
  }'
```

#### `DELETE /api/fornecedores/{id}`
Remove fornecedor.
```bash
curl -X DELETE "http://localhost:8080/api/fornecedores/1"
```

---

## 🔒 Validações Implementadas

### OrdemCompra
- ✅ Status obrigatório (PEND, ANDA, CONC)
- ✅ Valor positivo obrigatório
- ✅ Data de previsão não pode ser passada
- ✅ Transição de status controlada (PEND → ANDA → CONC)
- ✅ Ordens concluídas não podem ser alteradas
- ✅ Apenas ordens pendentes podem ser removidas

### ItemOrdemCompra
- ✅ Ordem de compra obrigatória
- ✅ Produto obrigatório
- ✅ Quantidade maior que zero
- ✅ Valor unitário positivo
- ✅ Verifica existência de ordem e produto

### Fornecedor
- ✅ Pessoa jurídica obrigatória
- ✅ Representante máximo 100 caracteres
- ✅ Contato máximo 15 caracteres

---

## 📝 Formato de Resposta Padrão

### Sucesso
```json
{
  "success": true,
  "message": "Operação realizada com sucesso",
  "data": { ... }
}
```

### Erro
```json
{
  "success": false,
  "message": "Descrição do erro",
  "data": null
}
```

### Paginação
```json
{
  "success": true,
  "data": {
    "content": [ ... ],
    "pageable": { ... },
    "totalPages": 5,
    "totalElements": 100,
    "size": 20,
    "number": 0
  }
}
```

---

## 🧪 Testar com Swagger UI

Acesse: **http://localhost:8080/swagger-ui.html**

Interface interativa com todos os endpoints documentados! 🎯

---

## 🚀 Iniciar o Backend

```bash
cd c:\Users\Matheus\Documents\estoque\estoque
.\mvnw.cmd spring-boot:run
```

Ou no VS Code: Pressione `F5` para debug

---

## 📊 Exemplos JavaScript (Frontend)

### Listar Ordens
```javascript
const response = await fetch('http://localhost:8080/api/ordens-compra?page=0&size=20');
const data = await response.json();
console.log(data.data.content); // Array de ordens
```

### Criar Ordem
```javascript
const novaOrdem = {
  status: 'PEND',
  valor: 1500.00,
  dataPrevisao: '2025-12-01',
  dataOrdem: '2025-11-23',
  dataEntrega: '2025-12-01'
};

const response = await fetch('http://localhost:8080/api/ordens-compra', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(novaOrdem)
});

const result = await response.json();
console.log(result.data); // Ordem criada com ID
```

### Atualizar Status
```javascript
const response = await fetch(
  'http://localhost:8080/api/ordens-compra/1/status?novoStatus=ANDA', 
  { method: 'PATCH' }
);
const result = await response.json();
console.log(result.data); // Ordem atualizada
```

---

## ✅ Checklist de Implementação

- [x] DTOs com validações Jakarta
- [x] Services com lógica de negócio
- [x] Controllers REST completos
- [x] Tratamento global de exceções
- [x] Documentação OpenAPI
- [x] Compilação sem erros
- [x] CORS configurado
- [x] Multi-datasource (Matheus, Yuri, Erasmo)
- [ ] Frontend integrado (OrdemCompraManager.js)
- [ ] Testes de integração

---

## 🎯 Próximos Passos

1. **Iniciar o backend**: `.\mvnw.cmd spring-boot:run`
2. **Testar endpoints**: Usar Swagger UI ou curl
3. **Integrar frontend**: Ajustar OrdemCompraManager.js
4. **Criar dados de teste**: Popular banco MySQL
5. **Deploy**: Seguir instruções em DEPLOY.md
