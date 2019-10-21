


# Image Processing
A command line application that can perform various image manipulation processes to a specified image. Written in Java.

## Features:
* Command-Line options
* Keeps original - Makes modified copy
* Options:
	* Grayscale
	* Invert
	* Add Grid
	* Blur
	* Emboss
## Code Highlights:
### Grayscale:

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
### Invert:
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
### Add Grid
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
### Blur:
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
### Emboss:
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

## Screenshots:
![OriginalImage](https://raw.githubusercontent.com/edwardayala/imageProcessing/master/memoji_black.png)
![Gray](https://raw.githubusercontent.com/edwardayala/imageProcessing/master/grayMemoji.png)
![Invert](https://raw.githubusercontent.com/edwardayala/imageProcessing/master/invertMemoji.png)
![Bars](https://raw.githubusercontent.com/edwardayala/imageProcessing/master/barsMemoji.png)
![Blur](https://raw.githubusercontent.com/edwardayala/imageProcessing/master/blurMemoji.png)
![Emboss](https://raw.githubusercontent.com/edwardayala/imageProcessing/master/embossMemoji.png)
