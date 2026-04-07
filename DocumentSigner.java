import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Base64;
import java.util.List;

/**
 * Digital Document Signing Application
 * Compile:  javac DocumentSigner.java
 * Run:      java DocumentSigner
 */
public class DocumentSigner extends JFrame {

    // ─── Color Palette ───────────────────────────────────────────────
    static final Color DARK       = new Color(26, 26, 46);
    static final Color DARK2      = new Color(42, 42, 69);
    static final Color GOLD       = new Color(200, 169, 110);
    static final Color BG         = new Color(244, 244, 240);
    static final Color WHITE      = Color.WHITE;
    static final Color BORDER_CLR = new Color(229, 229, 229);
    static final Color TEXT_MAIN  = new Color(26, 26, 46);
    static final Color TEXT_GREY  = new Color(136, 136, 136);
    static final Color GREEN      = new Color(34, 197, 94);
    static final Color GREEN_BG   = new Color(220, 252, 231);
    static final Color AMBER_BG   = new Color(254, 249, 195);
    static final Color AMBER_FG   = new Color(133, 77, 14);
    static final Color BLUE       = new Color(59, 130, 246);
    static final Color BLUE_BG    = new Color(219, 234, 254);
    static final Color BLUE_FG    = new Color(29, 78, 216);

    // ─── Document Data ────────────────────────────────────────────────
    static class Document {
        int id;
        String title, subtitle, content;
        boolean signed;
        String signerName, signerEmail, signedAt;
        BufferedImage signature;

        // PDF support fields
        String filePath;
        boolean isPDF;
        byte[] pdfBytes; // raw PDF bytes stored in memory

        Document(int id, String title, String subtitle, String content) {
            this.id = id;
            this.title = title;
            this.subtitle = subtitle;
            this.content = content;
            this.isPDF = false;
        }

        // Constructor for PDF documents
        Document(int id, String title, String filePath, byte[] pdfBytes) {
            this.id = id;
            this.title = title;
            this.subtitle = "PDF Document";
            this.content = "[This document is a PDF file. Click 'Open in Viewer' to view the full content.]";
            this.isPDF = true;
            this.filePath = filePath;
            this.pdfBytes = pdfBytes;
        }
    }

    List<Document> docs = new ArrayList<>();
    JPanel cardPanel;
    CardLayout cardLayout;
    JPanel sidebarPanel;

    public DocumentSigner() {
        initDocs();
        setTitle("SignFlow — Digital Document Signing");
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.add(buildHome(), "home");
        add(cardPanel);
        cardLayout.show(cardPanel, "home");
        setVisible(true);
    }

    void initDocs() {
        docs.add(new Document(1, "Non-Disclosure Agreement", "Confidentiality Contract",
            "This Non-Disclosure Agreement (\"Agreement\") is entered into as of the date signed below.\n\n" +
            "1. CONFIDENTIAL INFORMATION\n" +
            "Each party may disclose certain confidential and proprietary information. " +
            "\"Confidential Information\" means any information disclosed by one party to the other, " +
            "either directly or indirectly, in writing, orally, or by inspection of tangible objects.\n\n" +
            "2. OBLIGATIONS\n" +
            "Each party agrees to: (a) hold the other party's Confidential Information in strict confidence; " +
            "(b) not disclose the Confidential Information to any third parties; " +
            "(c) not use any Confidential Information for any purpose except to evaluate and engage in " +
            "discussions concerning a potential business relationship.\n\n" +
            "3. EXCLUSIONS\n" +
            "Obligations of confidentiality shall not apply to information that: " +
            "(a) is or becomes generally available to the public; " +
            "(b) was in the receiving party's possession prior to disclosure; " +
            "(c) is independently developed by the receiving party.\n\n" +
            "4. TERM\n" +
            "This Agreement shall remain in effect for a period of three (3) years from the date of execution.\n\n" +
            "5. GOVERNING LAW\n" +
            "This Agreement shall be governed by and construed in accordance with applicable law.\n\n" +
            "By signing below, the parties agree to the terms and conditions set forth in this Agreement."));

        docs.add(new Document(2, "Service Agreement", "Professional Services Contract",
            "This Service Agreement (\"Agreement\") is made between the Service Provider and the Client.\n\n" +
            "1. SCOPE OF SERVICES\n" +
            "Service Provider agrees to perform consulting, development, and implementation of software " +
            "solutions as outlined in any attached Statement of Work. Services shall be performed in a " +
            "professional and workmanlike manner.\n\n" +
            "2. COMPENSATION\n" +
            "Client agrees to pay Service Provider the agreed-upon fees. Payment is due within 30 days " +
            "of invoice. Late payments shall accrue interest at 1.5% per month.\n\n" +
            "3. INTELLECTUAL PROPERTY\n" +
            "All work product created by Service Provider shall be considered work-for-hire. Upon receipt " +
            "of full payment, all intellectual property rights shall transfer to Client.\n\n" +
            "4. CONFIDENTIALITY\n" +
            "Both parties acknowledge they may have access to confidential information and agree to keep " +
            "such information confidential and not disclose it to third parties.\n\n" +
            "5. LIMITATION OF LIABILITY\n" +
            "In no event shall either party be liable for indirect, incidental, special, or consequential damages.\n\n" +
            "6. TERMINATION\n" +
            "Either party may terminate this Agreement with 30 days written notice."));

        docs.add(new Document(3, "Employment Offer Letter", "Terms of Employment",
            "Dear Candidate,\n\n" +
            "We are pleased to offer you the position as outlined herein, subject to the terms and " +
            "conditions set forth in this letter.\n\n" +
            "POSITION & DUTIES\n" +
            "Your title and responsibilities will be as described in the attached job description. " +
            "You will report to your designated manager and are expected to perform your duties to " +
            "the best of your abilities.\n\n" +
            "COMPENSATION\n" +
            "Your base salary will be as agreed upon, paid bi-weekly. You will be eligible for " +
            "performance bonuses as outlined in the company's bonus policy.\n\n" +
            "BENEFITS\n" +
            "You will be entitled to participate in the company's benefit programs, including health " +
            "insurance, retirement plan, and paid time off, subject to eligibility requirements.\n\n" +
            "AT-WILL EMPLOYMENT\n" +
            "Your employment is at-will, meaning either you or the company may terminate the " +
            "employment relationship at any time, with or without cause.\n\n" +
            "Please indicate your acceptance by signing and returning this letter.\n\n" +
            "Sincerely,\nHuman Resources Department"));
    }

