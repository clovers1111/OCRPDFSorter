package com.clovers1111;
import java.io.File;
import net.sourceforge.tess4j.Tesseract;

public class OcrParserTesseractService {
    public String parse(String imagePath) throws Exception {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/home/harry/IdeaProjects/pdfsort/src/main/resources/tessdata/");
        // Perform OCR on the image
        tesseract.setLanguage("eng"); // Set language for OCR
        File imageFile = new File(imagePath);
        return tesseract.doOCR(imageFile);
    }
}
