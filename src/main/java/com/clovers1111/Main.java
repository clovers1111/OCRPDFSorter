package com.clovers1111;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main( String[] args ) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path to the PDF file starting from root: ");
        final String pdfString = scanner.nextLine();
        String[] pathToPdfDirWithPdf = pdfString.split("/");
        String pathToPdfDir = "/";
        for (int i = 0; i < pathToPdfDirWithPdf.length - 1; i++){
            pathToPdfDir += pathToPdfDirWithPdf[i] + "/";
        }

        //Needs to be changed eventually
        DataManager.pathToPdfDir = pathToPdfDir;
        DataManager.tempFileDir = pathToPdfDir + "temp/";

        PDDocument pdfFile = Loader.loadPDF(new File(pdfString));


        //We'll split pdf files into their individual pages for later conversion into image files
        Splitter splitter = new Splitter();

        List<PDDocument> splitPdfFiles = splitter.split(pdfFile);
        Iterator<PDDocument> pdfIterator = splitPdfFiles.listIterator();

        /*
        * Instantiate fileWrapper objects with images of each page of the imported PDF file.
        */

        LinkedFileHandler fileWrapperHandler = new LinkedFileHandler();

        int count = 1;
        while(pdfIterator.hasNext()){
            PDFRenderer pdfRenderer = new PDFRenderer(pdfIterator.next());
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0,120, ImageType.RGB);              // Creates a buffered image for later saving;
            String tempImgName = DataManager.tempFileDir + "temp_img" + count++ + ".jpg";                        // produces a string to specify file saving location
            File tempImgFile = new File(tempImgName);                                                            // and a file object to store
            tempImgFile.createNewFile();
            ImageIO.write(bim, "JPG", tempImgFile);                                                     // Writes data to blank file


            fileWrapperHandler.add(new FileWrapper(tempImgFile));                //  fileWrapper obj. created; contains prev. file
        }

        System.out.println("Successfully processed " + fileWrapperHandler.getFileWrappers().size() + " pages.");
        System.out.println("Starting page number selection process . . . ");
        System.out.println("Highlight the page number and then exit the page.");
        // Now we need to ascertain where to find the page number in the photos
        do{
            fileWrapperHandler.setSelections();
            System.out.println("Are you finished selecting? (y/n): ");
            if (scanner.nextLine().equals("n"))
                continue;
            else {

                System.out.print("Your selections: ");
                for (Rectangle rectangle : fileWrapperHandler.getSelections()){
                    System.out.print(rectangle + ", ");
                }

                System.out.println("\nRetry? (y/n): ");
                if (scanner.nextLine().equals("y")){
                    fileWrapperHandler.clearSelections();
                }
                else {          //exit, finally
                    break;
                }

            }

        }while(true);

        /*
        *
        * Selections have been made and confirmed by user. Now we need to use those
        * selections to create cropped images for our ocr service to evaluate. The
        * evaluations are immediately saved to their respective FileWrapper object
        * in the "ocrText" private data member.
        *
        */















        /*
        String filePath = "/home/harry/Documents/Scans/STA381/test/Selection_162.png";
            String text;
            System.out.println("OCR");
            OcrParserTesseractService ocr = new OcrParserTesseractService();
            text = ocr.parse(filePath);

        System.out.println(text);
         */
    }
}
