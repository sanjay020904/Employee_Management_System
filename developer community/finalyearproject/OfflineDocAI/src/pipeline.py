import os
import fitz # PyMuPDF
import faiss
import numpy as np
import requests
from langchain.text_splitter import RecursiveCharacterTextSplitter

class HashedMemoryRAG:
    def __init__(self, embeddings_model):
        """
        Initializes the Hashed Memory RAG using Locality Sensitive Hashing (LSH).
        This compresses vectors into binary hashes for extreme memory efficiency.
        """
        self.embeddings_model = embeddings_model
        # all-MiniLM-L6-v2 outputs 384-dimensional vectors
        self.dimension = 384 
        # Number of bits for the hash (higher = more accurate but slightly larger)
        self.nbits = 768
        
        # Initialize LSH FAISS Index
        self.index = faiss.IndexLSH(self.dimension, self.nbits)
        
        # In-memory mapping of FAISS internal IDs to the actual text chunks
        self.chunk_store = {}
        self.current_id = 0

    def ingest_pdf(self, pdf_path):
        """Extracts text, chunks it, and hashes it into memory."""
        doc = fitz.open(pdf_path)
        text = ""
        for page in doc:
            text += page.get_text()
            
        splitter = RecursiveCharacterTextSplitter(
            chunk_size=1000,
            chunk_overlap=200,
            length_function=len
        )
        chunks = splitter.split_text(text)
        
        if not chunks:
            return 0
            
        # Generate dense embeddings
        embeddings = self.embeddings_model.embed_documents(chunks)
        embeddings_np = np.array(embeddings).astype('float32')
        
        # Add to LSH index (FAISS will automatically hash these dense vectors)
        self.index.add(embeddings_np)
        
        # Store the mapping
        for chunk in chunks:
            self.chunk_store[self.current_id] = chunk
            self.current_id += 1
            
        return len(chunks)

    def retrieve_context(self, query, k=4):
        """Hashes the query and finds the closest chunks in memory using Hamming distance."""
        if self.current_id == 0:
            return "No documents indexed."
            
        query_embedding = self.embeddings_model.embed_query(query)
        query_np = np.array([query_embedding]).astype('float32')
        
        # Search the LSH index
        distances, indices = self.index.search(query_np, min(k, self.current_id))
        
        # Retrieve the actual text chunks
        retrieved_chunks = []
        for idx in indices[0]:
            if idx != -1 and idx in self.chunk_store:
                retrieved_chunks.append(self.chunk_store[idx])
                
        return "\n\n".join(retrieved_chunks)

    def ask_question(self, query):
        """Retrieves context from local hashed memory and sends it to Vercel API."""
        import json
        from setup import CONFIG_FILE
        
        # Read API URL and Scale from Config
        api_url = "http://localhost:3000"
        model_scale = "8b"
        if os.path.exists(CONFIG_FILE):
            with open(CONFIG_FILE, "r") as f:
                config = json.load(f)
                api_url = config.get("api_url", api_url)
                model_scale = config.get("model_scale", model_scale)
                
        context = self.retrieve_context(query)
        
        try:
            response = requests.post(
                f"{api_url}/api/chat",
                json={"query": query, "context": context, "model_scale": model_scale},
                headers={"Content-Type": "application/json"}
            )
            response.raise_for_status()
            data = response.json()
            return data.get("answer", "No answer returned.")
        except Exception as e:
            return f"Error communicating with Vercel API: {e}"
