# Digital Document Signing Application (SignFlow)

## Objective
The objective of this project is to develop a desktop-based digital document signing system using Java.

The application allows users to:
- Manage documents
- Import PDF files
- Digitally sign documents
- Download signed copies

This project demonstrates concepts of GUI development, file handling, and event-driven programming.

---

## Tools & Technologies Used

- **Programming Language:** Java  
- **GUI Framework:** Java Swing  
- **IDE/Compiler:** Any Java IDE / Terminal  

### Libraries Used:
- javax.swing (GUI)
- java.awt (Graphics & Events)
- java.io (File Handling)
- java.util (Utilities)

---

## Features of the Application

### Document Management
- View list of documents
- Add new documents
- Display document details

### PDF Support
- Import PDF files
- Validate PDF format using magic bytes
- Open PDF in system viewer

### Digital Signature
- User enters:
  - Name
  - Email
- Draw signature using mouse
- Signature stored as image

### Status Tracking
- Documents categorized as:
  - Signed
  - Pending
  - PDF Files

### Download Feature
- Download signed documents
- Generate signed HTML/PDF output

---

## Working Principle

- The application starts with a home dashboard showing all documents.
- User can:
  - Add a new document
  - Import a PDF
- On selecting a document:
  - Content is displayed
  - User fills signer details
  - Signature is drawn using a canvas panel
- After signing:
  - Document status updates
  - Signature is embedded
  - User can download the signed document

---

## How to Run the Project

### Step 1: Compile the Program
```bash
javac DocumentSigner.java
