package com.paint.user.gui;

import com.paint.global.net.Message;
import com.paint.user.net.ClientNetManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by jetbrains on 02/26/2017.
 */
public class Frame extends JFrame {

    static int framePad = 1, innerPad = 1;

    private int
            FRAME_WIDTH = 900, FRAME_HEIGHT = 600,
            CHAT_ROWS = 4,
            EXTENSIONS_HEIGHT = ExtensionsPanel.staticHeight(),
            CHAT_HEIGHT = ChatPanel.computeHeight(CHAT_ROWS),
            WORKSPACE_HEIGHT = FRAME_HEIGHT - EXTENSIONS_HEIGHT - CHAT_HEIGHT;

    private Color
            BACKGROUND_COLOR = Color.lightGray;

    private ClientNetManager clientNetManager;
    private volatile String username = null;

    private Container contentPane;
    private JPanel workspacePanel;
    private PaintPanel paintPanel;
    private ChatPanel chatPanel;
    private ExtensionsPanel extensionsPanel;

    private Frame() throws HeadlessException, IOException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        contentPane = getContentPane();
        contentPane.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        contentPane.setBackground(BACKGROUND_COLOR);
        paintPanel = new PaintPanel(this, FRAME_WIDTH, WORKSPACE_HEIGHT + CHAT_HEIGHT, BACKGROUND_COLOR);
        chatPanel = new ChatPanel(this, FRAME_WIDTH, CHAT_ROWS);
        extensionsPanel = new ExtensionsPanel(this, FRAME_WIDTH);

        chatPanel.requestName();
        clientNetManager = new ClientNetManager(this);
        new Thread(() -> {
            while (username == null) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            clientNetManager.sendToServer(new Message().addToken("init", username));
        }).start();

        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        constructWorkspacePanel();
        contentPane.add(workspacePanel);
        contentPane.add(extensionsPanel);

        layout.putConstraint(SpringLayout.WEST, workspacePanel, innerPad, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, workspacePanel, innerPad, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.EAST, workspacePanel, -innerPad, SpringLayout.EAST, contentPane);

        layout.putConstraint(SpringLayout.WEST, extensionsPanel, innerPad + 1, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, extensionsPanel, innerPad + 1, SpringLayout.SOUTH, workspacePanel);
        layout.putConstraint(SpringLayout.EAST, extensionsPanel, -innerPad - 1, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, extensionsPanel, -innerPad - 1, SpringLayout.SOUTH, contentPane);

        pack();
        setVisible(true);
        System.err.println(paintPanel.getHeight() + " " + chatPanel.getHeight());

        relocateToCenter();
    }

    private void constructWorkspacePanel() {
        workspacePanel = new JPanel();
        workspacePanel.setBackground(contentPane.getBackground());

        workspacePanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1;

        constraints.gridy = 0;
        constraints.weighty = WORKSPACE_HEIGHT;
        constraints.fill = GridBagConstraints.BOTH;
        workspacePanel.add(paintPanel, constraints);

        constraints.gridy = 1;
        constraints.weighty = 0;
        constraints.insets = new Insets(innerPad, framePad, 0, framePad);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        workspacePanel.add(chatPanel, constraints);

        workspacePanel.setSize(new Dimension(FRAME_WIDTH, WORKSPACE_HEIGHT + CHAT_HEIGHT));
        workspacePanel.setPreferredSize(new Dimension(FRAME_WIDTH, WORKSPACE_HEIGHT + CHAT_HEIGHT));
    }

    private void relocateToCenter() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        setLocation(new Point((int) (screenSize.getWidth() - getWidth()) / 2, (int) (screenSize.getHeight() - getHeight()) / 2));
    }

    void handleChatActivator() {
        chatPanel.setVisible(!chatPanel.isVisible());
    }

    ClientNetManager getClientNetManager() {
        return clientNetManager;
    }

    public FieldPanel getFieldPanel() {
        return paintPanel.getFieldPanel();
    }

    public ChatPanel getChatPanel() {
        return chatPanel;
    }

    public String getUsername() {
        return username;
    }

    void setUsername(String value) {
        username = value;
    }

    public static void main(String[] args) throws IOException {
        new Frame();
    }

}
