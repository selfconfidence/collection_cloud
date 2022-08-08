package com.manyun.business.config.InviteUtil;

import sun.font.FontDesignMetrics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @ClassName PictorialUtils
 * @Description TODO
 * @Author fengfanli
 * @Date 2021/3/29 16:48
 * @Version 1.0
 */
public class PosterUtil {


    /**
     * @return java.awt.image.BufferedImage
     * @Author fengfanli
     * @Description //TODO 初始化背景模板
     * @Date 9:55 2021/3/30
     * @Param [path]
     **/
    public static BufferedImage drawInit(String path, InputStream inputStream) throws Exception {
        BufferedImage canvas = null;
        if (path != null) {
            URL url = new URL(path);
            canvas = ImageIO.read(url);
        }
        if (inputStream != null) {
            canvas = ImageIO.read(inputStream);
        }
        return canvas;
    }

    /**
     * @return java.awt.image.BufferedImage
     * @Author fengfanli
     * @Description //TODO 初始化模板并改变宽度和高度
     * @Date 9:56 2021/3/30
     * @Param [path, width, height]
     **/
    public static BufferedImage drawInitAndChangeSize(String path, InputStream inputStream, int width, int height) throws Exception {
        BufferedImage canvas = null;
        if (path != null) {
            URL url = new URL(path);
            canvas = ImageIO.read(url);
        }
        if (inputStream != null) {
            canvas = ImageIO.read(inputStream);
        }
        canvas = changeSize(canvas, width, height);
        return canvas;
    }


