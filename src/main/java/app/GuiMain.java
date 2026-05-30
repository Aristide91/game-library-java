package app;

import gui.SteamLikeGui;
import javax.swing.SwingUtilities;

public final class GuiMain {
    
    private GuiMain() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SteamLikeGui gui = new SteamLikeGui();
            gui.setVisible(true);
        });
    }
}