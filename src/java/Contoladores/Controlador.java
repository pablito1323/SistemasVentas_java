/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Contoladores;

import Modelos.Producto;
import Modelos.ProductoDAO;
import Modelos.Usuario;
import Modelos.UsuarioDAO;
import Modelos.Venta;
import Modelos.VentaDAO;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pablito
 */
@WebServlet(name = "Controlador", urlPatterns = {"/Controlador"})
public class Controlador extends HttpServlet {

    Usuario usuario = new Usuario();
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    int idUsuario;
    Producto producto = new Producto();
    ProductoDAO productoDAO = new ProductoDAO();
    Venta venta = new Venta();
    int item, codProducto, precio, cantidad, totalapagar;
    String descripcion;
    double subtotal;
    List<Venta> listaVentas = new ArrayList();
    VentaDAO ventaDAO = new VentaDAO();
    int numfac = 0;
    NumberFormat formatoNumero1;
    String total1;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String menu = request.getParameter("menu");
        String accion = request.getParameter("accion");

        if (menu.equals("Principal")) {
            request.getRequestDispatcher("Principal.jsp").forward(request, response);
        }
        if (menu.equals("Productos")) {
            request.getRequestDispatcher("Productos.jsp").forward(request, response);
        }
        if (menu.equals("Empleados")) {
            switch (accion) {
                case "listar":
                    List lista = usuarioDAO.Listar();
                    request.setAttribute("usuarios", lista);
                    break;

                case "Agregar":
                    int documento = Integer.parseInt(request.getParameter("txtdocumento"));
                    String nombre = request.getParameter("txtnombre");
                    String correo = request.getParameter("txtcorreo");
                    String password = request.getParameter("txtpassword");
                    String rol = request.getParameter("txtrol");
                    String estado = request.getParameter("txtestado");
                    usuario.setDocumento(documento);
                    usuario.setNombre(nombre);
                    usuario.setPassword(password);
                    usuario.setCorreo(correo);
                    usuario.setRol(rol);
                    usuario.setEstado(estado);
                    usuarioDAO.Agregar(usuario);
                    request.getRequestDispatcher(
                            "Controlador?menu=Empleados&accion=listar").forward(request, response);
                    break;

                case "Actualizar":
                    Usuario usuario1 = new Usuario();
                    int documentoUpdate = Integer.parseInt(request.getParameter("txtdocumento"));
                    String nombreUpdate = request.getParameter("txtnombre");
                    String correoUpdate = request.getParameter("txtcorreo");
                    String passwordUpdate = request.getParameter("txtpassword");
                    String rolUpdate = request.getParameter("txtrol");
                    String estadoUpdate = request.getParameter("txtestado");
                    usuario1.setDocumento(documentoUpdate);
                    usuario1.setNombre(nombreUpdate);
                    usuario1.setPassword(passwordUpdate);
                    usuario1.setCorreo(correoUpdate);
                    usuario1.setRol(rolUpdate);
                    usuario1.setEstado(estadoUpdate);
                    usuario1.setId(idUsuario);
                    usuarioDAO.Actualizar(usuario1);
                    request.getRequestDispatcher("Controlador?menu=Empleados&accion=listar").forward(request, response);
                    break;

                case "Cargar":
                    idUsuario = Integer.parseInt(request.getParameter("id"));
                    Usuario usuario = usuarioDAO.ListarPorId(idUsuario);
                    request.setAttribute("usuarioSeleccionado", usuario);
                    request.getRequestDispatcher("Controlador?menu=Empleados&accion=listar").forward(request, response);
                    break;
                case "Eliminar":
                    idUsuario = Integer.parseInt(request.getParameter("id"));
                    usuarioDAO.Eliminar(idUsuario);
                    request.getRequestDispatcher("Controlador?menu=Empleados&accion=listar").forward(request, response);
                    break;
            }
            request.getRequestDispatcher("Empleados.jsp").forward(request, response);
        }

        if (menu.equals("Clientes")) {
            request.getRequestDispatcher("Clientes.jsp").forward(request, response);
        }
        if (menu.equals("Ventas")) {
            switch (accion) {
                case "BuscarCliente":
                    int documentoCliente = Integer.parseInt(request.getParameter("documentocliente"));
                    usuario = usuarioDAO.BuscarCliente(documentoCliente);
                    request.setAttribute("cliente", usuario);
                    break;
                case "BuscarProducto":
                    int codigoProducto = Integer.parseInt(request.getParameter("codigoproducto"));
                    producto = productoDAO.ConsultaPorCodigo(codigoProducto);
                    request.setAttribute("productoseleccionado", producto);
                    request.setAttribute("cliente", usuario);
                    for (int i = 0; i < listaVentas.size(); i++) {
                        totalapagar += listaVentas.get(i).getSubtotal();
                    }
                    formatoNumero1 = NumberFormat.getNumberInstance();
                    total1 = formatoNumero1.format(totalapagar);
                    request.setAttribute("totalapagar", total1);
                    request.setAttribute("listaventas", listaVentas);
                    break;
                case "AgregarProducto":
                    totalapagar = 0;
                    venta = new Venta();
                    item++;
                    codProducto = Integer.parseInt(request.getParameter("codigoproducto"));
                    descripcion = request.getParameter("nombreproducto");
                    precio = Integer.parseInt(request.getParameter("precioproducto"));
                    cantidad = Integer.parseInt(request.getParameter("cantidadproducto"));
                    subtotal = precio * cantidad;
                    venta.setItem(item);
                    venta.setDescripcionProducto(descripcion);
                    venta.setCantidad(cantidad);
                    venta.setPrecio(precio);
                    venta.setSubtotal(subtotal);
                    venta.setIdProducto(codProducto);
                    listaVentas.add(venta);
                    request.setAttribute("listaventas", listaVentas);
                    for (int i = 0; i < listaVentas.size(); i++) {
                        totalapagar += listaVentas.get(i).getSubtotal();
                    }
                    formatoNumero1 = NumberFormat.getNumberInstance();
                    total1 = formatoNumero1.format(totalapagar);
                    request.setAttribute("totalapagar", total1);
                    break;

                case "GenerarVenta":
                    venta.setIdCliente(usuario.getId());
                    venta.setIdEmpleado(1);
                    venta.setNumeroComprobante("" + numfac);
                    venta.setFecha("2020-10-21");
                    venta.setMonto(totalapagar);
                    venta.setEstado("1");
                    ventaDAO.RegistrarVenta(venta);
                    int idv = ventaDAO.ObtenerMaximoIdVentas();
                    for (int i = 0; i < listaVentas.size(); i++) {
                        venta = new Venta();
                        venta.setIdVenta(idv);
                        venta.setIdProducto(listaVentas.get(i).getIdProducto());
                        venta.setCantidad(listaVentas.get(i).getCantidad());
                        venta.setPrecio(listaVentas.get(i).getPrecio());
                        ventaDAO.GuardarDetalleVenta(venta);
                    }
                    break;

                default:
                    String factura = ventaDAO.ObtenerNumeroDeFactura();
                    System.err.println("nume factura" + factura);
                    if (factura == null) {
                        factura = "1";
                    } else {
                        numfac = Integer.parseInt(factura) + 1;
                    }
                    request.setAttribute("numerofactura", numfac);

            }
            request.getRequestDispatcher("Ventas.jsp").forward(request, response);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