    /**
     * @return java.awt.image.BufferedImage
     * @Author fengfanli
     * @Description // TODO 改变宽度和高度：先画一个空白的图，再将需要改变大小的图 画上去，再限制大小
     * @Date 9:56 2021/3/30
     * @Param [originalImage, width, height]
     **/
    public static BufferedImage changeSize(BufferedImage originalImage, int width, int height) {
        // 抗锯齿
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  // originalImage.getType() 改为 BufferedImage.TYPE_INT_RGB
        Graphics2D g2d = image.createGraphics();
        image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.OPAQUE);
        g2d = image.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return image;
    }


    /**
     * @return java.awt.image.BufferedImage
     * @Author fengfanli
     * @Description //TODO 在背景模板上放置 image
     * @Date 9:57 2021/3/30
     * @Param [canvas, logo, logoX, logoY]
     **/
    public static void drawImage(BufferedImage canvas, BufferedImage logo, int logoX, int logoY, int x, int y) {
        Graphics2D g2d = canvas.createGraphics();
        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(logo, x, y, logoX, logoY, null);
        g2d.dispose();
    }


    /**
     * @return java.awt.image.BufferedImage
     * @Author fengfanli
     * @Description //TODO 画图 改变背景颜色，并改变大小（原图片先生成，合成时在缩小）
     * TODO  重点1 ：图片合成时会出现失真的情况。
     * TODO  合成方案有两种：a、原图片先缩小，在合成（会失真）
     * TODO                b、原图片先生成，合成时在缩小（会好点）
     * @Date 9:57 2021/3/30
     * @Param [canvas, logo, logoX, logoY]
     **/
    public static void drawImageAndChangeBackgroundColorAndChangeSize(BufferedImage canvas, BufferedImage logo, Integer logoX, Integer logoY, Integer width, Integer height) throws IOException {
        Graphics2D g2d = canvas.createGraphics();

        // 解决png透明图片会变黑的问题(画一个新图片，然后合成，记得透明度)
        Graphics2D graphics = logo.createGraphics();
        BufferedImage compatibleImage = null;
        compatibleImage = graphics.getDeviceConfiguration().createCompatibleImage(logo.getWidth(null), logo.getHeight(null), BufferedImage.TYPE_INT_RGB);
        // 下面这行也可以代替上面这行
        //BufferedImage compatibleImage = new BufferedImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
        graphics = compatibleImage.createGraphics();
        graphics.drawImage(logo, 0, 0, logo.getWidth(null), logo.getHeight(null), null);
        graphics.dispose();

        // 合成图片
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(compatibleImage, logoX, logoY, width, height, null);
        g2d.dispose();
    }

    /**
     * @return void
     * @Author fengfanli
     * @Description //TODO 画图 改变背景颜色
     * TODO  重点1 ：png图片合成到另一个图片时，透明地带出现黑色情况, 原因：Graphics2D 创建图片背景为黑色
     * @Date 16:31 2021/4/8
     * @Param [canvas, logo, logoX, logoY]
     **/
    public static void drawImageAndChangeBackgroundColor(BufferedImage canvas, BufferedImage logo, Integer logoX, Integer logoY) throws IOException {
        Graphics2D g2d = canvas.createGraphics();

        // 解决png透明图片会变黑的问题(画一个新图片，然后合成，记得透明度)
        Graphics2D graphics = logo.createGraphics();
        BufferedImage compatibleImage = graphics.getDeviceConfiguration().createCompatibleImage(logo.getWidth(null), logo.getHeight(null), Transparency.TRANSLUCENT);
        // 下面这行也可以代替上面这行
        //BufferedImage compatibleImage = new BufferedImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
        graphics = compatibleImage.createGraphics();
        graphics.drawImage(logo, 0, 0, null);
        graphics.dispose();

        // 合成图片
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(compatibleImage, logoX, logoY, null);
        g2d.dispose();
    }


    /**
     * @return java.awt.Graphics2D
     * @Author fengfanli
     * @Description //TODO 在背景模板上写字，注意 需要换行算法
     * @Date 9:58 2021/3/30
     * @Param [bufferedImage, words, wordsFont, fontSize, wordsX, wordsY, wordsWidth, wordsHeight]
     **/
    public static void drawWords(BufferedImage bufferedImage, String words, Boolean isAddFontSpace, String wordsFont, int fontSize, int wordsX,
                                 int wordsY, int wordsWidth, int wordsHeight) {
        Graphics2D g2d = bufferedImage.createGraphics();
        // 抗锯齿 添加文字
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB); // VALUE_TEXT_ANTIALIAS_ON 改为 VALUE_TEXT_ANTIALIAS_LCD_HRGB
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.getDeviceConfiguration().createCompatibleImage(wordsWidth, wordsHeight, Transparency.TRANSLUCENT);
        Font font = new Font(wordsFont, Font.BOLD, fontSize);
        g2d.setFont(font);
        Color color = new Color(51, 51, 51);
        g2d.setColor(color);
        // 换行算法
        drawWordAndLineFeed(g2d, font, words, wordsX, wordsY, wordsWidth);
        g2d.dispose();
    }

    /**
     * @return void
     * @Author fengfanli
     * @Description //TODO 写字换行算法
     * @Date 18:08 2021/4/1
     * @Param []
     **/
    private static void drawWordAndLineFeed(Graphics2D g2d, Font font, String words, int wordsX, int wordsY, int wordsWidth) {
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        // 获取字符的最高的高度
        int height = metrics.getHeight();

        int width = 0;
        int count = 0;
        int total = words.length();
        String subWords = words;
        int b = 0;
        for (int i = 0; i < total; i++) {
            // 统计字符串宽度 并与 预设好的宽度 作比较
            if (width <= wordsWidth) {
                width += metrics.charWidth(words.charAt(i)); // 获取每个字符的宽度
                count++;
            } else {
                // 画 除了最后一行的前几行
                String substring = subWords.substring(0, count);
                g2d.drawString(substring, wordsX, wordsY + (b * height));
                subWords = subWords.substring(count);
                b++;
                width = 0;
                count = 0;
            }
            // 画 最后一行字符串
            if (i == total - 1) {
                g2d.drawString(subWords, wordsX, wordsY + (b * height));
            }
        }
    }


    /**
     * @return java.awt.image.BufferedImage
     * @Author fengfanli
     * @Description //TODO 将正方形的头像 改为 圆形头像
     * @Date 14:37 2021/3/31
     * @Param [imgUrl, width]
     **/
    public static BufferedImage drawImgToCircle(String imgUrl, int width) throws IOException {
        BufferedImage avatarImage = ImageIO.read(new URL(imgUrl));
        //int width = 120;
        // 透明底的图片
        BufferedImage formatAvatarImage = new BufferedImage(width, width, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = formatAvatarImage.createGraphics();
        //把图片切成一个圓
        {
            // 抗锯齿
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //留一个像素的空白区域，这个很重要，画圆的时候把这个覆盖
            int border = 1;
            //图片是一个圆型
            Ellipse2D.Double shape = new Ellipse2D.Double(border, border, width - border * 2, width - border * 2);
            //需要保留的区域
            graphics.setClip(shape);
            graphics.drawImage(avatarImage, border, border, width - border * 2, width - border * 2, null);
            graphics.dispose();
        }
        return formatAvatarImage;
    }


    /**
     * @return java.lang.String
     * @Author fengfanli
     * @Description //TODO 存放到本地
     * @Date 9:58 2021/3/30
     * @Param [canvas, type, urlPath]
     **/
    public static void save(BufferedImage canvas, String type, String urlPath) throws Exception {
        ImageIO.write(canvas, type, new File(urlPath));
    }
}
