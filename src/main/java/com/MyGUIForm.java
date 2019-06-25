package com;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyGUIForm extends JFrame{
    private JPanel app;
    private JPanel trees;
    private JPanel docs;
    private JLabel textToSearch;
    private JTextField textToSearchField;
    private JLabel fileType;
    private JTextField formatTextField;
    private JTabbedPane tabbedPane1;
    private JButton directoryButton;
    private JLabel directory;
    private JLabel dirNameFieldLabel;
    private JTextField dirNameField;
    private JButton searchButton;
    private JTree filesTree;

    private String dirNameStr;
    private List<File> filesWithGivenFormat;
    private List<File> filesWithGivenText;

    public MyGUIForm() throws HeadlessException {
        filesTree.setModel(null);

        /* creating FileChooser window to choose
        *  what directory to search file in */

        directoryButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // set fileChooser to be able to select only directories
            int response = jFileChooser.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION) {                     // if user didn't close the dir selection without approving selected directory
                dirNameStr = jFileChooser.getSelectedFile().getAbsolutePath();
                dirNameField.setText(dirNameStr);
            }
        });


        searchButton.addActionListener(e -> {
            filesTree.setModel(null);
            Searcher searcher = Searcher.getInstance();
            if(filesWithGivenFormat != null) {
                filesWithGivenFormat.clear();
            }
            filesWithGivenFormat = new ArrayList<>();
            Searcher.searchFilesWithGivenFormat(dirNameStr, formatTextField.getText(), filesWithGivenFormat);

            filesWithGivenText = searcher.searchFilesWithGivenText(filesWithGivenFormat, textToSearchField.getText());

            PathsTree pathsTree = new PathsTree(dirNameStr);
            pathsTree.showTree(filesTree, filesWithGivenText, dirNameStr);
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
