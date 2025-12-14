package com.clovers1111;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LinkedFileHandler {
    private File selectio;
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


    //Data needs to be saved to a file each time this is complete to pass info from ImageSelector
    // back to this class.
    public void setSelections(){

            File image = getRandomImgFile();


            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Image Region Selector");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                ImageSelector selector = new ImageSelector(image);

                JScrollPane scrollPane = new JScrollPane(selector);
                frame.add(scrollPane);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });

    }


    private void setSelectionDocument(){

    }

    private void populateSelectionDocument(){

    }
    private void setDirectoryLocation(){
        String[] splitDir = fileWrappers.get(0).getPdfImgFile().getAbsolutePath().split("/");
        for (int i = 0; i < splitDir.length - 1; i++){  //creates </path/to/temp/dir>
            this.tempFileDir += "/" + splitDir[i];
        }
        this.tempFileDir += tempFileDir + "/";          //creates </path/to/temp/dir/>
    }
}
