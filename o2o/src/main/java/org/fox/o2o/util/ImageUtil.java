package org.fox.o2o.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.fox.o2o.dto.ImageHolder;

import com.sun.media.jfxmedia.logging.Logger;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	//获取项目根路径
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
	//获取格式化时间对象
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	//获取随机数对象
	private static final Random r = new Random();
	
	/**
	 * 添加水印，生成缩略图
	 * @param thumbnail
	 * @param targetAddr(项目子路径)
	 * @return
	 */
	public static String generateThumbnail(ImageHolder thumbnail,String targetAddr) {
		//使用随机数生成 图片名
		String realFileName = getRandomFileName();
		//获取文件图片扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		//创建图片存储的绝对路径
		makeDirPath(targetAddr);
		//图片子路径
		String relativeAddr = targetAddr+realFileName+extension;
//		logger.debug("current relativeAddr is:"+relativeAddr);
		//图片绝对路径（全路径）
		File dest = new File(PathUtil.getImgBasePath()+relativeAddr);
//		System.out.println(basePath);
		basePath="D:/Users/fox/eclipse-workspace/o2o/src/main/resources";
		
		try {
			//获取图片输入流：设置图片大小；设置水印位置，获取水印图片在项目下的地址，水印透明度0.25f，压缩图片尺寸0.85f，将更新后图片输入到dest目录下
			Thumbnails.of(thumbnail.getImage()).size(337,640)
			.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+ "/foxlogo.png")), 0.6f)
			.determineOutputFormat().outputQuality(0.9f).toFile(dest);
		}catch(IOException e){
			e.printStackTrace();
		}
		return relativeAddr;
	}
	
	
	public static String generateNormalImg(ImageHolder thumbnail,String targetAddr) {
		//使用随机数生成 图片名
		String realFileName = getRandomFileName();
		//获取文件图片扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		//创建图片存储的绝对路径
		makeDirPath(targetAddr);
		//图片子路径
		String relativeAddr = targetAddr+realFileName+extension;
		//图片绝对路径（全路径）
		File dest = new File(PathUtil.getImgBasePath()+relativeAddr);
//		System.out.println(basePath);
		basePath="D:/Users/fox/eclipse-workspace/o2o/src/main/resources";
		
		try {
			//获取图片输入流：设置图片大小；设置水印位置，获取水印图片在项目下的地址，水印透明度0.25f，压缩图片尺寸0.85f，将更新后图片输入到dest目录下
			Thumbnails.of(thumbnail.getImage()).size(200,200)
			.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+ "/foxlogo.png")), 0.6f)
			.determineOutputFormat().outputQuality(0.8f).toFile(dest);
		}catch(IOException e){
			e.printStackTrace();
		}
		return relativeAddr;
	}
	
	/**
	 * 创建目标路径所涉及的目录，即D:/Users/ProjectDev/image/xxx.jpg,那么Users、ProjectDev、image、将自动创建
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath()+targetAddr;
		File dirPath = new File(realFileParentPath);
		//该路径不存在将被创建
		if(!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	/**
	 * 获取文件输入流的扩展名
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fielName) {
		//getOriginalFilename()返回原始文件名
		return fielName.substring(fielName.lastIndexOf("."));
	}

	/**
	 * 生成随机文件名，当前的年/月/日/时/分/秒+五位随机数
	 * @return
	 */
	public static String getRandomFileName() {
		//获取五位随机数
		int rannum = r.nextInt(89999)+10000;
		//将时间格式化为字符串
		String nowTimeStr = sDateFormat.format(new Date());
		//自动转换为String返回
		return nowTimeStr+rannum;
	}
	
	/**
	 * 判断storePath是文件路径还是目录路径
	 * 1.如果是文件路径则删除该文件
	 * 2.如果是目录路径则删除该目录下的所有文件
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath) {
		//拼凑出图片的全路径
		File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
		if(fileOrPath.exists()) {
			//如果是目录
			if(fileOrPath.isDirectory()) {
				//将文件目录转换为数组，并遍历删除
				File files[] = fileOrPath.listFiles();
				for(int i=0;i<files.length;i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
		
	}
}














