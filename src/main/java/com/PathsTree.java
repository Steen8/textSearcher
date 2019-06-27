package com;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.util.*;

class PathsTree {

    private DefaultMutableTreeNode pathRoot;

    public DefaultMutableTreeNode getPathRoot() {
        return pathRoot;
    }

    public PathsTree(String rootDirNamePath) {
        this.pathRoot = new DefaultMutableTreeNode(rootDirNamePath.substring(rootDirNamePath.lastIndexOf('\\') + 1)); // + 1 to substring with '\' symbol
    }

    public void add(String str) {
        DefaultMutableTreeNode currentRoot = pathRoot;
        StringTokenizer s = new StringTokenizer(str, "\\");
        while (s.hasMoreElements()) {
            str = (String) s.nextElement();
            Enumeration children = currentRoot.children();
            int nodeIndex = -1;
            boolean doesTreeAlreadyContainThisStr = false;
            if (!children.equals(Collections.emptyEnumeration())) {
                do {
                    DefaultMutableTreeNode childStr = (DefaultMutableTreeNode) children.nextElement();
                    nodeIndex++;
                    if (childStr.getUserObject().toString().equals(str)) {
                        doesTreeAlreadyContainThisStr = true;
                        break;
                    }
                }
                while (children.hasMoreElements());
            }
            DefaultMutableTreeNode childNode;
            if (!doesTreeAlreadyContainThisStr) {
                childNode = new DefaultMutableTreeNode(str);
                currentRoot.add(childNode);
                currentRoot = childNode;
            } else {
                currentRoot = (DefaultMutableTreeNode) currentRoot.getChildAt(nodeIndex);
            }
        }
    }

    public void showTree(JTree jtree, List<File> files, String rootDirNamePath) {
        List<String> pathNamesFromRootDir = new ArrayList<>();
        if (files.size() != 0) {
            for (File f : files) {
                String nameFromRootDir = f.getAbsolutePath().substring(rootDirNamePath.length() + 1); // + 1 to substring with '\' symbol
                pathNamesFromRootDir.add(nameFromRootDir);
            }

            PathsTree pathsTree = new PathsTree(rootDirNamePath);
            for (String currentFilePath : pathNamesFromRootDir) {
                pathsTree.add(currentFilePath);
            }
            DefaultTreeModel model = new DefaultTreeModel(pathsTree.getPathRoot());
            jtree.setModel(model);
        }
    }
}