import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private Set<String> nombresUtilizados = new HashSet<>();
    private PrintWriter escritor;
    private Set<PrintWriter> escritores = new HashSet<>();
    private final int PUERTO = 5555;

    public static void main(String[] args) {
        Server servidor = new Server();
        servidor.iniciar();
    }

    private void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado. Esperando conexiones...");

            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Nuevo cliente conectado.");

                this.escritor = new PrintWriter(socketCliente.getOutputStream(), true);

                escritores.add(escritor);
                String nombreCliente = new BufferedReader(new InputStreamReader(socketCliente.getInputStream())).readLine();

                if (nombreEstaEnUso(nombreCliente)) {    // Verificar si el nombre ya está en uso
                    escritor.println("#NOMBRE_EN_USO#");
                    socketCliente.close();
                }else {
                    nombresUtilizados.add(nombreCliente);       // Añadimos Nombre a la lista y se envia a todos los usuarios conectados.
                    enviarListaUsuarios();

                    broadcastMensaje("Bienvenido, " + nombreCliente + "!", escritor);

                    // Iniciar un nuevo hilo para manejar al cliente
                    new Thread(() -> manejarCliente(socketCliente, escritor, nombreCliente)).start();
                }
            }
        } catch (IOException e) {
            //Nos aseguramos que servidor la ejecucion de este mismo no termine
            System.out.println("Fallo al conectar con el cliente.");
            iniciar();
        }
    }
    private boolean nombreEstaEnUso(String nombre) {
        return nombresUtilizados.contains(nombre);
    }
    private void manejarCliente(Socket socket, PrintWriter escritor, String nombreCliente) {
        try {
            BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String mensaje;
            while ((mensaje = lector.readLine()) != null) {
                broadcastMensaje(nombreCliente + ": " + mensaje, escritor);
            }
        } catch (IOException e) {
            nombresUtilizados.remove(nombreCliente);
            enviarListaUsuarios();
            escritores.remove(escritor);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void broadcastMensaje(String mensaje, PrintWriter emisor) {
        for (PrintWriter escritor : escritores) {
            if (escritor != emisor) {
                escritor.println(mensaje); // Enviar el mensaje a cada cliente conectado
            }
        }
    }

    /*Funcion enviarListaUsuarios
     *
     *   Encargada de enviar la Lista de Usuarios a todos los Clientes conectados.
     *   Se trata de un for each que recorre todos los usuarios
     *   Eliminamos el ultimo caracter antes de enviarlo (dicho caracter se utiliza para diferenciar usuarios)
     * */
    private void enviarListaUsuarios() {
        StringBuilder listaUsuarios = new StringBuilder("#LISTA_USUARIOS# ");
        for (String nombre : nombresUtilizados) {
            listaUsuarios.append(nombre).append(",");
        }
        listaUsuarios.deleteCharAt(listaUsuarios.length() - 1); // Eliminar la última coma
        broadcastMensaje(listaUsuarios.toString(), null);
    }

}
