package com.clovers1111;

import java.io.File;

public class FileWrapper extends PdfToImageService {
    public FileWrapper(File file){
        this.pdfImgFile = file;
        this.ocrInteger = null;
    }

    public File getPdfImgFile(){
        return this.pdfImgFile;
    }



    public void setOcrImage(File img){
        this.ocrImgFile = img;
    }

    public void setOcrInteger(int integer){
        this.ocrInteger = integer;
    }

    public int getOcrInteger(){
        return this.ocrInteger;
    }

    public File getOcrImage(){
        return this.ocrImgFile;
    }

    private File pdfImgFile;
    private Integer ocrInteger;
    private File ocrImgFile;

}
