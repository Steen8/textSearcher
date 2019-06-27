package com;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MyGUIForm extends JFrame{
    private JPanel app;
    private JPanel trees;
    private JPanel docs;
    private JLabel textToSearch;
    private JTextField textToSearchField;
    private JLabel fileType;
    private JTextField formatTextField;
    private JTabbedPane tabbedPanel;
    private JButton directoryButton;
    private JLabel directory;
    private JLabel dirNameFieldLabel;
    private JTextField dirNameField;
    private JButton searchButton;
    private JTree filesTree;
    private JList listOfFileContent;

    private String dirNameStr;
    private List<File> filesWithGivenFormat;
    private List<File> filesWithGivenText;

    public MyGUIForm() throws HeadlessException {
        filesTree.setModel(null);
        dirNameField.setEditable(false);

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

        filesTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    TreePath selectionPath = filesTree.getSelectionPath();
                    if(selectionPath != null) {
                        String selectedName = selectionPath.getLastPathComponent().toString();
                        System.out.println(selectedName);
                        int indexOfFormat = selectedName.lastIndexOf("txt");
                        if(indexOfFormat > 0 && selectedName.substring(indexOfFormat).equals(formatTextField.getText())) { //checks that selected path is not the folder that may contain "given format of file"
                            String currentFilePath = dirNameStr;
                            for(int i = 1; i < selectionPath.getPathCount(); ++i) { //starts with "i = 1" because rootDirPath already contains rootDirName (rootDirName index is 0 in selectionPath)
                                currentFilePath = currentFilePath.concat("\\").concat(selectionPath.getPathComponent(i).toString());
                            }
                            System.out.println(currentFilePath);
                            File f = new File(currentFilePath);
                            try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
                                Vector<String> lines = new Vector<>();
                                String currentLine;
                                //tabbedPanel.setTitleAt(0, currentFilePath);
                                //listOfFileContent.setListData(new Object[] {});
                                while((currentLine = input.readLine()) != null) {
                                    lines.add(currentLine);
                                }
                                listOfFileContent.setListData(lines);
                                System.gc();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
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
