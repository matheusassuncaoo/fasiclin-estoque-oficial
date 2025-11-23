@echo off
REM ============================================
REM Script para iniciar o Backend Fasiclin
REM Modulo: Ordem de Compra (Usuario: Matheus)
REM ============================================

echo.
echo ========================================
echo   Backend Fasiclin - Sistema Estoque
echo ========================================
echo.
echo Modulo: Ordem de Compra
echo Usuario DB: aluno4 (Matheus)
echo Servidor: 160.20.22.99:3360
echo Database: fasiclin
echo.
echo ========================================
echo.

REM Verificar se o arquivo .env existe
if not exist ".env" (
    echo [ERRO] Arquivo .env nao encontrado!
    echo Por favor, configure o arquivo .env antes de iniciar.
    pause
    exit /b 1
)

echo [INFO] Carregando variaveis de ambiente do .env...
echo [INFO] Iniciando Spring Boot...
echo.

REM Iniciar o Spring Boot
call mvnw.cmd spring-boot:run

pause
