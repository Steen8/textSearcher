package com;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
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
    private JButton previousButton;
    private JButton nextButton;
    private JSeparator separator;
    private JScrollPane Tab1;

    private String dirNameStr;
    private List<File> filesWithGivenFormat;
    private List<File> filesWithGivenText;

    private int currentIndexOfFoundText = 0;
    private List<Integer> indexesOfFoundText;

    public MyGUIForm() throws HeadlessException {
        filesTree.setModel(null);
        dirNameField.setEditable(false);
        indexesOfFoundText = new ArrayList<>();

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
            if (!dirNameField.getText().equals("")) {
                filesTree.setModel(null);
                Searcher searcher = Searcher.getInstance();
                if (filesWithGivenFormat != null) {
                    filesWithGivenFormat.clear();
                }
                filesWithGivenFormat = new ArrayList<>();

                //creating worker to make the awaiting window until job is done
                final JDialog loading = new JDialog();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        Searcher.searchFilesWithGivenFormat(dirNameStr, formatTextField.getText(), filesWithGivenFormat);

                        filesWithGivenText = searcher.searchFilesWithGivenText(filesWithGivenFormat, textToSearchField.getText());

                        PathsTree pathsTree = new PathsTree(dirNameStr);
                        pathsTree.showTree(filesTree, filesWithGivenText, dirNameStr);
                        return null;
                    }

                    @Override
                    protected void done() {
                        loading.dispose();
                    }
                };
                AwaitingWindowWorker.executeWorker(loading, filesTree, worker);
                worker.execute();
            }


        });

        filesTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    TreePath selectionPath = filesTree.getSelectionPath();
                    if(selectionPath != null) {
                        String selectedName = selectionPath.getLastPathComponent().toString();
                        int indexOfFormat = selectedName.lastIndexOf(formatTextField.getText());

                        //checks that selected path is not the folder that may contain "given format of file"
                        if(indexOfFormat > 0 && selectedName.substring(indexOfFormat).equals(formatTextField.getText())) {
                            String currentFilePath = dirNameStr;

                            //starts with "i = 1" because rootDirPath already contains rootDirName (rootDirName index is 0 in selectionPath)
                            for(int i = 1; i < selectionPath.getPathCount(); ++i) {
                                currentFilePath = currentFilePath.concat("\\").concat(selectionPath.getPathComponent(i).toString());
                            }
                            indexesOfFoundText.clear();
                            currentIndexOfFoundText = 0;
                            File f = new File(currentFilePath);
                            try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
                                Vector<String> lines = new Vector<>();

                                //creating worker to make the awaiting window until job is done
                                final JDialog loading = new JDialog();
                                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() throws IOException {
                                        String currentLine;
                                        int i = 0;
                                        while ((currentLine = input.readLine()) != null) {
                                            if(currentLine.contains("123")) {
                                                indexesOfFoundText.add(i);
                                            }
                                            i++;
                                            lines.add(currentLine);
                                        }
                                        listOfFileContent.setListData(lines);
                                        if(indexesOfFoundText.size() != 0) {
                                            listOfFileContent.setSelectedValue
                                                    (listOfFileContent.getModel().getElementAt(indexesOfFoundText.get(currentIndexOfFoundText)), true
                                                    );
                                        }
                                        return null;
                                    }

                                    @Override
                                    protected void done() {
                                        loading.dispose();
                                    }
                                };
                                AwaitingWindowWorker.executeWorker(loading, tabbedPanel, worker);
                                worker.execute();
                                tabbedPanel.setTitleAt(0, currentFilePath);

                                System.gc();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        previousButton.addActionListener(e -> {
            if(indexesOfFoundText.size() > 0 && currentIndexOfFoundText != 0) {
                listOfFileContent.setSelectedIndex(indexesOfFoundText.get(--currentIndexOfFoundText));
                listOfFileContent.ensureIndexIsVisible(indexesOfFoundText.get(currentIndexOfFoundText));
            }
        });

        nextButton.addActionListener(e -> {
            if(indexesOfFoundText.size() > 0 && currentIndexOfFoundText < indexesOfFoundText.size() - 1) {
                listOfFileContent.setSelectedIndex(indexesOfFoundText.get(++currentIndexOfFoundText));
                listOfFileContent.ensureIndexIsVisible(indexesOfFoundText.get(currentIndexOfFoundText));
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
