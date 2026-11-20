package com.rummikub.gui;

import com.rummikub.domain.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;

public class RummikubGUI extends JFrame {
    private final Juego juego;
    private final Jugador jugadorHumano;
    private final List<Ficha> fichasManoActual = new ArrayList<>();
    private final Set<Integer> indicesManoSeleccionados = new LinkedHashSet<>();
    private int indiceMesaSeleccionado = -1;
    private boolean hizoJugadaValidaEnTurno = false;

    private final JLabel lblEstado = new JLabel();
    private final JLabel lblMetricas = new JLabel();
    private final DefaultListModel<String> modeloMesa = new DefaultListModel<>();
    private final JList<String> listaMesa = new JList<>(modeloMesa);
    private final JPanel panelMano = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
    private final JTextArea areaEventos = new JTextArea(6, 50);
    

public RummikubGUI(Jugador jugadorHumano, Jugador jugadorBot, long seed) {
        super("Rummikub - Entrega 3 (Swing + Patrones)");

        this.juego = new JuegoBuilder()
                .conSemilla(seed)
                .agregarJugador("1", jugadorHumano.getNombre(), (j, g) -> {})
                .agregarJugador("2", jugadorBot.getNombre(), new EstrategiaBot())
                .agregarObservador(msg -> appendEvento("[OBSERVER] " + msg))
                .build();

        // Tomamos el jugador real de la partida para leer sus fichas repartidas
        this.jugadorHumano = juego.getJugadores().get(0);
        this.jugadorHumano.setEstrategia((j, g) -> refrescarVista());

        configurarVentana();
        refrescarVista();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 660);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel superior: estado y métricas
        JPanel panelTop = new JPanel(new GridLayout(2, 1, 4, 4));
        panelTop.setBorder(BorderFactory.createEmptyBorder(8, 12, 4, 12));
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblMetricas.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panelTop.add(lblEstado);
        panelTop.add(lblMetricas);
        add(panelTop, BorderLayout.NORTH);

        // Panel central: combinaciones en mesa + bitácora de eventos (Observer)
        JSplitPane splitCentro = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitCentro.setResizeWeight(0.6);

