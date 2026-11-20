package com.rummikub.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EstrategiaHumano implements IEstrategiaJugador {
    private final Scanner scanner;

    public EstrategiaHumano(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void ejecutarTurno(Jugador jugador, Juego juego) {
        boolean turnoFinalizado = false;

        while (!turnoFinalizado && jugador.getMano().cantidadFichas() > 0) {
            System.out.println("\n==========================================");
            System.out.println("Turno de: " + jugador.getNombre()
                    + " | Apertura (30 pts): " + (jugador.haAbierto() ? "SÍ" : "NO")
                    + " | Pozo: " + juego.getPozo().cantidadRestante());
            System.out.println("Mesa (" + juego.getMesa().getCombinaciones().size() + " combinaciones): "
                    + juego.getMesa().getCombinaciones());
            System.out.println("Tu mano: " + jugador.getMano());
            System.out.println("Opciones:");
            System.out.println("  [1] Bajar combinación (ingresa índices separados por espacio, ej: 0 3 7)");
            System.out.println("  [2] Robar ficha y terminar turno");
            System.out.println("  [3] Deshacer última acción");
            System.out.println("  [4] Terminar turno");
            System.out.println("  [5] Complementar combinación en la mesa");
            System.out.println("  [6] Tomar ficha de combinación en mesa para formar nueva combinación");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> {
                    System.out.print("Índices de fichas a bajar: ");
                    String linea = scanner.nextLine().trim();
                    List<Ficha> seleccionadas = parsearIndices(linea, jugador.getMano().getFichas());
                    Meld meld = juego.getMeldFactory().crearMeld(seleccionadas);
                    if (meld == null) {
                        System.out.println("Combinación inválida. Debe ser grupo (3-4 mismo número, distinto color) o escalera (3+ consecutivos, mismo color).");
                    } else if (!jugador.haAbierto() && meld.calcularPuntos() < 30) {
                        System.out.println("Aún no has abierto. La combinación suma " + meld.calcularPuntos() + " pts y necesitas al menos 30.");
                    } else {
                        IComando cmd = new ColocarMeldComando(jugador, juego.getMesa(), meld);
                        if (juego.ejecutarComando(cmd)) {
                            System.out.println("¡Combinación bajada con éxito!");
                        } else {
                            System.out.println("No fue posible colocar la combinación.");
                        }
                    }
                }
                case "2" -> {
                    IComando robarCmd = new RobarFichaComando(jugador, juego.getPozo());
                    if (juego.ejecutarComando(robarCmd)) {
                        System.out.println("Robaste una ficha del pozo.");
                    } else {
                        System.out.println("El pozo está vacío.");
                    }
                    turnoFinalizado = true;
                }
                case "3" -> {
                    if (!juego.deshacerUltimoComando()) {
                        System.out.println("No hay acciones para deshacer.");
                    }
                }
                case "4" -> turnoFinalizado = true;
                default -> System.out.println("Opción no válida.");

                case "5" -> {
                    if (!jugador.haAbierto()) {
                        System.out.println("Debes realizar tu apertura de al menos 30 puntos antes de complementar combinaciones en la mesa.");
                        break;
                    }
                    List<Meld> enMesa = juego.getMesa().getCombinaciones();
                    if (enMesa.isEmpty()) {
                        System.out.println("No hay combinaciones en la mesa para complementar.");
                        break;
                       }
                    for (int i = 0; i < enMesa.size(); i++) {
                        System.out.println("  Mesa [" + i + "]: " + enMesa.get(i));
                    }
                    System.out.print("Índice de la combinación en mesa: ");
                    String idxMesaStr = scanner.nextLine().trim();
                    int idxMesa;
                    try {
                        idxMesa = Integer.parseInt(idxMesaStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Índice de mesa no válido.");
                        break;
                    }
                    if (idxMesa < 0 || idxMesa >= enMesa.size()) {
                        System.out.println("Índice de mesa fuera de rango.");
                        break;
                    }

                    System.out.print("Índices de fichas de tu mano para agregar: ");
                    String lineaFichas = scanner.nextLine().trim();
                    List<Ficha> seleccionadas = parsearIndices(lineaFichas, jugador.getMano().getFichas());

                    Meld meldDestino = enMesa.get(idxMesa);
                    IComando cmd = new ComplementarMeldComando(
                        jugador,
                        juego.getMesa(),
                        meldDestino,
                        seleccionadas,
                        juego.getMeldFactory()
                    );

                    if (juego.ejecutarComando(cmd)) {
                        System.out.println("¡Combinación en la mesa complementada con éxito!");
                    } else {
                        System.out.println("No se pudo complementar la combinación. Verifica que las fichas formen un grupo o escalera válida y que estén en tu mano.");
                    }
                    
                }

                case "6" -> {
                    if (!jugador.haAbierto()) {
                        System.out.println("Debes realizar tu apertura de al menos 30 puntos antes de manipular fichas de la mesa.");
                        break;
                    }
                    List<Meld> enMesa = juego.getMesa().getCombinaciones();
                    if (enMesa.isEmpty()) {
                        System.out.println("No hay combinaciones en la mesa.");
                        break;
                    }

                    for (int i = 0; i < enMesa.size(); i++) {
                        System.out.println("  Mesa [" + i + "]: " + enMesa.get(i));
                    }
                    System.out.print("Índice de la combinación origen en mesa: ");
                    int idxOrigen;
                    try {
                        idxOrigen = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Índice no válido.");
                        break;
                    }
                    if (idxOrigen < 0 || idxOrigen >= enMesa.size()) {
                        System.out.println("Índice fuera de rango.");
                        break;
                    }

                    Meld origen = enMesa.get(idxOrigen);
                    List<Ficha> fichasOrigen = origen.getFichas();
                    for (int j = 0; j < fichasOrigen.size(); j++) {
                        System.out.println("  Ficha origen [" + j + "]: " + fichasOrigen.get(j));
                    }
                    System.out.print("Índice de la ficha a tomar de esa combinación: ");
                    int idxFicha;
                    try {
                        idxFicha = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Índice no válido.");
                        break;
                    }
                    if (idxFicha < 0 || idxFicha >= fichasOrigen.size()) {
                        System.out.println("Índice de ficha fuera de rango.");
                        break;
                    }

                    Ficha fichaTomada = fichasOrigen.get(idxFicha);
                    System.out.print("Índices de fichas de tu mano a combinar con ella (ej: 4 5): ");
                    List<Ficha> adicionales = parsearIndices(scanner.nextLine().trim(), jugador.getMano().getFichas());

                    IComando cmd = new MoverFichaEntreMeldsComando(
                            jugador,
                            juego.getMesa(),
                            origen,
                            fichaTomada,
                            adicionales,
                            juego.getMeldFactory()
                    );

                    if (juego.ejecutarComando(cmd)) {
                        System.out.println("¡Movimiento realizado con éxito!");
                    } else {
                        System.out.println("Movimiento inválido. Revisa que el grupo/escalera en mesa siga siendo válido y que la nueva combinación sea legal.");
                    }
                }
            }
        
        }
    }

    private List<Ficha> parsearIndices(String linea, List<Ficha> mano) {
        List<Ficha> resultado = new ArrayList<>();
        if (linea.isEmpty()) return resultado;
        for (String parte : linea.split("\\s+")) {
            try {
                int idx = Integer.parseInt(parte);
                if (idx >= 0 && idx < mano.size()) {
                    Ficha candidata = mano.get(idx);
                    if (!resultado.contains(candidata)) {
                        resultado.add(candidata);
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return resultado;
    }
}