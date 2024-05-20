package org.hieunguyen.imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Main {
    private static final String INPUT_IMAGE = "/Users/hieunguyen/Documents/Working/Repo/Java_Multithreading_Concurrency_Performance_Optimization/src/main"
        + "/resources/many-flowers.jpg";
    private static final String OUTPUT_IMAGE = "/Users/hieunguyen/Documents/Working/Repo/Java_Multithreading_Concurrency_Performance_Optimization/src/main"
        + "/resources/recolor-many-flowers.jpg";
    private static final int NUM_WORKER = 10;


  public static void main(String[] args) throws IOException, InterruptedException {
    BufferedImage originalImage = ImageIO.read(new File(INPUT_IMAGE));
    BufferedImage outputImage = new BufferedImage(originalImage.getWidth(),
        originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

    int limitHeightWorker = originalImage.getHeight() / NUM_WORKER;
    int endHeight = 0;
    int startHeight;
    List<ColorChangerWorker> workerList = new ArrayList<>();
    for (int i = 0; i < NUM_WORKER; i++) {
      startHeight = endHeight;
      endHeight = i == NUM_WORKER - 1 ? originalImage.getHeight() : startHeight + limitHeightWorker;
      ColorChangerWorker worker = new ColorChangerWorker(originalImage, outputImage, 0,
          startHeight, originalImage.getWidth(), endHeight);
      workerList.add(worker);
    }

    for (ColorChangerWorker colorChangerWorker : workerList) {
      colorChangerWorker.start();
    }

    for (ColorChangerWorker colorChangerWorker : workerList) {
      colorChangerWorker.join();
    }
    boolean result = ImageIO.write(outputImage, "JPG", new File(OUTPUT_IMAGE));
    System.out.println("result = " + result);
  }


}