package com.clovers1111;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LinkedFileHandler {
    private List<FileWrapper> fileWrappers;
    private List<Rectangle> selections;
    private String tempFileDir;

    public LinkedFileHandler(){
        this.fileWrappers = new ArrayList<>();
        this.selections = new ArrayList<>();
    }

    public void add(FileWrapper fileWrapper){
        this.fileWrappers.add(fileWrapper);
    }

    public File getRandomImgFile(){
        int randInt = (int)(Math.random() * (fileWrappers.size()));
        return fileWrappers.get(randInt).getPdfImgFile();
    }

    public List<FileWrapper> getFileWrappers(){
        return this.fileWrappers;
    }


    //Data needs to be saved to a file each time this is complete to pass info from ImageSelector
    // back to this class.
    public void setSelections(){

            File imageFile = getRandomImgFile();



            // Creates new JFrame object based on random file on previous line.
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Image Region Selector");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                ImageSelector selector = new ImageSelector(imageFile);

                // Our listener will pass through the rectangle objects from ImageSelector to our present class
                selector.setSelectionListener(this::addSelections);

                JScrollPane scrollPane = new JScrollPane(selector);
                frame.add(scrollPane);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            });


    }

    public void applyAttributesToFileWrappers(){

    }

    private void addSelections(Rectangle rectangle) {
        this.selections.add(rectangle);
    }

    //mem leak?
    public void clearSelections(){
        this.selections = new ArrayList<>();
    }

    public List<Rectangle> getSelections(){
        return this.selections;
    }



    /*
    private void setDirectoryLocation(){
        String[] splitDir = fileWrappers.get(0).getPdfImgFile().getAbsolutePath().split("/");
        for (int i = 0; i < splitDir.length - 1; i++){  //creates </path/to/temp/dir>
            this.tempFileDir += "/" + splitDir[i];
        }
        this.tempFileDir += tempFileDir + "/";          //creates </path/to/temp/dir/>
    }
     */
}
