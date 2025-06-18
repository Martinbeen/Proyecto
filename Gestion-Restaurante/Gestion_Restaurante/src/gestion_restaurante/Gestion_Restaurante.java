/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gestion_restaurante;

import java.awt.Component;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class Gestion_Restaurante {
    
    public static void main(String[] args) throws IOException {
        try{
            //inicializar servicios
            UsuarioServicio usuarioServicio = new UsuarioServicio();
            MenuServicio menuServicio = new MenuServicio();
            InventarioServicio inventarioServicio = new InventarioServicio();
            PedidoServicio pedidoServicio = new PedidoServicio(inventarioServicio);
            FacturaServicio facturaServicio = new FacturaServicio();
            ReportServicio reportServicio = new ReportServicio(pedidoServicio);
            //gestion de usuarios
            String input = JOptionPane.showInputDialog(null, "Deseas crear un usuario nuevo? (S/N)", "Usuarios", JOptionPane.QUESTION_MESSAGE);
            if(input != null && input.equalsIgnoreCase("S")){
                String idstr = JOptionPane.showInputDialog("Ingresa ID de usuario");
                int id = Integer.parseInt(idstr);
                String nombre = JOptionPane.showInputDialog("Ingresa nombre de usuarios: ");
                String rol = JOptionPane.showInputDialog("Ingresa rol (Cajero/Admin/Cocinero): ");
                Usuario u = usuarioServicio.crearUsuario(id, nombre, rol);
                JOptionPane.showInputDialog(null, "Usuario creado:\n" + u, "Usuarios", JOptionPane.INFORMATION_MESSAGE);
            }
            // mostrar lista actual de usuarios
            StringBuilder usuariosMsg = new StringBuilder("Usuarios registrados:\n");
            for(Usuario u : usuarioServicio.listaUsuarios()){
                usuariosMsg.append(u).append("\n");
            }
            JOptionPane.showInputDialog(null, usuariosMsg.toString(), "Usuarios", JOptionPane.INFORMATION_MESSAGE);
            // gestion de menu e inventario
            int crearMenu = JOptionPane.showConfirmDialog(null, "Deseas agregar platillos al menu?", "Menu e Inventario", JOptionPane.YES_NO_OPTION);
            if(crearMenu == JOptionPane.YES_OPTION){
                String platilloIdStr = JOptionPane.showInputDialog("Id del platillo: ");
                int platilloid = Integer.parseInt(platilloIdStr);
                String platilloNombre = JOptionPane.showInputDialog("Nombre del platillo: ");
                String precioStr = JOptionPane.showInputDialog("Precion del platillo: ");
                double precio = Double.parseDouble(precioStr);
                MenuItem mi = menuServicio.agregarPlatillo(platilloid, platilloNombre, precio);
                JOptionPane.showInputDialog(null, "Platillo agregado:\n" + mi, "Menu", JOptionPane.INFORMATION_MESSAGE);   
            }
            // cargar stock inicial, solo si hay al menos un platillo
            if(!menuServicio.listarPlatillos().isEmpty()){
                StringBuilder menuListMsg = new StringBuilder("Menu actual:\n");
                for(MenuItem mi : menuServicio.listarPlatillos()){
                    menuListMsg.append(mi).append("\n");
                }
                JOptionPane.showInputDialog(null, menuListMsg.toString(), "Menu", JOptionPane.INFORMATION_MESSAGE);
                int cargarStock = JOptionPane.showConfirmDialog(null, "Deseas cargar stock inicial para cada platillo?", "Inventario", JOptionPane.YES_NO_OPTION);
                if(cargarStock == JOptionPane.YES_OPTION){
                    for(MenuItem mi : menuServicio.listarPlatillos()){
                        String stockStr = JOptionPane.showInputDialog("Stock para \"" + mi.getNombre() + "\" (ID " + mi.getId() + "):");
                        int stock = Integer.parseInt(stockStr);
                        inventarioServicio.cargarStockInicial(Arrays.asList(new InventarioItem(mi, stock)));
                    }
                    JOptionPane.showInputDialog(null, "Stock inicial cargado.", "Inventario", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            // Mostrar inventario actual
            StringBuilder invMsg = new StringBuilder("inventario actual:\n");
            for(InventarioItem ii : inventarioServicio.listarInventario()){
                invMsg.append(ii).append("\n");
            }
            JOptionPane.showMessageDialog(null, invMsg.toString(), "Inventario", JOptionPane.INFORMATION_MESSAGE);
            // crear un pedido
            int nuevoPedido = JOptionPane.showConfirmDialog(null, "Deseas crear un nuevo pedido de prueba?", "Pedidos", JOptionPane.YES_NO_OPTION);
            if(nuevoPedido == JOptionPane.YES_OPTION){
                String pedidoIdStr = JOptionPane.showInputDialog("ID para este pedido: ");
                int pedidoId = Integer.parseInt(pedidoIdStr);
                // seleccion de platillos
                java.util.List<MenuItem> items = menuServicio.listarPlatillos();
                if(items.isEmpty()){
                    JOptionPane.showMessageDialog(null, "No hay platillos disponibles para crear un pedido.", "Pedidos", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    Pedido p = pedidoServicio.crearPedido(pedidoId, items);
                    JOptionPane.showMessageDialog(null, "Pedido creado:\n" + p, "Pedidos", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            // Mostrar todos los pedidos
            StringBuilder pedidosMsg = new StringBuilder("Pedidos registrados: \n");
            for(Pedido p : pedidoServicio.listarPedidos()){
                pedidosMsg.append(p).append("\n");
            }
            JOptionPane.showMessageDialog(null, pedidosMsg.toString(), "Pedidos", JOptionPane.INFORMATION_MESSAGE);
            // Facturacion
            int facturar = JOptionPane.showConfirmDialog(null, "Deseas facturar un pedido existente?", "Facturacion", JOptionPane.YES_NO_OPTION);
            if(facturar == JOptionPane.YES_OPTION){
                String idFactsStr = JOptionPane.showInputDialog("ID del pedido a facturar:");
                int idFact = Integer.parseInt(idFactsStr);
                Pedido p = pedidoServicio.buscarPedido(idFact);
                if(p == null){
                    JOptionPane.showMessageDialog(null, "No se encontro pedido con ID " + idFact, "Facturacion", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    String impuestoStr = JOptionPane.showInputDialog("Porcentaje de impuesto (ej.18): ");
                    double impuesto = Double.parseDouble(impuestoStr);
                    double totalSin = facturaServicio.calcularTotal(p);
                    double totalCon = facturaServicio.calcularTotal(p, impuesto);
                    
                    String detalles = String.format(
                            "Subtotal: $/ %.2f%nImpuesto (%.1f%%): $/ %.2f%nTotal: $/ %.2f",
                            totalSin, impuesto, totalSin * impuesto / 100, totalCon);
                    JOptionPane.showMessageDialog(null, detalles, "Totales", JOptionPane.INFORMATION_MESSAGE);
                    // Generar boleta pdf
                    int generaPdf = JOptionPane.showConfirmDialog(null, "Deseas generar boleta PDF?", "Facturacion", JOptionPane.YES_NO_OPTION);
                    if(generaPdf == JOptionPane.YES_OPTION){
                        String rutaBoleta = "boletas/boleta_" + p.getId() + ".pdf";
                        facturaServicio.generarBoletaPdf(p, null, impuesto, rutaBoleta);
                        JOptionPane.showMessageDialog(null, "Boleta generada en:\n" + rutaBoleta, "Facturacion", JOptionPane.INFORMATION_MESSAGE);
                
                    }
                }
            }
            // Reportes
            int generarReporte = JOptionPane.showConfirmDialog(null, "Deseas exportar reporte de ventas diario?", "Reportes", JOptionPane.YES_NO_OPTION);
            if(generarReporte == JOptionPane.YES_OPTION){
                reportServicio.exportarReporteVentasDiario();
                JOptionPane.showMessageDialog(null, "Reporte de ventas diario exportando en carpeta \"reportes/\".", "Reportes", JOptionPane.INFORMATION_MESSAGE);
            }
            int creaBackup = JOptionPane.showConfirmDialog(null, "Deseas crear copia de seguridad de datos?", "Reportes", JOptionPane.YES_NO_OPTION);
            if(creaBackup == JOptionPane.YES_OPTION){
                reportServicio.crearCopiaSeguridad();
                JOptionPane.showMessageDialog(null, "Copia de seguridad creada en carpeta \"backup/\".", "Reportes", JOptionPane.INFORMATION_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Proceso finalizado!", "Sistema Restaurante", JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Ocurrio un error:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        
        
        
        
        /*while(true){
            try{
                int opcion = Integer.parseInt(JOptionPane.showInputDialog("-----Inventario-----\n1. ver Inventario"
                        + "\n2. agregar Inventario\n3. Eliminar Inventario\n-----Pedido-----\n4. ver Pedidos"
                        + "\n5. agregar Pedido\n6. Eliminar Pedido\n7. Salir"));
        }*/
    }
}
