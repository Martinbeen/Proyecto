/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;


public class InventarioItem {
    private MenuItem item;
    private int stock;
    
    public InventarioItem(){}
    
    public InventarioItem(MenuItem menu, int stock) {
        this.item = menu;
        this.stock = stock;
    }
    
    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public int getStock() {
        return stock;
    }
    
    // incrementa el stock en la cantidad dada
    public void incrementarStock(int cantidad) {
        if(cantidad > 0){
            this.stock += cantidad;
        }
    }
    // redue el stock si hay suficiente
    public void reducirStock(int cantidad){
        if(cantidad <= 0){
            throw new IllegalArgumentException("Cantidad debe ser positiva");
        }
        if(cantidad > stock){
            throw new IllegalArgumentException("Stock insuficiente para el item: " + item.getNombre());
        }
        this.stock -= cantidad;
    }
    
    @Override
    public String toString(){
        return "item = " + item + ", stock = " + stock;
    }
}
