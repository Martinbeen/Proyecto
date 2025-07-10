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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PedidoFilaRepositorio {
    private static final Path PATH = Paths.get("data/pedidos.csv");
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private MenuServicio menuSvc = new MenuServicio();

    /** Carga pedidos con columna adicional de descuentoPct. */
    public List<Pedido> cargarTodo() throws IOException {
        List<Pedido> lista = new ArrayList<>();
        if (!Files.exists(PATH)) {
            Files.createDirectories(PATH.getParent());
            Files.createFile(PATH);
            return lista;
        }
        try (BufferedReader br = Files.newBufferedReader(PATH)) {
            String linea; boolean esPrimera = true;
            while ((linea = br.readLine()) != null) {
                if (esPrimera) { esPrimera = false; continue; }
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                String[] cols = linea.split(";");
                if (cols.length < 4) continue;
                int id = Integer.parseInt(cols[0].trim());
                LocalDateTime fecha = LocalDateTime.parse(cols[1].trim(), FORMAT);
                double pct = Double.parseDouble(cols[2].trim());
                String[] ids = cols[3].trim().split(",");
                Pedido p = new Pedido(id);
                p.setFechaHora(fecha);
                p.setDescuentoPct(pct);
                for (String sid : ids) {
                    if (sid.isBlank()) continue;
                    MenuItem mi = menuSvc.buscarPlatillo(Integer.parseInt(sid.trim()));
                    if (mi != null) p.agregarItem(mi);
                }
                lista.add(p);
            }
        }
        return lista;
    }

    /** Guarda pedidos con columna descuentoPct. */
    public void guardarTodo(List<Pedido> pedidos) throws IOException {
        Files.createDirectories(PATH.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(PATH)) {
            bw.write("ID;Fecha;DescuentoPct;Items");
            bw.newLine();
            for (Pedido p : pedidos) {
                StringBuilder sb = new StringBuilder();
                sb.append(p.getId()).append(";")
                  .append(p.getFechaHora().format(FORMAT)).append(";")
                  .append(String.format("%.2f", p.getDescuentoPct())).append(";");
                List<String> ids = new ArrayList<>();
                for (MenuItem mi : p.getItems()) ids.add(String.valueOf(mi.getId()));
                sb.append(String.join(",", ids));
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }
}
