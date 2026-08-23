import os
import json
import psutil
import requests

CONFIG_DIR = os.path.expanduser("~/.offlinedocai")
CONFIG_FILE = os.path.join(CONFIG_DIR, "config.json")

# Default placeholder Vercel URL
DEFAULT_VERCEL_URL = "https://vercel-api-rust-mu.vercel.app"

def run_setup_wizard():
    print("=======================================")
    print("Welcome to Offline Document AI Setup!")
    print("=======================================")
    print("Checking your system configuration...\n")
    
    # 1. Profile Hardware
    ram_bytes = psutil.virtual_memory().total
    ram_gb = round(ram_bytes / (1024 ** 3))
    cpu_cores = psutil.cpu_count(logical=False) or 4
    
    print(f"Detected RAM: {ram_gb} GB")
    print(f"Detected CPU Cores: {cpu_cores}\n")
    
    # 2. Configure Vercel API
    print("Automatically negotiating hardware-aware scaling with Vercel API...")
    api_url = DEFAULT_VERCEL_URL
    
    # 3. Send to Vercel to determine model scaling
    scale = "8b" # default fallback
    try:
        response = requests.post(f"{api_url}/api/setup", json={
            "ram_gb": ram_gb,
            "cpu_cores": cpu_cores
        }, timeout=10)
        
        if response.status_code == 200:
            data = response.json()
            scale = data.get("model_scale", "8b")
            print(f"Success! {data.get('message', '')}")
        else:
            print("Could not connect to Vercel API setup. Falling back to default 8b model.")
    except Exception as e:
        print(f"Error connecting to API: {e}. Falling back to default 8b model.")

    # 4. Save Config
    os.makedirs(CONFIG_DIR, exist_ok=True)
    config = {
        "api_url": api_url,
        "model_scale": scale
    }
    
    with open(CONFIG_FILE, "w") as f:
        json.dump(config, f)
        
    print("\nSetup Complete! Launching Document Summarizer...")
