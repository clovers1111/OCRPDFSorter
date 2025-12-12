package com.clovers1111;

import java.io.File;

public class FileWrapper extends PdfToImageService {
    public FileWrapper(File file){
        this.pdfImgFile = file;
    }

    public File getPdfImgFile(){
        return this.pdfImgFile;
    }



    public void setOcrImage(File img){
        this.ocrImgFile = img;
    }

    public void setOcrText(String text){
        this.ocrText = text;
    }

    public String getOcrText(){
        return this.ocrText;
    }

    public File getOcrImage(){
        return this.ocrImgFile;
    }

    private File pdfImgFile;
    private String ocrText;
    private File ocrImgFile;

}
