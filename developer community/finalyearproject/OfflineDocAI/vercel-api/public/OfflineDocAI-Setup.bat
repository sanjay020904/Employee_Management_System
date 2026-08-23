@echo off
setlocal

echo ========================================================
echo        Offline Document AI - One-Click Installer
echo ========================================================
echo.

:: Prompt for installation directory
set /p "INSTALL_DIR=Where would you like to install? (Press Enter for C:\OfflineDocAI): "
if "%INSTALL_DIR%"=="" set "INSTALL_DIR=C:\OfflineDocAI"

echo.
echo Installing to: %INSTALL_DIR%
echo Creating directories...
if not exist "%INSTALL_DIR%" mkdir "%INSTALL_DIR%"
cd /d "%INSTALL_DIR%"

echo.
echo Setting up Python Environment...
:: Ensure Python is installed
python --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Python is not installed or not in PATH! Please install Python from python.org
    pause
    exit /b
)

python -m venv venv
call venv\Scripts\activate

echo.
echo Downloading and Installing the latest SDK components...
:: We will pull the pre-built SDK directly. 
:: For production, the .whl would be hosted on Vercel and downloaded here using curl.
:: Example: curl -o sdk.whl https://offlinedocai.vercel.app/offlinedocai-1.0.0-py3-none-any.whl
:: Since we are currently local, we will simulate this by checking if the file is next to the batch file,
:: or downloading from a placeholder URL.

echo Installing the SDK...
:: Try installing from a local file if they downloaded the bundle, otherwise download it
if exist "%~dp0offlinedocai-1.0.0-py3-none-any.whl" (
    pip install "%~dp0offlinedocai-1.0.0-py3-none-any.whl" >nul
) else (
    echo Downloading SDK from Vercel...
    curl -s -L -o sdk.whl https://vercel-api-rust-mu.vercel.app/offlinedocai-1.0.0-py3-none-any.whl
    pip install sdk.whl >nul
)

echo.
echo Setup Successful!
echo Launching the application...
echo.

:: This will trigger the silent hardware scan, Vercel API connection, and open the Streamlit UI
offlinedocai

pause
