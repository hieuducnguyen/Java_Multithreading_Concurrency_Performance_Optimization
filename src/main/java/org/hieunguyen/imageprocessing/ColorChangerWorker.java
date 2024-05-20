package org.hieunguyen.imageprocessing;

import java.awt.image.BufferedImage;

public class ColorChangerWorker extends Thread {

  private final BufferedImage originalImage;
  private final BufferedImage outputImage;
  private final int startWidth;
  private final int startHeight;
  private final int endWidth;
  private final int endHeight;

  public ColorChangerWorker(BufferedImage originalImage, BufferedImage outputImage, int startWidth,
      int startHeight, int endWidth, int endHeight) {
    this.originalImage = originalImage;
    this.outputImage = outputImage;
    this.startWidth = startWidth;
    this.startHeight = startHeight;
    this.endWidth = endWidth;
    this.endHeight = endHeight;
  }

  public boolean isShadeOfGray(int red, int green, int blue) {
    return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
  }

  public int createRGBFromColor(int red, int green, int blue) {
    int rgb = 0;
    rgb |= blue;
    rgb |= green << 8;
    rgb |= red << 16;
    rgb |= 0xFF000000;
    return rgb;
  }

  public int getRed(int rgb) {
    return (rgb & 0x00FF0000) >> 16;
  }

  public int getBlue(int rgb) {
    return rgb & 0x000000FF;
  }

  public int getGreen(int rgb) {
    return (rgb & 0x0000FF00) >> 8;
  }

  public static void setRGB(BufferedImage image, int x, int y, int rgb) {
    image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
  }

  @Override
  public void run() {
    for (int i = startWidth; i < endWidth; i++) {
      for (int j = startHeight; j < endHeight; j++) {
        int red = getRed(originalImage.getRGB(i, j));
        int blue = getBlue(originalImage.getRGB(i, j));
        int green = getGreen(originalImage.getRGB(i, j));
        int newRed = red;
        int newBlue = blue;
        int newGreen = green;
        if (isShadeOfGray(red, green, blue)) {
          newRed = Math.min(255, red + 10);
          newGreen = Math.max(0, green - 80);
          newBlue = Math.max(0, blue - 20);
        }
        int newRGB = createRGBFromColor(newRed, newGreen, newBlue);
        setRGB(outputImage, i, j, newRGB);
      }
    }
  }
}
