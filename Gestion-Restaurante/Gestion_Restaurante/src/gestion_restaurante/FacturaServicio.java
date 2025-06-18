/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
/**
 *
 * @author HP
 */
public class FacturaServicio {
    
    // calcular el total sin impuesto
    public double calcularTotal(Pedido pedido){
        if(pedido.getItems().isEmpty()){
            throw new EmptyOrderException("La orden no contiene items");
        }
        return pedido.getSubtotal();
    }
    // calcular el total aplicando un porcentaje de impuesto
    public double calcularTotal(Pedido pedido, double porcentajeImpuesto){
        double subtotal = calcularTotal(pedido);
        return subtotal + (subtotal * porcentajeImpuesto / 100);
    }
    // genear boleta en pdf usando iText
    public void generarBoletaPdf(Pedido pedido, String cliente, double impuesto, String rutaSalida) throws IOException, DocumentException{
        if(pedido.getItems().isEmpty()){
            throw new EmptyOrderException("La orden no contiene items");
        }
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(rutaSalida));
        document.open();
        // cabecera
        document.add(new Paragraph("Restaurante Comunitario", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(new Paragraph("Boleta de venta", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(new Paragraph("ID Pedido: " + pedido.getId()));
        if(cliente != null){
            document.add(new Paragraph("Cliente: " + cliente));
        }
        document.add(new Paragraph("Fecha: " + pedido.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        document.add(Chunk.NEWLINE);
        // tabla de items
        PdfPTable table = new PdfPTable(3);
        table.addCell("ID item");
        table.addCell("Nombre");
        table.addCell("Precio");
        for(var item : pedido.getItems()){
            table.addCell(String.valueOf(item.getId()));
            table.addCell(item.getNombre());
            table.addCell(String.format("%.2f", item.getPrecio()));
        }
        document.add(table);
        
        document.add(Chunk.NEWLINE);
        double subtotal = calcularTotal(pedido);
        double total = calcularTotal(pedido, impuesto);
        document.add(new Paragraph(String.format("Subtotal: $/ %.2f", subtotal)));
        document.add(new Paragraph(String.format("Impuesto (%.1f%%): $/ %.2f", impuesto, subtotal * impuesto / 100)));
        document.add(new Paragraph(String.format("Total: $/ %.2f", total)));
        document.close();
    }

}
