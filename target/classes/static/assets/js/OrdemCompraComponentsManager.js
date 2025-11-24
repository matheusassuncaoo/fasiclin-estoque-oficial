/**
 * OrdemCompraComponentsManager
 * Gerencia os componentes de UI da página de Ordem de Compra
 */
class OrdemCompraComponentsManager {
  constructor() {
    this.elements = {};
    this.currentSort = { field: null, direction: "asc" };
    this.selectedItems = new Set();
    this.currentPage = 1;
    this.itemsPerPage = 10;
    
    // Inicializar elementos quando o DOM estiver pronto
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', () => this.initElements());
    } else {
      this.initElements();
    }
  }

  /**
   * Inicializa os elementos do DOM
   */
  initElements() {
    this.elements.tableBody = document.getElementById('ordemCompraTableBody');
  }

  /**
   * Gerencia a ordenação da tabela
   * @param {string} field Campo para ordenar
   */
  handleSort(field) {
    if (this.currentSort.field === field) {
      this.currentSort.direction =
        this.currentSort.direction === "asc" ? "desc" : "asc";
    } else {
      this.currentSort.field = field;
      this.currentSort.direction = "asc";
    }

    this.updateSortIndicators();
    this.dispatchEvent("table:sort", this.currentSort);
  }

  /**
   * Atualiza os indicadores visuais de ordenação
   */
  updateSortIndicators() {
    // Remover classes de todos os headers
    document.querySelectorAll('[data-sort]').forEach((el) => {
      el.classList.remove("sorted", "desc");
    });

    // Adicionar classe ao header atual
    if (this.currentSort.field) {
      const header = document.querySelector(
        `[data-sort="${this.currentSort.field}"]`
      );
      if (header) {
        header.classList.add("sorted");
        if (this.currentSort.direction === "desc") {
          header.classList.add("desc");
        }
      }
    }
  }

  /**
   * Retorna os parâmetros de consulta atuais
   */
  getQueryParams() {
    return {
      page: this.currentPage,
      size: this.itemsPerPage,
      sort: this.currentSort.field
        ? `${this.currentSort.field},${this.currentSort.direction}`
        : null,
    };
  }

  /**
   * Retorna os parâmetros de paginação
   * @returns {Object} Parâmetros de paginação
   */
  getPaginationParams() {
    return {
      page: this.currentPage - 1, // Backend usa índice 0
      size: this.itemsPerPage,
      sort: this.currentSort.field
        ? `${this.currentSort.field},${this.currentSort.direction}`
        : undefined,
    };
  }

  /**
   * Dispara um evento customizado no documento
   * @param {string} eventName Nome do evento
   * @param {any} detail Dados do evento
   */
  dispatchEvent(eventName, detail) {
    const event = new CustomEvent(eventName, { detail });
    document.dispatchEvent(event);
  }

  /**
   * Exibe o estado de carregamento na tabela
   * @param {boolean} isLoading Se está carregando ou não (padrão: true)
   */
  showTableLoading(isLoading = true) {
    const tableBody = document.querySelector('tbody');
    if (!tableBody) return;

    if (isLoading) {
        this.originalContent = tableBody.innerHTML;
        tableBody.innerHTML = `
            <tr>
                <td colspan="100%" style="text-align: center; padding: 40px;">
                    <div class="loading-spinner" style="
                        border: 4px solid #f3f3f3;
                        border-top: 4px solid #3498db;
                        border-radius: 50%;
                        width: 40px;
                        height: 40px;
                        animation: spin 1s linear infinite;
                        margin: 0 auto 15px;
                    "></div>
                    <p style="color: #666;">Carregando dados...</p>
                    <style>
                        @keyframes spin {
                            0% { transform: rotate(0deg); }
                            100% { transform: rotate(360deg); }
                        }
                    </style>
                </td>
            </tr>
        `;
    } else if (this.originalContent) {
        // Se tiver conteúdo original salvo e não for vazio, restaura
        // Caso contrário, o renderOrdens do Manager vai preencher
        if (!tableBody.hasChildNodes() || tableBody.querySelector('.loading-spinner')) {
             tableBody.innerHTML = ''; // Limpa para o render preencher
        }
    }
  }

  /**
   * Renderiza a tabela com as ordens de compra
   * @param {Array} ordens - Array de ordens de compra
   */
  renderTable(ordens) {
    const tableBody = document.getElementById('ordemCompraTableBody');
    if (!tableBody) {
      console.error('[OrdemCompraComponentsManager] Tabela não encontrada');
      return;
    }

    if (!ordens || ordens.length === 0) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="6" class="text-center" style="padding: 40px;">
            <div class="empty-state">
              <i data-feather="package" style="font-size: 48px; opacity: 0.6; color: #6c757d;"></i>
              <p style="margin-top: 16px; color: #6c757d;">Nenhuma ordem de compra encontrada</p>
            </div>
          </td>
        </tr>
      `;
      
      // Atualizar ícones
      if (typeof feather !== 'undefined') {
        feather.replace();
      }
      return;
    }

    // Aplicar paginação
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    const paginatedOrdens = ordens.slice(startIndex, endIndex);

    // Renderizar linhas
    tableBody.innerHTML = paginatedOrdens.map(ordem => {
      const statusClass = this.getStatusClass(ordem.statusOrdemCompra);
      const statusText = this.getStatusText(ordem.statusOrdemCompra);
      const isSelected = this.selectedItems.has(ordem.id);
      
      const dataPrev = ordem.dataPrev 
        ? this.formatDate(ordem.dataPrev) 
        : '-';
      const dataOrdem = ordem.dataOrdem 
        ? this.formatDate(ordem.dataOrdem) 
        : '-';

      return `
        <tr data-id="${ordem.id}" ${isSelected ? 'class="selected"' : ''}>
          <td class="checkbox-column">
            <input 
              type="checkbox" 
              class="row-checkbox" 
              data-id="${ordem.id}"
              ${isSelected ? 'checked' : ''}
            />
          </td>
          <td>${ordem.id || '-'}</td>
          <td>
            <span class="status-badge ${statusClass}">${statusText}</span>
          </td>
          <td>${dataPrev}</td>
          <td>${dataOrdem}</td>
          <td>
            <div class="actions">
              <button 
                class="action-btn btn-view" 
                data-id="${ordem.id}"
                title="Visualizar"
              >
                <i data-feather="eye"></i>
              </button>
              <button 
                class="action-btn btn-edit" 
                data-id="${ordem.id}"
                title="Editar"
              >
                <i data-feather="edit-2"></i>
              </button>
              <button 
                class="action-btn btn-delete" 
                data-id="${ordem.id}"
                title="Excluir"
              >
                <i data-feather="trash-2"></i>
              </button>
            </div>
          </td>
        </tr>
      `;
    }).join('');

    // Atualizar ícones
    if (typeof feather !== 'undefined') {
      feather.replace();
    }

    // Configurar event listeners
    this.setupTableEventListeners();
  }

  /**
   * Configura os event listeners da tabela
   */
  setupTableEventListeners() {
    // Checkboxes de seleção
    document.querySelectorAll('.row-checkbox').forEach(checkbox => {
      checkbox.addEventListener('change', (e) => {
        const id = parseInt(e.target.dataset.id);
        if (e.target.checked) {
          this.selectedItems.add(id);
          e.target.closest('tr').classList.add('selected');
        } else {
          this.selectedItems.delete(id);
          e.target.closest('tr').classList.remove('selected');
        }
        this.updateSelectAll();
      });
    });

    // Botão de visualizar
    document.querySelectorAll('.btn-view').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const id = parseInt(e.target.closest('.btn-view').dataset.id);
        this.dispatchEvent('ordemcompra:ordem:view', { id });
      });
    });

    // Botão de editar
    document.querySelectorAll('.btn-edit').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const id = parseInt(e.target.closest('.btn-edit').dataset.id);
        this.dispatchEvent('ordemcompra:ordem:edit', { id });
      });
    });

    // Botão de excluir
    document.querySelectorAll('.btn-delete').forEach(btn => {
      btn.addEventListener('click', (e) => {
        const id = parseInt(e.target.closest('.btn-delete').dataset.id);
        if (confirm('Tem certeza que deseja excluir esta ordem de compra?')) {
          this.dispatchEvent('ordemcompra:ordem:delete', { id });
        }
      });
    });

    // Checkbox "Selecionar todos"
    const selectAll = document.getElementById('selectAll');
    if (selectAll) {
      selectAll.addEventListener('change', (e) => {
        const checkboxes = document.querySelectorAll('.row-checkbox');
        checkboxes.forEach(checkbox => {
          checkbox.checked = e.target.checked;
          const id = parseInt(checkbox.dataset.id);
          if (e.target.checked) {
            this.selectedItems.add(id);
            checkbox.closest('tr').classList.add('selected');
          } else {
            this.selectedItems.delete(id);
            checkbox.closest('tr').classList.remove('selected');
          }
        });
      });
    }
  }

  /**
   * Atualiza o checkbox "Selecionar todos"
   */
  updateSelectAll() {
    const selectAll = document.getElementById('selectAll');
    if (!selectAll) return;

    const checkboxes = document.querySelectorAll('.row-checkbox');
    const checkedCount = Array.from(checkboxes).filter(cb => cb.checked).length;
    
    selectAll.checked = checkboxes.length > 0 && checkedCount === checkboxes.length;
    selectAll.indeterminate = checkedCount > 0 && checkedCount < checkboxes.length;
  }

  /**
   * Retorna a classe CSS para o status
   * @param {string} status - Status da ordem
   * @returns {string} Classe CSS
   */
  getStatusClass(status) {
    const statusMap = {
      'PEND': 'status-pend',
      'ANDA': 'status-anda',
      'CONC': 'status-conc',
      'CANC': 'status-canc'
    };
    return statusMap[status] || 'status-pend';
  }

  /**
   * Retorna o texto do status
   * @param {string} status - Status da ordem
   * @returns {string} Texto do status
   */
  getStatusText(status) {
    const statusMap = {
      'PEND': 'Pendente',
      'ANDA': 'Em Andamento',
      'CONC': 'Concluída',
      'CANC': 'Cancelada'
    };
    return statusMap[status] || status;
  }

  /**
   * Formata uma data para exibição
   * @param {string|Date} date - Data a ser formatada
   * @returns {string} Data formatada (dd/mm/yyyy)
   */
  formatDate(date) {
    if (!date) return '-';
    
    try {
      const dateObj = typeof date === 'string' ? new Date(date) : date;
      if (isNaN(dateObj.getTime())) return '-';
      
      const day = String(dateObj.getDate()).padStart(2, '0');
      const month = String(dateObj.getMonth() + 1).padStart(2, '0');
      const year = dateObj.getFullYear();
      
      return `${day}/${month}/${year}`;
    } catch (error) {
      return '-';
    }
  }

  /**
   * Atualiza a paginação
   * @param {number} totalItems - Total de itens
   */
  updatePagination(totalItems) {
    const totalPages = Math.ceil(totalItems / this.itemsPerPage);
    
    // Atualizar controles de paginação
    const firstPage = document.getElementById('firstPage');
    const prevPage = document.getElementById('prevPage');
    const nextPage = document.getElementById('nextPage');
    const lastPage = document.getElementById('lastPage');
    const currentPageSpan = document.querySelector('.pagination-current');
    const paginationDetails = document.getElementById('paginationDetails');

    if (currentPageSpan) {
      currentPageSpan.textContent = this.currentPage;
    }

    // Habilitar/desabilitar botões
    if (prevPage) {
      prevPage.disabled = this.currentPage === 1;
    }
    if (nextPage) {
      nextPage.disabled = this.currentPage >= totalPages || totalPages === 0;
    }
    if (firstPage) {
      firstPage.style.opacity = this.currentPage === 1 ? '0.5' : '1';
      firstPage.style.cursor = this.currentPage === 1 ? 'not-allowed' : 'pointer';
    }
    if (lastPage) {
      lastPage.style.opacity = this.currentPage >= totalPages || totalPages === 0 ? '0.5' : '1';
      lastPage.style.cursor = this.currentPage >= totalPages || totalPages === 0 ? 'not-allowed' : 'pointer';
    }

    // Atualizar detalhes da paginação
    if (paginationDetails) {
      const startItem = totalItems === 0 ? 0 : (this.currentPage - 1) * this.itemsPerPage + 1;
      const endItem = Math.min(this.currentPage * this.itemsPerPage, totalItems);
      paginationDetails.textContent = `${startItem}-${endItem} de ${totalItems}`;
    }

    // Configurar event listeners de paginação
    this.setupPaginationListeners();
  }

  /**
   * Configura os event listeners de paginação
   */
  setupPaginationListeners() {
    const prevPage = document.getElementById('prevPage');
    const nextPage = document.getElementById('nextPage');
    const firstPage = document.getElementById('firstPage');
    const lastPage = document.getElementById('lastPage');
    const itemsPerPageSelect = document.getElementById('itemsPerPage');

    if (prevPage && !prevPage.hasAttribute('data-listener')) {
      prevPage.setAttribute('data-listener', 'true');
      prevPage.addEventListener('click', () => {
        if (this.currentPage > 1) {
          this.currentPage--;
          this.dispatchEvent('ordemcompra:pagination:change', { page: this.currentPage });
        }
      });
    }

    if (nextPage && !nextPage.hasAttribute('data-listener')) {
      nextPage.setAttribute('data-listener', 'true');
      nextPage.addEventListener('click', () => {
        const totalPages = Math.ceil((window.ordemCompraManager?.ordensCompra?.length || 0) / this.itemsPerPage);
        if (this.currentPage < totalPages) {
          this.currentPage++;
          this.dispatchEvent('ordemcompra:pagination:change', { page: this.currentPage });
        }
      });
    }

    if (firstPage && !firstPage.hasAttribute('data-listener')) {
      firstPage.setAttribute('data-listener', 'true');
      firstPage.addEventListener('click', () => {
        if (this.currentPage > 1) {
          this.currentPage = 1;
          this.dispatchEvent('ordemcompra:pagination:change', { page: this.currentPage });
        }
      });
    }

    if (lastPage && !lastPage.hasAttribute('data-listener')) {
      lastPage.setAttribute('data-listener', 'true');
      lastPage.addEventListener('click', () => {
        const totalPages = Math.ceil((window.ordemCompraManager?.ordensCompra?.length || 0) / this.itemsPerPage);
        if (this.currentPage < totalPages) {
          this.currentPage = totalPages;
          this.dispatchEvent('ordemcompra:pagination:change', { page: this.currentPage });
        }
      });
    }

    if (itemsPerPageSelect && !itemsPerPageSelect.hasAttribute('data-listener')) {
      itemsPerPageSelect.setAttribute('data-listener', 'true');
      itemsPerPageSelect.addEventListener('change', (e) => {
        this.itemsPerPage = parseInt(e.target.value);
        this.currentPage = 1;
        this.dispatchEvent('ordemcompra:pagination:change', { 
          page: this.currentPage,
          itemsPerPage: this.itemsPerPage 
        });
      });
    }
  }

  /**
   * Limpa as seleções
   */
  clearSelections() {
    this.selectedItems.clear();
    document.querySelectorAll('.row-checkbox').forEach(checkbox => {
      checkbox.checked = false;
      checkbox.closest('tr')?.classList.remove('selected');
    });
    this.updateSelectAll();
  }

  /**
   * Fecha o modal
   */
  closeModal() {
    const modal = document.getElementById('modalOrdemCompra');
    if (modal) {
      modal.classList.remove('active');
    }
  }

  /**
   * Abre o modal de edição/criação
   * @param {string} mode - 'edit' ou 'create'
   * @param {Object} ordem - Dados da ordem (se edit)
   */
  openModal(mode, ordem = null) {
    const modal = document.getElementById('modalOrdemCompra');
    const modalTitle = document.getElementById('modalTitle');
    
    if (modal) {
      if (mode === 'edit' && ordem) {
        // Preencher formulário com dados da ordem
        this.fillForm(ordem);
        if (modalTitle) {
          modalTitle.innerHTML = '<i data-feather="edit-2"></i> Editar Ordem de Compra';
        }
      } else {
        // Limpar formulário para nova ordem
        this.clearForm();
        if (modalTitle) {
          modalTitle.innerHTML = '<i data-feather="shopping-cart"></i> Nova Ordem de Compra';
        }
      }
      
      modal.classList.add('active');
      
      // Atualizar ícones
      if (typeof feather !== 'undefined') {
        feather.replace();
      }
    }
  }

  /**
   * Preenche o formulário com dados da ordem
   * @param {Object} ordem - Dados da ordem
   */
  fillForm(ordem) {
    const form = document.getElementById('formOrdemCompra');
    if (!form) return;

    // Preencher campos do formulário
    const idField = form.querySelector('#id');
    if (idField) idField.value = ordem.id || '';

    const statusField = form.querySelector('#statusOrdemCompra');
    if (statusField) statusField.value = ordem.statusOrdemCompra || '';

    const valorField = form.querySelector('#valor');
    if (valorField) valorField.value = ordem.valor || '';

    const dataPrevField = form.querySelector('#dataPrev');
    if (dataPrevField && ordem.dataPrev) {
      dataPrevField.value = this.formatDateForInput(ordem.dataPrev);
    }

    const dataOrdemField = form.querySelector('#dataOrdem');
    if (dataOrdemField && ordem.dataOrdem) {
      dataOrdemField.value = this.formatDateForInput(ordem.dataOrdem);
    }

    const dataEntreField = form.querySelector('#dataEntre');
    if (dataEntreField && ordem.dataEntre) {
      dataEntreField.value = this.formatDateForInput(ordem.dataEntre);
    }
  }

  /**
   * Limpa o formulário
   */
  clearForm() {
    const form = document.getElementById('formOrdemCompra');
    if (!form) return;
    form.reset();
  }

  /**
   * Formata data para input type="date" (yyyy-mm-dd)
   * @param {string|Date} date - Data a ser formatada
   * @returns {string} Data formatada
   */
  formatDateForInput(date) {
    if (!date) return '';
    
    try {
      const dateObj = typeof date === 'string' ? new Date(date) : date;
      if (isNaN(dateObj.getTime())) return '';
      
      const year = dateObj.getFullYear();
      const month = String(dateObj.getMonth() + 1).padStart(2, '0');
      const day = String(dateObj.getDate()).padStart(2, '0');
      
      return `${year}-${month}-${day}`;
    } catch (error) {
      return '';
    }
  }

  /**
   * Mostra/esconde o loading do formulário
   * @param {boolean} isLoading - Se está carregando
   */
  showFormLoading(isLoading) {
    const form = document.getElementById('formOrdemCompra');
    const submitBtn = form?.querySelector('button[type="submit"]');
    
    if (submitBtn) {
      submitBtn.disabled = isLoading;
      if (isLoading) {
        submitBtn.innerHTML = '<i data-feather="loader"></i> Salvando...';
      } else {
        const isEdit = document.getElementById('id')?.value;
        submitBtn.innerHTML = isEdit 
          ? '<i data-feather="save"></i> Salvar Alterações'
          : '<i data-feather="plus"></i> Criar Ordem';
      }
      
      if (typeof feather !== 'undefined') {
        feather.replace();
      }
    }
  }

  /**
   * Abre o modal de visualização
   * @param {Object} ordem - Dados da ordem
   */
  async openViewModal(ordem) {
    const modal = document.getElementById('modalViewOrdem');
    if (!modal) return;

    // Preencher dados no modal
    this.fillViewModal(ordem);
    
    modal.style.display = 'flex';
    modal.classList.add('active');
    
    // Atualizar ícones
    if (typeof feather !== 'undefined') {
      feather.replace();
    }
  }

  /**
   * Preenche o modal de visualização com dados da ordem
   * @param {Object} ordem - Dados da ordem
   */
  fillViewModal(ordem) {
    // Implementar preenchimento dos campos do modal de visualização
    // Isso depende da estrutura do HTML do modal
    console.log('[OrdemCompraComponentsManager] Preenchendo modal de visualização:', ordem);
  }

  /**
   * Fecha o modal de credenciais
   */
  closeCredentialsModal() {
    const modal = document.getElementById('modalCredentials');
    if (modal) {
      modal.classList.remove('active');
    }
  }

  /**
   * Mostra/esconde o loading do modal de credenciais
   * @param {boolean} isLoading - Se está carregando
   */
  showCredentialsLoading(isLoading) {
    const modal = document.getElementById('modalCredentials');
    const submitBtn = modal?.querySelector('button[type="submit"]');
    
    if (submitBtn) {
      submitBtn.disabled = isLoading;
      if (isLoading) {
        submitBtn.innerHTML = '<i data-feather="loader"></i> Processando...';
      } else {
        submitBtn.innerHTML = '<i data-feather="check"></i> Confirmar';
      }
      
      if (typeof feather !== 'undefined') {
        feather.replace();
      }
    }
  }

  /**
   * Verifica se algum modal está aberto
   * @returns {boolean} Se algum modal está aberto
   */
  isModalOpen() {
    const modals = [
      'modalOrdemCompra',
      'modalViewOrdem',
      'modalCredentials',
      'modalInfo'
    ];
    
    return modals.some(modalId => {
      const modal = document.getElementById(modalId);
      return modal && (modal.classList.contains('active') || modal.style.display === 'flex');
    });
  }
}

// Instância global
window.ordemCompraComponentsManager = new OrdemCompraComponentsManager();
