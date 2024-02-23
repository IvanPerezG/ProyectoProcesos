package jframe;

import clientes.Cliente;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Chat extends JFrame {
    private JTextArea mensajesArea;
    private JTextField mensajeField;
    private Cliente cliente;
    private JList<String> usuariosList;

    public Chat(Cliente cliente) {
        super("Chat Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        this.cliente = cliente;

        mensajesArea = new JTextArea();
        mensajesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mensajesArea);

        mensajeField = new JTextField();
        JButton enviarButton = new JButton("Enviar");

        enviarButton.addActionListener(e -> enviarMensaje());

        usuariosList = new JList<>();
        JScrollPane usuariosScrollPane = new JScrollPane(usuariosList);
        usuariosScrollPane.setPreferredSize(new Dimension(150, getHeight()));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(mensajeField, BorderLayout.CENTER);
        inputPanel.add(enviarButton, BorderLayout.EAST);

        panel.add(inputPanel, BorderLayout.SOUTH);
        panel.add(usuariosScrollPane, BorderLayout.EAST);

        add(panel);
        setVisible(true);
    }

    private void enviarMensaje() {
        String mensaje = mensajeField.getText();
        if (!mensaje.isEmpty()) {
            cliente.enviarMensaje(mensaje);
            mensajeField.setText(""); // Limpia el campo de entrada después de enviar el mensaje
        }
    }

    public void appendMensaje(String mensaje) {
        mensajesArea.append(mensaje + "\n");
    }

    public void actualizarListaUsuarios(List<String> usuarios) {
        SwingUtilities.invokeLater(() -> {
            DefaultListModel<String> model = new DefaultListModel<>();
            for (String usuario : usuarios) {
                model.addElement(usuario);
            }
            usuariosList.setModel(model);
        });
    }
}
