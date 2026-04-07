# ✦ SignFlow — Digital Document Signing

A lightweight, zero-dependency desktop application for digitally signing documents, built entirely with Java Swing. No external libraries required — just the JDK.

![Java](https://img.shields.io/badge/Java-11%2B-orange?style=flat-square&logo=java)
![Platform](https://img.shields.io/badge/platform-Windows%20%7C%20macOS%20%7C%20Linux-lightgrey?style=flat-square)

---

## Overview

SignFlow is a single-file Java desktop app that lets you manage, sign, and export documents without any third-party dependencies. It ships with sample contracts (NDA, Service Agreement, Offer Letter) and supports importing your own PDF files. Signatures are drawn freehand on a canvas and embedded into a downloadable signed HTML export.

---

## Features

- **Document Management** — View, add, and organize text-based documents from a card-style dashboard
- **PDF Import** — Load any PDF file; metadata (version, estimated page count, file size) is extracted without any external library
- **Freehand Signature Canvas** — Draw your signature with your mouse; clear and redo as needed
- **Signer Details** — Capture signer name, email, and timestamp alongside the drawn signature
- **Signed Export** — Download a self-contained signed HTML file suitable for printing or archiving
- **Live Stats Sidebar** — At-a-glance counts of total, signed, pending, and PDF documents
- **Toast Notifications** — Non-intrusive feedback for every action
- **No Dependencies** — Pure Java SE; nothing to install beyond the JDK

---

## Screenshots
```
OUTPUT
```
<img width="1919" height="1079" alt="Screenshot 2026-04-07 214529" src="https://github.com/user-attachments/assets/bb8453f9-8b5b-4bd5-a57c-1930dbb8eeb8" />
<img width="1919" height="1074" alt="Screenshot 2026-04-07 215802" src="https://github.com/user-attachments/assets/ed8f2b94-0543-48d7-81f2-562c0bb6a1fe" />
<img width="1919" height="1079" alt="Screenshot 2026-04-07 215857" src="https://github.com/user-attachments/assets/661d1229-d3d7-4238-bfdd-d50bfba737ff" />
```
```

## Getting Started

### Prerequisites

- Java 11 or higher ([Download JDK](https://adoptium.net/))

### Build & Run

```bash
# Compile
javac DocumentSigner.java

# Run
java DocumentSigner
```

That's it. No build tool, no classpath, no configuration files.

---

## Usage

1. **Browse documents** in the main panel. Pre-loaded templates include an NDA, Service Agreement, and Employment Offer Letter.
2. **Import a PDF** using the "📎 Import PDF" button to load any existing PDF document.
3. **Click a document card** to open the signing view.
4. **Draw your signature** on the canvas using your mouse.
5. **Enter your name and email**, then click **Sign Document**.
6. **Download the signed document** as a standalone HTML file via the download button.

---

## Project Structure

```
DocumentSigner.java        # Entire application — single self-contained file
```

Key internal classes and sections:

| Section | Description |
|---|---|
| `Document` | Data model for text and PDF documents |
| `buildHome()` | Main dashboard layout (sidebar + document list) |
| `buildSidebar()` | Navigation panel with live stats |
| `buildDocumentList()` | Scrollable card grid with Add/Import controls |
| `buildDocCard()` | Individual document card with status badge |
| `showAddDocumentDialog()` | Dialog for creating new text documents |
| `importPDFDocument()` | File chooser + raw PDF byte loading |
| `extractPDFVersion()` | Parses PDF version from header bytes |
| `estimatePageCount()` | Heuristic page counter (no library needed) |
| `downloadPDFDoc()` | Exports a signed HTML file |
| `showToast()` | Lightweight toast notification overlay |

---

## PDF Handling (No External Library)

SignFlow reads PDFs as raw bytes and extracts metadata using two heuristics:

- **Version** — reads the `%PDF-x.y` header at the start of the file
- **Page count** — counts `/Type /Page` entries in the raw byte stream (excludes `/Pages` parent nodes)

This approach works reliably for standard PDFs and keeps the project completely dependency-free.

---

Pre-loaded sample documents are initialized in `initDocs()` and can be replaced or extended freely.

---

## Contributing

Contributions are welcome. To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---
