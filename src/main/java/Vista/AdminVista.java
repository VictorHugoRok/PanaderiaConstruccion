package Vista;

import Controller.AdministradorControlador;
import Modelo.Administrador;
import Modelo.AdministradorDAO;
import Modelo.Cliente;
import Modelo.Empleado;
import Modelo.Pedido;
import Modelo.Producto;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 * Esta clase representa la vista administrativa de la aplicación, que permite al administrador gestionar
 * pedidos, productos, empleados y clientes. Se encargará de mostrar estos elementos en tablas y otros
 * componentes gráficos, permitiendo la interacción con ellos a través de diferentes acciones.
 */
public class AdminVista extends javax.swing.JFrame {

    private AdministradorControlador administrador;
    private Administrador admin;
    private boolean pedidoSeleccionado;
    private DefaultTableModel modeloProductos;
    private DefaultTableModel modelo;
    private DefaultTableModel modeloP;
    private DefaultTableModel modeloClientes;
    private boolean productoSeleccionado;

    /**
     * Constructor de la clase AdminVista. Este constructor inicializa la vista
     * de administración con el objeto de tipo Administrador proporcionado. Se
     * establece el fondo de la ventana con una imagen, se inicializan los
     * componentes de la interfaz y se cargan los datos relevantes como pedidos,
     * productos, empleados y clientes.
     *
     * @param admin Objeto de tipo Administrador que contiene la información del
     * administrador actual.
     */
    public AdminVista(Administrador admin) {
        this.admin = admin;

        JPanel panelConFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image imagen = new ImageIcon(getClass().getResource("/fondo.png")).getImage();
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        panelConFondo.setLayout(new BorderLayout());
        setContentPane(panelConFondo);
        initComponents();
        inicializarDatos();
        
        this.administrador = new AdministradorControlador(admin);
        
        cargarPedidosEnTabla();
        cargarTablaProductos();
        cargarTablaEmpleados();
        cargarEmpleados();
        cargarClientes();
        cargarClientesTabla();
    }

    /**
     * Carga los pedidos en la tabla. Este método obtiene todos los pedidos
     * mediante el administrador y los agrega a la tabla de la interfaz gráfica,
     * limpiando previamente los datos de la tabla.
     */
    private void cargarPedidosEnTabla() {
        List<Pedido> pedidos = administrador.verPedidos();
        System.out.println(pedidos);
        // Modelo de tabla
        DefaultTableModel modelo = (DefaultTableModel) tblPedidos.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos

        // Agregar filas a la tabla
        for (Pedido pedido : pedidos) {
            Object[] fila = {
                pedido.getId(),
                pedido.getPrioridad(),
                obtenerNombresProductos(pedido.getProductos()),
                pedido.getCliente().getNombre(),
                pedido.getEmpleadoAsignado() != null ? pedido.getEmpleadoAsignado().getNombre() : "No asignado",
                pedido.getFechaEntrega()
            };
            modelo.addRow(fila);
        }
    }

    /**
     * Método auxiliar para obtener los nombres de los productos en un pedido.
     *
     * @param productos Lista de productos asociados a un pedido.
     * @return Una cadena con los nombres de los productos, separados por comas.
     */
    private String obtenerNombresProductos(List<Producto> productos) {
        StringBuilder nombresProductos = new StringBuilder();
        for (Producto producto : productos) {
            if (nombresProductos.length() > 0) {
                nombresProductos.append(", ");
            }
            nombresProductos.append(producto.getNombre());
        }
        System.out.println("Productos del pedido: " + nombresProductos.toString());
        return nombresProductos.toString();
    }

    /**
     * Carga los empleados disponibles en el cuadro de selección de empleados.
     * Este método obtiene la lista de empleados y los agrega al combo box de
     * empleados. Si no hay empleados, se muestra un mensaje de "No hay
     * empleados disponibles".
     */
    public void cargarEmpleados() {
        List<Empleado> empleados = administrador.verEmpleados();
        boxEmpleado.removeAllItems();
        if (empleados != null && !empleados.isEmpty()) {
            for (Empleado empleado : empleados) {
                boxEmpleado.addItem(empleado.getCorreo());
            }
        } else {
            // Si no hay empleados, mostrar un mensaje
            boxEmpleado.addItem("No hay empleados disponibles");
        }
    }

