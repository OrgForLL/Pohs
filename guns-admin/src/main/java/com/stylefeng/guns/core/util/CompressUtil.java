package com.stylefeng.guns.core.util;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;  
import javax.imageio.ImageIO;  
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.swing.ImageIcon;  

public class CompressUtil {
	/**
	 * 采用指定宽度、高度或压缩比例 的方式对图片进行压缩
	 * @param imgsrc 源图片地址
	 * @param imgdist 目标图片地址 
	 * @param widthdist 压缩后图片宽度（当rate==null时，必传）
	 * @param heightdist 压缩后图片高度（当rate==null时，必传）
	 * @param rate 压缩比例 
	 */
	public static void reduceImg(String imgsrc, String imgdist, int widthdist,
			int heightdist, Float rate) {
		try {
//			 System.out.println("----------"+imgdist+"--------"+imgsrc);
			File srcfile = new File(imgsrc);
			// 检查文件是否存在
			if (!srcfile.exists()) {
				return;
			}
			// 如果rate不为空说明是按比例压缩
			if (rate != null && rate > 0) {
				// 获取文件高度和宽度
				int[] results = getImgWidth(srcfile);
				if (results == null || results[0] == 0 || results[1] == 0) {
					return;
				} else {
					widthdist = (int) (results[0] * rate);
					heightdist = (int) (results[1] * rate);
				}
			}
			// 开始读取文件并进行压缩
			Image src = javax.imageio.ImageIO.read(srcfile);
			BufferedImage tag = new BufferedImage((int) widthdist,
					(int) heightdist, BufferedImage.TYPE_INT_RGB);

			tag.getGraphics().drawImage(
					src.getScaledInstance(widthdist, heightdist,
							Image.SCALE_SMOOTH), 0, 0, null);

			FileOutputStream out = new FileOutputStream(imgdist);
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//			encoder.encode(tag);
			
			String filePrefix = imgsrc.substring(imgsrc.lastIndexOf(".") + 1); 

			ImageIO.write(tag, filePrefix, out);
			
			out.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 获取图片宽度
	 * 
	 * @param file
	 *            图片文件
	 * @return 宽度
	 */
	public static int[] getImgWidth(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int result[] = { 0, 0 };
		try {
			is = new FileInputStream(file);
			src = javax.imageio.ImageIO.read(is);
			result[0] = src.getWidth(null); // 得到源图宽
			result[1] = src.getHeight(null); // 得到源图高
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void reduceImg2(String imgsrc, String imgdist,Float rate) {
		try {
			File srcfile = new File(imgsrc);
			// 检查文件是否存在
			if (!srcfile.exists()) {
				return;
			}
			// 开始读取文件并进行压缩
			byte[] imgByte = File2byte(imgsrc);
			byte[] inByte = compressPicByQuality(imgByte, rate);
			BufferedOutputStream bos = null;
			FileOutputStream out = new FileOutputStream(imgdist);  //目標地址
			//String filePrefix = imgsrc.substring(imgsrc.lastIndexOf(".") + 1);   //
			
			bos = new BufferedOutputStream(out);
			bos.write(inByte);
			//ImageIO.write(inByte, filePrefix, out);
			out.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}      
	 /**
	 * @Title: compressPicByQuality 
	 * @Description: 压缩图片,通过压缩图片质量，保持原图大小
	 * @param  quality：0-1
	 * @return byte[] 
	 * @throws
	 */
	 public static byte[] compressPicByQuality(byte[] imgByte, float quality) {
		 byte[] inByte = null;
		 try {
//		 ByteArrayInputStream byteInput = new ByteArrayInputStream(imgByte);
		// BufferedImage image = ImageIO.read(byteInput);
		 Image imageTookit = Toolkit.getDefaultToolkit().createImage(imgByte);
		 BufferedImage image = toBufferedImage(imageTookit);  
		 // 如果图片空，返回空
		 if (image == null) {
		 return null;
		 } 
		 // 得到指定Format图片的writer
		 Iterator<ImageWriter> iter = ImageIO
		 .getImageWritersByFormatName("jpeg");// 得到迭代器
		 ImageWriter writer = (ImageWriter) iter.next(); // 得到writer
	
		 // 得到指定writer的输出参数设置(ImageWriteParam )
		 ImageWriteParam iwp = writer.getDefaultWriteParam();
		 iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
		 iwp.setCompressionQuality(quality); // 设置压缩质量参数
	
		 iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
	
		 ColorModel colorModel = ColorModel.getRGBdefault();
		 // 指定压缩时使用的色彩模式
		 iwp.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel,
		 colorModel.createCompatibleSampleModel(16, 16)));
	
		 // 开始打包图片，写入byte[]
		 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // 取得内存输出流
		 IIOImage iIamge = new IIOImage(image, null, null);
	
		 // 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput
		 // 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput
		 writer.setOutput(ImageIO .createImageOutputStream(byteArrayOutputStream));
		 writer.write(null, iIamge, iwp);
		 inByte = byteArrayOutputStream.toByteArray();
		 } catch (IOException e) {
		 System.out.println("write errro");
		 e.printStackTrace();
		 }
		 return inByte;
	 }
	 
	 public static BufferedImage toBufferedImage(Image image) {  
	        if (image instanceof BufferedImage) {  
	            return (BufferedImage) image;  
	        }  
	        // This code ensures that all the pixels in the image are loaded  
	        image = new ImageIcon(image).getImage();  
	        BufferedImage bimage = null;  
	        GraphicsEnvironment ge = GraphicsEnvironment  
	                .getLocalGraphicsEnvironment();  
	        try {  
	            int transparency = Transparency.OPAQUE;  
	            GraphicsDevice gs = ge.getDefaultScreenDevice();  
	            GraphicsConfiguration gc = gs.getDefaultConfiguration();  
	            bimage = gc.createCompatibleImage(image.getWidth(null),  
	                    image.getHeight(null), transparency);  
	        } catch (HeadlessException e) {  
	            // The system does not have a screen  
	        }  
	        if (bimage == null) {  
	            // Create a buffered image using the default color model  
	            int type = BufferedImage.TYPE_INT_RGB;  
	            bimage = new BufferedImage(image.getWidth(null),  
	                    image.getHeight(null), type);  
	        }  
	        // Copy image to buffered image  
	        Graphics g = bimage.createGraphics();  
	        // Paint the image onto the buffered image  
	        g.drawImage(image, 0, 0, null);  
	        g.dispose();  
	        return bimage;  
	    }
	 
	 public static byte[] File2byte(String filePath)
	    {
	        byte[] buffer = null;
	        try
	        {
	            File file = new File(filePath);
	            FileInputStream fis = new FileInputStream(file);
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            byte[] b = new byte[1024];
	            int n;
	            while ((n = fis.read(b)) != -1)
	            {
	                bos.write(b, 0, n);
	            }
	            fis.close();
	            bos.close();
	            buffer = bos.toByteArray();
	        }
	        catch (FileNotFoundException e)
	        {
	            e.printStackTrace();
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	        return buffer;
	    }
}
