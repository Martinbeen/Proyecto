/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;


public class InventarioServicio {
    private List<InventarioItem> inventario;
    private InventarioFilaRepositorio invRepo;
    
    public InventarioServicio(){
        this.invRepo = new InventarioFilaRepositorio();
        try {
            this.inventario = invRepo.cargarTodo();
        } catch(IOException e){
            this.inventario = new java.util.ArrayList<>();
        }
    }
    // obtiene la cantidad en stock de un platillo por id
    public int getStock(int itemId){
        Optional<InventarioItem> opt = inventario.stream()
                .filter(i -> i.getItem().getId() == itemId)
                .findFirst();
        return opt.map(InventarioItem::getStock).orElse(0);
    }
    // reduce el stock de un platillo
    public void reducirStock(int itemId, int cantidad){
        Optional<InventarioItem> opt = inventario.stream()
                .filter(i -> i.getItem().getId() == itemId)
                .findFirst();
        if(!opt.isPresent()){
            throw new IllegalArgumentException("No existe item con Id " + itemId);
        }
        InventarioItem ii = opt.get();
        if(ii.getStock() < cantidad){
            throw new InsuficienteStockException("Stock insuficiente para el platillo: " + ii.getItem().getNombre());
        }
        ii.reducirStock(cantidad);
    }
    // incrementa el stock al cancelar un pedido
    public void incrementarStock(int itemId, int cantidad){
        Optional<InventarioItem> opt = inventario.stream()
                .filter(i -> i.getItem().getId() == itemId)
                .findFirst();
        if(!opt.isPresent()){
            throw new IllegalArgumentException("No existe item con Id " + itemId);
        }
        opt.get().incrementarStock(cantidad);
    }
    // persiste el inventario en csv
    public void persistirInventario() throws IOException{
        invRepo.guardarTodo(inventario);
    }
    // actualiza el stock inicial
    public void cargarStockInicial(List<InventarioItem> nuevos) throws IOException{
        this.inventario = nuevos;
        invRepo.guardarTodo(inventario);
    }
    // obtiene la lista completa de invetarioitem
    public List<InventarioItem> listarInventario(){
        return new java.util.ArrayList<>(inventario);
    }
}
