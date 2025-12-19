package com.clovers1111;

import java.io.File;

public class FileWrapper implements Comparable<FileWrapper> {
    public FileWrapper(File file){
        this.pdfImgFile = file;
        this.ocrInteger = -1;
    }

    public File getPdfImgFile(){
        return this.pdfImgFile;
    }

    public void setOcrInteger(short integer){
        this.ocrInteger = integer;
    }

    public int getOcrInteger(){
        return this.ocrInteger;
    }


    private File pdfImgFile;    //Stored as a file rather than a buffered image for reduced memory usage!
    private short ocrInteger;

    @Override
    public int compareTo(FileWrapper otherWrapper) {
        return ocrInteger - otherWrapper.getOcrInteger();
    }
    @Override
    public String toString(){
        return pdfImgFile.getName();
    }
}
