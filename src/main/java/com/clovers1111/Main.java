package com.clovers1111;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
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
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0,120, ImageType.RGB);  // Creates a buffered image for later saving;
            String tempImgName = DataManager.tempFileDir + "temp_img" + count++ + ".jpg";                        // produces a string to specify file saving location
            File tempImgFile = new File(tempImgName);                                                // and a file object to store
            tempImgFile.createNewFile();
            ImageIO.write(bim, "JPG", tempImgFile);                                       // Saves object


            fileWrapperHandler.add(new FileWrapper(tempImgFile));                //  fileWrapper obj. created; contains prev. file
        }


        // Now we need to ascertain where to find the page number in the photos

        fileWrapperHandler.setSelections();
        System.out.println("test");










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
