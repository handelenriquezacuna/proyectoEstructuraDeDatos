package Main.ui;

import Main.config.Configuracion;
import Main.persistence.ArchivoManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author jomas
 */
public class InterfazLogin {
    private final Configuracion configuracion;
    private final ArchivoManager archivoManager;
    private Map<String, String> usuarios;

    private final String RUTA_USUARIOS = "src/main/resources/data/usuarios.txt";
    private final String RUTA_LOG = "src/main/resources/data/login.log";
    private final int MAX_INTENTOS = 3;

    public InterfazLogin(Configuracion configuracion) {
        this.configuracion = configuracion;
        this.archivoManager = new ArchivoManager();
        cargarUsuarios();
    }
    
    private void cargarUsuarios() {

        usuarios = archivoManager.leerConfiguracion(RUTA_USUARIOS);

        // Si no hay usuarios, crear automáticamente usuarios iniciales
        if (usuarios.isEmpty()) {
            usuarios = new HashMap<>();
            usuarios.put("admin", "1234");
            usuarios.put("user1", "abcd");

            archivoManager.guardarConfiguracion(RUTA_USUARIOS, usuarios);

            JOptionPane.showMessageDialog(null, """
                                                Archivo de usuarios generado automaticamente con dos usuarios iniciales:
                                                admin/1234
                                                user1/abcd""",
                    "Usuarios creados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    

     public void iniciarLogin() {
        int intentos = 0;
        boolean autenticado = false;

        while (!autenticado && intentos < MAX_INTENTOS) {
            String usuario = JOptionPane.showInputDialog("Ingrese su usuario:");
            if (usuario == null) break; // Cancelar

            String contrasena = JOptionPane.showInputDialog("Ingrese su contraseña:");
            if (contrasena == null) break; // Cancelar

            if (verificarCredenciales(usuario, contrasena)) {
                JOptionPane.showMessageDialog(null, "¡Bienvenido " + usuario + "!");
                autenticado = true;

                // Abrir interfaz principal
                InterfazPrincipal principal = new InterfazPrincipal(configuracion);
                principal.iniciarSistema();

            } else {
                intentos++;
                JOptionPane.showMessageDialog(null,
                        "Usuario o contraseña incorrectos. Intento " + intentos + " de " + MAX_INTENTOS,
                        "Error de login", JOptionPane.ERROR_MESSAGE);

                registrarIntentoFallido(usuario);
            }
        }

        if (!autenticado) {
            JOptionPane.showMessageDialog(null,
                    "Se ha superado el número máximo de intentos. El sistema se cerrará.",
                    "Login fallido", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private boolean verificarCredenciales(String usuario, String contrasena) {
        return usuarios.containsKey(usuario) && usuarios.get(usuario).equals(contrasena);
    }

    private void registrarIntentoFallido(String usuario) {
        String mensaje = LocalDateTime.now() + " - Intento fallido de usuario: " + usuario + "\n";
        archivoManager.escribirArchivo(RUTA_LOG, mensaje + archivoManager.leerArchivo(RUTA_LOG));
    }
    
    
}
