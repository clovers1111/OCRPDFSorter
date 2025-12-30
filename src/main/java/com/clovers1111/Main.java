package com.clovers1111;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.nio.file.Files;
import javax.imageio.ImageIO;

//

public class Main {
    public static void main( String[] args ) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path to the PDF file starting from root: ");
        String pdfString;
        while (true){
            pdfString = scanner.nextLine();
            if ((new File(pdfString)).isFile()){
                break;
            }
            System.out.println("The file location is invalid. Please try again: ");
        }

        DataManager.pathToPdfFile = Paths.get(pdfString);
        DataManager.pathToPdfDir = DataManager.pathToPdfFile.getParent();
        DataManager.tempFileDir = (Files.createTempDirectory("pdfsort"));

        PDDocument pdfFile = Loader.loadPDF(new File(
                DataManager.pathToPdfFile.toString())   // This is the absolute path incl. the PDF file
        );


        //We'll split pdf files into their individual pages for later conversion into image files
        Splitter splitter = new Splitter();

        List<PDDocument> splitPdfFiles = splitter.split(pdfFile);
        Iterator<PDDocument> pdfIterator = splitPdfFiles.listIterator();

        /*
        * Instantiate fileWrapper objects with images of each page of the imported PDF file.
        */

        LinkedFileHandler fileWrapperHandler = new LinkedFileHandler();


        // Now create all of the FileWrapper objects with their respective files
        int count = 1;
        while(pdfIterator.hasNext()){
            PDFRenderer pdfRenderer = new PDFRenderer(pdfIterator.next());
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0,120, ImageType.RGB);              // Creates a buffered image for later saving
            String tempImgName = DataManager.tempFileDir + "/temp_img" + count++ + ".png";                        // produces a string to specify file saving location
            File tempImgFile = new File(tempImgName);                                                            // and a file object to store
            tempImgFile.createNewFile();
            ImageIO.write(bim, "PNG", tempImgFile);                                                     // Writes data to blank file


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
        * in the "ocrInteger" private data member.
        *
        */
        fileWrapperHandler.applyAttributesToFileWrappers();

        //If anything wasn't labeled, it'll have been added to the unlabeled file arraylist. (Note: make this into a member function for fileWrapperHandler)
        if (!fileWrapperHandler.getUnlabeledFileWrappers().isEmpty()){
            do{
                fileWrapperHandler.setSelectionsForUnlabeled();;
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
        }


        // FileWrapper objects now have all the necessary info to commence sorting


        fileWrapperHandler.printFileWrappersAndLocation();
        fileWrapperHandler.sortFileWrappers();
        fileWrapperHandler.printFileWrappersAndLocation();

        // All of the fileWrappers are sorted in the fileWrapperHandler;
        // now we need to compile all of the file wrappers into a pdf and save in pdf dir

        fileWrapperHandler.makePdfFromFileWrappers();


    }
}
