A personal Java utility designed to reorder scrambled PDF files automatically. This program uses optical character recognition to identify page numbers within a document and reassemble the pages in the correct numerical order.

# 💡 Overview

When PDFs are scanned or compiled incorrectly, they often end up out of order. This tool provides a streamlined workflow to fix them:

    Load: Import a PDF from your local directory.

    Process: The program splits the PDF into individual pages and converts them into high-resolution images.

    Select: A JPanel UI allows you to manually highlight the specific area on the page where the page number is located.

    OCR & Sort: The program crops that region, reads the digits via OCR, and sorts the pages accordingly.

    Export: A new, perfectly ordered PDF is generated.

#  Getting Started
## Prerequisites

    Java Development Kit (JDK) installed.

    Tesseract OCR engine installed on your system.

# How to Use

    Run the application: Launch the Main class.

    Provide Path: Enter the absolute path to your target PDF when prompted in the console.

    Define the Search Area: * A UI window will appear showing a page of your PDF.
        - Click and drag to draw a rectangle around the page number.
        - Close the window when finished.

    Confirm: Verify your selection in the console. You can retry if the selection was inaccurate.

    Result: The program will process all pages, sort them, and save the final document in the original directory.


# 📝 Implementation Details
The core logic utilizes a "Select-Once-Apply-All" approach. By allowing the user to define a Rectangle via JPanel, the program gains high accuracy by only looking at a specific coordinate for numerical data, significantly reducing OCR noise from other text on the page.

# Future Enhancements

    Implement a progress bar for each process.
    Handle off cases wherein pages are offset or do not contain page numbers.
    Create a fully integrated GUI to replace the console input.

(Written with the help of AI.)
