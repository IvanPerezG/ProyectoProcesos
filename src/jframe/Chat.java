package jframe;

import javax.swing.*;
import java.awt.*;

public class Chat extends JFrame {
    public Chat() {
        setTitle("WashUp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);

        // Panel principal dividido en dos secciones
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para la sección de mensajes
        JPanel messagePanel = new JPanel(new BorderLayout());
        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messagePanel.add(messageScrollPane, BorderLayout.CENTER);

        // Panel para la sección de usuarios conectados
        JPanel usersPanel = new JPanel();
        usersPanel.setPreferredSize(new Dimension(200, getHeight()));
        // Agregar una lista de usuarios conectados (aún por implementar)

        // Panel para la sección de entrada de texto y botón
        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField inputField = new JTextField();
        inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputField.setBorder(BorderFactory.createLineBorder(Color.blue));
        JButton sendButton = new JButton("Enviar");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Agregar los paneles al panel principal
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        mainPanel.add(usersPanel, BorderLayout.EAST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Agregar el panel principal al JFrame
        add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Chat::new);
    }
}
