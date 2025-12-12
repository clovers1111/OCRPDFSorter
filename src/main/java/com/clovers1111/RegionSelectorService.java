package com.clovers1111;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RegionSelectorService extends JLabel {
    private int selectionX;
    private int selectionY;
    private int selectionWidth;
    private int selectionHeight;

    private BufferedImage image;

    private Point startPoint;
    private Point currentPoint;
    private boolean isSelecting = false;


    public RegionSelectorService(File imageFile) throws IOException {
        this.image = ImageIO.read(imageFile);

        MouseAdapter mouseHandler = new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    isSelecting = true;
                    startPoint = e.getPoint();
                    currentPoint = startPoint;
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (isSelecting) {
                        currentPoint = e.getPoint();
                        repaint();
                    }

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isSelecting = false;
                    calculateSelection();
                    repaint();
                }
            };

        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);

    }

    private void calculateSelection(){
        selectionX = (int) Math.min(startPoint.getX(), currentPoint.getX());
        selectionY = (int) Math.min(startPoint.getY(), currentPoint.getY());
        selectionWidth = (int) Math.max(startPoint.getX(), currentPoint.getX()) - selectionX;
        selectionHeight = (int) Math.max(startPoint.getY(), currentPoint.getY()) - selectionY;

    }
}




