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

    public PedidoServicio(InventarioServicio invServicio) {
        this.inventarioServicio = invServicio;
        this.pedidoRepo = new PedidoFilaRepositorio();
        try {
            this.pedidos = pedidoRepo.cargarTodo();
        } catch (IOException e) {
            this.pedidos = new java.util.ArrayList<>();
        }
    }

    public Pedido crearPedido(int id, List<MenuItem> items) throws IOException {
        if (pedidos.stream().anyMatch(p -> p.getId() == id))
            throw new IllegalArgumentException("Ya existe un pedido con Id " + id);
        // validar y reducir stock
        for (MenuItem item : items) {
            if (inventarioServicio.getStock(item.getId()) <= 0)
                throw new InsuficienteStockException("Stock insuficiente para platillo: " + item.getNombre());
        }
        for (MenuItem item : items) {
            inventarioServicio.reducirStock(item.getId(), 1);
        }
        Pedido p = new Pedido(id, items);
        pedidos.add(p);
        guardarPedidos();  // persiste pedido e inventario
        return p;
    }

    public List<Pedido> listarPedidos() {
        return new java.util.ArrayList<>(pedidos);
    }

    public Pedido buscarPedido(int id) {
        return pedidos.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public boolean cancelarPedido(int id) throws IOException {
        Optional<Pedido> opt = pedidos.stream().filter(p -> p.getId() == id).findFirst();
        if (opt.isPresent()) {
            Pedido p = opt.get();
            for (MenuItem item : p.getItems()) {
                inventarioServicio.incrementarStock(item.getId(), 1);
            }
            pedidos.remove(p);
            guardarPedidos();
            return true;
        }
        return false;
    }

    public double aplicarDescuento(Pedido pedido, double porcentaje) {
        pedido.setDescuentoPct(porcentaje);
        return pedido.getSubtotal();
    }

    /** Persiste pedidos e inventario juntos */
    public void guardarPedidos() throws IOException {
        pedidoRepo.guardarTodo(pedidos);
        inventarioServicio.persistirInventario();
    }
}
