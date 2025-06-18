/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public class PedidoServicio {
    private List<Pedido> pedidos;
    private PedidoFilaRepositorio pedidoRepo;
    private InventarioServicio inventarioServicio;
    
    public PedidoServicio(InventarioServicio invServicio){
        this.inventarioServicio = invServicio;
        this.pedidoRepo = new PedidoFilaRepositorio();
        try{
            this.pedidos = pedidoRepo.cargarTodo();
        } catch(IOException e){
            this.pedidos = new java.util.ArrayList<>();
        }
    }
    // crea un nuevo pedido con lista de Ids de platillos
    // descarga stock y persiste el pedido
    public Pedido crearPedido(int id, List<MenuItem> items) throws IOException{
        Optional<Pedido> existe = pedidos.stream().filter(p -> p.getId() == id).findFirst();
        if(existe.isPresent()){
            throw new IllegalArgumentException("Ya existe un pedido con Id " + id);
        }
        // validar stock para cada item
        for(MenuItem item : items){
            int stockActual = inventarioServicio.getStock(item.getId());
            if(stockActual <= 0){
                throw new InsuficienteStockException("Stock insuficiente para platillo: " + item.getNombre());
            }
        }
        // reducir stock
        for(MenuItem item : items){
            inventarioServicio.reducirStock(item.getId(), 1);
        }
        // crear y guardar
        Pedido p = new Pedido(id, items);
        pedidos.add(p);
        pedidoRepo.guardarTodo(pedidos);
        //actualizar inventario fisico
        inventarioServicio.persistirInventario();
        return p;
    }
    // lista todos los pedidos
    public List<Pedido> listarPedidos(){
        return new java.util.ArrayList<>(pedidos);
    }
    // busca un pedido por id
    public Pedido buscarPedido(int id){
        return pedidos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
    // Elimina un pedido
    public boolean cancelarPedido(int id) throws IOException{
        Optional<Pedido> opt = pedidos.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
        if(opt.isPresent()){
            Pedido p = opt.get();
            //devolver stock
            for(MenuItem item : p.getItems()){
                inventarioServicio.incrementarStock(item.getId(), 1);
            }
            pedidos.remove(p);
            pedidoRepo.guardarTodo(pedidos);
            inventarioServicio.persistirInventario();
            return true;
        }
        return false;
    }
}
