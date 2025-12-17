package com.clovers1111;
import java.awt.image.BufferedImage;
import java.io.File;
import net.sourceforge.tess4j.Tesseract;

public class OcrParserTesseractService {
    public String parse(BufferedImage imageFile) throws Exception {
        Tesseract tesseract = new Tesseract();
        //Eventually implement pwd
        tesseract.setDatapath(System.getProperty("user.dir")+ "/src/main/resources/tessdata/");
        // Perform OCR on the image
        tesseract.setLanguage("eng"); // Set language for OCR
        return tesseract.doOCR(imageFile);
    }
}
