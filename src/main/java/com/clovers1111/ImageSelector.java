package com.clovers1111;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageSelector extends JPanel {
    private BufferedImage image;

    // Variables to store the selection coordinates
    private int selectionX;
    private int selectionY;
    private int selectionWidth;
    private int selectionHeight;

    // Temporary variables for the mouse drag logic
    private Point startPoint;
    private Point currentPoint;
    private boolean isSelecting = false;

    public ImageSelector(File image) {
        try {
            this.image = ImageIO.read(image);
            // Set the panel size to match the image
            this.setPreferredSize(new Dimension(this.image.getWidth(), this.image.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage());
        }

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
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
                    repaint(); // Redraw panel to show the rectangle growing
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isSelecting = false;
                calculateSelection(); // Finalize the coordinates
                System.out.println("Selection Saved: [X=" + selectionX + ", Y=" + selectionY +
                        ", W=" + selectionWidth + ", H=" + selectionHeight + "]");
                repaint();
            }
        };

        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    // Calculates the final top-left x/y and width/height
    // Handles dragging in any direction (e.g., bottom-right to top-left)
    private void calculateSelection() {
        int x1 = startPoint.x;
        int y1 = startPoint.y;
        int x2 = currentPoint.x;
        int y2 = currentPoint.y;

        selectionX = Math.min(x1, x2);
        selectionY = Math.min(y1, y2);
        selectionWidth = Math.abs(x1 - x2);
        selectionHeight = Math.abs(y1 - y2);
        // Save selection to a file after it has been calculated

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 1. Draw the Image
        if (this.image != null) {
            g2d.drawImage(this.image, 0, 0, this);
        }

        // 2. Draw the Selection Rectangle (if actively selecting or a selection exists)
        if (startPoint != null && currentPoint != null) {
            // Re-calculate temporarily for drawing purposes
            int x = Math.min(startPoint.x, currentPoint.x);
            int y = Math.min(startPoint.y, currentPoint.y);
            int width = Math.abs(startPoint.x - currentPoint.x);
            int height = Math.abs(startPoint.y - currentPoint.y);

            // Set color and stroke for the box
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2)); // Thickness of 2px
            g2d.drawRect(x, y, width, height);

            // Optional: Draw a semi-transparent fill
            g2d.setColor(new Color(255, 0, 0, 50)); // Red with low opacity
            g2d.fillRect(x, y, width, height);
        }
    }
}