    /**
     * Carga los clientes disponibles en el cuadro de selección de clientes.
     * Este método obtiene la lista de clientes y los agrega al combo box de
     * clientes. Si no hay clientes, se muestra un mensaje de "No hay empleados
     * disponibles".
     */
    public void cargarClientes() {
        List<Cliente> clientes = administrador.verClientes();
        boxCliente.removeAllItems();
        if (clientes != null && !clientes.isEmpty()) {
            for (Cliente cliente : clientes) {
                boxCliente.addItem(cliente.getNombre());
            }
        } else {
            boxCliente.addItem("No hay empleados disponibles");
        }
    }

    /**
     * Crea un nuevo pedido utilizando los datos proporcionados en la interfaz
     * de usuario. Este método valida los campos de entrada, verifica que los
     * productos, el cliente y el empleado existan, y luego crea un nuevo
     * pedido, que es añadido a través del administrador.
     */
    private void crearPedido() {
        try {
            List<Producto> productos = obtenerProductosDesdeTabla();
            String fechaEntrega = txtFecha.getText();
            String correoEmpleado = (String) boxEmpleado.getSelectedItem();
            String nombreCliente = (String) boxCliente.getSelectedItem();
            int prioridad = (int) spnPrioridad.getValue();
            if (productos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe agregar al menos un producto.");
                return;
            }
            if (fechaEntrega.isEmpty() || correoEmpleado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos.");
                return;
            }
            Empleado empleadoAsignado = administrador.obtenerEmpleadoPorCorreo(correoEmpleado);
            if (empleadoAsignado == null) {
                JOptionPane.showMessageDialog(null, "Empleado no encontrado.");
                return;
            }

            Cliente cliente = administrador.obtenerClientePorNombre(nombreCliente);
            if (cliente == null) {
                JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
                return;
            }

            Pedido nuevoPedido = new Pedido(cliente, prioridad, productos, fechaEntrega, empleadoAsignado);
            System.out.println("Pedido: " + nuevoPedido.toString());
            administrador.añadirPedido(nuevoPedido);

            // Confirmación
            JOptionPane.showMessageDialog(null, "Pedido creado exitosamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al crear el pedido: " + ex.getMessage());
        }
    }

    /**
     * Actualiza un pedido existente utilizando los datos proporcionados en la
     * interfaz de usuario. Este método valida los campos de entrada, verifica
     * que los productos, el cliente y el empleado existan, y luego actualiza el
     * pedido en la base de datos mediante el administrador.
     */
    private void actualizarPedido() {
        try {
            List<Producto> productos = obtenerProductosDesdeTabla();
            String fechaEntrega = txtFecha.getText();
            String id = txtFecha.getText();
            String correoEmpleado = (String) boxEmpleado.getSelectedItem();
            String nombreCliente = (String) boxCliente.getSelectedItem();
            int prioridad = (int) spnPrioridad.getValue();
            if (productos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe agregar al menos un producto.");
                return;
            }
            if (fechaEntrega.isEmpty() || correoEmpleado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos.");
                return;
            }
            Empleado empleadoAsignado = administrador.obtenerEmpleadoPorCorreo(correoEmpleado);
            if (empleadoAsignado == null) {
                JOptionPane.showMessageDialog(null, "Empleado no encontrado.");
                return;
            }

            Cliente cliente = administrador.obtenerClientePorNombre(nombreCliente);
            if (cliente == null) {
                JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
                return;
            }
            int idPedido = Integer.valueOf(txtIdPedido.getText());
            Pedido nuevoPedido = new Pedido(cliente, prioridad, productos, fechaEntrega, empleadoAsignado);
            administrador.editarPedido(idPedido, nuevoPedido);

            // Confirmación
            JOptionPane.showMessageDialog(null, "Pedido actualizado exitosamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al crear el pedido: " + ex.getMessage());
        }
    }

