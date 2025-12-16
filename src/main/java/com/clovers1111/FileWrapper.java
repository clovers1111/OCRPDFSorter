package com.clovers1111;

import java.io.File;

public class FileWrapper extends PdfToImageService implements Comparable<FileWrapper> {
    public FileWrapper(File file){
        this.pdfImgFile = file;
        this.ocrInteger = null;
    }

    public File getPdfImgFile(){
        return this.pdfImgFile;
    }

    public void setOcrInteger(Integer integer){
        this.ocrInteger = integer;
    }

    public int getOcrInteger(){
        return this.ocrInteger;
    }



    private File pdfImgFile;    //Stored as a file rather than a buffered image for reduced memory usage!
    private Integer ocrInteger;

    @Override
    public int compareTo(FileWrapper otherWrapper) {
        return ocrInteger - otherWrapper.getOcrInteger();
    }
}
