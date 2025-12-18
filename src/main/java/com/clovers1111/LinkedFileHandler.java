package com.clovers1111;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import static java.util.Arrays.stream;

public class LinkedFileHandler {
    private List<FileWrapper> fileWrappers;
    private List<Rectangle> selections;
    private String tempFileDir;
    private List<FileWrapper> unlabledFileWrappers;

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

    public void applyAttributesToFileWrappers() throws Exception {

        OcrParserTesseractService ocrParser = new OcrParserTesseractService();

        for (FileWrapper fileWrapper : fileWrappers) {
            //Convert the file to a buffered image - this conversion is preferred to reduce RAM usage.
            BufferedImage tempBim = ImageIO.read(fileWrapper.getPdfImgFile());
            for (Rectangle rectangle : selections) {       // We'll crop the image and iterate through the user's selections
                String tempOcrText = ocrParser.parse(
                        /* <--! IMPORTANT NOTE FOR LATER !-->
                        * If you extract a small subimage from a very large
                        * original image, the entire large image's data remains
                        * in memory as long as the subimage reference is held.
                         */
                        tempBim.getSubimage(               // Image crop
                            rectangle.x,
                            rectangle.y,
                            rectangle.width,
                            rectangle.height)
                        ).chars()                                   // Stream the input
                        .filter(c -> Character.isDigit(c))      // and throw away non-digits
                        .mapToObj(c -> String.valueOf((char) c))
                        .collect(Collectors.joining()
                        );
                //This will never run if our OCR service never reads a number;
                // this is intentional because the ocrInteger is init. to null
                if (!tempOcrText.isBlank()){
                    fileWrapper.setOcrInteger(Short
                            .parseShort(tempOcrText));
                    break;
                }
            };

        }

    }

    public void sortFileWrappers(){
        fileWrappers.sort(FileWrapper::compareTo);
    }

    public void printFileWrappersAndLocation(){
        short i = 0;
        for (FileWrapper fileWrapper : fileWrappers){
            System.out.println(fileWrapper + " " + i);
            i++;
        }
    }




    //mem leak?
    public void clearSelections(){
        this.selections = new ArrayList<>();
    }

    public List<Rectangle> getSelections(){
        return this.selections;
    }

    public void printSelections(){

    }


    public void makePdfFromFileWrappers() throws IOException {
        PDDocument workingDocument = new PDDocument();
        for (FileWrapper fileWrappers : fileWrappers){
            PDPage page = new PDPage();
            workingDocument.addPage(page);

            String imgPath = fileWrappers.getPdfImgFile().getAbsolutePath();

            PDImageXObject pdImage = PDImageXObject.createFromFile(imgPath, workingDocument);

            try (PDPageContentStream contents = new PDPageContentStream(workingDocument, page)) {
                contents.drawImage(pdImage,
                        0, 0,
                        page.getMediaBox().getWidth(),
                        page.getMediaBox().getHeight());
            };
        }
        String sortedPdfString = DataManager.pathToPdfFile
                .toString()
                .substring(0, DataManager.pathToPdfFile.toString().length()-4)
                + "_sorted.pdf";


        workingDocument.save(sortedPdfString);
        workingDocument.close();

    }

    private void addSelections(Rectangle rectangle) {
        this.selections.add(rectangle);
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