    /**
     * Obtiene los productos seleccionados en la tabla y los convierte en una
     * lista de objetos Producto.
     *
     * @return Una lista de productos obtenidos desde la tabla de la interfaz de
     * usuario.
     */
    private List<Producto> obtenerProductosDesdeTabla() {
        List<Producto> productos = new ArrayList<>();
        DefaultTableModel modelo = (DefaultTableModel) tblProductos.getModel();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            String nombreProducto = (String) modelo.getValueAt(i, 0);
            Producto producto = administrador.obtenerProductoPorNombre(nombreProducto);
            productos.add(producto);
        }
        System.out.println("Productos de la tabla: ");
        for (Producto producto : productos) {
            System.out.println(producto.getId());
        }
        return productos;
    }

    /**
     * Carga un producto previamente seleccionado en la tabla de productos.
     *
     * @param producto Nombre del producto a cargar en la tabla.
     */
    public void cargarTablaPrevia(String producto) {
        DefaultTableModel modelo = (DefaultTableModel) tblProductos.getModel();
        modelo.addRow(new Object[]{producto});
    }

    /**
     * Carga los productos disponibles en la tabla de productos. Este método
     * obtiene la lista de productos y los agrega a la tabla de la interfaz
     * gráfica.
     */
    private void cargarTablaProductos() {
        List<Producto> productos = administrador.verProductos();
        DefaultTableModel modelo = (DefaultTableModel) tblProductos.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos

        // Agregar filas a la tabla
        for (Producto producto : productos) {
            Object[] fila = {
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getTiempoElaboracion()
            };
            modelo.addRow(fila);
        }
    }

    /**
     * Carga los empleados y administradores en la tabla de usuarios. Este
     * método obtiene tanto a los empleados como a los administradores y los
     * agrega a la tabla, indicando su rol correspondiente.
     */
    private void cargarTablaEmpleados() {
        List<Empleado> empleados = administrador.verEmpleados();
        List<Administrador> administradores = administrador.getAdministradores();
        DefaultTableModel modelo = (DefaultTableModel) tblUsuarios.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos

        for (Administrador admin : administradores) {
            Object[] fila = {
                admin.getNombre(),
                admin.getCorreo(),
                "Administrador"
            };
            modelo.addRow(fila);
        }
        // Agregar filas a la tabla
        for (Empleado empleado : empleados) {
            Object[] fila = {
                empleado.getNombre(),
                empleado.getCorreo(),
                "Empleado"
            };
            modelo.addRow(fila);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPedidos = new javax.swing.JTable();
        btnCrear = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        btnEliminarPedido = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnAgregarAlPedido = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        boxEmpleado = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        boxCliente = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        spnPrioridad = new javax.swing.JSpinner();
        btnDeseleccionar = new javax.swing.JButton();
        txtIdPedido = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtTelefonoCliente = new javax.swing.JTextField();
        btnAgregarCliente = new javax.swing.JButton();
        btnEliminarCliente = new javax.swing.JButton();
        btnDeseleccionarCliente = new javax.swing.JButton();
        txtIdCliente = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProductosInventario = new javax.swing.JTable();
        txtNombreProducto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTiempo = new javax.swing.JTextField();
        btnAgregarProducto = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtIdProducto = new javax.swing.JLabel();
        jBtnLimpiarCampos = new javax.swing.JButton();
        BtnDeseleccionarProducto = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        txtNombreUsuario = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCorreoUsuario = new javax.swing.JTextField();
        boxTipo = new javax.swing.JComboBox<>();
        btnAgregarUsuario = new javax.swing.JButton();
        btnDeseleccionarUsuario = new javax.swing.JButton();
        btnCambioPedidos = new javax.swing.JButton();
        btnCambioUsuarios = new javax.swing.JButton();
        btnCambioProductos = new javax.swing.JButton();
        btnClientes = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();
        txtUsuario = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.CardLayout());

        jLabel1.setText("PEDIDOS");

        tblPedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Prioridad", "Productos", "Cliente", "Empleado", "Fecha"
            }
        ));
        tblPedidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPedidosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPedidos);

        btnCrear.setText("Crear Pedido");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto"
            }
        ));
        tblProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblProductos);

        jLabel2.setText("Empleado");

        btnEliminarPedido.setText("Eliminar Pedido");
        btnEliminarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarPedidoActionPerformed(evt);
            }
        });

        jLabel4.setText("Prioridad");

        btnAgregarAlPedido.setText("Agregar");
        btnAgregarAlPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarAlPedidoActionPerformed(evt);
            }
        });

        btnEliminarProducto.setText("Eliminar");
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        jLabel11.setText("Cliente");

        jLabel12.setText("Fecha");

        btnDeseleccionar.setText("Deseleccionar");
        btnDeseleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeseleccionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btnAgregarAlPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(btnEliminarPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(57, 57, 57)
                                        .addComponent(btnDeseleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 70, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(boxCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(boxEmpleado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(spnPrioridad)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtIdPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(23, 23, 23)))))))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(286, 286, 286)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(boxEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(boxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel12)
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnPrioridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCrear))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarPedido)
                    .addComponent(btnAgregarAlPedido)
                    .addComponent(btnEliminarProducto)
                    .addComponent(btnDeseleccionar))
                .addGap(88, 88, 88))
        );

        jPanel1.add(jPanel2, "card2");

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "id", "Cliente", "Telefono"
            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblClientes);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Clientes");

        jLabel14.setText("Cliente:");

        jLabel15.setText("Telefono:");

        btnAgregarCliente.setText("Agregar");
        btnAgregarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarClienteActionPerformed(evt);
            }
        });

        btnEliminarCliente.setText("Eliminar");

        btnDeseleccionarCliente.setText("Deseleccionar");

        txtIdCliente.setText("jLabel16");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(283, 283, 283)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCliente)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(btnAgregarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(112, 112, 112)
                                .addComponent(btnDeseleccionarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarCliente)
                    .addComponent(btnEliminarCliente))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeseleccionarCliente)
                    .addComponent(txtIdCliente))
                .addContainerGap(115, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel5, "card5");

        tblProductosInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Nombre", "Descripcion", "Precio", "Tiempo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductosInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosInventarioMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblProductosInventario);

        jLabel3.setText("Nombre");

        jLabel5.setText("Descripcion");

        jLabel6.setText("Precio");

        jLabel7.setText("Tiempo");

        btnAgregarProducto.setText("Agregar");
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        jBtnLimpiarCampos.setText("Limpiar Campos");
        jBtnLimpiarCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLimpiarCamposActionPerformed(evt);
            }
        });

        BtnDeseleccionarProducto.setText("Deseleccionar");
        BtnDeseleccionarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDeseleccionarProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTiempo))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                                .addGap(34, 34, 34)
                                .addComponent(BtnDeseleccionarProducto)))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBtnLimpiarCampos))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnLimpiarCampos))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(txtTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAgregarProducto)
                            .addComponent(BtnDeseleccionarProducto)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminar)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, "card3");

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nombre", "Correo", "Tipo"
            }
        ));
        tblUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuariosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblUsuarios);

        txtNombreUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreUsuarioActionPerformed(evt);
            }
        });

        jLabel8.setText("Nombre");

        jLabel9.setText("Correo");

        jLabel10.setText("Tipo");

        boxTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Empleado", "Administrador" }));

        btnAgregarUsuario.setText("Agregar");
        btnAgregarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarUsuarioActionPerformed(evt);
            }
        });

        btnDeseleccionarUsuario.setText("Deseleccionar");
        btnDeseleccionarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeseleccionarUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreUsuario)
                            .addComponent(txtCorreoUsuario)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(boxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(btnAgregarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(92, 92, 92)
                        .addComponent(btnDeseleccionarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 175, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtCorreoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(boxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarUsuario)
                    .addComponent(btnDeseleccionarUsuario))
                .addContainerGap(103, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, "card4");

        btnCambioPedidos.setText("Pedidos");
        btnCambioPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambioPedidosActionPerformed(evt);
            }
        });

        btnCambioUsuarios.setText("Usuarios");
        btnCambioUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambioUsuariosActionPerformed(evt);
            }
        });

        btnCambioProductos.setText("Productos");
        btnCambioProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambioProductosActionPerformed(evt);
            }
        });

        btnClientes.setText("Clientes");
        btnClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesActionPerformed(evt);
            }
        });

        btnCerrarSesion.setText("Cerrar sesión");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        txtUsuario.setText("Usuario: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCambioPedidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCambioUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCambioProductos, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addComponent(btnClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCerrarSesion)
                    .addComponent(txtUsuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCambioPedidos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCambioUsuarios)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCambioProductos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClientes)))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        if (pedidoSeleccionado) {
            actualizarPedido();
            btnCrear.setText("Crear Pedido");
            pedidoSeleccionado = false;
            btnDeseleccionar.setVisible(false);
        } else {
            crearPedido();
        }
        cargarPedidosEnTabla();
        limpiarPedidoSeleccionado();
    }//GEN-LAST:event_btnCrearActionPerformed

    private void btnCambioPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambioPedidosActionPerformed
        CardLayout cardLayout = (CardLayout) jPanel1.getLayout();
        cardLayout.show(jPanel1, "card2");
        cargarEmpleados();
        cargarClientes();
    }//GEN-LAST:event_btnCambioPedidosActionPerformed

    private void btnCambioUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambioUsuariosActionPerformed
        CardLayout cardLayout = (CardLayout) jPanel1.getLayout();
        cardLayout.show(jPanel1, "card4");
    }//GEN-LAST:event_btnCambioUsuariosActionPerformed

    private void btnCambioProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambioProductosActionPerformed
        CardLayout cardLayout = (CardLayout) jPanel1.getLayout();
        cardLayout.show(jPanel1, "card3");
    }//GEN-LAST:event_btnCambioProductosActionPerformed

    private void tblPedidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPedidosMouseClicked
        int filaSeleccionada = tblPedidos.getSelectedRow();

        if (filaSeleccionada != -1) {
            pedidoSeleccionado = true;
            btnDeseleccionar.setVisible(true);
            btnCrear.setText("Actualizar Pedido");
            // Obtener los valores de la fila seleccionada
            Object idPedido = tblPedidos.getValueAt(filaSeleccionada, 0);
            Object prioridad = tblPedidos.getValueAt(filaSeleccionada, 1); // Columna 0
            Object productos = tblPedidos.getValueAt(filaSeleccionada, 2); // Columna 1 (productos como texto)
            Object cliente = tblPedidos.getValueAt(filaSeleccionada, 3); // Columna 2
            Object empleado = tblPedidos.getValueAt(filaSeleccionada, 4); // Columna 3
            Object fecha = tblPedidos.getValueAt(filaSeleccionada, 5);

            txtIdPedido.setText(String.valueOf(idPedido));
            spnPrioridad.setValue(prioridad);
            boxCliente.setSelectedItem(cliente);
            boxEmpleado.setSelectedItem(empleado);
            txtFecha.setText((String) fecha);
            // Limpiar la tabla tblProductos antes de agregar nuevos productos

            modeloProductos.setRowCount(0); // Limpiar todas las filas existentes

            // Verificar si los productos están presentes
            if (productos != null && !productos.toString().isEmpty()) {
                // Suponemos que los productos están separados por comas en la cadena
                String[] productosArray = productos.toString().split(","); // Separar por comas

                // Agregar cada producto a la tabla tblProductos
                for (String producto : productosArray) {
                    producto = producto.trim(); // Eliminar posibles espacios extra
                    // Aquí asumimos que quieres agregar el producto con una cantidad por defecto
                    // (Puedes cambiar la lógica de cantidad según tu caso)
                    modeloProductos.addRow(new Object[]{producto, 1}); // 1 es la cantidad por defecto
                }
            } else {
                JOptionPane.showMessageDialog(null, "Este pedido no tiene productos.");
            }
        }

    }//GEN-LAST:event_tblPedidosMouseClicked

    private void btnAgregarAlPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarAlPedidoActionPerformed
        agregarProductos agregar = new agregarProductos(administrador, this);
        agregar.setVisible(true);
        agregar.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }//GEN-LAST:event_btnAgregarAlPedidoActionPerformed

    private void tblProductosInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosInventarioMouseClicked
        int filaSeleccionada = tblProductosInventario.getSelectedRow();
        productoSeleccionado = true;
        btnAgregarProducto.setText("Acualizar");
        BtnDeseleccionarProducto.setVisible(true);

        if (filaSeleccionada != -1) {
            // Obtener valores de la fila seleccionada
            Object id = tblProductosInventario.getValueAt(filaSeleccionada, 0);
            Object nombre = tblProductosInventario.getValueAt(filaSeleccionada, 1); // Columna 0
            Object descripcion = tblProductosInventario.getValueAt(filaSeleccionada, 2); // Columna 1
            Object precio = tblProductosInventario.getValueAt(filaSeleccionada, 3); // Columna 2
            Object tiempo = tblProductosInventario.getValueAt(filaSeleccionada, 4); // Columna 3

            // Asignar valores a los campos de texto
            txtNombreProducto.setText(nombre != null ? nombre.toString() : "");
            txtDescripcion.setText(descripcion != null ? descripcion.toString() : "");
            txtPrecio.setText(precio != null ? precio.toString() : "");
            txtTiempo.setText(tiempo != null ? tiempo.toString() : "");
            txtIdProducto.setText(id != null ? id.toString() : "");
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para editar.");
        }

    }//GEN-LAST:event_tblProductosInventarioMouseClicked

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        String nombre = txtNombreProducto.getText();
        String descripcion = txtDescripcion.getText();
        String precio = txtPrecio.getText();
        String tiempo = txtTiempo.getText();

        Producto producto = new Producto(nombre, descripcion, Double.parseDouble(precio), tiempo);
        if (productoSeleccionado) {
            int idProducto = Integer.valueOf(txtIdProducto.getText());
            administrador.editarProducto(idProducto, producto);
            productoSeleccionado = false;
        } else {
            administrador.añadirProducto(producto);
        }
        BtnDeseleccionarProducto.setText("Agregar");
        cargarTablaProductos();
        limpiarCamposProductos();
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnDeseleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeseleccionarActionPerformed
        btnDeseleccionar.setVisible(false);
        btnCrear.setText("Crear Pedido");
        pedidoSeleccionado = false;
        modeloProductos.setRowCount(0);
        tblPedidos.clearSelection();
        limpiarPedidoSeleccionado();
    }//GEN-LAST:event_btnDeseleccionarActionPerformed

    private void btnEliminarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarPedidoActionPerformed
        if (!pedidoSeleccionado) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un pedido para eliminar.");
        } else {
            int idPedido = Integer.valueOf(txtIdPedido.getText());
            administrador.eliminarPedido(idPedido);
            JOptionPane.showMessageDialog(null, "Pedido eliminado con éxito");
        }
        btnDeseleccionar.setVisible(false);
        btnCrear.setText("Crear Pedido");
        pedidoSeleccionado = false;
        cargarPedidosEnTabla();
        limpiarPedidoSeleccionado();
    }//GEN-LAST:event_btnEliminarPedidoActionPerformed

    private void btnAgregarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarUsuarioActionPerformed
        String nombre = txtNombreUsuario.getText();
        String correo = txtCorreoUsuario.getText();
        String tipo = (String) boxTipo.getSelectedItem();

        if (tipo.equals("Administrador")) {
            Administrador administradorNuevo = new Administrador(nombre, correo, "contraseña");
            int idAdmin = administrador.añadirAdministrador(administradorNuevo);
            administradorNuevo.setId(idAdmin);
        } else {
            Empleado empleadoNuevo = new Empleado(nombre, correo, "contraseña");
            administrador.añadirEmpleado(empleadoNuevo);
        }

        cargarTablaEmpleados();
    }//GEN-LAST:event_btnAgregarUsuarioActionPerformed

    private void btnDeseleccionarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeseleccionarUsuarioActionPerformed
        btnDeseleccionarUsuario.setVisible(false);
    }//GEN-LAST:event_btnDeseleccionarUsuarioActionPerformed

    private void tblUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuariosMouseClicked
        int filaSeleccionada = tblUsuarios.getSelectedRow();

        if (filaSeleccionada != -1) {
            // Obtener valores de la fila seleccionada
            Object nombre = tblUsuarios.getValueAt(filaSeleccionada, 0); // Columna 0
            Object correo = tblUsuarios.getValueAt(filaSeleccionada, 1); // Columna 1
            Object tipo = tblUsuarios.getValueAt(filaSeleccionada, 2); // Columna 2

            // Asignar valores a los campos de texto
            txtNombreUsuario.setText(nombre != null ? nombre.toString() : "");
            txtCorreoUsuario.setText(correo != null ? correo.toString() : "");
            boxTipo.setSelectedItem(tipo);
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para editar.");
        }
    }//GEN-LAST:event_tblUsuariosMouseClicked

    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
        CardLayout cardLayout = (CardLayout) jPanel1.getLayout();
        cardLayout.show(jPanel1, "card5");
    }//GEN-LAST:event_btnClientesActionPerformed

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        int filaSeleccionada = tblClientes.getSelectedRow();

        if (filaSeleccionada != -1) {
            // Obtener valores de la fila seleccionada
            Object id = tblClientes.getValueAt(filaSeleccionada, 0);
            Object nombre = tblClientes.getValueAt(filaSeleccionada, 1); // Columna 0
            Object telefono = tblClientes.getValueAt(filaSeleccionada, 2); // Columna 1

            // Asignar valores a los campos de texto
            txtCliente.setText(nombre != null ? nombre.toString() : "");
            txtTelefonoCliente.setText(telefono != null ? telefono.toString() : "");
            txtIdCliente.setText(id.toString());
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para editar.");
        }
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnAgregarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarClienteActionPerformed
        String nombreCliente = txtCliente.getText();
        String telefonoCliente = txtTelefonoCliente.getText();
        Cliente cliente = new Cliente(nombreCliente, telefonoCliente);
        int idCliente = administrador.añadirCliente(cliente);
        cliente.setId(idCliente);
        cargarClientesTabla();
    }//GEN-LAST:event_btnAgregarClienteActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed

    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void tblProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int id = Integer.valueOf(txtIdProducto.getText());

        administrador.eliminarProducto(id);
        cargarTablaProductos();
        limpiarCamposProductos();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jBtnLimpiarCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLimpiarCamposActionPerformed
        // TODO add your handling code here:
        limpiarCamposProductos();
    }//GEN-LAST:event_jBtnLimpiarCamposActionPerformed

    private void BtnDeseleccionarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDeseleccionarProductoActionPerformed
        // TODO add your handling code here:
        tblProductosInventario.clearSelection();
        BtnDeseleccionarProducto.setVisible(false);
        btnAgregarProducto.setText("Agregar");
        productoSeleccionado = false;

    }//GEN-LAST:event_BtnDeseleccionarProductoActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        LoginVista login = new LoginVista();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void txtNombreUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreUsuarioActionPerformed

    private void limpiarCamposProductos() {
        txtNombreProducto.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtTiempo.setText("");
        txtIdProducto.setText("");
    }

    private void cargarClientesTabla() {
        List<Cliente> clientes = administrador.verClientes();
        modeloClientes.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos

        for (Cliente cliente : clientes) {
            System.out.println(cliente.toString());
            Object[] fila = {
                cliente.getId(),
                cliente.getNombre(),
                cliente.getTelefono()
            };
            modeloClientes.addRow(fila);
        }
    }

    private void limpiarPedidoSeleccionado() {
        spnPrioridad.setValue(0);
        txtFecha.setText("");
        txtIdPedido.setText("");
        boxCliente.setSelectedIndex(0);
        boxEmpleado.setSelectedIndex(0);
        modeloP.setRowCount(0);
    }

    private void inicializarDatos() {
        modelo = (DefaultTableModel) tblProductosInventario.getModel();
        modeloP = (DefaultTableModel) tblProductos.getModel();
        modeloClientes = (DefaultTableModel) tblClientes.getModel();
        btnDeseleccionar.setVisible(false);
        btnDeseleccionarUsuario.setVisible(false);
        txtIdPedido.setText("");
        pedidoSeleccionado = true;
        modeloProductos = (DefaultTableModel) tblProductos.getModel();
        jPanel2.setBackground(Color.white);
        jPanel3.setBackground(Color.white);
        jPanel4.setBackground(Color.white);
        productoSeleccionado = false;
        BtnDeseleccionarProducto.setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnDeseleccionarProducto;
    private javax.swing.JComboBox<String> boxCliente;
    private javax.swing.JComboBox<String> boxEmpleado;
    private javax.swing.JComboBox<String> boxTipo;
    private javax.swing.JButton btnAgregarAlPedido;
    private javax.swing.JButton btnAgregarCliente;
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnAgregarUsuario;
    private javax.swing.JButton btnCambioPedidos;
    private javax.swing.JButton btnCambioProductos;
    private javax.swing.JButton btnCambioUsuarios;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnClientes;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnDeseleccionar;
    private javax.swing.JButton btnDeseleccionarCliente;
    private javax.swing.JButton btnDeseleccionarUsuario;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnEliminarPedido;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton jBtnLimpiarCampos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSpinner spnPrioridad;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTable tblPedidos;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTable tblProductosInventario;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCorreoUsuario;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JLabel txtIdCliente;
    private javax.swing.JLabel txtIdPedido;
    private javax.swing.JLabel txtIdProducto;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtNombreUsuario;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtTelefonoCliente;
    private javax.swing.JTextField txtTiempo;
    private javax.swing.JLabel txtUsuario;
    // End of variables declaration//GEN-END:variables
}
