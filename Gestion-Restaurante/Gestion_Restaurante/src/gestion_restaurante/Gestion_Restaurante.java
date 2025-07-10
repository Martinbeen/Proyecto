/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gestion_restaurante;

import java.awt.Component;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class Gestion_Restaurante {

    public static void menuUsuarios(UsuarioServicio svc) {
        while (true) {
            String s =
                "GESTIÓN DE USUARIOS\n\n" +
                "[1] Crear Usuario\n" +
                "[2] Editar Usuario\n" +
                "[3] Eliminar Usuario\n" +
                "[4] Listar Usuarios\n" +
                "[5] Volver\n\n" +
                "Elija opción:";
            String opcion = JOptionPane.showInputDialog(s);
            if (opcion == null || opcion.equals("5")) break;
            try {
                switch (opcion) {
                    case "1" -> crearUsuario(svc);
                    case "2" -> editarUsuario(svc);
                    case "3" -> {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("ID a eliminar:"));
                        boolean eliminar = svc.eliminarUsuario(id);
                        JOptionPane.showMessageDialog(null,eliminar ? "Usuario eliminado." : "No existe usuario con ID " + id);
                    }
                    case "4" -> listarUsuarios(svc);
                    default  -> mostrarError("Opción inválida");
                }
            } catch (Exception e) {
                mostrarError("Error: " + e.getMessage());
            }
        }
    }

    public static void menuMenu(MenuServicio svc, InventarioServicio invSvc) {
        while (true) {
            String s =
                "CATÁLOGO DE MENÚ\n\n" +
                "[1] Agregar Platillo\n" +
                "[2] Modificar Platillo\n" +
                "[3] Eliminar Platillo\n" +
                "[4] Listar Platillos\n" +
                "[5] Buscar Platillo por nombre\n" +
                "[6] Volver\n\n" +
                "Elija opción:";
            String sel = JOptionPane.showInputDialog(s);
            if (sel == null || sel.equals("6")) break;
            try {
                switch (sel) {
                    case "1" -> agregarPlatilloConStock(svc, invSvc);
                    case "2" -> modificarPlatilloConStock(svc, invSvc);
                    case "3" -> {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("ID a eliminar:"));
                        boolean ok = svc.eliminarPlatillo(id);
                        JOptionPane.showMessageDialog(null,
                            ok ? "Platillo eliminado." : "No existe platillo con ID " + id);
                    }
                    case "4" -> listarPlatillos(svc, invSvc);
                    case "5" -> {
                        String txt = JOptionPane.showInputDialog("Nombre o parte a buscar:");
                        List<MenuItem> res = svc.buscarPorNombre(txt);
                        // Mostrar resultados
                        StringBuilder sb = new StringBuilder("Resultados:\n");
                        for (var m : res) sb.append(m).append("\n");
                        JOptionPane.showMessageDialog(null, sb.toString());
                    }
                    default -> mostrarError("Opción inválida");
                }
            } catch (Exception e) {
                mostrarError("Error: " + e.getMessage());
            }
        }
    }
    public static void menuPedidos(PedidoServicio svc, MenuServicio menuSvc) {
        DateTimeFormatter fechaFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            String menu =
                "TOMA DE PEDIDOS\n\n" +
                "[1] Iniciar Pedido\n" +
                "[2] Agregar ítems a un pedido\n" +
                "[3] Eliminar ítems de un pedido\n" +
                "[4] Listar todos los pedidos\n" +
                "[5] Ver ítems de un pedido\n" +
                "[6] Aplicar descuento a un pedido\n" +
                "[7] Volver al menú principal\n\n" +
                "Elija opción:";
            String sel = JOptionPane.showInputDialog(null, menu, "Pedidos", JOptionPane.PLAIN_MESSAGE);
            if (sel == null || sel.equals("7")) break;

            try {
                switch (sel) {
                    case "1" -> {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("ID del nuevo pedido:"));
                        svc.crearPedido(id, List.of());
                        JOptionPane.showMessageDialog(null,
                            "Pedido iniciado. ID: " + id,
                            "Iniciar Pedido", JOptionPane.INFORMATION_MESSAGE);
                    }
                    case "2" -> {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("ID del pedido a modificar:"));
                        Pedido p = svc.buscarPedido(id);
                        if (p == null) { mostrarError("No existe pedido con ID " + id); break; }
                        String in = JOptionPane.showInputDialog("IDs de platillos a agregar (coma):");
                        if (in == null || in.isBlank()) break;
                        for (String pid : in.split(",")) {
                            MenuItem mi = menuSvc.buscarPlatillo(Integer.parseInt(pid.trim()));
                            if (mi != null) p.agregarItem(mi);
                        }
                        svc.guardarPedidos();
                        JOptionPane.showMessageDialog(null,
                            "Ítems agregados al pedido " + id,
                            "Agregar Ítems", JOptionPane.INFORMATION_MESSAGE);
                    }
                    case "3" -> {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("ID del pedido a modificar:"));
                        Pedido p = svc.buscarPedido(id);
                        if (p == null) { mostrarError("No existe pedido con ID " + id); break; }
                        String in = JOptionPane.showInputDialog("IDs de platillos a eliminar (coma):");
                        if (in == null || in.isBlank()) break;
                        for (String pid : in.split(",")) {
                            MenuItem mi = menuSvc.buscarPlatillo(Integer.parseInt(pid.trim()));
                            if (mi != null) p.eliminarItem(mi);
                        }
                        svc.guardarPedidos();
                        JOptionPane.showMessageDialog(null,
                            "Ítems eliminados del pedido " + id,
                            "Eliminar Ítems", JOptionPane.INFORMATION_MESSAGE);
                    }
                    case "4" -> {
                        StringBuilder sb = new StringBuilder("<html><body><b>Pedidos existentes:</b><br>");
                        for (Pedido p : svc.listarPedidos()) {
                            String f = p.getFechaHora().format(fechaFmt);
                            sb.append("ID ").append(p.getId())
                              .append(" | Fecha ").append(f)
                              .append(" | Ítems ").append(p.getItems().size())
                              .append("<br>");
                        }
                        sb.append("</body></html>");
                        JOptionPane.showMessageDialog(null, sb.toString(),
                                                      "Listar Pedidos", JOptionPane.INFORMATION_MESSAGE);
                    }
                    case "5" -> {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("ID del pedido:"));
                        Pedido p = svc.buscarPedido(id);
                        if (p == null) { mostrarError("No existe pedido con ID " + id); break; }
                        StringBuilder sb = new StringBuilder("<html><body><b>Ítems del pedido " + id + ":</b><br>");
                        for (MenuItem mi : p.getItems()) {
                            sb.append("ID ").append(mi.getId())
                              .append(" | ").append(mi.getNombre())
                              .append(" | S/ ").append(String.format("%.2f", mi.getPrecio()))
                              .append("<br>");
                        }
                        sb.append("</body></html>");
                        JOptionPane.showMessageDialog(null, sb.toString(),
                                                      "Ver Ítems", JOptionPane.INFORMATION_MESSAGE);
                    }
                    case "6" -> {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("ID del pedido:"));
                        Pedido p = svc.buscarPedido(id);
                        if (p == null) { mostrarError("No existe pedido con ID " + id); break; }
                        String dStr = JOptionPane.showInputDialog("Descuento (%) a aplicar:");
                        if (dStr == null || dStr.isBlank()) break;
                        double pct = Double.parseDouble(dStr.trim());
                        svc.aplicarDescuento(p, pct);
                        svc.guardarPedidos();
                        JOptionPane.showMessageDialog(null,
                            String.format("Descuento de %.1f%% aplicado al pedido %d.", pct, id),
                            "Aplicar Descuento", JOptionPane.INFORMATION_MESSAGE);
                    }
                    default -> mostrarError("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                mostrarError("Entrada numérica inválida.");
            } catch (InsuficienteStockException e) {
                mostrarError("Stock insuficiente: " + e.getMessage());
            } catch (IOException e) {
                mostrarError("Error al guardar cambios: " + e.getMessage());
            }
        }
    }

    public static void menuFacturacion(FacturaServicio factSvc, PedidoServicio pedSvc) {
        while (true) {
            String s =
                "FACTURACIÓN Y BOLETA\n\n" +
                "[1] Generar boleta PDF de un pedido\n" +
                "[2] Volver al menú principal\n\n" +
                "Elija opción:";
            String sel = JOptionPane.showInputDialog(null, s, "Facturación", JOptionPane.PLAIN_MESSAGE);
            if (sel == null || sel.equals("2")) {
                break;  // regresa al menú principal
            }
            try {
                if ("1".equals(sel)) {
                    generarBoletaPDF(factSvc, pedSvc);
                } else {
                    mostrarError("Opción inválida");
                }
            } catch (Exception e) {
                mostrarError("Error generando boleta: " + e.getMessage());
            }
        }
    }

    public static final DateTimeFormatter FECHA_FORMAT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        // Inicializar servicios
        UsuarioServicio usuarioSvc = new UsuarioServicio();
        MenuServicio    menuSvc    = new MenuServicio();
        InventarioServicio invSvc  = new InventarioServicio();
        PedidoServicio  pedidoSvc  = new PedidoServicio(invSvc);
        FacturaServicio factSvc    = new FacturaServicio();
        ReportServicio  reportSvc  = new ReportServicio(pedidoSvc);

        while (true) {
            String mainMenu =
                "MENU PRINCIPAL\n\n" +
                "[1] Gestión de Usuarios\n" +
                "[2] Catálogo de Menú\n" +
                "[3] Toma de Pedidos\n" +
                "[4] Facturación y Boleta\n" +
                "[5] Salir\n\n" +
                "Elija opción:";
            String mSel = JOptionPane.showInputDialog(mainMenu);
            if (mSel == null || mSel.equals("5")) break;

            switch (mSel) {
                case "1" -> menuUsuarios(usuarioSvc);
                case "2" -> menuMenu(menuSvc, invSvc);
                case "3" -> menuPedidos(pedidoSvc, menuSvc);
                case "4" -> menuFacturacion(factSvc, pedidoSvc);
                default  -> mostrarError("Opción inválida en menú principal");
            }
        }
        System.exit(0);
    }
    public static int mostrarMenu() {
        String menu =
            "🍽️ MENÚ PRINCIPAL 🍽️\n\n" +
            " 1. Crear usuario\n" +
            " 2. Editar usuario\n" +
            " 3. Listar usuarios\n" +
            " 4. Agregar platillo (con stock)\n" +
            " 5. Modificar platillo y stock\n" +
            " 6. Listar todos los platillos\n" +
            " 7. Iniciar pedido\n" +
            " 8. Listar todos los pedidos\n" +
            " 9. Calcular subtotal\n" +
            "10. Generar boleta PDF\n" +
            "11. Salir\n\n" +
            "Ingrese opción:";
        String input = JOptionPane.showInputDialog(null, menu, "Restaurante", JOptionPane.PLAIN_MESSAGE);
        if (input == null) return 11;
        try { return Integer.parseInt(input.trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    public static void crearUsuario(UsuarioServicio svc) throws IOException {
        int id = Integer.parseInt(JOptionPane.showInputDialog("ID de usuario:"));
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String rol    = JOptionPane.showInputDialog("Rol (CAJERO/ADMIN/COCINERO):");
        svc.crearUsuario(id, nombre, rol);
        JOptionPane.showMessageDialog(null, "Usuario creado.");
    }

    public static void editarUsuario(UsuarioServicio svc) throws IOException {
        int id = Integer.parseInt(JOptionPane.showInputDialog("ID usuario a editar:"));
        var u = svc.buscarUsuario(id);
        if (u == null) { mostrarError("Usuario no encontrado."); return; }
        String nombre = JOptionPane.showInputDialog("Nuevo nombre:", u.getNombre());
        String rol    = JOptionPane.showInputDialog("Nuevo rol:", u.getRol());
        u.setNombre(nombre); u.setRol(rol);
        svc.actualizarUsuario(u);
        JOptionPane.showMessageDialog(null, "Usuario actualizado.");
    }

    public static void listarUsuarios(UsuarioServicio svc) {
        StringBuilder sb = new StringBuilder("<html><body><b>Usuarios:</b><br>");
        for (var u : svc.listaUsuarios()) {
            sb.append("ID ").append(u.getId())
              .append(" | ").append(u.getNombre())
              .append(" | ").append(u.getRol())
              .append("<br>");
        }
        sb.append("</body></html>");
        JOptionPane.showMessageDialog(null, sb.toString(), "Usuarios", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void agregarPlatilloConStock(
        MenuServicio mSvc, InventarioServicio iSvc) throws IOException {

        int id = Integer.parseInt(JOptionPane.showInputDialog("ID del platillo:"));
        String nombre = JOptionPane.showInputDialog("Nombre del platillo:");
        double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));
        var mi = mSvc.agregarPlatillo(id, nombre, precio);

        int stock = Integer.parseInt(
            JOptionPane.showInputDialog("Stock inicial para \"" + mi.getNombre() + "\":"));

        var inventarioActual = iSvc.listarInventario();
        boolean encontrado = false;
        for (var ii : inventarioActual) {
            if (ii.getItem().getId() == mi.getId()) {
                ii.incrementarStock(stock);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            inventarioActual.add(new InventarioItem(mi, stock));
        }
        iSvc.cargarStockInicial(inventarioActual);

        JOptionPane.showMessageDialog(null,
            "Platillo y stock registrados correctamente.");
    }

    public static void modificarPlatilloConStock(
        MenuServicio mSvc, InventarioServicio iSvc) throws IOException {

        int id = Integer.parseInt(
            JOptionPane.showInputDialog("ID del platillo a modificar:"));
        var m = mSvc.buscarPlatillo(id);
        if (m == null) { mostrarError("Platillo no existe."); return; }

        // 1) Actualiza nombre y precio
        String nombre = JOptionPane.showInputDialog("Nuevo nombre:", m.getNombre());
        double precio = Double.parseDouble(
            JOptionPane.showInputDialog("Nuevo precio:", String.valueOf(m.getPrecio())));
        m.setNombre(nombre);
        m.setPrecio(precio);
        mSvc.actualizarPlatillo(m);

        // 2) Ajuste de stock al valor deseado
        int stockDeseado = Integer.parseInt(
            JOptionPane.showInputDialog("Nuevo stock para este platillo:", iSvc.getStock(id)));

        // Obtenemos inventario actual
        var inventarioActual = iSvc.listarInventario();
        boolean encontrado = false;
        for (var ii : inventarioActual) {
            if (ii.getItem().getId() == id) {
                int stockActual = ii.getStock();
                int diff = stockDeseado - stockActual;
                if (diff > 0) {
                    // Incrementar para alcanzar el stock deseado
                    ii.incrementarStock(diff);
                } else if (diff < 0) {
                    // Reducir para llegar al stock deseado
                    ii.reducirStock(-diff);
                }
                encontrado = true;
                break;
            }
        }
        // Si no existía en inventario, lo agregamos
        if (!encontrado && stockDeseado > 0) {
            inventarioActual.add(new InventarioItem(m, stockDeseado));
        }
        // Persistimos la lista completa con los ajustes
        iSvc.cargarStockInicial(inventarioActual);

        JOptionPane.showMessageDialog(null,
            "Platillo y stock actualizados correctamente.");
    }

    public static void listarPlatillos(
        MenuServicio mSvc, InventarioServicio iSvc) {

        StringBuilder sb = new StringBuilder("<html><body><b>Platillos y Stock:</b><br>");
        for (var mi : mSvc.listarPlatillos()) {
            int st = iSvc.getStock(mi.getId());
            sb.append("ID ").append(mi.getId())
              .append(" | ").append(mi.getNombre())
              .append(" | S/ ").append(String.format("%.2f", mi.getPrecio()))
              .append(" | Stock ").append(st)
              .append("<br>");
        }
        sb.append("</body></html>");
        JOptionPane.showMessageDialog(null, sb.toString(), "Menú", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void iniciarPedido(
        PedidoServicio pSvc, MenuServicio mSvc, InventarioServicio iSvc) {

        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID para pedido:"));
            // Mostrar menú con stock
            StringBuilder prompt = new StringBuilder("Elige IDs (coma-sep):<br>");
            for (var mi : mSvc.listarPlatillos()) {
                prompt.append(mi.getId())
                      .append(") ").append(mi.getNombre())
                      .append(" (stock ").append(iSvc.getStock(mi.getId())).append(")<br>");
            }
            String input = JOptionPane.showInputDialog(null,
                "<html><body>" + prompt + "</body></html>",
                "Iniciar Pedido", JOptionPane.PLAIN_MESSAGE);
            if (input == null) return;

            String[] parts = input.split(",");
            List<MenuItem> items = new ArrayList<>();
            for (String p : parts) {
                int pid = Integer.parseInt(p.trim());
                var plat = mSvc.buscarPlatillo(pid);
                if (plat != null) items.add(plat);
            }
            var pedido = pSvc.crearPedido(id, items);
            String fecha = pedido.getFechaHora().format(FECHA_FORMAT);
            JOptionPane.showMessageDialog(null,
                "<html>Pedido ID " + pedido.getId() +
                "<br>Fecha: " + fecha +
                "<br>Ítems: " + items.size() + "</html>",
                "Pedido Creado", JOptionPane.INFORMATION_MESSAGE);

        } catch (InsuficienteStockException ex) {
            mostrarError("Stock insuficiente: " + ex.getMessage());
        } catch (Exception e) {
            mostrarError("Error al crear pedido.");
        }
    }

    public static void listarPedidosBonito(PedidoServicio svc) {
        StringBuilder sb = new StringBuilder("<html><body><b>Pedidos:</b><br>");
        for (var p : svc.listarPedidos()) {
            String fecha = p.getFechaHora().format(FECHA_FORMAT);
            sb.append("ID ").append(p.getId())
              .append(" | Fecha ").append(fecha)
              .append(" | Ítems ").append(p.getItems().size())
              .append(" | Total S/ ").append(String.format("%.2f", p.getSubtotal()))
              .append("<br>");
        }
        sb.append("</body></html>");
        JOptionPane.showMessageDialog(null, sb.toString(), "Pedidos", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void calcularSubtotal(PedidoServicio svc) {
        int id = Integer.parseInt(JOptionPane.showInputDialog("ID pedido para subtotal:"));
        var p = svc.buscarPedido(id);
        if (p == null) { mostrarError("Pedido no existe."); return; }
        JOptionPane.showMessageDialog(null,
            String.format("Subtotal: S/ %.2f", p.getSubtotal()),
            "Subtotal", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void generarBoletaPDF(
        FacturaServicio facturaSvc,
        PedidoServicio pedidoSvc) {

        try {
            // 1) Pedir ID de pedido
            String idStr = JOptionPane.showInputDialog("ID del pedido para boleta:");
            if (idStr == null) return;
            int id = Integer.parseInt(idStr.trim());

            // 2) Buscar pedido
            var pedido = pedidoSvc.buscarPedido(id);
            if (pedido == null || pedido.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "No se encontró el pedido o está vacío.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3) Pedir impuesto
            String impStr = JOptionPane.showInputDialog("Porcentaje de impuesto (ej: 10):");
            if (impStr == null) return;
            double impuesto = Double.parseDouble(impStr.trim());

            // 4) Calcular totales (opcionalmente mostrar)
            double subtotal = facturaSvc.calcularTotal(pedido);
            double totalConImpuesto = facturaSvc.calcularTotal(pedido, impuesto);

            // 5) Crear carpeta boletas si no existe
            java.nio.file.Path boletaDir = java.nio.file.Paths.get("boletas");
            if (!java.nio.file.Files.exists(boletaDir)) {
                java.nio.file.Files.createDirectories(boletaDir);
            }

            // 6) Generar PDF
            String ruta = "boletas/boleta_" + id + ".pdf";
            facturaSvc.generarBoletaPdf(pedido, null, impuesto, ruta);

            // 7) Éxito
            JOptionPane.showMessageDialog(null,
                String.format(
                "Boleta generada con éxito:\nSubtotal: S/ %.2f\nImpuesto: %.1f%%\nTotal: S/ %.2f\n\nArchivo: %s",
                subtotal, impuesto, totalConImpuesto, ruta),
                "Boleta PDF", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null,
                "El porcentaje de impuesto debe ser un número válido.",
                "Error de formato", JOptionPane.ERROR_MESSAGE);

        } catch (java.io.IOException ioe) {
            JOptionPane.showMessageDialog(null,
                "Error al crear carpeta o archivo:\n" + ioe.getMessage(),
                "Error de E/S", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            // Muestra el mensaje real del error para poder diagnosticar
            JOptionPane.showMessageDialog(null,
                "Error al generar boleta:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void cargarStockInicial(
        InventarioServicio iSvc, MenuServicio mSvc) throws IOException {

        for (var mi : mSvc.listarPlatillos()) {
            int stock = Integer.parseInt(
                JOptionPane.showInputDialog(
                    "Stock para " + mi.getNombre() + ":"));
            iSvc.cargarStockInicial(
                List.of(new InventarioItem(mi, stock))
            );
        }
        JOptionPane.showMessageDialog(null,
            "Stock inicial completado.");
    }

    public static void exportarReporte(ReportServicio svc) {
        try {
            svc.exportarReporteVentasDiario();
            JOptionPane.showMessageDialog(null,
                "Reporte diario exportado.");
        } catch (IOException e) {
            mostrarError("Error al exportar reporte.");
        }
    }

    public static void mostrarError(String msg) {
        JOptionPane.showMessageDialog(null,
            msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
