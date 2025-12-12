package com.clovers1111;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfParserPDFBoxService {
    public String parse(String filePath) throws Exception {
        PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(filePath));

        if (document.isEncrypted()) {
            throw new Exception("Document is encrypted.");
        }

        PDFTextStripper pdfStripper = new PDFTextStripper();
        return pdfStripper.getText(document);
    }
}