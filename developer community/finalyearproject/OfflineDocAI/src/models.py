import os
from langchain_community.embeddings import HuggingFaceEmbeddings

def get_embeddings():
    """Returns the local sentence-transformers embedding model for FAISS hashing."""
    # This runs locally so the user's PDF is never sent to the cloud in full.
    return HuggingFaceEmbeddings(model_name="all-MiniLM-L6-v2")
