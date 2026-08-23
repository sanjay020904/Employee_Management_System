import React from 'react';

export default function Home() {
  return (
    <div style={{ fontFamily: 'sans-serif', textAlign: 'center', marginTop: '50px' }}>
      <h1>Offline Document AI</h1>
      <p>Secure, local, and lightning-fast RAG document summarization.</p>
      
      <div style={{ marginTop: '30px' }}>
        <h3>Download the SDK</h3>
        <p>Install our Python SDK to analyze documents locally without uploading your private PDFs.</p>
        <a 
          href="/OfflineDocAI-Setup.bat" 
          download
          style={{
            display: 'inline-block',
            padding: '10px 20px',
            backgroundColor: '#0070f3',
            color: 'white',
            textDecoration: 'none',
            borderRadius: '5px',
            fontWeight: 'bold'
          }}
        >
          Download One-Click Setup
        </a>
      </div>

      <div style={{ marginTop: '50px', textAlign: 'left', maxWidth: '600px', margin: '50px auto' }}>
        <h4>How to Install:</h4>
        <ol style={{ backgroundColor: '#f4f4f4', padding: '20px 40px', borderRadius: '5px', lineHeight: '1.6' }}>
          <li>Download the <b>OfflineDocAI-Setup.bat</b> file above.</li>
          <li>Double-click the downloaded file.</li>
          <li>Follow the prompt to choose an installation folder (e.g., C:\OfflineDocAI).</li>
          <li>The setup will automatically install the app, scan your PC's hardware to give you the best AI model, and open the Document Summarizer!</li>
        </ol>
      </div>
    </div>
  );
}
