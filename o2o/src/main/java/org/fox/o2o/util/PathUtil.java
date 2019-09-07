package org.fox.o2o.util;

public class PathUtil {
	//获取当前系统路径符("/"或"\")
//	private static String seperator = System.getProperty("file.separtor");
	
	/**
	 * 返回项目图片的根路径
	 * @return
	 */
	public static String getImgBasePath() {
		//获取当前运行环境的系统类型(mac/windows/lunix)
		String os = System.getProperty("os.name");
		String basePath = "";
		//针对不同类型的系统设置不同的存储路径
		if(os.toLowerCase().startsWith("win")) {
			basePath="D:/Users/ProjectDev/image";
		}else {
			basePath="/home/fox/image/";
		}
		//替换路径符号
//		basePath = basePath.replace("/",seperator);
		return basePath;
	}
	
	/**
	 * 依据不同需求，返回项目图片的子路径
	 * @param shopId
	 * @return
	 */
	public static String getShopImagePath(long shopId) {
		String imagePath = "/upload/items/shop/"+shopId+"/";
//		return imagePath.replace("/", seperator);
		return imagePath;
	}
	
}
