/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class Pedidos {
    private List<Menu> menus = new ArrayList<>();
    
    // agregar un menu al pedido
    public void addMenu(Menu menu){
        menus.add(menu);
    }
    // Eliminar un menu del pedido
    public boolean removeMenu(Menu menu){
        return menus.remove(menu);
    }
    // retorna el total sin impuesto
    public double getSubtotal(){
        return menus.stream()
                .mapToDouble(Menu::getPrecio)
                .sum();
    }
    // Buscar Menu
    public Menu buscarMenu(String nombre){
        if(menus == null){
            return null;
        }
        else{
            for(Menu menu: menus){
                if(menu.getNombre().equals(nombre)){
                    return menu;
                }
            }
            return null;
        }
    }
    public void mostrarInventario(){
        if(this.menus.isEmpty()){
            JOptionPane.showMessageDialog((Component)null, "Pedidos vacio");
            return;
        }
        else {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Pedidos: \n");
            for(Menu menu: menus){
                mensaje.append(menu.toString());
            }
            JOptionPane.showMessageDialog((Component)null, mensaje.toString());
        }
    }
}
