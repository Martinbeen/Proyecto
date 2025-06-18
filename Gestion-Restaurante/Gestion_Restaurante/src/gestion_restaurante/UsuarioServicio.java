/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UsuarioServicio {
    private UsuarioFilaRepositorio usuarioRepo;
    private List<Usuario> usuarios;
    
    public UsuarioServicio(){
        this.usuarioRepo = new UsuarioFilaRepositorio();
        try{
            this.usuarios = usuarioRepo.cargarTodo();
        } catch(IOException e){
            this.usuarios = new java.util.ArrayList<>();
        }
    }
    // crear un nuevo usuario y lo persiste en users.csv
    public Usuario crearUsuario(int id, String nombre, String rol) throws IOException{
        Optional<Usuario> existe = usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
        if(existe.isPresent()){
            throw new IllegalArgumentException("Ya existe un usuario con ID " + id);
        }
        Usuario user = new Usuario(id, nombre, rol);
        usuarios.add(user);
        usuarioRepo.guardarTodo(usuarios);
        return user;
    }
    // actualiza datos de un usuario existente
    public void actualizarUsuario(Usuario userActualizado) throws IOException{
        usuarios.removeIf(u -> u.getId() == userActualizado.getId());
        usuarios.add(userActualizado);
        usuarioRepo.guardarTodo(usuarios);
    }
    //Elimina un usuario por su id
    public boolean eliminarUsuario(int id) throws IOException{
        boolean removed = usuarios.removeIf(u -> u.getId() == id);
        if(removed){
            usuarioRepo.guardarTodo(usuarios);
        }
        return removed;
    }
    // obtiene la lista de usuarios
    public List<Usuario> listaUsuarios(){
        return new java.util.ArrayList<>(usuarios);
    }
    // busca un usuario por id
    public Usuario buscarUsuario(int id){
        return usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    
    
}
