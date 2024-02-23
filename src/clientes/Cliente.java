package clientes;

import jframe.Chat;

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
    private boolean existe = false;
    private List<String> lista;

    public Cliente() {
        conectarAlServidor();
    }

    private void conectarAlServidor() {
        try {
            Socket socket = new Socket("localhost", 5555);
            BufferedReader lectorServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            escritor = new PrintWriter(socket.getOutputStream(), true);

            // Iniciar un nuevo hilo para recibir mensajes del servidor
            new Thread(() -> recibirMensajesServidor(lectorServidor)).start();

            while (!lista.contains(nombreUsuario)) {
                // Solicitar al usuario que ingrese su nombre
                nombreUsuario = JOptionPane.showInputDialog("Ingrese su nombre:");
                if(lista.contains(nombreUsuario)){
                    //JOptionPane.showMessageDialog(this,"Nombre ya usado, vuelva a intentarlo.");
                }
                // Enviar el nombre de usuario al servidor
                escritor.println(nombreUsuario);

            }
            // Inicializar el frame del chat
            chat = new Chat(this);

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
                    String[] usuarios = mensaje.substring(15).split(",");
                    lista= new ArrayList<>();
                    for (String usuario : usuarios) {
                        lista.add(usuario.trim());
                    }
                    chat.actualizarListaUsuarios(lista); // Aquí se llama al método
                } else {
                    chat.appendMensaje(mensaje);
                }
                if(mensaje.startsWith("#NOMBRE_EN_USO#")){
                    this.existe = true;
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
        SwingUtilities.invokeLater(() -> {
            new Cliente();
        });
    }
}
