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

public class UsuarioFilaRepositorio {
    private static final Path PATH = Paths.get("data/users.csv");

    /** Carga todos los usuarios desde data/users.csv, saltando el encabezado. */
    public List<Usuario> cargarTodo() throws IOException {
        List<Usuario> lista = new ArrayList<>();

        if (!Files.exists(PATH)) {
            Files.createDirectories(PATH.getParent());
            Files.createFile(PATH);
            return lista;
        }

        try (BufferedReader br = Files.newBufferedReader(PATH)) {
            String linea;
            boolean esPrimera = true;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                // Saltar encabezado
                if (esPrimera) {
                    esPrimera = false;
                    continue;
                }
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] cols = linea.split(";");
                if (cols.length < 3) continue;

                try {
                    int id = Integer.parseInt(cols[0].trim());
                    String nombre = cols[1].trim();
                    String rol    = cols[2].trim();
                    lista.add(new Usuario(id, nombre, rol));
                } catch (NumberFormatException ex) {
                    System.err.println("Entrada inválida en users.csv: " + linea);
                }
            }
        }

        return lista;
    }

    /** Guarda todos los usuarios en data/users.csv, incluyendo encabezado. */
    public void guardarTodo(List<Usuario> usuarios) throws IOException {
        Files.createDirectories(PATH.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(PATH)) {
            // Encabezado
            bw.write("ID;NOMBRE;ROL");
            bw.newLine();
            for (Usuario u : usuarios) {
                bw.write(u.getId() + ";" + u.getNombre() + ";" + u.getRol());
                bw.newLine();
            }
        }
    }
}
