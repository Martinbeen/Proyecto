/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gestion_restaurante;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class Gestion_Restaurante {

    static Inventarios inventarios = new Inventarios(); 
    static Pedidos pedidos = new Pedidos();
    
    public static void Agregar(Inventario inventario){
        inventarios.addInventario(inventario);
    }
    public static void Agregar(Menu menu){
        pedidos.addMenu(menu);
    }
    
    public static void main(String[] args) {
        
        while(true){
            try{
                int opcion = Integer.parseInt(JOptionPane.showInputDialog("-----Inventario-----\n1. ver Inventario"
                        + "\n2. agregar Inventario\n3. Eliminar Inventario\n-----Pedido-----\n4. ver Pedidos"
                        + "\n5. agregar Pedido\n6. Eliminar Pedido\n7. Salir"));
            switch(opcion){
                case 1:
                    inventarios.mostrarInventario();
                    break;
                    
                case 2:
                    String newNombre = JOptionPane.showInputDialog("Ingresar nombre: ");
                    double precio = Double.parseDouble(JOptionPane.showInputDialog("Ingresar precio: "));
                    int stock = Integer.parseInt(JOptionPane.showInputDialog("Ingresar stock: "));
                    Menu newMenu = new Menu(newNombre, precio);
                    Agregar(new Inventario(newMenu, stock));
                    break;
                    
                case 3:
                    String nombre = JOptionPane.showInputDialog("Ingresar nombre del Menu: ");
                    
                    if(inventarios.removeInventario(nombre)){
                        JOptionPane.showMessageDialog((Component)null, "Se Elimino correctamente");
                    }
                    else{
                        JOptionPane.showMessageDialog((Component)null, "No existe el menu");
                    }
                    break;
                case 4:
                    pedidos.mostrarInventario();
                    break;
                    
                case 5:
                    String nombrePlato = JOptionPane.showInputDialog("Ingresar nombre del Menu: ");
                    Menu menu = inventarios.buscarMenuInventario(nombrePlato);
                    
                    if(menu == null){}
                    else{
                        pedidos.addMenu(menu);
                        JOptionPane.showMessageDialog((Component)null, "se agrego correctamenteu al pedido");
                    }
                    break;
                    
                case 6:
                    String nombreMenu = JOptionPane.showInputDialog("Ingresar nombre del Menu: ");
                    Menu nombrePedido = pedidos.buscarMenu(nombreMenu);
                    
                    if(nombrePedido == null){
                        JOptionPane.showMessageDialog((Component)null, "No existe el pedido");
                    }
                    else {
                        pedidos.removeMenu(nombrePedido);
                        JOptionPane.showMessageDialog((Component)null, "Se elimino el pedido");
                    }
                    break;
                    
                case 7:
                    JOptionPane.showMessageDialog((Component)null, "Saliendo...");
                    return;
                    
                default:
                    JOptionPane.showMessageDialog((Component)null, "Porfavor ingresar valores del 1 al 7");
                    return;
            }
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Ingrese un numero valido");
            }
        }
    }
    
}
