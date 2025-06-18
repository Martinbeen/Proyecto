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
    private static final Path PATH = Paths.get("data/usuarios.csv");
    
    // cargar todos los usuarios desde data/Usuarios.csv
    public List<Usuario> cargarTodo() throws IOException{
        List<Usuario> list = new ArrayList<>();
        if(!Files.exists(PATH)) return list;
        try (BufferedReader br = Files.newBufferedReader(PATH)){
            String line;
            while((line = br.readLine()) != null){
                String[] cols = line.split(";");
                int id = Integer.parseInt(cols[0]);
                String nombre = cols[1];
                String rol = cols[2];
                list.add(new Usuario(id, nombre, rol));
            }
        }
        return list;
    } 
    // guardar todos los usuarios en data/Usuarios.csv
    public void guardarTodo(List<Usuario> usuarios) throws IOException{
        Files.createDirectories(PATH.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(PATH)){
            for (Usuario u: usuarios){
                bw.write(u.getId() + ";" + u.getNombre() + ";" + u.getRol());
                bw.newLine();
            }
        }
    }
    
}