        JPanel panelMesaWrapper = new JPanel(new BorderLayout(4, 4));
        panelMesaWrapper.setBorder(BorderFactory.createTitledBorder("Combinaciones en la Mesa (haz clic para seleccionar)"));
        listaMesa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaMesa.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                indiceMesaSeleccionado = listaMesa.getSelectedIndex();
            }
        });
        panelMesaWrapper.add(new JScrollPane(listaMesa), BorderLayout.CENTER);

        JPanel panelEventosWrapper = new JPanel(new BorderLayout(4, 4));
        panelEventosWrapper.setBorder(BorderFactory.createTitledBorder("Eventos del Juego (Patrón Observer)"));
        areaEventos.setEditable(false);
        areaEventos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panelEventosWrapper.add(new JScrollPane(areaEventos), BorderLayout.CENTER);

        splitCentro.setTopComponent(panelMesaWrapper);
        splitCentro.setBottomComponent(panelEventosWrapper);
        add(splitCentro, BorderLayout.CENTER);

        // Panel inferior: mano del jugador + botones de acción (Command / Strategy)
        JPanel panelInferior = new JPanel(new BorderLayout(6, 6));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(4, 12, 10, 12));

        JPanel panelManoWrapper = new JPanel(new BorderLayout());
        panelManoWrapper.setBorder(BorderFactory.createTitledBorder("Tu Mano (selecciona fichas con clic)"));
        panelManoWrapper.setPreferredSize(new Dimension(920, 145));
        panelManoWrapper.setMinimumSize(new Dimension(0, 130));

        JScrollPane scrollMano = new JScrollPane(panelMano);
        scrollMano.setPreferredSize(new Dimension(920, 105));
        scrollMano.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollMano.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelManoWrapper.add(scrollMano, BorderLayout.CENTER);
        panelInferior.add(panelManoWrapper, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 8, 6));
        JButton btnBajar = new JButton("Bajar combinación");
        JButton btnComplementar = new JButton("Complementar en mesa");
        JButton btnTomarFicha = new JButton("Tomar ficha de mesa");
        JButton btnRobar = new JButton("Robar ficha y pasar");
        JButton btnDeshacer = new JButton("Deshacer (Command)");
        JButton btnPasar = new JButton("Terminar turno / Jugar Bot");
        JButton btnFinalizar = new JButton("Finalizar partida");

        btnBajar.addActionListener(e -> accionBajarCombinacion());
        btnComplementar.addActionListener(e -> accionComplementarMesa());
        btnTomarFicha.addActionListener(e -> accionTomarFichaDeMesa());
        btnRobar.addActionListener(e -> accionRobarYPasar());
        btnDeshacer.addActionListener(e -> accionDeshacer());
        btnPasar.addActionListener(e -> accionPasarTurnoYEjecutarBots());
        btnFinalizar.addActionListener(e -> accionFinalizarPartida());

        panelBotones.add(btnBajar);
        panelBotones.add(btnComplementar);
        panelBotones.add(btnTomarFicha);
        panelBotones.add(btnRobar);
        panelBotones.add(btnDeshacer);
        panelBotones.add(btnPasar);
        panelBotones.add(btnFinalizar);

        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private boolean esTurnoHumano() {
        return !juego.isPartidaTerminada() && juego.getJugadorActual() == jugadorHumano;
    }

    private List<Ficha> obtenerFichasSeleccionadas() {
        List<Ficha> seleccion = new ArrayList<>();
        for (Integer idx : indicesManoSeleccionados) {
            if (idx >= 0 && idx < fichasManoActual.size()) {
                seleccion.add(fichasManoActual.get(idx));
            }
        }
        return seleccion;
    }

    private void accionBajarCombinacion() {
        if (!esTurnoHumano()) {
            mostrarAviso("No es tu turno o la partida ya terminó.");
            return;
        }
        List<Ficha> seleccion = obtenerFichasSeleccionadas();
        Meld meld = juego.getMeldFactory().crearMeld(seleccion);
        if (meld == null) {
            mostrarAviso("Combinación inválida. Usa 3-4 del mismo valor y distinto color, o 3+ consecutivas del mismo color.");
            return;
        }
        if (!jugadorHumano.haAbierto() && meld.calcularPuntos() < 30) {
            mostrarAviso("Aún no has abierto. La combinación suma " + meld.calcularPuntos() + " pts y necesitas mínimo 30.");
            return;
        }
        IComando cmd = new ColocarMeldComando(jugadorHumano, juego.getMesa(), meld);
        if (juego.ejecutarComando(cmd)) {
            hizoJugadaValidaEnTurno = true;
            indicesManoSeleccionados.clear();
            refrescarVista();
        } else {
            mostrarAviso("No se pudo colocar la combinación.");
        }
    }

    private void accionComplementarMesa() {
        if (!esTurnoHumano()) {
            mostrarAviso("No es tu turno o la partida ya terminó.");
            return;
        }
        if (!jugadorHumano.haAbierto()) {
            mostrarAviso("Debes abrir primero (mínimo 30 puntos) antes de complementar en mesa.");
            return;
        }
        List<Meld> enMesa = juego.getMesa().getCombinaciones();
        if (indiceMesaSeleccionado < 0 || indiceMesaSeleccionado >= enMesa.size()) {
            mostrarAviso("Selecciona primero una combinación en la lista de la mesa.");
            return;
        }
        List<Ficha> seleccion = obtenerFichasSeleccionadas();
        if (seleccion.isEmpty()) {
            mostrarAviso("Selecciona al menos una ficha de tu mano para agregar.");
            return;
        }
        Meld meldDestino = enMesa.get(indiceMesaSeleccionado);
        IComando cmd = new ComplementarMeldComando(
                jugadorHumano, juego.getMesa(), meldDestino, seleccion, juego.getMeldFactory()
        );
        if (juego.ejecutarComando(cmd)) {
            hizoJugadaValidaEnTurno = true;
            indicesManoSeleccionados.clear();
            refrescarVista();
        } else {
            mostrarAviso("No se pudo complementar la combinación seleccionada.");
        }
    }

    private void accionTomarFichaDeMesa() {
        if (!esTurnoHumano()) {
            mostrarAviso("No es tu turno o la partida ya terminó.");
            return;
        }
        if (!jugadorHumano.haAbierto()) {
            mostrarAviso("Debes abrir primero (mínimo 30 puntos) antes de tomar fichas de la mesa.");
            return;
        }
        List<Meld> enMesa = juego.getMesa().getCombinaciones();
        if (indiceMesaSeleccionado < 0 || indiceMesaSeleccionado >= enMesa.size()) {
            mostrarAviso("Selecciona primero la combinación origen en la mesa.");
            return;
        }
        Meld origen = enMesa.get(indiceMesaSeleccionado);
        List<Ficha> fichasOrigen = origen.getFichas();
        if (fichasOrigen.isEmpty()) return;

        String[] opciones = new String[fichasOrigen.size()];
        for (int i = 0; i < fichasOrigen.size(); i++) {
            opciones[i] = i + ": " + fichasOrigen.get(i);
        }
        String elegida = (String) JOptionPane.showInputDialog(
                this,
                "Elige la ficha a tomar de " + origen + ":",
                "Tomar ficha de mesa",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );
        if (elegida == null) return;

        int idxFicha = Integer.parseInt(elegida.split(":")[0].trim());
        Ficha fichaTomada = fichasOrigen.get(idxFicha);
        List<Ficha> adicionales = obtenerFichasSeleccionadas();

        IComando cmd = new MoverFichaEntreMeldsComando(
                jugadorHumano, juego.getMesa(), origen, fichaTomada, adicionales, juego.getMeldFactory()
        );
        if (juego.ejecutarComando(cmd)) {
            hizoJugadaValidaEnTurno = true;
            indicesManoSeleccionados.clear();
            refrescarVista();
        } else {
            mostrarAviso("Movimiento inválido. Verifica que el grupo/escalera en mesa siga siendo válido y que la nueva combinación sea legal.");
        }
    }

    private void accionRobarYPasar() {
        if (!esTurnoHumano()) {
            mostrarAviso("No es tu turno o la partida ya terminó.");
            return;
        }
        if (hizoJugadaValidaEnTurno) {
            mostrarAviso("Ya realizaste una jugada válida en este turno; no puedes robar ficha.");
            return;
        }    
        IComando robarCmd = new RobarFichaComando(jugadorHumano, juego.getPozo());
        if (juego.ejecutarComando(robarCmd)) {
            indicesManoSeleccionados.clear();
            accionPasarTurnoYEjecutarBots();
        } else {
            mostrarAviso("El pozo está vacío.");
        }
    }

    private void accionDeshacer() {
        if (juego.deshacerUltimoComando()) {
            indicesManoSeleccionados.clear();
            refrescarVista();
        } else {
            mostrarAviso("No hay acciones para deshacer.");
        }
    }

    private void accionPasarTurnoYEjecutarBots() {
        if (juego.isPartidaTerminada()) {
            return;
        }
        if (esTurnoHumano() && !hizoJugadaValidaEnTurno) {
            IComando robarCmd = new RobarFichaComando(jugadorHumano, juego.getPozo());
            if (juego.ejecutarComando(robarCmd)) {
            appendEvento("[REGLA] No realizaste jugada válida: se robó 1 ficha antes de pasar turno.");
            } else {
                appendEvento("[REGLA] Pozo vacío: se pasa el turno sin robar.");
            }
        }

        indicesManoSeleccionados.clear();
        hizoJugadaValidaEnTurno = false;
        juego.pasarTurno();

        int guard = 0;
        while (!juego.isPartidaTerminada() && juego.getJugadorActual() != jugadorHumano && guard < 50) {
            juego.jugarTurnoActual();
            guard++;
        }
        refrescarVista();
    }

    private void refrescarVista() {
        Jugador actual = juego.getJugadorActual();
        lblEstado.setText("Turno actual: " + actual.getNombre()
                + " | Apertura (30 pts): " + (jugadorHumano.haAbierto() ? "SÍ" : "NO")
                + (juego.isPartidaTerminada() && juego.getGanador() != null
                        ? " | GANADOR: " + juego.getGanador().getNombre()
                        : ""));

        lblMetricas.setText("Pozo: " + juego.getPozo().cantidadRestante()
                + " fichas | En mesa: " + juego.getMesa().contarFichas()
                + " fichas (" + juego.getMesa().calcularPuntos() + " pts) | Fichas Bot: "
                + juego.getJugadores().get(1).getMano().cantidadFichas());

        modeloMesa.clear();
        List<Meld> melds = juego.getMesa().getCombinaciones();
        for (int i = 0; i < melds.size(); i++) {
            modeloMesa.addElement("[" + i + "] " + melds.get(i) + " (" + melds.get(i).calcularPuntos() + " pts)");
        }
        if (indiceMesaSeleccionado >= 0 && indiceMesaSeleccionado < modeloMesa.size()) {
            listaMesa.setSelectedIndex(indiceMesaSeleccionado);
        } else {
            indiceMesaSeleccionado = -1;
        }

        panelMano.removeAll();
        fichasManoActual.clear();
        fichasManoActual.addAll(jugadorHumano.getMano().getFichas());

        for (int i = 0; i < fichasManoActual.size(); i++) {
            final int idx = i;
            Ficha f = fichasManoActual.get(i);
            JToggleButton btnFicha = new JToggleButton(f.toString());
            btnFicha.setSelected(indicesManoSeleccionados.contains(idx));
            btnFicha.setForeground(colorSwing(f.getColor()));
            btnFicha.setFont(new Font("SansSerif", Font.BOLD, 12));
            btnFicha.addActionListener(e -> {
                if (btnFicha.isSelected()) {
                    indicesManoSeleccionados.add(idx);
                } else {
                    indicesManoSeleccionados.remove(idx);
                }
            });
            panelMano.add(btnFicha);
        }

        panelMano.revalidate();
        panelMano.repaint();
    }

private java.awt.Color colorSwing(com.rummikub.domain.Color c) {
        if (c == null) {
            return java.awt.Color.DARK_GRAY;
        }
        return switch (c) {
            case ROJO -> new java.awt.Color(180, 25, 25);
            case AZUL -> new java.awt.Color(25, 70, 180);
            case AMARILLO -> new java.awt.Color(165, 125, 0);
            case NEGRO -> java.awt.Color.BLACK;
            case COMODIN -> new java.awt.Color(110, 30, 150);
        };
    }


    private void appendEvento(String texto) {
        areaEventos.append(texto + "\n");
        areaEventos.setCaretPosition(areaEventos.getDocument().getLength());
    }

    private void mostrarAviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void accionFinalizarPartida() {
        if (juego.isPartidaTerminada()) {
            mostrarAviso("La partida ya está finalizada.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas finalizar la partida actual?",
                "Finalizar partida",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            juego.finalizarPartidaManual();
            refrescarVista();
        }
    }
}
