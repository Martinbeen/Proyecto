/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class InventarioFilaRepositorio {
    private static final Path PATH = Paths.get("data/inventory.csv");

    /** Carga todos los InventoryItem desde data/inventory.csv, saltando encabezado. */
    public List<InventarioItem> cargarTodo() throws IOException {
        List<InventarioItem> lista = new ArrayList<>();

        if (!Files.exists(PATH)) {
            Files.createDirectories(PATH.getParent());
            Files.createFile(PATH);
            return lista;
        }

        try (BufferedReader br = Files.newBufferedReader(PATH)) {
            String linea;
            boolean esPrimeraLinea = true;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                // Saltar encabezado
                if (esPrimeraLinea) {
                    esPrimeraLinea = false;
                    continue;
                }
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] cols = linea.split(";");
                if (cols.length < 3) continue;

                try {
                    int itemId = Integer.parseInt(cols[0].trim());
                    String nombre = cols[1].trim();
                    int stock  = Integer.parseInt(cols[2].trim());
                    // Construir el MenuItem con solo id y nombre (precio se ignora aquí)
                    MenuItem mi = new MenuItem(itemId, nombre, 0.0);
                    lista.add(new InventarioItem(mi, stock));
                } catch (NumberFormatException ex) {
                    System.err.println("Entrada inválida en inventory.csv: " + linea);
                }
            }
        }
        return lista;
    }

    /** Guarda todos los InventoryItem en data/inventory.csv, incluyendo encabezado. */
    public void guardarTodo(List<InventarioItem> items) throws IOException {
        Files.createDirectories(PATH.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(PATH)) {
            // Cabecera para Excel
            bw.write("ID;Nombre;Stock");
            bw.newLine();
            for (InventarioItem it : items) {
                bw.write(it.getItem().getId()
                         + ";" + it.getItem().getNombre()
                         + ";" + it.getStock());
                bw.newLine();
            }
        }
    }
}
