/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author HP
 */
public class ReportServicio {
    private PedidoServicio pedidoServicio;

    public ReportServicio(PedidoServicio pedidoServicio) {
        this.pedidoServicio = pedidoServicio;
    }
    
    //Exporta reporte de ventas diarias a csv
    public void exportarReporteVentasDiario() throws IOException{
        LocalDate hoy = LocalDate.now();
        String nombreArchivo = String.format("reportes/ventas_%s.csv", hoy.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy")));
        List<Pedido> pedidos = pedidoServicio.listarPedidos();
        // asegurar directorio
        Path carpeta = Paths.get("reprotes");
        Files.createDirectories(carpeta);
        
        try(BufferedWriter bw = Files.newBufferedWriter(carpeta.resolve(nombreArchivo))){
            bw.write("IDPedido;FechaHora;ItemID;NombreItem;Precio");
            bw.newLine();
            for(Pedido p : pedidos){
                for(MenuItem mi : p.getItems()){
                    bw.write(String.format("%d;%s;%d;%s;%.2f",
                            p.getId(),
                            p.getFechaHora(),
                            mi.getId(),
                            mi.getNombre(),
                            mi.getPrecio()));
                    bw.newLine();
                }
            }
        }
    }
    // crea una copia de seguridad de todos los archivos de datos
    public void crearCopiaSeguridad() throws IOException{
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreZip = String.format("backup/backup_%s.zip", timestamp);
        Path zipPath = Paths.get(nombreZip);
        Files.createDirectories(zipPath.getParent());
        
        try(FileOutputStream fos = new FileOutputStream(zipPath.toFile());
                java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(fos)){
            // incluir
            String[] archivos = {"data/users.csv", "data/inventory.csv", "data/pedidos.csv", "data/menu.json"};
            for(String ruta : archivos){
                File file = new File(ruta);
                if(file.exists()){
                    try(FileInputStream fis = new FileInputStream(file)){
                        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(ruta);
                        zos.putNextEntry(zipEntry);
                        byte[] buffer = new byte[1024];
                        int len;
                        while((len = fis.read(buffer)) > 0){
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                    }
                }
            }
        }
    }
}
