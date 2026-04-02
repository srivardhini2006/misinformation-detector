import { useState, useRef } from "react";

const STYLES = `
  @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:ital,wght@0,300;0,400;0,500;1,300&display=swap');

  *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

  :root {
    --bg: #0b0f1a;
    --surface: #111827;
    --surface2: #1a2235;
    --border: rgba(255,255,255,0.07);
    --accent: #00e5c3;
    --accent2: #7c6ff7;
    --text: #e8eaf0;
    --muted: #6b7a96;
    --warn: #f0a05a;
  }
   
  body { background: var(--bg); }

  .app {
    font-family: 'DM Sans', sans-serif;
    background: var(--bg);
    min-height: 100vh;
    color: var(--text);
    position: relative;
    overflow-x: hidden;
  }

  /* Noise texture overlay */
  .app::before {
    content: '';
    position: fixed;
    inset: 0;
    background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noise'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noise)' opacity='0.03'/%3E%3C/svg%3E");
    pointer-events: none;
    z-index: 0;
  }

  /* Glow blobs */
  .blob {
    position: fixed;
    border-radius: 50%;
    filter: blur(80px);
    opacity: 0.12;
    pointer-events: none;
    z-index: 0;
  }
  .blob-1 { width: 500px; height: 500px; background: var(--accent2); top: -100px; left: -100px; }
  .blob-2 { width: 400px; height: 400px; background: var(--accent); bottom: 0; right: -100px; }

  nav {
    position: relative;
    z-index: 10;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 40px;
    height: 64px;
    border-bottom: 1px solid var(--border);
    background: rgba(11,15,26,0.85);
    backdrop-filter: blur(16px);
  }

  .logo {
    display: flex;
    align-items: center;
    gap: 10px;
    font-family: 'Syne', sans-serif;
    font-weight: 800;
    font-size: 1.25rem;
    letter-spacing: -0.02em;
    color: var(--text);
  }

  .logo-icon {
    width: 34px; height: 34px;
    background: linear-gradient(135deg, var(--accent2), var(--accent));
    border-radius: 8px;
    display: flex; align-items: center; justify-content: center;
    font-size: 16px;
  }

  .logo span { color: var(--accent); }

  .nav-links {
    display: flex;
    align-items: center;
    gap: 32px;
    list-style: none;
  }

  .nav-links li a {
    text-decoration: none;
    color: var(--muted);
    font-size: 0.875rem;
    font-weight: 400;
    transition: color 0.2s;
  }

  .nav-links li a:hover { color: var(--text); }

  .nav-actions { display: flex; gap: 10px; align-items: center; }

  .btn-ghost {
    background: transparent;
    border: 1px solid var(--border);
    color: var(--text);
    padding: 7px 18px;
    border-radius: 8px;
    font-size: 0.875rem;
    cursor: pointer;
    transition: all 0.2s;
    font-family: 'DM Sans', sans-serif;
  }
  .btn-ghost:hover { background: var(--surface2); border-color: rgba(255,255,255,0.15); }

  .btn-primary {
    background: linear-gradient(135deg, var(--accent2), #5b54e0);
    border: none;
    color: #fff;
    padding: 8px 20px;
    border-radius: 8px;
    font-size: 0.875rem;
    font-weight: 500;
    cursor: pointer;
    transition: opacity 0.2s, transform 0.15s;
    font-family: 'DM Sans', sans-serif;
  }
  .btn-primary:hover { opacity: 0.9; transform: translateY(-1px); }

  /* Hero */
  .hero {
    position: relative;
    z-index: 2;
    text-align: center;
    padding: 64px 20px 48px;
  }

  .badge {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    background: rgba(0,229,195,0.08);
    border: 1px solid rgba(0,229,195,0.2);
    color: var(--accent);
    font-size: 0.75rem;
    font-weight: 500;
    padding: 4px 12px;
    border-radius: 999px;
    margin-bottom: 20px;
    letter-spacing: 0.04em;
    text-transform: uppercase;
  }

  .badge::before { content: '●'; font-size: 8px; animation: pulse 2s infinite; }

  @keyframes pulse {
    0%, 100% { opacity: 1; }
    50% { opacity: 0.3; }
  }

  .hero h1 {
    font-family: 'Syne', sans-serif;
    font-size: clamp(2.2rem, 5vw, 3.6rem);
    font-weight: 800;
    line-height: 1.1;
    letter-spacing: -0.03em;
    margin-bottom: 16px;
  }

  .hero h1 .grad {
    background: linear-gradient(90deg, var(--accent2), var(--accent));
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }

  .hero p {
    color: var(--muted);
    font-size: 1rem;
    max-width: 560px;
    margin: 0 auto;
    line-height: 1.7;
  }

  .hero p strong { color: var(--text); font-weight: 500; }

  /* Banner */
  .banner {
    position: relative;
    z-index: 2;
    max-width: 860px;
    margin: 0 auto 28px;
    background: linear-gradient(90deg, rgba(124,111,247,0.08), rgba(0,229,195,0.06));
    border: 1px solid rgba(124,111,247,0.25);
    border-radius: 12px;
    padding: 14px 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    font-size: 0.85rem;
  }

  .banner-left { display: flex; align-items: center; gap: 10px; }
  .banner-left svg { flex-shrink: 0; color: var(--accent2); }

  .banner strong { color: var(--text); font-weight: 600; display: block; }
  .banner span { color: var(--muted); }

  .btn-banner {
    background: var(--accent2);
    color: #fff;
    border: none;
    padding: 8px 16px;
    border-radius: 8px;
    font-size: 0.8rem;
    cursor: pointer;
    white-space: nowrap;
    font-family: 'DM Sans', sans-serif;
    font-weight: 500;
    transition: opacity 0.2s;
  }
  .btn-banner:hover { opacity: 0.85; }

  .banner-close {
    background: none; border: none; color: var(--muted);
    cursor: pointer; font-size: 18px; line-height: 1;
    padding: 0 4px;
  }

  /* Main panel */
  .main-panel {
    position: relative;
    z-index: 2;
    max-width: 860px;
    margin: 0 auto 48px;
    background: var(--surface);
    border: 1px solid var(--border);
    border-radius: 16px;
    overflow: hidden;
    display: grid;
    grid-template-columns: 1fr 300px;
  }

  .panel-left { border-right: 1px solid var(--border); display: flex; flex-direction: column; }

  .panel-toolbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 14px 18px;
    border-bottom: 1px solid var(--border);
    gap: 10px;
  }

  .toolbar-left { display: flex; gap: 8px; }

  .select-pill {
    background: var(--surface2);
    border: 1px solid var(--border);
    color: var(--text);
    font-size: 0.8rem;
    padding: 5px 12px;
    border-radius: 8px;
    cursor: pointer;
    font-family: 'DM Sans', sans-serif;
    appearance: none;
    -webkit-appearance: none;
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='10' height='6' fill='none'%3E%3Cpath d='M1 1l4 4 4-4' stroke='%236b7a96' stroke-width='1.5' stroke-linecap='round' stroke-linejoin='round'/%3E%3C/svg%3E");
    background-repeat: no-repeat;
    background-position: right 10px center;
    padding-right: 28px;
  }

  .scan-history-btn {
    background: none; border: none; color: var(--muted);
    font-size: 0.8rem; cursor: pointer;
    display: flex; align-items: center; gap: 5px;
    font-family: 'DM Sans', sans-serif;
  }

  .textarea-wrap {
    flex: 1;
    position: relative;
    min-height: 200px;
  }

  .text-area {
    width: 100%; height: 100%;
    min-height: 200px;
    background: transparent;
    border: none;
    resize: none;
    color: var(--text);
    font-family: 'DM Sans', sans-serif;
    font-size: 0.925rem;
    padding: 18px;
    outline: none;
    line-height: 1.7;
  }

  .text-area::placeholder { color: var(--muted); }

  .drop-zone {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 32px 20px;
    border-top: 1px dashed var(--border);
    gap: 12px;
  }

  .drop-label {
    color: var(--muted);
    font-size: 0.875rem;
  }

  .drop-divider {
    display: flex; align-items: center; gap: 10px;
    color: var(--muted); font-size: 0.75rem; width: 200px;
  }
  .drop-divider::before, .drop-divider::after {
    content: ''; flex: 1; height: 1px; background: var(--border);
  }

  .drop-actions { display: flex; gap: 10px; }

  .btn-drop {
    display: flex; align-items: center; gap: 8px;
    padding: 10px 20px;
    border-radius: 10px;
    font-size: 0.875rem;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s;
    font-family: 'DM Sans', sans-serif;
  }

  .btn-paste {
    background: linear-gradient(135deg, var(--accent2), #5b54e0);
    color: #fff; border: none;
  }
  .btn-paste:hover { transform: translateY(-2px); box-shadow: 0 8px 24px rgba(124,111,247,0.3); }

  .btn-upload {
    background: transparent;
    color: var(--text);
    border: 1px solid var(--border);
  }
  .btn-upload:hover { background: var(--surface2); border-color: rgba(255,255,255,0.15); }

  .accepts { color: var(--muted); font-size: 0.75rem; padding: 0 18px 14px; }

  /* Panel right */
  .panel-right {
    display: flex;
    flex-direction: column;
  }

  .brand-header {
    display: flex; align-items: center; gap: 8px;
    padding: 16px 18px;
    border-bottom: 1px solid var(--border);
    font-family: 'Syne', sans-serif;
    font-weight: 700;
    font-size: 0.9rem;
    color: var(--muted);
  }

  .result-zone {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 32px 18px;
    gap: 8px;
  }

  .confidence-ring {
    position: relative;
    width: 110px; height: 110px;
    margin-bottom: 8px;
  }

  .confidence-ring svg { transform: rotate(-90deg); }

  .confidence-ring .track { stroke: var(--surface2); }
  .confidence-ring .fill {
    stroke: var(--accent);
    stroke-linecap: round;
    stroke-dasharray: 283;
    transition: stroke-dashoffset 1s cubic-bezier(.4,0,.2,1);
  }

  .confidence-center {
    position: absolute; inset: 0;
    display: flex; flex-direction: column;
    align-items: center; justify-content: center;
    font-family: 'Syne', sans-serif;
  }

  .confidence-pct {
    font-size: 1.6rem; font-weight: 800;
    line-height: 1;
  }

  .confidence-label {
    font-size: 0.65rem; color: var(--muted); text-transform: uppercase; letter-spacing: 0.08em;
  }

  .result-text {
    color: var(--muted); font-size: 0.8rem; text-align: center; line-height: 1.5;
  }

  .result-text strong { color: var(--text); display: block; font-size: 0.9rem; margin-bottom: 4px; }

  /* Sidebar card */
  .sidebar-card {
    margin: 12px;
    background: linear-gradient(135deg, rgba(124,111,247,0.08), rgba(0,229,195,0.04));
    border: 1px solid rgba(124,111,247,0.18);
    border-radius: 10px;
    padding: 16px;
  }

  .sidebar-card h4 {
    font-family: 'Syne', sans-serif;
    font-weight: 700;
    font-size: 0.875rem;
    color: var(--text);
    margin-bottom: 6px;
  }

  .sidebar-card p {
    font-size: 0.78rem;
    color: var(--muted);
    line-height: 1.55;
  }

  .sidebar-card .tag {
    display: inline-block;
    background: rgba(0,229,195,0.1);
    color: var(--accent);
    font-size: 0.7rem;
    padding: 2px 8px;
    border-radius: 4px;
    margin-top: 8px;
    font-weight: 500;
  }

  /* Scan btn */
  .scan-row {
    padding: 12px 18px;
    border-top: 1px solid var(--border);
    display: flex; justify-content: flex-end;
  }

  .btn-scan {
    background: linear-gradient(135deg, var(--accent), #00b89c);
    border: none; color: #0b0f1a;
    font-family: 'Syne', sans-serif;
    font-weight: 700;
    font-size: 0.9rem;
    padding: 10px 28px;
    border-radius: 10px;
    cursor: pointer;
    letter-spacing: 0.03em;
    transition: transform 0.15s, box-shadow 0.2s;
  }
  .btn-scan:hover { transform: translateY(-2px); box-shadow: 0 8px 24px rgba(0,229,195,0.3); }
  .btn-scan:disabled { opacity: 0.5; cursor: not-allowed; }

  /* Scanning animation */
  @keyframes scanLine {
    0% { transform: translateY(-100%); }
    100% { transform: translateY(100%); }
  }

  .scanning-overlay {
    position: absolute; inset: 0;
    background: linear-gradient(to bottom, transparent 30%, rgba(0,229,195,0.04) 50%, transparent 70%);
    animation: scanLine 1.5s linear infinite;
    pointer-events: none;
  }

  /* Models row */
  .models-row {
    position: relative; z-index: 2;
    max-width: 860px; margin: 0 auto 20px;
    display: flex; align-items: center; gap: 8px; flex-wrap: wrap;
  }

  .models-label { color: var(--muted); font-size: 0.75rem; margin-right: 4px; }

  .model-chip {
    background: var(--surface);
    border: 1px solid var(--border);
    color: var(--muted);
    font-size: 0.72rem;
    padding: 3px 10px;
    border-radius: 999px;
  }

  .model-chip.active {
    border-color: rgba(0,229,195,0.35);
    color: var(--accent);
    background: rgba(0,229,195,0.06);
  }
`;

