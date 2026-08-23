import streamlit as st
import os
import tempfile
from models import get_embeddings
from pipeline import HashedMemoryRAG

st.set_page_config(page_title="Lightweight Document AI", layout="wide")

st.title("📄 Lightweight Document AI (Cloud RAG)")
st.markdown("Upload a PDF to extract text and compress it into **Binary Hashes** locally. When you ask a question, the SDK performs blazing-fast bitwise matching in memory and securely sends only the relevant snippets to your Vercel API!")

# --- Session State Initialization ---
if "rag_engine" not in st.session_state:
    st.session_state.rag_engine = None
if "chat_history" not in st.session_state:
    st.session_state.chat_history = []

# --- Sidebar: Setup & File Upload ---
with st.sidebar:
    st.header("1. Initialization")
    if st.button("Initialize Hashed Memory"):
        with st.spinner("Loading LSH embeddings model..."):
            embeddings = get_embeddings()
            st.session_state.rag_engine = HashedMemoryRAG(embeddings)
            st.success("Advanced Hashing Engine Ready!")

    st.header("2. Upload Document")
    uploaded_file = st.file_uploader("Upload PDF", type=["pdf"])
    
    if uploaded_file is not None and st.session_state.rag_engine is not None:
        if st.button("Process Document"):
            with st.spinner("Extracting text, chunking, and compressing into binary hashes locally..."):
                # Save uploaded file temporarily
                with tempfile.NamedTemporaryFile(delete=False, suffix=".pdf") as tmp:
                    tmp.write(uploaded_file.getvalue())
                    tmp_path = tmp.name
                
                # Flowchart execution
                chunks_indexed = st.session_state.rag_engine.ingest_pdf(tmp_path)
                
                os.remove(tmp_path)
                st.success(f"Document highly compressed! {chunks_indexed} chunks hashed into memory.")
    elif uploaded_file is not None:
        st.warning("Please initialize the Hashed Memory first.")

# --- Main Area: Q&A / Summarization ---
st.header("3. Query the Document")

if st.session_state.rag_engine is None or st.session_state.rag_engine.current_id == 0:
    st.info("Please initialize memory and process a document to begin.")
else:
    # Quick Summarization
    if st.button("Generate Document Summary"):
        with st.spinner("Matching hashes and auto-pinging Vercel API..."):
            answer = st.session_state.rag_engine.ask_question("Summarize the main points of this document.")
            st.write("### Summary:")
            st.write(answer)
            
    st.divider()
    
    # Q&A Chat
    st.subheader("Ask Questions")
    
    # Display chat history
    for role, message in st.session_state.chat_history:
        if role == "user":
            st.chat_message("user").write(message)
        else:
            st.chat_message("assistant").write(message)
            
    # Chat Input
    query = st.chat_input("Ask a question about the PDF...")
    if query:
        st.chat_message("user").write(query)
        st.session_state.chat_history.append(("user", query))
        
        with st.spinner("Scanning binary hashes locally and auto-pinging Vercel API..."):
            answer = st.session_state.rag_engine.ask_question(query)
            
        st.chat_message("assistant").write(answer)
        st.session_state.chat_history.append(("assistant", answer))
