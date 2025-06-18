/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author HP
 */
public class MenuFilaRepositorio {
    private static final Path PATH = Paths.get("data/menu.json");
    private ObjectMapper mapper = new ObjectMapper();
    
    // leer todos los MenuItem data/menu.json
    public List<MenuItem> cargarTodo() throws IOException{
        if(!Files.exists(PATH)) return new ArrayList<>();
        return mapper.readValue(PATH.toFile(), new TypeReference<List<MenuItem>>() {});
    }
    // guarda todos los MenuItem data/menu.json
    public void guardarTodo(List<MenuItem> items) throws IOException{
        Files.createDirectories(PATH.getParent());
        mapper.writerWithDefaultPrettyPrinter().writeValue(PATH.toFile(), items);
    }
    
    
    
    
    
    
    
    
}