const MODELS = ["GPT-4o", "Claude 4", "Gemini 2.5", "Llama 4", "Mistral", "Grok 3", "Copilot"];

export default function VeritasDetector() {
  const [text, setText] = useState("");
  const [scanning, setScanning] = useState(false);
  const [result, setResult] = useState(null);
  const [bannerVisible, setBannerVisible] = useState(true);
  const fileRef = useRef(null);

  const confidence = result?.confidence ?? 0;
  const circumference = 283;
  const offset = circumference - (confidence / 100) * circumference;

  const handleScan = () => {
    if (!text.trim()) return;
    setScanning(true);
    setResult(null);
    setTimeout(() => {
      const pct = Math.floor(Math.random() * 85) + 5;
      setResult({ confidence: pct, label: pct > 60 ? "Likely AI-generated" : pct > 35 ? "Uncertain" : "Likely human" });
      setScanning(false);
    }, 2200);
  };

  const handlePaste = async () => {
    try {
      const t = await navigator.clipboard.readText();
      setText(t);
    } catch {
      setText("Paste your content here to analyze it...");
    }
  };

  const handleFile = (e) => {
    const file = e.target.files?.[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = (ev) => setText(ev.target.result);
    reader.readAsText(file);
  };

  const getColor = (pct) => {
    if (pct > 60) return "#f87171";
    if (pct > 35) return "#f0a05a";
    return "#00e5c3";
  };

  return (
    <>
      <style>{STYLES}</style>
      <div className="app">
        <div className="blob blob-1" />
        <div className="blob blob-2" />

        {/* NAV */}
        <nav>
          <div className="logo">
            <div className="logo-icon">⚡</div>
            Veritas<span>.detect</span>
          </div>
          <ul className="nav-links">
            <li><a href="#">Pricing</a></li>
            <li><a href="#">Features ▾</a></li>
            <li><a href="#">Resources ▾</a></li>
          </ul>
          <div className="nav-actions">
            <button className="btn-ghost">Log in</button>
            <button className="btn-primary">Get Started</button>
          </div>
        </nav>

        {/* HERO */}
        <div className="hero">
          <div className="badge">Live Detection Engine</div>
          <h1>
            The Sharpest<br />
            <span className="grad">AI Content Detector</span>
          </h1>
          <p>
            Veritas analyzes text for signals of <strong>GPT-4o, Claude 4, Gemini 2.5, Llama</strong>, and every major model — instantly, for free.
          </p>
        </div>

        {/* MODELS */}
        <div className="models-row" style={{padding: "0 20px", maxWidth: "860px", margin: "0 auto 20px"}}>
          <span className="models-label">Detects:</span>
          {MODELS.map((m, i) => (
            <span key={m} className={`model-chip${i < 3 ? " active" : ""}`}>{m}</span>
          ))}
        </div>

        {/* BANNER */}
        {bannerVisible && (
          <div className="banner" style={{maxWidth: "820px", margin: "0 auto 20px", padding: "0 20px"}}>
            <div style={{display: "flex", alignItems: "center", gap: "12px", width: "100%", padding: "14px"}}>
              <div className="banner-left">
                <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
                  <path d="M9 12l2 2 4-4" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                  <circle cx="12" cy="12" r="9" stroke="currentColor" strokeWidth="2"/>
                </svg>
                <div>
                  <strong>Prove Your Writing is Human</strong>
                  <span>Our Chrome Extension records your writing process in real-time.</span>
                </div>
              </div>
              <div style={{display:"flex", gap:"8px", marginLeft:"auto", alignItems:"center"}}>
                <button className="btn-banner">Add to Chrome</button>
                <button className="banner-close" onClick={() => setBannerVisible(false)}>×</button>
              </div>
            </div>
          </div>
        )}

        {/* MAIN PANEL */}
        <div className="main-panel" style={{maxWidth: "820px", margin: "0 auto 48px", padding: "0 20px"}}>
          <div style={{display:"grid", gridTemplateColumns:"1fr 280px", background:"var(--surface)", border:"1px solid var(--border)", borderRadius:"16px", overflow:"hidden", width:"100%"}}>
            {/* LEFT */}
            <div style={{borderRight:"1px solid var(--border)", display:"flex", flexDirection:"column"}}>
              <div className="panel-toolbar">
                <div className="toolbar-left">
                  <select className="select-pill">
                    <option>Model Pro 2.1</option>
                    <option>Model Lite 1.0</option>
                  </select>
                  <select className="select-pill">
                    <option>English</option>
                    <option>Spanish</option>
                    <option>French</option>
                  </select>
                </div>
                <button className="scan-history-btn">
                  <svg width="13" height="13" fill="none" viewBox="0 0 24 24">
                    <circle cx="12" cy="12" r="9" stroke="currentColor" strokeWidth="2"/>
                    <path d="M12 7v5l3 3" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
                  </svg>
                  Scan History
                </button>
              </div>

              {/* Textarea */}
              <div className="textarea-wrap" style={{position:"relative", flex:1, minHeight:"180px"}}>
                <textarea
                  className="text-area"
                  style={{width:"100%", height:"100%", minHeight:"180px", background:"transparent", border:"none", resize:"none", color:"var(--text)", fontFamily:"'DM Sans', sans-serif", fontSize:"0.925rem", padding:"18px", outline:"none", lineHeight:"1.7"}}
                  placeholder="Drag & drop or paste text here and click Analyze…"
                  value={text}
                  onChange={e => setText(e.target.value)}
                  disabled={scanning}
                />
                {scanning && <div className="scanning-overlay" />}
              </div>

              {/* Drop zone */}
              <div className="drop-zone">
                <span className="drop-label">Drag and drop files to upload</span>
                <div className="drop-divider">or</div>
                <div className="drop-actions">
                  <button className="btn-drop btn-paste" onClick={handlePaste}>
                    <svg width="14" height="14" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M16 4h2a2 2 0 012 2v14a2 2 0 01-2 2H6a2 2 0 01-2-2V6a2 2 0 012-2h2"/>
                      <rect x="8" y="2" width="8" height="4" rx="1" ry="1"/>
                    </svg>
                    Paste
                  </button>
                  <button className="btn-drop btn-upload" onClick={() => fileRef.current?.click()}>
                    <svg width="14" height="14" fill="none" viewBox="0 0 24 24">
                      <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4M17 8l-5-5-5 5M12 3v12" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                    </svg>
                    Upload
                  </button>
                  <input ref={fileRef} type="file" accept=".txt,.docx" style={{display:"none"}} onChange={handleFile} />
                </div>
              </div>
              <p className="accepts">Accepts .docx, .txt files</p>

              <div className="scan-row">
                <button className="btn-scan" onClick={handleScan} disabled={!text.trim() || scanning}>
                  {scanning ? "Analyzing…" : "Analyze →"}
                </button>
              </div>
            </div>

            {/* RIGHT */}
            <div style={{display:"flex", flexDirection:"column"}}>
              <div className="brand-header">
                <span>⚡</span> veritas.detect
              </div>

              <div className="result-zone">
                <div className="confidence-ring">
                  <svg width="110" height="110" viewBox="0 0 110 110">
                    <circle className="track" cx="55" cy="55" r="45" fill="none" strokeWidth="8" stroke="var(--surface2)"/>
                    <circle
                      className="fill"
                      cx="55" cy="55" r="45" fill="none" strokeWidth="8"
                      stroke={result ? getColor(confidence) : "var(--accent2)"}
                      strokeDasharray={circumference}
                      strokeDashoffset={result ? offset : circumference}
                      strokeLinecap="round"
                      style={{transform:"rotate(-90deg)", transformOrigin:"center", transition:"stroke-dashoffset 1s cubic-bezier(.4,0,.2,1), stroke 0.4s"}}
                    />
                  </svg>
                  <div className="confidence-center">
                    <span className="confidence-pct" style={{color: result ? getColor(confidence) : "var(--muted)"}}>
                      {result ? `${confidence}%` : "--"}
                    </span>
                    <span className="confidence-label">AI</span>
                  </div>
                </div>

                <div className="result-text">
                  {result ? (
                    <>
                      <strong style={{color: getColor(confidence)}}>{result.label}</strong>
                      {confidence > 60
                        ? "Strong AI patterns detected in this content."
                        : confidence > 35
                        ? "Mixed signals — could be AI-assisted."
                        : "This content reads as human-written."}
                    </>
                  ) : (
                    <>Paste text and click <strong>Analyze</strong> to see your score</>
                  )}
                </div>
              </div>

              <div className="sidebar-card">
                <h4>Tools for Writers</h4>
                <p>Understand exactly why your text is flagged and how to make it more authentically yours.</p>
                <span className="tag">Free · No signup</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
