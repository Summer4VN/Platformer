import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CheckImg {
    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageIO.read(new File("res/button_atlas.png"));
        int minX = img.getWidth(), minY = img.getHeight(), maxX = 0, maxY = 0;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int alpha = (img.getRGB(x, y) >> 24) & 0xff;
                if (alpha > 0) {
                    if (x < minX)
                        minX = x;
                    if (x > maxX)
                        maxX = x;
                    if (y < minY)
                        minY = y;
                    if (y > maxY)
                        maxY = y;
                }
            }
        }
        System.out.println("Size: " + img.getWidth() + "x" + img.getHeight());
        System.out.println("BBox: minX=" + minX + ", maxX=" + maxX + ", minY=" + minY + ", maxY=" + maxY);
        int centerBBox = minX + (maxX - minX) / 2;
        System.out.println("Center of BBox: " + centerBBox);
        System.out.println("Center of Image: " + (img.getWidth() / 2));
        System.out.println("Offset needed to center visually: " + (img.getWidth() / 2 - centerBBox));
    }
}
