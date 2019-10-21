import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ImageProcessing {
    public static void main(String[] args) {
        //The user enters input, output image files and the type of operation through command line arguments

        if (args.length == 0) {
            System.out.println("Missing input, output file names and the type of operation");
        }

        String inputFile = args[0];
        String outputFile = args[1];
        String op = args[2];

        System.out.println("Input image file: " + inputFile);
        System.out.println("Output image file: " + outputFile);
        System.out.println("What operation do you want to perform: " + op);

        //Extract pixel values from a real image file and return to a 2D integer array
        int[][] arr = extract(inputFile);

        //Create an Image object
        Image image = new Image(arr, arr.length, arr[0].length);

        //Operation menu
        switch (op) {
            case "grayscale":
                image.grayScale();
                break;
            case "invert":
                image.invertImage();
                break;
            case "add_bars":
                image.addBars();
                break;
            case "blur":
                image.blurImage();
                break;
            case "emboss":
                image.embossImage();
                break;
        }

        //Get pixel values (a 2D array of integers) from the Image object
        arr = image.getPixels();

        //Create a real output image file with pixel values from the Image object
        write(arr, outputFile);

        System.out.println("Image Processing is complete!");
    }

    public static int[][] extract(String imgInput) {
        BufferedImage imgBuf = null;
        File imgFile = null;

        //read image
        try {
            imgFile = new File(imgInput);
            imgBuf = ImageIO.read(imgFile);
        } catch (IOException e) {
            System.out.println(e);
        }

        //get width and height of the image
        int width = imgBuf.getWidth();
        int height = imgBuf.getHeight();

        // extract pixel values
        int[][] arr = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                arr[j][i] = imgBuf.getRGB(j, i);
            }
        }
        return arr;
    }

    public static void write(int[][] arr, String out) {
        BufferedImage img = new BufferedImage(arr.length, arr[0].length, BufferedImage.TYPE_INT_RGB );
        File imgFile;
        for (int i = 0; i < arr[0].length; i++)
            for (int j = 0; j < arr.length; j++)
                img.setRGB(j,i,arr[j][i]);
        try {
            imgFile = new File(out);
            ImageIO.write(img, "jpg", imgFile);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static class Pixel {
        private int value;
        private int row;
        private int col;
        public Image img;

        public Pixel() {
            value = 0;
        }

        public Pixel(int value, int row, int col, Image img) {
            this.value = value;
            this.row = row;
            this.col = col;
            this.img = img;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        public void print() {
            System.out.print("\t" + value + "\t");
        }
    }

    public static class Image {
        private int rows;
        private int cols;
        public Pixel[][] pixels;

        public Image() {

        }

        public Image(int[][] arr, int rows, int cols) {
            pixels = new Pixel[rows][];
            for (int i = 0; i < rows; i++)
                pixels[i] = new Pixel[cols];
            this.rows = rows;
            this.cols = cols;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    pixels[i][j] = new Pixel(arr[i][j], i, j, this);
                }
            }
        }

        public int[][] getPixels() {
            int rows = pixels.length;
            int cols = pixels[0].length;
            int[][] arr = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    arr[i][j] = pixels[i][j].getValue();
                }
            }
            return arr;
        }

        public void grayScale() {

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int pixel = pixels[i][j].getValue();

                    //extract color values
                    int a = (pixel >> 24) & 0xff;
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;

                    // change color values for an invert image
                    int avg = (r + g + b) / 3;
                    r = avg;
                    g = avg;
                    b = avg;

                    //set new RGB values
                    pixel = (a << 24) | (r << 16) | (g << 8) | b;

                    pixels[i][j].setValue(pixel);
                }
            }
        }

        public void invertImage() {

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int pixel = pixels[i][j].getValue();

                    //extract color values
                    int a = (pixel >> 24) & 0xff;
                    int r = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int b = pixel & 0xff;

                    // change color values for an invert image

                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;

                    //set new RGB values
                    pixel = (a << 24) | (r << 16) | (g << 8) | b;

                    pixels[i][j].setValue(pixel);
                }
            }
        }

        // working
        public void addBars() {
            Scanner input = new Scanner(System.in);
            System.out.println("Image Dimensions: " + rows + "x" + cols);
            System.out.println("Enter grid size separated by a space (rows columns): ");
            int inRows = input.nextInt();
            int inCols = input.nextInt();

            int vertBars = rows/inRows;
            int horzBars = cols/inCols;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // Horizontal
                    if (i%vertBars == 0){
                        pixels[i][j].setValue(0);
                        if (i+5>rows){
                            for (int x = 1; x <= 5; x++)
                                pixels[i-x][j].setValue(0);
                        }
                        else{
                            for (int x = 1; x <= 5; x++)
                                pixels[i+x][j].setValue(0);
                        }
                    }
                    // Vertical
                    if(j%horzBars == 0){
                        pixels[i][j].setValue(0);
                        if (j+5>cols){
                            for (int x = 1; x <= 5; x++)
                                pixels[i][j-x].setValue(0);

                        }
                        else{
                            for (int x = 1; x <= 5; x++)
                                pixels[i][j+x].setValue(0);
                        }
                    }
                }
            }
        }

        // filter function for blur
        public BufferedImage filterImageB(float [] matrix){
            BufferedImage img= new BufferedImage(rows, cols, BufferedImage.TYPE_INT_ARGB ); // create buffimg
            // fill image with pixel values
            int [][] arr = this.getPixels();
            for (int i=0; i < rows; i++){
                for (int j = 0; j < cols; j++) {
                    img.setRGB(i,j,arr[i][j]);
                }
            }
            // filter image through blur filter
            BufferedImageOp blur = new ConvolveOp( new Kernel(3, 3, matrix));
            BufferedImage imgDst = blur.createCompatibleDestImage(img, null);
            img = blur.filter(img, imgDst);
            return img;

        }
        // working
        public void blurImage() {
            float[] blurMatrix = {
                    1 / 9f, 1 / 9f, 1 / 9f,
                    1 / 9f, 1 / 9f, 1 / 9f,
                    1 / 9f, 1 / 9f, 1 / 9f,
            };

            BufferedImage img = filterImageB(blurMatrix);
            int[][] arr = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    arr[i][j] = img.getRGB(i, j);

                    pixels[i][j].setValue(arr[i][j]);
                }
            }


        }
        // filter for emboss
        public BufferedImage filterImageE(float [] matrix){
            BufferedImage img= new BufferedImage(rows, cols, BufferedImage.TYPE_INT_ARGB ); // create buffimg
            // fill image with pixel values
            int [][] arr = this.getPixels();
            for (int i=0; i < rows; i++){
                for (int j = 0; j < cols; j++) {
                    img.setRGB(i,j,arr[i][j]);
                }
            }
            // filter image through blur filter
            BufferedImageOp blur = new ConvolveOp( new Kernel(3, 3, matrix));
            BufferedImage imgDst = blur.createCompatibleDestImage(img, null);
            img = blur.filter(img, imgDst);
            return img;

        }
        // working
        public void embossImage() {
            float [] embossMatrix = {
                    -2, -1, 0,
                    -1, 1, 1,
                    0, 1, 2,
            };

            BufferedImage img = filterImageE(embossMatrix);
            int [][] arr = new int [rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    arr[i][j] = img.getRGB(i, j);

                    pixels[i][j].setValue(arr[i][j]);
                }
            }
        }
    }
}
