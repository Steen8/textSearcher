package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyGUIForm extends JFrame{
    private JPanel app;
    private JPanel trees;
    private JPanel docs;
    private JLabel textToSearch;
    private JTextField textToSearchField;
    private JTree filesTree;
    private JLabel fileType;
    private JTextField logTextField;
    private JTabbedPane tabbedPane1;
    private JScrollBar scrollBar1;
    private JButton directoryButton;
    private JLabel directory;
    private JPanel tabs;
    private JLabel dirNameFieldLabel;
    private JTextField dirNameField;
    private JButton searchButton;

    private String dirNameStr;

    public MyGUIForm() throws HeadlessException {
        super("Text");

        /* creating FileChooser window to choose
        *  what directory to search file in */

        directoryButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // set fileChooser to be able to select only directories
            int response = jFileChooser.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION) {                     // if user didn't close the dir selection without approving selected directory
                dirNameStr = jFileChooser.getSelectedFile().toString();
                dirNameField.setText(dirNameStr);
            }
        });


        searchButton.addActionListener(e -> {

        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TextSearcher");
        frame.setContentPane(new MyGUIForm().app);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);


    }
}
