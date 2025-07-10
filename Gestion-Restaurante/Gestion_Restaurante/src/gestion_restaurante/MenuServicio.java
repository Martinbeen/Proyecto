/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author HP
 */
public class MenuServicio {
    private MenuFilaRepositorio menuRepo;
    private List<MenuItem> menuItems;
    
    public MenuServicio(){
        this.menuRepo = new MenuFilaRepositorio();
        try{
            this.menuItems = menuRepo.cargarTodo();
        } catch (IOException e){
            this.menuItems = new java.util.ArrayList<>();
        }
    }
    // agrega un nuevo platillo al menu
    public MenuItem agregarPlatillo(int id, String nombre, double precio) throws IOException{
        Optional<MenuItem> existe = menuItems.stream()
                .filter(m -> m.getId() == id)
                .findFirst();
        if(existe.isPresent()){
            throw new IllegalArgumentException("Ya existe un platillo con ID " + id);
        }
        MenuItem mi = new MenuItem(id, nombre, precio);
        menuItems.add(mi);
        menuRepo.guardarTodo(menuItems);
        return mi;
    }
    // actualiza los datos de un platillo existente
    public void actualizarPlatillo(MenuItem actualizado) throws IOException{
        menuItems.removeIf(m -> m.getId() == actualizado.getId());
        menuItems.add(actualizado);
        menuRepo.guardarTodo(menuItems);
    }
    // elimina un platillo por id
    public boolean eliminarPlatillo(int id) throws IOException{
        boolean removed = menuItems.removeIf(m -> m.getId() == id);
        if(removed){
            menuRepo.guardarTodo(menuItems);
        }
        return removed;
    }
    // obtiene la lista de todos los platillos
    public List<MenuItem> listarPlatillos(){
        return new java.util.ArrayList<>(menuItems);
    }
    // busca un platillo por Id
    public MenuItem buscarPlatillo(int id){
        return menuItems.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }
    /** Busca platillos cuyo nombre contenga el texto dado (case‑insensitive) */
    public List<MenuItem> buscarPorNombre(String texto) {
        String lower = texto.toLowerCase();
        return menuItems.stream()
            .filter(m -> m.getNombre().toLowerCase().contains(lower))
            .collect(Collectors.toList());
}
}
