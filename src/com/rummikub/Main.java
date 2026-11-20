package com.rummikub;

import com.rummikub.domain.EstrategiaBot;
import com.rummikub.domain.Jugador;
import com.rummikub.gui.RummikubGUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Jugador p1 = new Jugador("1", "Laura", null);
            Jugador p2 = new Jugador("2", "Bot", new EstrategiaBot());
            RummikubGUI gui = new RummikubGUI(p1, p2, 42L);
            gui.setVisible(true);
        });
    }
}