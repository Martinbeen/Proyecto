/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

import java.awt.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Pedido {
    private int id;
    private List<MenuItem> items;
    private LocalDateTime fechaHora;
    private double descuentoPct; 
    
    public Pedido(){
        this.items = new ArrayList<>();
        this.fechaHora = LocalDateTime.now();
    }

    public Pedido(int id) {
        this();
        this.id = id;
        this.descuentoPct = 0.0;
    }

    public Pedido(int id, List<MenuItem> items) {
        this(id);
        this.items = new ArrayList<>(items);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
    
    public LocalDateTime getFechaHora(){
        return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora){
        this.fechaHora = fechaHora;
    }
    
    // agregar un menu al pedido
    public void agregarItem(MenuItem item){
        items.add(item);
    }
    // Eliminar un menu del pedido
    public boolean eliminarItem(MenuItem item){
        return items.remove(item);
    }
    public double getDescuentoPct() { return descuentoPct; }
    public void setDescuentoPct(double pct) { this.descuentoPct = pct; }

    // retorna el total sin impuesto y con descuento
    public double getSubtotal() {
        double suma = items.stream().mapToDouble(MenuItem::getPrecio).sum();
        return suma * (1 - descuentoPct / 100);
    }
    @Override
    public String toString(){
        return "id = " + id + ", items = " + items + ", fechaHora = " + fechaHora + ", subtotal= " + getSubtotal();
    }
}