    // ═══════════════════════════════════════════════════════
    //  HOME SCREEN
    // ═══════════════════════════════════════════════════════
    JPanel buildHome() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildDocumentList(), BorderLayout.CENTER);
        return root;
    }

    JPanel buildSidebar() {
        sidebarPanel = new JPanel();
        JPanel side = sidebarPanel;
        side.setBackground(DARK);
        side.setPreferredSize(new Dimension(210, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(28, 0, 28, 0));

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        logoPanel.setBackground(DARK);
        logoPanel.setMaximumSize(new Dimension(210, 60));
        JLabel mark = new JLabel("✦");
        mark.setFont(new Font("Serif", Font.BOLD, 22));
        mark.setForeground(GOLD);
        JPanel logoText = new JPanel();
        logoText.setBackground(DARK);
        logoText.setLayout(new BoxLayout(logoText, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("SignFlow");
        name.setFont(new Font("Serif", Font.BOLD, 16));
        name.setForeground(WHITE);
        JLabel tag = new JLabel("DIGITAL SIGNING");
        tag.setFont(new Font("SansSerif", Font.PLAIN, 9));
        tag.setForeground(TEXT_GREY);
        logoText.add(name); logoText.add(tag);
        logoPanel.add(mark); logoPanel.add(logoText);
        side.add(logoPanel);

        JSeparator sep = new JSeparator();
        sep.setForeground(DARK2);
        sep.setMaximumSize(new Dimension(210, 2));
        side.add(Box.createVerticalStrut(18));
        side.add(sep);
        side.add(Box.createVerticalStrut(18));

        // Nav items
        String[][] navItems = {{"📄", "Documents"}, {"✓", "Completed"}, {"📋", "Templates"}};
        for (int i = 0; i < navItems.length; i++) {
            JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 8));
            nav.setMaximumSize(new Dimension(210, 40));
            nav.setBackground(i == 0 ? DARK2 : DARK);
            if (i == 0) nav.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, GOLD));
            JLabel lbl = new JLabel(navItems[i][0] + "  " + navItems[i][1]);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setForeground(i == 0 ? GOLD : TEXT_GREY);
            nav.add(lbl);
            side.add(nav);
        }

        side.add(Box.createVerticalGlue());

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(DARK2);
        sep2.setMaximumSize(new Dimension(210, 2));
        side.add(sep2);
        side.add(Box.createVerticalStrut(16));

        long signedCount = docs.stream().filter(d -> d.signed).count();
        long pdfCount    = docs.stream().filter(d -> d.isPDF).count();
        String[][] stats = {
            {String.valueOf(docs.size()), "Total Docs"},
            {String.valueOf(signedCount), "Signed"},
            {String.valueOf(docs.size() - signedCount), "Pending"},
            {String.valueOf(pdfCount), "PDF Files"}
        };
        Color[] statColors = {WHITE, GREEN, new Color(245, 158, 11), BLUE};
        for (int i = 0; i < stats.length; i++) {
            JPanel sc = new JPanel(new BorderLayout());
            sc.setBackground(DARK2);
            sc.setBorder(new EmptyBorder(8, 14, 8, 14));
            sc.setMaximumSize(new Dimension(182, 44));
            JLabel num = new JLabel(stats[i][0]);
            num.setFont(new Font("Serif", Font.BOLD, 20));
            num.setForeground(statColors[i]);
            JLabel lbl2 = new JLabel(stats[i][1].toUpperCase());
            lbl2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            lbl2.setForeground(TEXT_GREY);
            sc.add(num, BorderLayout.WEST);
            sc.add(lbl2, BorderLayout.EAST);

            JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
            wrap.setBackground(DARK);
            wrap.setMaximumSize(new Dimension(210, 56));
            wrap.add(sc);
            side.add(wrap);
        }
        return side;
    }

    JPanel docListPanel;

    JPanel buildDocumentList() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG);
        topBar.setBorder(new EmptyBorder(28, 36, 16, 36));
        JLabel title = new JLabel("Documents");
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(TEXT_MAIN);
        JLabel sub = new JLabel("Manage and sign your documents");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(TEXT_GREY);
        JPanel titles = new JPanel();
        titles.setBackground(BG);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.add(title); titles.add(sub);
        topBar.add(titles, BorderLayout.WEST);

        // Buttons: Add Document + Import PDF
        JButton addBtn = roundButton("+ Add Document", DARK, DARK);
        addBtn.setForeground(WHITE);
        addBtn.addActionListener(e -> showAddDocumentDialog());

        JButton pdfBtn = roundButton("📎 Import PDF", BLUE, BLUE_BG);
        pdfBtn.setForeground(BLUE_FG);
        pdfBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BLUE, 1, true),
            new EmptyBorder(9, 18, 9, 18)));
        pdfBtn.addActionListener(e -> importPDFDocument());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setBackground(BG);
        rightPanel.add(pdfBtn);
        rightPanel.add(addBtn);

        topBar.add(rightPanel, BorderLayout.EAST);
        outer.add(topBar, BorderLayout.NORTH);

        // Cards
        docListPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        docListPanel.setBackground(BG);
        docListPanel.setBorder(new EmptyBorder(0, 36, 36, 36));
        refreshDocCards();

        JScrollPane scroll = new JScrollPane(docListPanel);
        scroll.setBorder(null);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        outer.add(scroll, BorderLayout.CENTER);
        return outer;
    }

    void refreshDocCards() {
        docListPanel.removeAll();
        for (Document doc : docs) {
            docListPanel.add(buildDocCard(doc));
        }
        docListPanel.revalidate();
        docListPanel.repaint();
    }

    JPanel buildDocCard(Document doc) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = doc.signed
                    ? new Color(240, 253, 244)
                    : (doc.isPDF ? new Color(239, 246, 255) : WHITE);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                Color border = doc.signed
                    ? new Color(187, 247, 208)
                    : (doc.isPDF ? new Color(191, 219, 254) : BORDER_CLR);
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Top row
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        // Icon: PDF gets a red PDF icon, others get document icon
        JLabel icon = new JLabel(doc.isPDF ? "📕" : "📄");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 28));

        JLabel badge;
        if (doc.signed) {
            badge = new JLabel("✓ Signed");
            badge.setForeground(new Color(21, 128, 61));
            badge.setBackground(GREEN_BG);
        } else if (doc.isPDF) {
            badge = new JLabel("PDF");
            badge.setForeground(BLUE_FG);
            badge.setBackground(BLUE_BG);
        } else {
            badge = new JLabel("Pending");
            badge.setForeground(AMBER_FG);
            badge.setBackground(AMBER_BG);
        }
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(3, 10, 3, 10));
        topRow.add(icon, BorderLayout.WEST);
        topRow.add(badge, BorderLayout.EAST);
        card.add(topRow);
        card.add(Box.createVerticalStrut(12));

        JLabel titleLbl = new JLabel("<html><b>" + doc.title + "</b></html>");
        titleLbl.setFont(new Font("Serif", Font.BOLD, 15));
        titleLbl.setForeground(TEXT_MAIN);
        titleLbl.setAlignmentX(0);
        card.add(titleLbl);

        JLabel subLbl = new JLabel(doc.subtitle);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subLbl.setForeground(TEXT_GREY);
        subLbl.setAlignmentX(0);
        card.add(subLbl);
        card.add(Box.createVerticalStrut(8));

        // Show file size for PDFs
        if (doc.isPDF && doc.pdfBytes != null) {
            String size = formatFileSize(doc.pdfBytes.length);
            JLabel sizeLbl = new JLabel("📦 " + size);
            sizeLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
            sizeLbl.setForeground(TEXT_GREY);
            sizeLbl.setAlignmentX(0);
            card.add(sizeLbl);
            card.add(Box.createVerticalStrut(4));
        }

        if (doc.signed && doc.signerName != null) {
            JLabel sigLbl = new JLabel("● Signed by " + doc.signerName);
            sigLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
            sigLbl.setForeground(new Color(21, 128, 61));
            sigLbl.setAlignmentX(0);
            card.add(sigLbl);
            card.add(Box.createVerticalStrut(6));
        }

        card.add(Box.createVerticalGlue());

        // Button
        String btnText = doc.signed ? "View Signed Document"
                       : (doc.isPDF ? "Review PDF & Sign →" : "Review & Sign →");
        Color btnBg   = doc.signed ? GREEN_BG : (doc.isPDF ? BLUE : DARK);
        Color btnFg   = doc.signed ? new Color(21, 128, 61) : WHITE;

        JButton btn = roundButton(btnText, btnBg, btnBg);
        btn.setForeground(btnFg);
        btn.setAlignmentX(0);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.addActionListener(e -> openDocument(doc));
        card.add(btn);
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { openDocument(doc); }
        });
        return card;
    }

    // ═══════════════════════════════════════════════════════
    //  PDF IMPORT
    // ═══════════════════════════════════════════════════════

    /**
     * Imports a PDF file from disk.
     * Reads raw bytes into memory (no library needed for storage).
     * Display is handled via Desktop.open() or a built-in preview panel.
     */
    void importPDFDocument() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select PDF File");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files (*.pdf)", "pdf"));
        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();

            // Validate it is actually a PDF by checking magic bytes
            if (!isValidPDF(selectedFile)) {
                JOptionPane.showMessageDialog(this,
                    "The selected file does not appear to be a valid PDF.\nPlease select a proper .pdf file.",
                    "Invalid PDF", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                byte[] pdfBytes = Files.readAllBytes(selectedFile.toPath());
                String fileName = selectedFile.getName();
                // Strip .pdf extension for title
                String docTitle = fileName.endsWith(".pdf")
                    ? fileName.substring(0, fileName.length() - 4)
                    : fileName;

                int newId = docs.size() + 1;
                Document pdfDoc = new Document(newId, docTitle, selectedFile.getAbsolutePath(), pdfBytes);
                docs.add(pdfDoc);

                refreshSidebar();
                showToast("PDF imported: " + docTitle);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Failed to read PDF file:\n" + ex.getMessage(),
                    "Import Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Checks PDF magic number (%PDF-) without any external library.
     * The first 5 bytes of every valid PDF file are: 0x25 0x50 0x44 0x46 0x2D (%PDF-)
     */
    boolean isValidPDF(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[5];
            if (fis.read(header) == 5) {
                return header[0] == 0x25 && header[1] == 0x50 &&
                       header[2] == 0x44 && header[3] == 0x46 &&
                       header[4] == 0x2D;
            }
        } catch (IOException ignored) {}
        return false;
    }

    // ═══════════════════════════════════════════════════════
    //  DOCUMENT VIEWER
    // ═══════════════════════════════════════════════════════
    void openDocument(Document doc) {
        JPanel viewer = doc.isPDF ? buildPDFViewer(doc) : buildViewer(doc);
        cardPanel.add(viewer, "viewer_" + doc.id);
        cardLayout.show(cardPanel, "viewer_" + doc.id);
    }

    // ─── PDF Viewer ───────────────────────────────────────────────────

    /**
     * PDF viewer panel.
     * Since Java has no built-in PDF renderer, we:
     *   1. Show PDF metadata extracted from raw bytes (title, page count hint, file size).
     *   2. Offer "Open in System Viewer" via Desktop.open() to let the OS render it.
     *   3. Allow signing (draw signature + store certificate).
     *   4. On download, save the original PDF bytes + a signing certificate text file.
     */
    JPanel buildPDFViewer(Document doc) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK);
        header.setBorder(new EmptyBorder(16, 28, 16, 28));

        JButton back = roundButton("← Back", new Color(100,100,120), DARK2);
        back.setForeground(new Color(180, 180, 200));
        back.addActionListener(e -> {
            cardLayout.show(cardPanel, "home");
            refreshDocCards();
        });
        JPanel hLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        hLeft.setBackground(DARK);
        hLeft.add(back);

        JPanel hCenter = new JPanel();
        hCenter.setBackground(DARK);
        hCenter.setLayout(new BoxLayout(hCenter, BoxLayout.Y_AXIS));
        JLabel ht = new JLabel(doc.title);
        ht.setFont(new Font("Serif", Font.BOLD, 17));
        ht.setForeground(WHITE);
        JLabel hs = new JLabel("PDF Document · " + formatFileSize(doc.pdfBytes.length));
        hs.setFont(new Font("SansSerif", Font.PLAIN, 12));
        hs.setForeground(TEXT_GREY);
        hCenter.add(ht); hCenter.add(hs);

        header.add(hLeft, BorderLayout.WEST);
        header.add(hCenter, BorderLayout.CENTER);

        if (doc.signed) {
            JButton dl = roundButton("↓ Download", GOLD, GOLD);
            dl.setForeground(DARK);
            dl.addActionListener(e -> downloadPDFDoc(doc));
            JPanel hRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            hRight.setBackground(DARK);
            hRight.add(dl);
            header.add(hRight, BorderLayout.EAST);
        }
        root.add(header, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel(new BorderLayout(24, 0));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(28, 32, 28, 32));

        // PDF preview sheet
        JPanel sheet = buildPDFPreviewSheet(doc);
        JScrollPane sheetScroll = new JScrollPane(sheet);
        sheetScroll.setBorder(null);
        sheetScroll.setOpaque(false);
        sheetScroll.getViewport().setOpaque(false);
        body.add(sheetScroll, BorderLayout.CENTER);

        // Side panel (signing form or signed info)
        JPanel side = buildSidePanel(doc, root, sheet, sheetScroll);
        side.setPreferredSize(new Dimension(260, 0));
        body.add(side, BorderLayout.EAST);

        root.add(body, BorderLayout.CENTER);
        return root;
    }

    /**
     * Builds a visually rich PDF preview panel.
     * Extracts metadata from raw bytes without any library:
     *   - Detects PDF version from header
     *   - Counts approximate page number from "/Page " token occurrences
     *   - Shows file size
     */
    JPanel buildPDFPreviewSheet(Document doc) {
        JPanel sheet = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(BORDER_CLR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                // Watermark
                g2.setFont(new Font("Serif", Font.BOLD, 70));
                g2.setColor(new Color(0, 0, 0, 18));
                g2.rotate(Math.toRadians(-35), getWidth()/2.0, getHeight()/2.0);
                FontMetrics fm = g2.getFontMetrics();
                String wm = doc.signed ? "SIGNED" : "PDF";
                int wmW = fm.stringWidth(wm);
                g2.drawString(wm, (getWidth()-wmW)/2, getHeight()/2);
                g2.rotate(Math.toRadians(35), getWidth()/2.0, getHeight()/2.0);
            }
        };
        sheet.setOpaque(false);
        sheet.setLayout(new BoxLayout(sheet, BoxLayout.Y_AXIS));
        sheet.setBorder(new EmptyBorder(48, 52, 48, 52));

        // Title row with PDF icon
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titleRow.setOpaque(false);
        JLabel pdfIcon = new JLabel("📕");
        pdfIcon.setFont(new Font("SansSerif", Font.PLAIN, 36));
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        JLabel docTitleLbl = new JLabel(doc.title);
        docTitleLbl.setFont(new Font("Serif", Font.BOLD, 22));
        docTitleLbl.setForeground(TEXT_MAIN);
        JLabel docSubLbl = new JLabel("PDF Document");
        docSubLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        docSubLbl.setForeground(TEXT_GREY);
        titleBlock.add(docTitleLbl);
        titleBlock.add(docSubLbl);
        titleRow.add(pdfIcon);
        titleRow.add(titleBlock);
        sheet.add(titleRow);
        sheet.add(Box.createVerticalStrut(20));

        JSeparator divider = new JSeparator();
        divider.setForeground(BORDER_CLR);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        sheet.add(divider);
        sheet.add(Box.createVerticalStrut(20));

        // ── PDF Metadata (extracted from raw bytes, no library) ──
        String pdfVersion  = extractPDFVersion(doc.pdfBytes);
        int    pageCount   = estimatePageCount(doc.pdfBytes);
        String fileSize    = formatFileSize(doc.pdfBytes.length);

        JLabel metaTitle = new JLabel("Document Information");
        metaTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        metaTitle.setForeground(TEXT_MAIN);
        metaTitle.setAlignmentX(0);
        sheet.add(metaTitle);
        sheet.add(Box.createVerticalStrut(12));

        String[][] meta = {
            {"File Name",    doc.title + ".pdf"},
            {"PDF Version",  pdfVersion},
            {"Estimated Pages", pageCount > 0 ? String.valueOf(pageCount) : "—"},
            {"File Size",    fileSize},
            {"Status",       doc.signed ? "Signed" : "Awaiting Signature"}
        };

        for (String[] row : meta) {
            JPanel mr = new JPanel(new BorderLayout());
            mr.setOpaque(false);
            mr.setBorder(new EmptyBorder(6, 0, 6, 0));
            mr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            JLabel k = new JLabel(row[0]);
            k.setFont(new Font("SansSerif", Font.PLAIN, 12));
            k.setForeground(TEXT_GREY);
            JLabel v = new JLabel(row[1]);
            v.setFont(new Font("SansSerif", Font.BOLD, 12));
            boolean isStatus = row[0].equals("Status");
            v.setForeground(isStatus ? (doc.signed ? GREEN : AMBER_FG) : TEXT_MAIN);
            mr.add(k, BorderLayout.WEST);
            mr.add(v, BorderLayout.EAST);
            sheet.add(mr);
            JSeparator s = new JSeparator();
            s.setForeground(BORDER_CLR);
            s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            sheet.add(s);
        }

        sheet.add(Box.createVerticalStrut(24));

        // ── "Open in System Viewer" button ──
        JPanel openRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        openRow.setOpaque(false);
        JButton openBtn = roundButton("🔍 Open PDF in System Viewer", BLUE, BLUE_BG);
        openBtn.setForeground(BLUE_FG);
        openBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(147, 197, 253), 1, true),
            new EmptyBorder(10, 18, 10, 18)));
        openBtn.addActionListener(e -> openPDFInSystemViewer(doc));
        openRow.add(openBtn);
        openRow.setAlignmentX(0);
        sheet.add(openRow);

        sheet.add(Box.createVerticalStrut(16));

        // ── Info note ──
        JPanel infoBox = new JPanel();
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setBackground(new Color(239, 246, 255));
        infoBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(191, 219, 254), 1, true),
            new EmptyBorder(12, 14, 12, 14)));
        infoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        JLabel infoTitle = new JLabel("ℹ  About PDF Preview");
        infoTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        infoTitle.setForeground(BLUE_FG);
        JLabel infoText = new JLabel("<html>PDF rendering requires your system viewer.<br>" +
            "You can still sign this document — a signing certificate<br>will be saved alongside your PDF.</html>");
        infoText.setFont(new Font("SansSerif", Font.PLAIN, 11));
        infoText.setForeground(new Color(30, 64, 175));
        infoBox.add(infoTitle);
        infoBox.add(Box.createVerticalStrut(4));
        infoBox.add(infoText);
        infoBox.setAlignmentX(0);
        sheet.add(infoBox);

        // ── Signature block (if signed) ──
        if (doc.signed) {
            sheet.add(Box.createVerticalStrut(28));
            JSeparator sigDiv = new JSeparator();
            sigDiv.setForeground(TEXT_MAIN);
            sigDiv.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
            sheet.add(sigDiv);
            sheet.add(Box.createVerticalStrut(10));
            if (doc.signature != null) {
                JLabel sigImg = new JLabel(new ImageIcon(doc.signature.getScaledInstance(220, 70, Image.SCALE_SMOOTH)));
                sigImg.setAlignmentX(0);
                sheet.add(sigImg);
            }
            JLabel sigMeta = new JLabel(doc.signerName + "  ·  " + doc.signerEmail + "  ·  " + doc.signedAt);
            sigMeta.setFont(new Font("SansSerif", Font.PLAIN, 11));
            sigMeta.setForeground(TEXT_GREY);
            sigMeta.setAlignmentX(0);
            sheet.add(sigMeta);
            sheet.add(Box.createVerticalStrut(8));
            JLabel stamp = new JLabel("  ✓  Digitally Signed  ");
            stamp.setFont(new Font("SansSerif", Font.BOLD, 12));
            stamp.setForeground(new Color(21, 128, 61));
            stamp.setBackground(GREEN_BG);
            stamp.setOpaque(true);
            stamp.setBorder(new EmptyBorder(4, 10, 4, 10));
            stamp.setAlignmentX(0);
            sheet.add(stamp);
        }

        return sheet;
    }

    /**
     * Opens the PDF file in the OS default viewer using Desktop.open().
     * If the original file path is available and still exists, opens that.
     * Otherwise writes a temp file from the stored bytes and opens it.
     */
    void openPDFInSystemViewer(Document doc) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            JOptionPane.showMessageDialog(this,
                "System viewer not supported on this platform.",
                "Not Supported", JOptionPane.WARNING_MESSAGE);
            return;
        }
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        if (!desktop.isSupported(java.awt.Desktop.Action.OPEN)) {
            JOptionPane.showMessageDialog(this,
                "Opening files is not supported on this platform.",
                "Not Supported", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            File pdfFile = null;

            // Try original file path first
            if (doc.filePath != null) {
                File orig = new File(doc.filePath);
                if (orig.exists() && orig.canRead()) {
                    pdfFile = orig;
                }
            }

            // Fall back to writing bytes to a temp file
            if (pdfFile == null && doc.pdfBytes != null) {
                pdfFile = File.createTempFile("signflow_" + doc.id + "_", ".pdf");
                pdfFile.deleteOnExit();
                Files.write(pdfFile.toPath(), doc.pdfBytes);
            }

            if (pdfFile != null) {
                desktop.open(pdfFile);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Could not locate PDF data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Failed to open PDF:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─── Plain-text Viewer (unchanged) ───────────────────────────────
    JPanel buildViewer(Document doc) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK);
        header.setBorder(new EmptyBorder(16, 28, 16, 28));
        JButton back = roundButton("← Back", new Color(100,100,120), DARK2);
        back.setForeground(new Color(180, 180, 200));
        back.addActionListener(e -> {
            cardLayout.show(cardPanel, "home");
            refreshDocCards();
        });
        JPanel hLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        hLeft.setBackground(DARK);
        hLeft.add(back);
        JPanel hCenter = new JPanel();
        hCenter.setBackground(DARK);
        hCenter.setLayout(new BoxLayout(hCenter, BoxLayout.Y_AXIS));
        JLabel ht = new JLabel(doc.title);
        ht.setFont(new Font("Serif", Font.BOLD, 17));
        ht.setForeground(WHITE);
        JLabel hs = new JLabel(doc.subtitle);
        hs.setFont(new Font("SansSerif", Font.PLAIN, 12));
        hs.setForeground(TEXT_GREY);
        hCenter.add(ht); hCenter.add(hs);
        header.add(hLeft, BorderLayout.WEST);
        header.add(hCenter, BorderLayout.CENTER);

        if (doc.signed) {
            JButton dl = roundButton("↓ Download", GOLD, GOLD);
            dl.setForeground(DARK);
            dl.addActionListener(e -> downloadDoc(doc));
            JPanel hRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            hRight.setBackground(DARK);
            hRight.add(dl);
            header.add(hRight, BorderLayout.EAST);
        }
        root.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(24, 0));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(28, 32, 28, 32));

        JPanel sheet = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(BORDER_CLR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.setFont(new Font("Serif", Font.BOLD, 70));
                g2.setColor(new Color(0, 0, 0, 18));
                g2.rotate(Math.toRadians(-35), getWidth()/2.0, getHeight()/2.0);
                FontMetrics fm = g2.getFontMetrics();
                String wm = doc.signed ? "SIGNED" : "DRAFT";
                int wmW = fm.stringWidth(wm);
                g2.drawString(wm, (getWidth()-wmW)/2, getHeight()/2);
                g2.rotate(Math.toRadians(35), getWidth()/2.0, getHeight()/2.0);
            }
        };
        sheet.setOpaque(false);
        sheet.setLayout(new BoxLayout(sheet, BoxLayout.Y_AXIS));
        sheet.setBorder(new EmptyBorder(48, 52, 48, 52));

        JLabel docTitle = new JLabel(doc.title);
        docTitle.setFont(new Font("Serif", Font.BOLD, 22));
        docTitle.setForeground(TEXT_MAIN);
        docTitle.setAlignmentX(0);
        sheet.add(docTitle);

        JLabel docSub = new JLabel(doc.subtitle);
        docSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        docSub.setForeground(TEXT_GREY);
        docSub.setAlignmentX(0);
        sheet.add(Box.createVerticalStrut(4));
        sheet.add(docSub);
        sheet.add(Box.createVerticalStrut(16));

        JSeparator divider = new JSeparator();
        divider.setForeground(BORDER_CLR);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        sheet.add(divider);
        sheet.add(Box.createVerticalStrut(16));

        JTextArea content = new JTextArea(doc.content);
        content.setFont(new Font("Serif", Font.PLAIN, 13));
        content.setForeground(new Color(68, 68, 68));
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setEditable(false);
        content.setOpaque(false);
        content.setAlignmentX(0);
        sheet.add(content);

        if (doc.signed) {
            sheet.add(Box.createVerticalStrut(28));
            JSeparator sigDiv = new JSeparator();
            sigDiv.setForeground(TEXT_MAIN);
            sigDiv.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
            sheet.add(sigDiv);
            sheet.add(Box.createVerticalStrut(10));
            if (doc.signature != null) {
                JLabel sigImg = new JLabel(new ImageIcon(doc.signature.getScaledInstance(220, 70, Image.SCALE_SMOOTH)));
                sigImg.setAlignmentX(0);
                sheet.add(sigImg);
            }
            JLabel sigMeta = new JLabel(doc.signerName + "  ·  " + doc.signerEmail + "  ·  " + doc.signedAt);
            sigMeta.setFont(new Font("SansSerif", Font.PLAIN, 11));
            sigMeta.setForeground(TEXT_GREY);
            sigMeta.setAlignmentX(0);
            sheet.add(sigMeta);
            sheet.add(Box.createVerticalStrut(8));
            JLabel stamp = new JLabel("  ✓  Digitally Signed  ");
            stamp.setFont(new Font("SansSerif", Font.BOLD, 12));
            stamp.setForeground(new Color(21, 128, 61));
            stamp.setBackground(GREEN_BG);
            stamp.setOpaque(true);
            stamp.setBorder(new EmptyBorder(4, 10, 4, 10));
            stamp.setAlignmentX(0);
            sheet.add(stamp);
        }

        JScrollPane sheetScroll = new JScrollPane(sheet);
        sheetScroll.setBorder(null);
        sheetScroll.setOpaque(false);
        sheetScroll.getViewport().setOpaque(false);
        body.add(sheetScroll, BorderLayout.CENTER);

        JPanel side = buildSidePanel(doc, root, sheet, sheetScroll);
        side.setPreferredSize(new Dimension(260, 0));
        body.add(side, BorderLayout.EAST);

        root.add(body, BorderLayout.CENTER);
        return root;
    }

    JPanel buildSidePanel(Document doc, JPanel root, JPanel sheet, JScrollPane scroll) {
        JPanel side = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(BORDER_CLR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
            }
        };
        side.setOpaque(false);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(24, 20, 24, 20));

        if (doc.signed) {
            buildSignedPanel(side, doc);
        } else {
            buildSigningForm(side, doc, root, sheet, scroll);
        }
        return side;
    }

    void buildSignedPanel(JPanel side, Document doc) {
        JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconWrap.setOpaque(false);
        JLabel checkIcon = new JLabel("✓") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GREEN_BG);
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        checkIcon.setFont(new Font("SansSerif", Font.BOLD, 22));
        checkIcon.setForeground(GREEN);
        checkIcon.setHorizontalAlignment(SwingConstants.CENTER);
        checkIcon.setPreferredSize(new Dimension(52, 52));
        iconWrap.add(checkIcon);
        side.add(iconWrap);
        side.add(Box.createVerticalStrut(10));

        JLabel t = new JLabel("Document Signed!");
        t.setFont(new Font("Serif", Font.BOLD, 16));
        t.setForeground(TEXT_MAIN);
        t.setAlignmentX(0.5f);
        side.add(t);
        side.add(Box.createVerticalStrut(6));
        JLabel s = new JLabel("<html><center>Successfully signed and<br>legally binding.</center></html>");
        s.setFont(new Font("SansSerif", Font.PLAIN, 12));
        s.setForeground(TEXT_GREY);
        s.setAlignmentX(0.5f);
        side.add(s);
        side.add(Box.createVerticalStrut(20));

        String[][] meta = {{"Signer", doc.signerName}, {"Email", doc.signerEmail}, {"Date", doc.signedAt}, {"Status", "Completed"}};
        for (String[] row : meta) {
            JPanel mr = new JPanel(new BorderLayout());
            mr.setOpaque(false);
            mr.setBorder(new EmptyBorder(0, 0, 8, 0));
            JLabel k = new JLabel(row[0]);
            k.setFont(new Font("SansSerif", Font.PLAIN, 12));
            k.setForeground(TEXT_GREY);
            JLabel v = new JLabel(row[1]);
            v.setFont(new Font("SansSerif", Font.BOLD, 12));
            v.setForeground(row[0].equals("Status") ? GREEN : TEXT_MAIN);
            mr.add(k, BorderLayout.WEST);
            mr.add(v, BorderLayout.EAST);
            side.add(mr);
            JSeparator sep = new JSeparator();
            sep.setForeground(BORDER_CLR);
            sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            side.add(sep);
            side.add(Box.createVerticalStrut(4));
        }

        side.add(Box.createVerticalStrut(12));
        JButton dlBtn = roundButton("↓ Download Signed Copy", DARK, DARK);
        dlBtn.setForeground(WHITE);
        dlBtn.setAlignmentX(0);
        dlBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        dlBtn.addActionListener(e -> {
            if (doc.isPDF) downloadPDFDoc(doc);
            else           downloadDoc(doc);
        });
        side.add(dlBtn);
    }

    void buildSigningForm(JPanel side, Document doc, JPanel rootPanel, JPanel sheet, JScrollPane scroll) {
        JLabel t = new JLabel("Signer Information");
        t.setFont(new Font("Serif", Font.BOLD, 16));
        t.setForeground(TEXT_MAIN);
        t.setAlignmentX(0);
        side.add(t);
        side.add(Box.createVerticalStrut(4));
        JLabel s = new JLabel("Fill in your details to sign");
        s.setFont(new Font("SansSerif", Font.PLAIN, 12));
        s.setForeground(TEXT_GREY);
        s.setAlignmentX(0);
        side.add(s);
        side.add(Box.createVerticalStrut(18));

        JTextField nameField  = formField(side, "FULL NAME", "Your legal name");
        JTextField emailField = formField(side, "EMAIL ADDRESS", "your@email.com");

        side.add(Box.createVerticalStrut(16));
        JButton signBtn = roundButton("Continue to Sign →", DARK, DARK);
        signBtn.setForeground(WHITE);
        signBtn.setAlignmentX(0);
        signBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        signBtn.addActionListener(e -> {
            String name  = nameField.getText().trim();
            String email = emailField.getText().trim();
            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            showSignatureDialog(doc, name, email, rootPanel);
        });
        side.add(signBtn);
    }

    JTextField formField(JPanel parent, String label, String placeholder) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(TEXT_GREY);
        lbl.setAlignmentX(0);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(5));
        JTextField field = new JTextField(placeholder) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() || getText().equals(placeholder)) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(180, 180, 180));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    Insets ins = getInsets();
                    g2.drawString(placeholder, ins.left + 2, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
                }
            }
        };
        field.setText("");
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(8, 10, 8, 10)));
        field.setAlignmentX(0);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        parent.add(field);
        parent.add(Box.createVerticalStrut(12));
        return field;
    }

    // ═══════════════════════════════════════════════════════
    //  SIGNATURE DIALOG
    // ═══════════════════════════════════════════════════════
    void showSignatureDialog(Document doc, String name, String email, JPanel rootPanel) {
        JDialog dlg = new JDialog(this, "Draw Your Signature", true);
        dlg.setSize(540, 320);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(WHITE);

        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(DARK);
        hdr.setBorder(new EmptyBorder(16, 22, 16, 22));
        JLabel ht = new JLabel("Draw Your Signature");
        ht.setFont(new Font("Serif", Font.BOLD, 16));
        ht.setForeground(WHITE);
        hdr.add(ht, BorderLayout.WEST);
        dlg.add(hdr, BorderLayout.NORTH);

        BufferedImage sigImg = new BufferedImage(480, 160, BufferedImage.TYPE_INT_RGB);
        Graphics2D sg = sigImg.createGraphics();
        sg.setColor(WHITE);
        sg.fillRect(0, 0, 480, 160);
        sg.dispose();

        JPanel canvasWrap = new JPanel(new BorderLayout());
        canvasWrap.setBackground(WHITE);
        canvasWrap.setBorder(new EmptyBorder(16, 22, 0, 22));

        JPanel canvas = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(sigImg, 0, 0, getWidth(), getHeight(), null);
                if (isEmpty(sigImg)) {
                    g.setColor(new Color(200, 200, 200));
                    g.setFont(new Font("Serif", Font.ITALIC, 18));
                    FontMetrics fm = g.getFontMetrics();
                    String hint = "Sign here...";
                    g.drawString(hint, (getWidth() - fm.stringWidth(hint)) / 2, getHeight() / 2);
                }
            }
        };
        canvas.setPreferredSize(new Dimension(480, 160));
        canvas.setBorder(BorderFactory.createDashedBorder(new Color(200, 200, 200), 4f, 4f, 4f, false));
        canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        int[] lastPt = {-1, -1};
        canvas.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e)  { lastPt[0] = e.getX(); lastPt[1] = e.getY(); }
            public void mouseReleased(MouseEvent e) { lastPt[0] = -1; lastPt[1] = -1; }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (lastPt[0] < 0) return;
                double scaleX = sigImg.getWidth()  / (double) canvas.getWidth();
                double scaleY = sigImg.getHeight() / (double) canvas.getHeight();
                Graphics2D g2 = sigImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DARK);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine((int)(lastPt[0]*scaleX), (int)(lastPt[1]*scaleY),
                            (int)(e.getX()*scaleX),  (int)(e.getY()*scaleY));
                g2.dispose();
                lastPt[0] = e.getX(); lastPt[1] = e.getY();
                canvas.repaint();
            }
        });
        canvasWrap.add(canvas);
        dlg.add(canvasWrap, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(WHITE);
        footer.setBorder(new EmptyBorder(12, 22, 16, 22));

        JButton clearBtn = roundButton("Clear", new Color(239, 68, 68), new Color(255, 245, 245));
        clearBtn.setForeground(new Color(239, 68, 68));
        clearBtn.setBorder(BorderFactory.createLineBorder(new Color(254, 202, 202), 1, true));
        clearBtn.addActionListener(e -> {
            Graphics2D g2 = sigImg.createGraphics();
            g2.setColor(WHITE);
            g2.fillRect(0, 0, sigImg.getWidth(), sigImg.getHeight());
            g2.dispose();
            canvas.repaint();
        });

        JButton cancelBtn = roundButton("Cancel", TEXT_GREY, new Color(244, 244, 240));
        cancelBtn.setForeground(TEXT_GREY);
        cancelBtn.addActionListener(e -> dlg.dispose());

        JButton applyBtn = roundButton("Apply Signature", DARK, DARK);
        applyBtn.setForeground(WHITE);
        applyBtn.addActionListener(e -> {
            if (isEmpty(sigImg)) {
                JOptionPane.showMessageDialog(dlg, "Please draw your signature first.", "No Signature", JOptionPane.WARNING_MESSAGE);
                return;
            }
            doc.signed      = true;
            doc.signerName  = name;
            doc.signerEmail = email;
            doc.signedAt    = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(new Date());
            doc.signature   = sigImg;
            refreshSidebar();
            dlg.dispose();
            JPanel newViewer = doc.isPDF ? buildPDFViewer(doc) : buildViewer(doc);
            cardPanel.add(newViewer, "viewer_" + doc.id);
            cardLayout.show(cardPanel, "viewer_" + doc.id);
            showToast("Document signed successfully!");
        });

        JPanel btnRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRight.setBackground(WHITE);
        btnRight.add(cancelBtn); btnRight.add(applyBtn);
        footer.add(clearBtn, BorderLayout.WEST);
        footer.add(btnRight, BorderLayout.EAST);
        dlg.add(footer, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    boolean isEmpty(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++)
            for (int y = 0; y < img.getHeight(); y++)
                if (img.getRGB(x, y) != Color.WHITE.getRGB()) return false;
        return true;
    }

    // ═══════════════════════════════════════════════════════
    //  DOWNLOAD
    // ═══════════════════════════════════════════════════════

    /**
     * Converts a BufferedImage to a Base64-encoded PNG data URI.
     * Used to embed the signature image directly into HTML — no external files needed.
     */
    String signatureToBase64(BufferedImage img) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(img, "PNG", baos);
            String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
            return "data:image/png;base64," + base64;
        } catch (IOException ex) {
            return "";
        }
    }

    /**
     * Builds a signed HTML for plain-text documents.
     * Layout: document content on top, signature block below.
     */
    String buildSignedHTML(Document doc, String bodyContent) {
        String sigDataURI = (doc.signature != null) ? signatureToBase64(doc.signature) : "";
        String sigBlock = buildSigBlockHTML(doc, sigDataURI);

        return "<!DOCTYPE html>\n<html lang='en'>\n<head>\n" +
            "<meta charset='UTF-8'/>\n" +
            "<title>" + escapeHTML(doc.title) + " — Signed</title>\n" +
            "<style>\n" +
            commonCSS() +
            "  .page { max-width:780px; margin:48px auto; background:#fff;\n" +
            "          border:1px solid #e5e5e5; border-radius:10px;\n" +
            "          padding:60px 72px; box-shadow:0 4px 24px rgba(0,0,0,.08); }\n" +
            "  .content { font-size:13.5px; line-height:1.85; color:#444;\n" +
            "             white-space:pre-wrap; word-wrap:break-word; }\n" +
            "  @media print { body{background:#fff} .page{box-shadow:none;border:none;margin:0;padding:40px} }\n" +
            "</style>\n</head>\n<body>\n" +
            "<div class='page'>\n" +
            headerHTML(doc) +
            "  <div class='content'>" + bodyContent + "</div>\n" +
            sigBlock +
            footerHTML(doc) +
            "</div>\n</body>\n</html>";
    }

    /**
     * Builds a signed HTML for PDF documents.
     * Layout: two-column — left side shows the FULL embedded PDF via <embed>,
     * right side shows the signature panel and signer details.
     * The PDF bytes are Base64-encoded and embedded as a data URI — single self-contained file.
     */
    String buildSignedPDFHTML(Document doc) {
        String sigDataURI = (doc.signature != null) ? signatureToBase64(doc.signature) : "";
        String sigBlock   = buildSigBlockHTML(doc, sigDataURI);

        // Encode the entire PDF as Base64 so it can be embedded inline
        String pdfBase64   = Base64.getEncoder().encodeToString(doc.pdfBytes);
        String pdfDataURI  = "data:application/pdf;base64," + pdfBase64;
        String pdfVersion  = extractPDFVersion(doc.pdfBytes);
        int    pageCount   = estimatePageCount(doc.pdfBytes);
        String fileSize    = formatFileSize(doc.pdfBytes.length);

        return "<!DOCTYPE html>\n<html lang='en'>\n<head>\n" +
            "<meta charset='UTF-8'/>\n" +
            "<title>" + escapeHTML(doc.title) + " — Signed</title>\n" +
            "<style>\n" +
            commonCSS() +
            // Two-column layout
            "  html, body { height:100%; margin:0; }\n" +
            "  .shell { display:flex; flex-direction:column; min-height:100vh; background:#f4f4f0; }\n" +
            "  .topbar { background:#1a1a2e; padding:14px 32px; display:flex;\n" +
            "            align-items:center; justify-content:space-between; }\n" +
            "  .topbar-title { color:#fff; font-size:17px; font-weight:bold; font-family:Georgia,serif; }\n" +
            "  .topbar-sub   { color:#888; font-size:12px; font-family:sans-serif; }\n" +
            "  .topbar-badge { background:#dcfce7; color:#166534; font-family:sans-serif;\n" +
            "                  font-size:12px; font-weight:700; border-radius:20px; padding:4px 16px; }\n" +
            "  .columns { display:flex; flex:1; overflow:hidden; }\n" +
            // Left: PDF viewer
            "  .pdf-pane { flex:1; background:#525659; display:flex; flex-direction:column; }\n" +
            "  .pdf-pane embed { flex:1; width:100%; height:calc(100vh - 54px); border:none; }\n" +
            "  .pdf-fallback { color:#ccc; font-family:sans-serif; font-size:13px;\n" +
            "                  padding:32px; display:none; }\n" +
            // Right: signature panel
            "  .sig-pane { width:320px; min-width:300px; background:#fff;\n" +
            "              border-left:1px solid #e5e5e5; display:flex;\n" +
            "              flex-direction:column; overflow-y:auto; padding:28px 24px; }\n" +
            "  .sig-pane h2 { font-size:15px; color:#1a1a2e; margin-bottom:6px; }\n" +
            "  .sig-pane .desc { font-family:sans-serif; font-size:12px; color:#888; margin-bottom:20px; }\n" +
            "  .meta-table { width:100%; border-collapse:collapse; margin-bottom:20px; }\n" +
            "  .meta-table td { font-family:sans-serif; font-size:12px; padding:7px 0;\n" +
            "                   border-bottom:1px solid #f0f0f0; }\n" +
            "  .meta-table td:first-child { color:#888; width:90px; }\n" +
            "  .meta-table td:last-child  { color:#1a1a2e; font-weight:600; }\n" +
            "  .meta-table .status { color:#166534; }\n" +
            "  .divider { border:none; border-top:1.5px solid #1a1a2e; margin:18px 0 14px; }\n" +
            "  .sig-label { font-family:sans-serif; font-size:10px; color:#888;\n" +
            "               text-transform:uppercase; letter-spacing:1px; margin-bottom:8px; }\n" +
            "  .sig-img { display:block; max-width:100%; height:auto; margin-bottom:10px;\n" +
            "             border-bottom:1px solid #ccc; padding-bottom:8px; }\n" +
            "  .sig-meta { font-family:sans-serif; font-size:11px; color:#888; margin-bottom:14px; }\n" +
            "  .sig-name { font-weight:700; color:#1a1a2e; }\n" +
            "  .sig-stamp { display:inline-block; font-family:sans-serif; font-size:12px;\n" +
            "               font-weight:700; color:#166534; background:#dcfce7;\n" +
            "               border-radius:6px; padding:6px 14px; margin-bottom:20px; }\n" +
            "  .print-btn { width:100%; font-family:sans-serif; font-size:13px; font-weight:700;\n" +
            "               background:#1a1a2e; color:#fff; border:none; border-radius:7px;\n" +
            "               padding:11px; cursor:pointer; margin-top:auto; }\n" +
            "  .print-btn:hover { background:#2d2d50; }\n" +
            "  @media print { .print-btn{display:none} }\n" +
            "</style>\n</head>\n<body>\n" +

            // Top bar
            "<div class='shell'>\n" +
            "  <div class='topbar'>\n" +
            "    <div>\n" +
            "      <div class='topbar-title'>✦ SignFlow &nbsp;—&nbsp; " + escapeHTML(doc.title) + "</div>\n" +
            "      <div class='topbar-sub'>" + pdfVersion + " &nbsp;·&nbsp; " + fileSize +
                   (pageCount > 0 ? " &nbsp;·&nbsp; ~" + pageCount + " pages" : "") + "</div>\n" +
            "    </div>\n" +
            "    <div class='topbar-badge'>✓ Signed Document</div>\n" +
            "  </div>\n" +

            // Two columns
            "  <div class='columns'>\n" +

            // Left: embedded PDF
            "    <div class='pdf-pane'>\n" +
            "      <embed src='" + pdfDataURI + "' type='application/pdf'\n" +
            "             onerror=\"this.style.display='none';document.getElementById('fb').style.display='block'\" />\n" +
            "      <div class='pdf-fallback' id='fb'>\n" +
            "        Your browser could not render the embedded PDF.<br/>\n" +
            "        Try opening this file in Chrome or Edge.\n" +
            "      </div>\n" +
            "    </div>\n" +

            // Right: signature panel
            "    <div class='sig-pane'>\n" +
            "      <h2>Document Signed</h2>\n" +
            "      <p class='desc'>This PDF has been digitally signed via SignFlow.</p>\n" +
            "      <table class='meta-table'>\n" +
            "        <tr><td>Document</td><td>" + escapeHTML(doc.title) + ".pdf</td></tr>\n" +
            "        <tr><td>PDF Ver.</td><td>" + escapeHTML(pdfVersion) + "</td></tr>\n" +
            "        <tr><td>File Size</td><td>" + fileSize + "</td></tr>\n" +
            (pageCount > 0 ? "        <tr><td>Pages</td><td>~" + pageCount + "</td></tr>\n" : "") +
            "        <tr><td>Signer</td><td>" + escapeHTML(doc.signerName) + "</td></tr>\n" +
            "        <tr><td>Email</td><td>" + escapeHTML(doc.signerEmail) + "</td></tr>\n" +
            "        <tr><td>Signed At</td><td>" + escapeHTML(doc.signedAt) + "</td></tr>\n" +
            "        <tr><td>Status</td><td class='status'>✓ Signed</td></tr>\n" +
            "      </table>\n" +
            "      <hr class='divider'/>\n" +
            "      <div class='sig-label'>Authorized Signature</div>\n" +
            (!sigDataURI.isEmpty()
                ? "      <img src='" + sigDataURI + "' class='sig-img' alt='Signature'/>\n"
                : "") +
            "      <div class='sig-meta'><span class='sig-name'>" + escapeHTML(doc.signerName) + "</span>" +
            "        &nbsp;·&nbsp; " + escapeHTML(doc.signerEmail) + "<br/>" + escapeHTML(doc.signedAt) + "</div>\n" +
            "      <div class='sig-stamp'>✓&nbsp; Digitally Signed via SignFlow</div>\n" +
            "      <button class='print-btn' onclick='window.print()'>🖨 Print / Save as PDF</button>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>\n</body>\n</html>";
    }

    // ── Shared HTML building blocks ────────────────────────────────────

    String commonCSS() {
        return "  * { box-sizing:border-box; margin:0; padding:0; }\n" +
               "  body { font-family:Georgia,serif; color:#1a1a2e; }\n" +
               "  .header { border-bottom:2px solid #1a1a2e; padding-bottom:20px; margin-bottom:28px; }\n" +
               "  .header h1 { font-size:24px; font-weight:bold; }\n" +
               "  .header .subtitle { font-family:sans-serif; font-size:13px; color:#888; margin-top:5px; }\n" +
               "  .badge { display:inline-block; font-family:sans-serif; font-size:11px; font-weight:700;\n" +
               "           background:#dcfce7; color:#166534; border-radius:20px;\n" +
               "           padding:3px 14px; margin-top:10px; }\n" +
               "  .sig-section { margin-top:40px; border-top:1.5px solid #1a1a2e; padding-top:18px; }\n" +
               "  .sig-line-label { font-family:sans-serif; font-size:10px; color:#888;\n" +
               "                    text-transform:uppercase; letter-spacing:1px; margin-bottom:8px; }\n" +
               "  .sig-img { display:block; max-width:280px; height:auto; margin-bottom:10px;\n" +
               "             border-bottom:1px solid #ccc; padding-bottom:6px; }\n" +
               "  .sig-meta { font-family:sans-serif; font-size:11px; color:#888; margin-bottom:10px; }\n" +
               "  .sig-name { font-weight:700; color:#1a1a2e; }\n" +
               "  .sig-stamp { display:inline-block; font-family:sans-serif; font-size:12px;\n" +
               "               font-weight:700; color:#166534; background:#dcfce7;\n" +
               "               border-radius:6px; padding:5px 14px; }\n" +
               "  .footer { margin-top:48px; border-top:1px solid #e5e5e5; padding-top:16px;\n" +
               "            font-family:sans-serif; font-size:10px; color:#aaa; text-align:center; }\n";
    }

    String headerHTML(Document doc) {
        return "  <div class='header'>\n" +
               "    <h1>" + escapeHTML(doc.title) + "</h1>\n" +
               "    <div class='subtitle'>" + escapeHTML(doc.subtitle) + "</div>\n" +
               "    <div class='badge'>✓ Signed Document</div>\n" +
               "  </div>\n";
    }

    String footerHTML(Document doc) {
        return "  <div class='footer'>Generated by SignFlow &nbsp;·&nbsp; " +
               escapeHTML(doc.signedAt) + "</div>\n";
    }

    String buildSigBlockHTML(Document doc, String sigDataURI) {
        if (sigDataURI.isEmpty()) return "";
        return "<div class='sig-section'>\n" +
               "  <div class='sig-line-label'>Authorized Signature</div>\n" +
               "  <img src='" + sigDataURI + "' class='sig-img' alt='Signature'/>\n" +
               "  <div class='sig-meta'>\n" +
               "    <span class='sig-name'>" + escapeHTML(doc.signerName) + "</span>" +
               "    &nbsp;·&nbsp; " + escapeHTML(doc.signerEmail) +
               "    &nbsp;·&nbsp; " + escapeHTML(doc.signedAt) + "\n" +
               "  </div>\n" +
               "  <div class='sig-stamp'>✓&nbsp; Digitally Signed via SignFlow</div>\n" +
               "</div>\n";
    }

    /** Download for plain-text documents — saves signed HTML with embedded signature. */
    void downloadDoc(Document doc) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(doc.title.replace(" ", "_") + "_signed.html"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(
                    new OutputStreamWriter(new FileOutputStream(fc.getSelectedFile()), "UTF-8"))) {
                pw.print(buildSignedHTML(doc, escapeHTML(doc.content)));
                showToast("Signed HTML saved — open in any browser!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Download for PDF documents.
     * Saves a single self-contained HTML file that:
     *   - Shows the full PDF embedded on the left (Base64 data URI, no external file needed)
     *   - Shows the drawn signature + signer details on the right panel
     */
    void downloadPDFDoc(Document doc) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(doc.title.replace(" ", "_") + "_signed.html"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(
                    new OutputStreamWriter(new FileOutputStream(fc.getSelectedFile()), "UTF-8"))) {
                pw.print(buildSignedPDFHTML(doc));
                showToast("Signed HTML saved — open in Chrome or Edge!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /** Escapes special HTML characters in document content. */
    String escapeHTML(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }

    // ═══════════════════════════════════════════════════════
    //  ADD DOCUMENT DIALOG
    // ═══════════════════════════════════════════════════════
    void showAddDocumentDialog() {
        JTextField titleField    = new JTextField();
        JTextField subtitleField = new JTextField();
        JTextArea  contentArea   = new JTextArea(10, 30);
        JScrollPane scroll = new JScrollPane(contentArea);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Title:"));    panel.add(titleField);
        panel.add(new JLabel("Subtitle:")); panel.add(subtitleField);
        panel.add(new JLabel("Content:")); panel.add(scroll);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Document", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title    = titleField.getText().trim();
            String subtitle = subtitleField.getText().trim();
            String content  = contentArea.getText().trim();
            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and Content required!");
                return;
            }
            int newId = docs.size() + 1;
            docs.add(new Document(newId, title, subtitle, content));
            refreshSidebar();
            showToast("Document added successfully!");
        }
    }

    // ═══════════════════════════════════════════════════════
    //  PDF UTILITY METHODS (no external library)
    // ═══════════════════════════════════════════════════════

    /**
     * Extracts PDF version string from header bytes.
     * PDF spec: first line is always "%PDF-x.y"
     */
    String extractPDFVersion(byte[] pdfBytes) {
        if (pdfBytes == null || pdfBytes.length < 8) return "Unknown";
        try {
            // Read up to first 20 bytes to find version
            int len = Math.min(pdfBytes.length, 20);
            String header = new String(pdfBytes, 0, len, "ISO-8859-1");
            if (header.startsWith("%PDF-")) {
                int end = header.indexOf('\n');
                if (end < 0) end = header.indexOf('\r');
                if (end < 0) end = len;
                return header.substring(0, end).trim(); // e.g. "%PDF-1.7"
            }
        } catch (Exception ignored) {}
        return "Unknown";
    }

    /**
     * Estimates page count by counting "/Type /Page" or "/Type/Page" entries.
     * This is a heuristic — it scans the raw bytes as a string.
     * Works reliably for most standard PDFs without any library.
     */
    int estimatePageCount(byte[] pdfBytes) {
        if (pdfBytes == null) return 0;
        try {
            // Convert to string (Latin-1 keeps byte values intact)
            String text = new String(pdfBytes, "ISO-8859-1");
            int count = 0;
            int idx   = 0;
            // Count occurrences of "/Type /Page" (with optional whitespace variants)
            // We look for "/Type" followed (possibly with spaces) by "/Page" but NOT "/Pages"
            while ((idx = text.indexOf("/Type", idx)) != -1) {
                int after = idx + 5;
                // Skip spaces/newlines
                while (after < text.length() && (text.charAt(after) == ' ' || text.charAt(after) == '\n' || text.charAt(after) == '\r')) after++;
                if (after + 5 <= text.length()) {
                    String token = text.substring(after, after + 5);
                    // "/Page" but not "/Pages"
                    if (token.equals("/Page") && (after + 5 >= text.length() || !Character.isLetterOrDigit(text.charAt(after + 5)))) {
                        count++;
                    }
                }
                idx++;
            }
            return count;
        } catch (Exception ignored) {}
        return 0;
    }

    /** Formats byte size to human-readable string. */
    String formatFileSize(long bytes) {
        if (bytes < 1024)        return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }

    // ═══════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════
    void refreshSidebar() {
        cardPanel.removeAll();
        cardPanel.add(buildHome(), "home");
        cardLayout.show(cardPanel, "home");
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    JButton roundButton(String text, Color border, Color bg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    void showToast(String msg) {
        JWindow toast = new JWindow(this);
        JLabel lbl = new JLabel("  " + msg + "  ");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(WHITE);
        lbl.setBackground(new Color(34, 197, 94));
        lbl.setOpaque(true);
        lbl.setBorder(new EmptyBorder(10, 18, 10, 18));
        toast.add(lbl);
        toast.pack();
        toast.setLocation(getX() + getWidth() - toast.getWidth() - 20, getY() + 20);
        toast.setVisible(true);
        new javax.swing.Timer(2800, e -> toast.dispose()).start();
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(DocumentSigner::new);
    }
}
