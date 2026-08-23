import os
import sys
import subprocess
from setup import CONFIG_FILE, run_setup_wizard

def main():
    """Entry point for the SDK to launch the Streamlit app."""
    # Check if first-time setup is needed
    if not os.path.exists(CONFIG_FILE):
        run_setup_wizard()
        
    current_dir = os.path.dirname(os.path.abspath(__file__))
    app_path = os.path.join(current_dir, "app.py")
        
    print(f"Launching Offline Document AI from: {app_path}")
    subprocess.run([sys.executable, "-m", "streamlit", "run", app_path])

if __name__ == "__main__":
    main()
