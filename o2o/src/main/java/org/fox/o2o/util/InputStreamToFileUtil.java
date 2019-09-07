package org.fox.o2o.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamToFileUtil {
	public static void inputStreamToFile(InputStream ins,File file) {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while((bytesRead = ins.read(buffer))!=-1) {
				os.write(buffer,0,bytesRead); 
			}
		}catch(Exception e) {
			throw new RuntimeException("调用inputStreamToFile产生异常"+e.getMessage());
		}finally {
			try {
				if(os!=null)
					os.close();
				if(ins!=null)
					ins.close();
			}catch(IOException e) {
				throw new RuntimeException("inputStreamToFile关闭io产生异常"+e.getMessage());
			}
		}
	}
}
//方法addShop的第二个参数是File，此时传入的参数是CommonsMultipartFile
//由于这个时候强转会出错，只能用字符流转换绕过：定义方法
//File shopImgFile = new File(PathUtil.getImgBasePath()+ImageUtil.getRandomFileName());
//try {
//	shopImgFile.createNewFile();
//}catch(Exception e) {
//	modelMap.put("success",false);
//	modelMap.put("errorMessage",e.getMessage());
//	return modelMap;
//}
//
//try {
//	InputStreamToFileUtil.inputStreamToFile(shopImg.getInputStream(),shopImgFile);
//}catch(Exception e) {
//	modelMap.put("success",false);
//	modelMap.put("errorMessage",e.getMessage());
//	return modelMap;
//}
