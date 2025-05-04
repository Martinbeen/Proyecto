/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class Inventarios {
    List<Inventario> inventarios = new ArrayList<>();
    
    public void addInventario(Inventario inventario){
        inventarios.add(inventario);
    }
    public boolean removeInventario(String nombre){
        if(inventarios == null){
            return false;
        }
        else {
            for(Inventario inventario: inventarios){
                if(inventario.getMenu().getNombre().equals(nombre)){
                    inventarios.remove(inventario);
                    return true;
                }
            }
            return false;
        }
    }
    public void mostrarInventario(){
        if(this.inventarios.isEmpty()){
            JOptionPane.showMessageDialog((Component)null, "inventario vacio");
            return;
        }
        else {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Inventario: \n");
            for(Inventario inventario: inventarios){
                mensaje.append(inventario.toString()).append("\n");
            }
            JOptionPane.showMessageDialog((Component)null, mensaje.toString());
        }
    }
    public Menu buscarMenuInventario(String nombre){
        if(this.inventarios.isEmpty()){
            JOptionPane.showMessageDialog((Component)null, "inventario vacio");
            return null;
        }
        else{
            for(Inventario inventario: inventarios){
                if(inventario.getMenu().getNombre().equals(nombre)){ // encontrar el menu
                    if(inventario.getStock() > 0){ // si tiene suficiente stocks
                        inventario.reducirStock(1); // reducir el stock
                        return inventario.getMenu();
                    }
                    else{
                        JOptionPane.showMessageDialog((Component)null, "Menu agotado");
                        return null;
                    }
                }
            }
            JOptionPane.showMessageDialog((Component)null, "No existe el menu");
            return null;
        }
    }
}
