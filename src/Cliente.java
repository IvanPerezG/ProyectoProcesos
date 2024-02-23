import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private PrintWriter escritor;
    private Chat chat;
    private String nombreUsuario;
    private List<String> lista =  new ArrayList<>();;

    public Cliente() {
        conectarAlServidor();

    }
    private void conectarAlServidor() {
        try {
            Socket socket = new Socket("localhost", 5555);
            BufferedReader lectorServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            escritor = new PrintWriter(socket.getOutputStream(), true);
            lista.clear();
            new Thread(() -> recibirMensajesServidor(lectorServidor)).start();  // Iniciar un nuevo hilo para recibir mensajes del servidor
            chat = new Chat(this);  // Inicializar el frame del chat
            boolean salir = false;
            do {
                nombreUsuario = JOptionPane.showInputDialog("Ingrese su nombre:");   // Solicitar al usuario que ingrese su nombre
                escritor.println(nombreUsuario);    // Enviar el nombre de usuario al servidor
                if(lista.contains(nombreUsuario)){
                    JOptionPane.showInputDialog("Nombre ya usado, vuelva a intentarlo.");
                }
            } while (lista.contains(nombreUsuario));

            chat.setVisible(true);  //Cuando el usuario es aceptado se le muestra la interfaz
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void recibirMensajesServidor(BufferedReader lectorServidor) {
        try {
            String mensaje;
            while ((mensaje = lectorServidor.readLine()) != null) {
                System.out.println(mensaje);
                if (mensaje.startsWith("#LISTA_USUARIOS#")) {
                    String[] usuarios = mensaje.substring(16).split(",");
                    for (String usuario : usuarios) {
                        lista.add(usuario.trim());
                    }
                    chat.actualizarListaUsuarios(lista);
                } else if (mensaje.startsWith("#NOMBRE_EN_USO#")){
                    System.exit(0);
                } else {
                    chat.appendMensaje(mensaje);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void enviarMensaje(String mensaje) {
        if (!mensaje.isEmpty()) {
            escritor.println(mensaje);
            chat.appendMensaje(mensaje);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Cliente::new);
    }
}