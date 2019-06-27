package com;

import javax.swing.*;
import java.awt.*;

public class AwaitingWindowWorker {
    public static void executeWorker (JDialog loading, JComponent pane, SwingWorker<Void, Void> worker){
        initializeAwaitingWindow(loading, pane);
        worker.execute();
        loading.setVisible(true);
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private static void initializeAwaitingWindow(JDialog loading, JComponent pane) {
        JPanel p1 = new JPanel(new BorderLayout());
        JLabel awaiting = new JLabel("Please wait...");
        awaiting.setBackground(Color.DARK_GRAY);
        awaiting.setFont(new Font("Calibri", Font.PLAIN, 20));
        p1.setBorder(BorderFactory.createEtchedBorder());
        p1.add(awaiting, BorderLayout.CENTER);
        loading.setUndecorated(true);
        loading.getContentPane().add(p1);
        loading.pack();
        loading.setLocationRelativeTo(pane);
        loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loading.setModal(true);
    }
}
