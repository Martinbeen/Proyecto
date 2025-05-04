/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;


public class Inventario {
    private Menu menu;
    private int stock;

    public Inventario(Menu menu, int stock) {
        this.menu = menu;
        this.stock = stock;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getStock() {
        return stock;
    }
    
    // incrementa el stock en la cantidad dada
    public void addStock(int cantidad) {
        if(cantidad >= 0){
            this.stock += cantidad;
        }
    }
    // redue el stock si hay suficiente
    public void reducirStock(int cantidad){
        if(cantidad <= stock){
            this.stock -= cantidad;
        }
    }
    
    @Override
    public String toString(){
        return "Nombre = " + menu + ", stock = " + stock;
    }
}
