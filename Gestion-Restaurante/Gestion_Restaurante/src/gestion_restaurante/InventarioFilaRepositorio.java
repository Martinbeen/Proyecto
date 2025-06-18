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
    private MenuServicio menuServicio = new MenuServicio();
    
    // lee todos los invetariositems desde data/inventory.csv
    public List<InventarioItem> cargarTodo() throws IOException{
        List<InventarioItem> list = new ArrayList<>();
        if(!Files.exists(PATH)) return list;
        
        try(BufferedReader br = Files.newBufferedReader(PATH)){
            String line = br.readLine();
            if(line == null) return list;
            
            do{
                String[] cols = line.split(";");
                int itemId = Integer.parseInt(cols[0]);
                int stock = Integer.parseInt(cols[1]);
                MenuItem mi = menuServicio.buscarPlatillo(itemId);
                if(mi != null){
                    list.add(new InventarioItem(mi, stock));
                }
                line = br.readLine();
            } while(line != null);
        }
        return list;
    }
    // guarda todos los inventarioItem
    public void guardarTodo(List<InventarioItem> items) throws IOException{
        Files.createDirectories(PATH.getParent());
        try(BufferedWriter bw = Files.newBufferedWriter(PATH)){
            for(InventarioItem it : items){
                bw.write(it.getItem().getId() + ";" + it.getStock());
                bw.newLine();
            }
        }
    }
}
