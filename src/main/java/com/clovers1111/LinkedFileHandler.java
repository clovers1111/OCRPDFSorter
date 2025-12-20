package com.clovers1111;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


public class LinkedFileHandler {
    private List<FileWrapper> fileWrappers;
    private List<Rectangle> selections;
    private String tempFileDir;
    private List<FileWrapper> unlabeledFileWrappers;

    public LinkedFileHandler(){
        this.fileWrappers = new ArrayList<>();
        this.selections = new ArrayList<>();
        this.unlabeledFileWrappers = new ArrayList<>();
    }

    public void add(FileWrapper fileWrapper){
        this.fileWrappers.add(fileWrapper);
    }

    public File getRandomImgFile(List<FileWrapper> fileWrappers){
        int randInt = (int)(Math.random() * (fileWrappers.size()));
        return fileWrappers.get(randInt).getPdfImgFile();
    }

    public List<FileWrapper> getFileWrappers(){
        return this.fileWrappers;
    }

    public List<FileWrapper> getUnlabeledFileWrappers(){
        return this.unlabeledFileWrappers;
    }

    public void setSelections(){
        setSelections(this.fileWrappers);
    }

    public void setSelectionsForUnlabeled(){
        setSelections(this.unlabeledFileWrappers);
    }

    public void applyAttributesToUnlabeledFileWrappers() throws Exception {
        applyAttributesToFileWrappers(this.unlabeledFileWrappers);
    }


    public void applyAttributesToFileWrappers() throws Exception {
        applyAttributesToFileWrappers(this.fileWrappers);
    }


    //Data needs to be saved to a file each time this is complete to pass info from ImageSelector
    // back to this class.

    //Make helper functions



    private void setSelections(List<FileWrapper> fileWrapper){            //Eventually update to integrate class func. into UI

            File imageFile = getRandomImgFile(fileWrapper);

            // Creates new JFrame object based on random file on previous line.
            SwingUtilities.invokeLater(() -> {
                JFrame mainPanel = new JFrame("Image Region Selector");
                mainPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ImageSelector selector = new ImageSelector(imageFile);
                mainPanel.setLayout(new BorderLayout());
                // Our listener will pass through the rectangle objects from ImageSelector to our present class
                selector.confirmSelectionListener(this::addSelections); //calls this method with rectangle parameter



                JScrollPane scrollPane = new JScrollPane(selector);
                JButton confirmButton = new JButton("Confirm selection");
                mainPanel.add(confirmButton, BorderLayout.NORTH);
                confirmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        selector.notifySelection();
                    }

                });
                mainPanel.getContentPane().add(scrollPane);


                mainPanel.pack();
                mainPanel.setLocationRelativeTo(null);
                mainPanel.setVisible(true);

            });


    }

    private void applyAttributesToFileWrappers(List<FileWrapper> fileWrappers) throws Exception {

        OcrParserTesseractService ocrParser = new OcrParserTesseractService();

        for (FileWrapper fileWrapper : fileWrappers) {
            //Convert the file to a buffered image - this conversion is preferred to reduce RAM usage.
            BufferedImage tempBim = ImageIO.read(fileWrapper.getPdfImgFile());
            for (Rectangle rectangle : selections) // We'll crop the image and iterate through the user's selections
            {
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
                }   // If OcrInteger = -1, ocr failed to rec. image, and we don't want to double dip
                if (fileWrapper.getOcrInteger() == -1 && !unlabeledFileWrappers.contains(fileWrapper)){
                    this.unlabeledFileWrappers.add(fileWrapper);
                }
            };

        }
        //We're done with these selections
        clearSelections();
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

    public void addSelections(Rectangle rectangle) {
        this.selections.add(rectangle);
    }

}
