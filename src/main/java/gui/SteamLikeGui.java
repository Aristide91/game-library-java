package gui;

import model.*;
import service.PlatformManager;
import util.GameException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SteamLikeGui extends JFrame {
    private PlatformManager pm;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private final Color bgDark = new Color(32, 32, 32);       
    private final Color navDark = new Color(24, 24, 24);      
    private final Color cardDark = new Color(45, 45, 45);     
    private final Color textLight = new Color(243, 243, 243); 
    private final Color textDim = new Color(180, 180, 180);   
    private final Color accentBlue = new Color(96, 205, 255); 
    private final Color accentGreen = new Color(108, 203, 95);
    private final Color dangerRed = new Color(255, 99, 103);  

    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 18);
    private final Font bodyFont = new Font("Segoe UI", Font.PLAIN, 14);

    private JPanel storeContainer;
    private JPanel libraryContainer;
    private JPanel historyContainer;
    private JLabel walletLabel;
    private JLabel loginErrorLabel;
    private JTextField searchField;
    private String currentSearch = "";

    public SteamLikeGui() {
        // Appliquer le style du système pour les pop-ups
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        pm = new PlatformManager();

        setTitle("Pro Du Jeu - Next Gen");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(bgDark);

        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createAppPanel(), "APP");

        add(cardPanel);
    }

    private void showNotification(String message, boolean isError) {
        JDialog dialog = new JDialog(this);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());
        dialog.setBackground(new Color(0, 0, 0, 0));

        ModernPanel panel = new ModernPanel();
        panel.setBackground(isError ? dangerRed : accentGreen);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        JLabel lbl = new JLabel(message);
        lbl.setForeground(isError ? Color.WHITE : Color.BLACK);
        lbl.setFont(headerFont);
        panel.add(lbl);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocation(getX() + (getWidth() - dialog.getWidth()) / 2, getY() + 50);
        new Timer(3000, e -> dialog.dispose()).start();
        dialog.setVisible(true);
    }

    private class ModernButton extends JButton {
        private Color normalColor;
        private Color hoverColor;
        private boolean isHover = false;
        private boolean isPressed = false;
        private int radius = 12;
        private boolean isPlayButton = false;

        public ModernButton(String text, Color bg, Color fg) {
            this(text, bg, fg, false);
        }

        public ModernButton(String text, Color bg, Color fg, boolean isPlayButton) {
            super(text);
            this.normalColor = bg;
            this.hoverColor = bg.brighter();
            this.isPlayButton = isPlayButton;
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, isPlayButton ? 16 : 14));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, isPlayButton ? 30 : 20, 10, 20));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { isHover = true; repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { isHover = false; isPressed = false; repaint(); }
                @Override
                public void mousePressed(MouseEvent e) { isPressed = true; repaint(); }
                @Override
                public void mouseReleased(MouseEvent e) { isPressed = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color color = isPressed ? normalColor.darker() : (isHover ? hoverColor : normalColor);
            
            // Effet de dégradé pour le bouton jouer
            if (isPlayButton) {
                GradientPaint gp = new GradientPaint(0, 0, color, 
                                                    0, getHeight(), color.darker());
                g2.setPaint(gp);
            } else {
                g2.setColor(color);
            }
            
            int offset = isPressed ? 1 : 0;
            g2.fillRoundRect(offset, offset, getWidth() - offset*2, getHeight() - offset*2, radius, radius);

            if (isPlayButton) {
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1 + offset, 1 + offset, getWidth()-3 - offset*2, getHeight()-3 - offset*2, radius, radius);
                
                // Dessiner l'icône Play (triangle)
                g2.setColor(getForeground());
                int s = 12;
                int x = 12 + offset;
                int y = (getHeight() - s) / 2 + offset;
                int[] px = {x, x + s, x};
                int[] py = {y, y + s / 2, y + s};
                g2.fillPolygon(px, py, 3);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void showFloatingText(Component parent, String text, Color color) {
        if (!parent.isShowing()) return;
        Point p = parent.getLocationOnScreen();
        JWindow window = new JWindow();
        window.setBackground(new Color(0,0,0,0));
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(color);
        window.add(label);
        window.pack();
        
        int startX = p.x + parent.getWidth() / 2 - window.getWidth() / 2;
        int startY = p.y - 10;
        window.setLocation(startX, startY);
        window.setVisible(true);
        window.setAlwaysOnTop(true);

        Timer timer = new Timer(20, null);
        final int[] y = {startY};
        final int[] alpha = {255};
        timer.addActionListener(e -> {
            y[0] -= 2;
            alpha[0] -= 10;
            if (alpha[0] <= 0) {
                timer.stop();
                window.dispose();
            } else {
                label.setForeground(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha[0]));
                window.setLocation(startX, y[0]);
            }
        });
        timer.start();
    }

    private String showPromoDialog() {
        final String[] result = {null};
        JDialog dialog = new JDialog(this, "Achat", true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0,0,0,0));

        ModernPanel panel = new ModernPanel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(400, 220));
        panel.setBorder(BorderFactory.createLineBorder(accentBlue, 1));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;

        JLabel title = new JLabel("Code Promo (ex: PRO20 pour -20%)");
        title.setFont(headerFont);
        title.setForeground(textLight);
        panel.add(title, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 10, 15, 10);
        JLabel subtitle = new JLabel("Laissez vide pour acheter sans code");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitle.setForeground(textDim);
        panel.add(subtitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 15, 10);
        JTextField codeField = new JTextField(15);
        codeField.setBackground(bgDark);
        codeField.setForeground(textLight);
        codeField.setCaretColor(textLight);
        codeField.setFont(bodyFont);
        codeField.setHorizontalAlignment(JTextField.CENTER);
        codeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cardDark, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(codeField, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        ModernButton okBtn = new ModernButton("Acheter", accentBlue, Color.BLACK);
        ModernButton cancelBtn = new ModernButton("Annuler", cardDark, textDim);
        
        panel.add(okBtn, gbc);
        gbc.gridx = 1;
        panel.add(cancelBtn, gbc);

        okBtn.addActionListener(e -> { result[0] = codeField.getText(); dialog.dispose(); });
        cancelBtn.addActionListener(e -> { dialog.dispose(); });

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        
        return result[0];
    }

    private String showAddFundsDialog() {
        final String[] result = {null};
        JDialog dialog = new JDialog(this, "Ajouter des fonds", true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0,0,0,0));

        ModernPanel panel = new ModernPanel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(350, 200));
        panel.setBorder(BorderFactory.createLineBorder(accentGreen, 1));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;

        JLabel title = new JLabel("Montant à ajouter (€)");
        title.setFont(headerFont);
        title.setForeground(textLight);
        panel.add(title, gbc);

        gbc.gridy++;
        JTextField amountField = new JTextField(15);
        amountField.setBackground(bgDark);
        amountField.setForeground(textLight);
        amountField.setCaretColor(textLight);
        amountField.setFont(headerFont);
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cardDark, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(amountField, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        ModernButton okBtn = new ModernButton("Ajouter", accentGreen, Color.BLACK);
        ModernButton cancelBtn = new ModernButton("Annuler", cardDark, textDim);
        
        panel.add(okBtn, gbc);
        gbc.gridx = 1;
        panel.add(cancelBtn, gbc);

        okBtn.addActionListener(e -> { result[0] = amountField.getText(); dialog.dispose(); });
        cancelBtn.addActionListener(e -> { dialog.dispose(); });

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        
        return result[0];
    }

    private class ModernPanel extends JPanel {
        private int radius = 16;
        public ModernPanel() {
            setOpaque(false);
            setBackground(cardDark);
            setBorder(new EmptyBorder(20, 20, 20, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JPanel createLoginPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(bgDark);
        
        ModernPanel panel = new ModernPanel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(500, 450));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;

        JLabel title = new JLabel("Bienvenue");
        title.setForeground(textLight);
        title.setFont(titleFont);
        panel.add(title, gbc);

        gbc.gridy++;
        loginErrorLabel = new JLabel(" ");
        loginErrorLabel.setForeground(dangerRed);
        loginErrorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Plus grand et gras
        panel.add(loginErrorLabel, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        JLabel userLbl = new JLabel("Pseudo");
        userLbl.setForeground(textDim);
        panel.add(userLbl, gbc);

        gbc.gridx = 1;
        JTextField userField = new JTextField(15);
        panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel passLbl = new JLabel("Mot de passe");
        passLbl.setForeground(textDim);
        panel.add(passLbl, gbc);

        gbc.gridx = 1;
        JPasswordField passField = new JPasswordField(15);
        panel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setOpaque(false);

        ModernButton loginBtn = new ModernButton("Connexion", accentBlue, Color.BLACK);
        ModernButton registerBtn = new ModernButton("Créer un compte", navDark, textLight);

        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        panel.add(btnPanel, gbc);

        wrapper.add(panel);

        loginBtn.addActionListener(e -> {
            loginErrorLabel.setText(" ");
            String nick = userField.getText();
            String pass = new String(passField.getPassword());
            
            if (nick.isEmpty() || pass.isEmpty()) {
                loginErrorLabel.setText("Veuillez remplir tous les champs.");
                showNotification("Champs vides", true);
                return;
            }

            User u = pm.getUsers().get(nick);
            if (u == null) {
                loginErrorLabel.setText("Utilisateur inconnu.");
                showNotification("Compte inexistant", true);
            } else if (!u.validatePassword(pass)) {
                loginErrorLabel.setText("Mot de passe incorrect.");
                showNotification("Erreur mot de passe", true);
            } else {
                pm.setCurrentUser(u);
                updateViews();
                cardLayout.show(cardPanel, "APP");
            }
        });

        registerBtn.addActionListener(e -> {
            loginErrorLabel.setText(" ");
            String nick = userField.getText();
            String pass = new String(passField.getPassword());
            
            if (nick.isEmpty() || pass.isEmpty()) {
                loginErrorLabel.setText("Pseudo/Pass requis pour s'inscrire.");
                showNotification("Données manquantes", true);
                return;
            }
            if (pm.getUsers().containsKey(nick)) {
                loginErrorLabel.setText("Ce pseudo est déjà utilisé.");
                showNotification("Pseudo indisponible", true);
                return;
            }
            
            User newUser = new User(nick, nick + "@email.com", pass);
            pm.getUsers().put(nick, newUser);
            pm.forceSaveData();
            showNotification("Compte créé !", false);
            loginErrorLabel.setForeground(accentGreen);
            loginErrorLabel.setText("Compte créé avec succès ! Connectez-vous.");
            new Timer(4000, ev -> {
                loginErrorLabel.setForeground(dangerRed);
                loginErrorLabel.setText(" ");
            }).start();
        });

        return wrapper;
    }

    private JPanel createAppPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgDark);

        // Navbar
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(navDark);
        navPanel.setBorder(new EmptyBorder(15, 30, 15, 30));

        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftNav.setOpaque(false);

        ModernButton storeBtn = new ModernButton("Magasin", navDark, textLight);
        ModernButton libBtn = new ModernButton("Bibliothèque", navDark, textLight);
        ModernButton histBtn = new ModernButton("Historique", navDark, textLight);
        
        searchField = new JTextField(15);
        searchField.setBackground(cardDark);
        searchField.setForeground(textLight);
        searchField.setCaretColor(textLight);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgDark, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.setText("Rechercher un jeu...");
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Rechercher un jeu...")) searchField.setText("");
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) searchField.setText("Rechercher un jeu...");
            }
        });
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                currentSearch = searchField.getText().equalsIgnoreCase("Rechercher un jeu...") ? "" : searchField.getText();
                updateViews();
            }
        });

        leftNav.add(storeBtn);
        leftNav.add(libBtn);
        leftNav.add(histBtn);
        leftNav.add(Box.createHorizontalStrut(20));
        leftNav.add(searchField);

        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightNav.setOpaque(false);
        walletLabel = new JLabel("Solde: 0.00€");
        walletLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        walletLabel.setForeground(accentGreen);
        ModernButton addFundsBtn = new ModernButton("+", cardDark, textLight);
        ModernButton logoutBtn = new ModernButton("Déconnexion", cardDark, textDim);
        rightNav.add(walletLabel);
        rightNav.add(addFundsBtn);
        rightNav.add(logoutBtn);

        navPanel.add(leftNav, BorderLayout.WEST);
        navPanel.add(rightNav, BorderLayout.EAST);

        // Contenu Central
        JPanel contentPanel = new JPanel(new CardLayout());
        CardLayout contentLayout = (CardLayout) contentPanel.getLayout();

        storeContainer = new JPanel(new GridLayout(0, 1, 15, 15));
        storeContainer.setBackground(bgDark);
        storeContainer.setBorder(new EmptyBorder(30, 50, 30, 50));
        JPanel proxyStore = new JPanel(new BorderLayout());
        proxyStore.setBackground(bgDark);
        proxyStore.add(storeContainer, BorderLayout.NORTH);
        JScrollPane scrollStore = new JScrollPane(proxyStore);
        scrollStore.setBorder(null);

        libraryContainer = new JPanel(new GridLayout(0, 1, 15, 15));
        libraryContainer.setBackground(bgDark);
        libraryContainer.setBorder(new EmptyBorder(30, 50, 30, 50));
        JPanel proxyLib = new JPanel(new BorderLayout());
        proxyLib.setBackground(bgDark);
        proxyLib.setBorder(new EmptyBorder(30, 50, 30, 50));
        JPanel libHeader = new JPanel(new BorderLayout());
        libHeader.setOpaque(false);
        JLabel libTitle = new JLabel("Mes Jeux");
        libTitle.setFont(titleFont);
        libTitle.setForeground(textLight);
        ModernButton pdfBtn = new ModernButton("Exporter PDF", accentBlue, Color.BLACK);
        libHeader.add(libTitle, BorderLayout.WEST);
        libHeader.add(pdfBtn, BorderLayout.EAST);
        proxyLib.add(libHeader, BorderLayout.NORTH);
        proxyLib.add(libraryContainer, BorderLayout.CENTER);
        JScrollPane scrollLib = new JScrollPane(proxyLib);
        scrollLib.setBorder(null);

        historyContainer = new JPanel(new GridLayout(0, 1, 10, 10));
        historyContainer.setBackground(bgDark);
        historyContainer.setBorder(new EmptyBorder(30, 50, 30, 50));
        JPanel proxyHist = new JPanel(new BorderLayout());
        proxyHist.setBackground(bgDark);
        proxyHist.add(historyContainer, BorderLayout.NORTH);
        JScrollPane scrollHist = new JScrollPane(proxyHist);
        scrollHist.setBorder(null);

        contentPanel.add(scrollStore, "STORE");
        contentPanel.add(scrollLib, "LIBRARY");
        contentPanel.add(scrollHist, "HISTORY");

        panel.add(navPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        storeBtn.addActionListener(e -> { contentLayout.show(contentPanel, "STORE"); storeBtn.setForeground(accentBlue); libBtn.setForeground(textLight); histBtn.setForeground(textLight); });
        libBtn.addActionListener(e -> { contentLayout.show(contentPanel, "LIBRARY"); libBtn.setForeground(accentBlue); storeBtn.setForeground(textLight); histBtn.setForeground(textLight); });
        histBtn.addActionListener(e -> { contentLayout.show(contentPanel, "HISTORY"); histBtn.setForeground(accentBlue); storeBtn.setForeground(textLight); libBtn.setForeground(textLight); });
        logoutBtn.addActionListener(e -> { pm.setCurrentUser(null); cardLayout.show(cardPanel, "LOGIN"); });
        addFundsBtn.addActionListener(e -> {
            String val = showAddFundsDialog();
            if (val != null && !val.trim().isEmpty()) {
                pm.getCurrentUser().addMoney(val);
                pm.forceSaveData();
                updateViews();
            }
        });
        pdfBtn.addActionListener(e -> {
            util.Exportable exporter = new util.PdfService();
            exporter.export(pm.getCurrentUser());
            showNotification("PDF exporté !", false);
        });

        return panel;
    }

    private void updateViews() {
        if (pm.getCurrentUser() == null) return;
        walletLabel.setText(String.format("Solde: %.2f€", pm.getCurrentUser().getWallet()));

        storeContainer.removeAll();
        for (Game g : pm.getCatalog().getGamesList()) {
            if (currentSearch.isEmpty() || g.getTitle().toLowerCase().contains(currentSearch.toLowerCase()) || g.getGenre().toLowerCase().contains(currentSearch.toLowerCase())) {
                storeContainer.add(createGameCard(g));
            }
        }

        libraryContainer.removeAll();
        historyContainer.removeAll();
        for (GamePurchase p : pm.getCurrentUser().getLibrary().getPurchases()) {
            historyContainer.add(createHistoryCard(p));
            if (!p.isRefunded()) libraryContainer.add(createLibraryCard(p));
        }

        storeContainer.revalidate(); storeContainer.repaint();
        libraryContainer.revalidate(); libraryContainer.repaint();
        historyContainer.revalidate(); historyContainer.repaint();
    }

    private JPanel createGameCard(Game g) {
        ModernPanel card = new ModernPanel();
        card.setLayout(new BorderLayout());

        JPanel info = new JPanel(new GridLayout(3, 1, 0, 2));
        info.setOpaque(false);
        JLabel title = new JLabel(g.getTitle());
        title.setFont(headerFont);
        title.setForeground(textLight);
        JLabel studio = new JLabel(g.getStudio() + " • " + g.getGenre());
        studio.setForeground(textDim);
        
        String specInfo = "";
        if (g instanceof MultiplayerGame) specInfo = "Multijoueur (" + (((MultiplayerGame)g).isOnline() ? "En ligne" : "Local") + ")";
        else if (g instanceof SubscriptionGame) specInfo = "Abonnement: " + String.format("%.2f€/mois", ((SubscriptionGame)g).getMonthlyFee());
        else if (g instanceof SoloGame) specInfo = "Solo - Durée: " + ((SoloGame)g).getEstimatedDuration() + "h";
        JLabel spec = new JLabel(specInfo);
        spec.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        spec.setForeground(accentBlue);

        info.add(title); info.add(studio); info.add(spec);

        ModernButton buyBtn = new ModernButton(String.format("Acheter (%.2f€)", g.getPrice()), accentBlue, Color.BLACK);
        buyBtn.addActionListener(e -> {
            String code = showPromoDialog();
            if (code == null) {
                // Annulé
                return;
            }
            double discount = (code.trim().equalsIgnoreCase("PRO20")) ? 0.20 : 0.0;
            try {
                pm.getCurrentUser().buyGame(g, discount);
                pm.forceSaveData();
                updateViews();
                showNotification(discount > 0 ? "Achat réussi (-20%) !" : "Achat réussi !", false);
            } catch (GameException ex) {
                showNotification(ex.getMessage(), true);
            }
        });

        card.add(info, BorderLayout.CENTER);
        card.add(buyBtn, BorderLayout.EAST);
        return card;
    }

    private JPanel createLibraryCard(GamePurchase p) {
        ModernPanel card = new ModernPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Utilisation de GridLayout(0, 1) pour une liste verticale automatique
        JPanel leftPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        leftPanel.setOpaque(false);
        
        JLabel title = new JLabel(p.getGame().getTitle());
        title.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Titre plus imposant
        title.setForeground(textLight);
        
        JLabel studio = new JLabel("Studio : " + p.getGame().getStudio());
        studio.setForeground(textDim);
        studio.setFont(bodyFont);

        JLabel genre = new JLabel("Genre : " + p.getGame().getGenre());
        genre.setForeground(textDim);
        genre.setFont(bodyFont);

        String progText = (p.getGame() instanceof SoloGame) ? " | Progression : " + p.getProgression() + "%" : "";
        JLabel playDetails = new JLabel("Temps de jeu : " + p.getPlayTime() + "h" + progText);
        playDetails.setForeground(accentBlue);
        playDetails.setFont(new Font("Segoe UI", Font.BOLD, 22));

        leftPanel.add(title);
        leftPanel.add(studio);
        leftPanel.add(genre);
        leftPanel.add(playDetails);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 60));
        centerPanel.setOpaque(false);
        
        if (p.getGame() instanceof SoloGame) {
            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue(p.getProgression());
            bar.setStringPainted(false);
            bar.setBackground(navDark);
            bar.setForeground(accentBlue);
            bar.setBorder(null);
            bar.setPreferredSize(new Dimension(200, 20));
            centerPanel.add(bar);
        }

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        actions.setOpaque(false);
        ModernButton playBtn = new ModernButton("Jouer", accentGreen, Color.BLACK, true);
        ModernButton refundBtn = new ModernButton("Rembourser", dangerRed, Color.BLACK);
        
        playBtn.addActionListener(e -> { 
            p.addPlayTime(1); 
            pm.forceSaveData(); 
            showFloatingText(playBtn, "+1h", accentGreen);
            updateViews(); 
        });
        
        refundBtn.addActionListener(e -> {
            try { pm.getCurrentUser().requestRefund(p); pm.forceSaveData(); updateViews(); showNotification("Remboursé !", false); }
            catch (GameException ex) { showNotification(ex.getMessage(), true); }
        });

        actions.add(playBtn);
        actions.add(refundBtn);

        card.add(leftPanel, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(actions, BorderLayout.EAST);
        return card;
    }

    private JPanel createHistoryCard(GamePurchase p) {
        ModernPanel card = new ModernPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(navDark);
        JPanel info = new JPanel(new GridLayout(2, 1, 0, 5));
        info.setOpaque(false);
        JLabel title = new JLabel(p.getGame().getTitle());
        title.setFont(headerFont);
        title.setForeground(textLight);
        String status = p.isRefunded() ? "REMBOURSÉ" : "ACHETÉ";
        JLabel details = new JLabel("Date: " + p.getPurchaseDate() + " • " + status);
        details.setForeground(textDim);
        info.add(title); info.add(details);
        JLabel price = new JLabel(String.format("%.2f€", p.getPricePaid()));
        price.setForeground(p.isRefunded() ? dangerRed : accentGreen);
        card.add(info, BorderLayout.CENTER);
        card.add(price, BorderLayout.EAST);
        return card;
    }
}