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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
    /** Copia el PDF de boleta a la carpeta facturacion/ */
    public void registrarFacturacion(String pdfRuta) throws IOException {
        Path fuente = Paths.get(pdfRuta);
        if (!Files.exists(fuente) || !pdfRuta.endsWith(".pdf")) {
            throw new IOException("Archivo inválido o no existe: " + pdfRuta);
        }
        // Directorio en la carpeta de usuario, fuera del proyecto
        Path destinoDir = Paths.get(System.getProperty("user.home"), "FacturacionRecords");
        Files.createDirectories(destinoDir);

        Path destino = destinoDir.resolve(fuente.getFileName());
        Files.copy(fuente, destino, StandardCopyOption.REPLACE_EXISTING);
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
    public void backupBoletas() throws IOException {
    Path boletasDir = Paths.get("boletas");
    if (!Files.exists(boletasDir) || !Files.isDirectory(boletasDir)) {
        throw new IOException("No existe el directorio de boletas: " + boletasDir);
    }
    // Carpeta de destino fuera del proyecto, con timestamp
    String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    Path destinoDir = Paths.get(System.getProperty("user.home"), "BoletasBackup", ts);
    Files.createDirectories(destinoDir);

    // Copiar cada PDF
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(boletasDir, "*.pdf")) {
        for (Path pdf : stream) {
            Path destino = destinoDir.resolve(pdf.getFileName());
            Files.copy(pdf, destino, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
}
