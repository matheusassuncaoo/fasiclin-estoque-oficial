/**
 * NotificationManager - Gerenciador de Notificações
 * Exibe mensagens de sucesso, erro, aviso e carregamento
 */
class NotificationManager {
    constructor() {
        this.container = document.createElement('div');
        this.container.id = 'notification-container';
        this.container.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            display: flex;
            flex-direction: column;
            gap: 10px;
        `;
        document.body.appendChild(this.container);
    }

    show(message, type = 'info', duration = 3000) {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        
        // Estilos base
        let backgroundColor = '#333';
        let icon = 'info';
        
        switch(type) {
            case 'success':
                backgroundColor = '#28a745';
                icon = 'check-circle';
                break;
            case 'error':
                backgroundColor = '#dc3545';
                icon = 'alert-circle';
                break;
            case 'warning':
                backgroundColor = '#ffc107';
                icon = 'alert-triangle';
                break;
            case 'loading':
                backgroundColor = '#17a2b8';
                icon = 'loader';
                break;
        }

        notification.style.cssText = `
            background-color: ${backgroundColor};
            color: white;
            padding: 15px 20px;
            border-radius: 4px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            display: flex;
            align-items: center;
            gap: 10px;
            min-width: 300px;
            opacity: 0;
            transform: translateX(100%);
            transition: all 0.3s ease;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
        `;

        notification.innerHTML = `
            <i data-feather="${icon}"></i>
            <span>${message}</span>
        `;

        this.container.appendChild(notification);
        
        // Renderizar ícone
        if (typeof feather !== 'undefined') {
            feather.replace();
        }

        // Animação de entrada
        requestAnimationFrame(() => {
            notification.style.opacity = '1';
            notification.style.transform = 'translateX(0)';
        });

        // Auto-remoção (exceto para loading)
        if (type !== 'loading') {
            setTimeout(() => {
                this.close(notification);
            }, duration);
        }

        return notification;
    }

    success(message) {
        return this.show(message, 'success');
    }

    error(message) {
        return this.show(message, 'error');
    }

    warning(message) {
        return this.show(message, 'warning');
    }

    loading(message) {
        return this.show(message, 'loading', 0);
    }

    close(notification) {
        if (!notification) return;
        
        notification.style.opacity = '0';
        notification.style.transform = 'translateX(100%)';
        
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }
}

// Instância global
window.notify = new NotificationManager();
