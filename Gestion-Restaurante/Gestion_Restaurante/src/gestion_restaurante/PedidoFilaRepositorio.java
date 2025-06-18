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
import java.util.ArrayList;
import java.util.List;

public class PedidoFilaRepositorio {
    private static final Path PATH = Paths.get("data/pedidos.csv");
    private MenuServicio menuServicio = new MenuServicio();
    
    // lee todos los pedidod desde data/pedidos.csv
    public List<Pedido> cargarTodo() throws IOException{
        List<Pedido> list = new ArrayList<>();
        if(!Files.exists(PATH)) return list;
        
        try (BufferedReader br = Files.newBufferedReader(PATH)){
            String line;
            while((line = br.readLine()) != null){
                String[] cols = line.split(";");
                int id = Integer.parseInt(cols[0]);
                LocalDateTime fecha = LocalDateTime.parse(cols[1]);
                Pedido p = new Pedido(id);
                p.setFechaHora(fecha);
                
                if(cols.length > 2 && !cols[2].isEmpty()){
                    String[] itemsPart = cols[2].split(";");
                    for(String itemIdstr : itemsPart){
                        int itemId = Integer.parseInt(itemIdstr);
                        MenuItem mi = menuServicio.buscarPlatillo(itemId);
                        if(mi != null){
                            p.agregarItem(mi);
                        }
                    }
                }
                list.add(p);
            }
        }
        return list;
    }
    // guarda todos los pedidos
    public void guardarTodo(List<Pedido> pedidos) throws IOException{
        Files.createDirectories(PATH.getParent());
        try(BufferedWriter bw = Files.newBufferedWriter(PATH)){
            for(Pedido p : pedidos){
                StringBuilder sb = new StringBuilder();
                sb.append(p.getId()).append(";").append(p.getFechaHora()).append(";");
                
                List<String> ids = new ArrayList<>();
                for(MenuItem mi : p.getItems()){
                    ids.add(String.valueOf(mi.getId()));
                }
                sb.append(String.join(",", ids));
                bw.write(sb.toString());
                bw.newLine();
            }  
        }  
    }
}
