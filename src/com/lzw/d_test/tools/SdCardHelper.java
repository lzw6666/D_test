package com.lzw.d_test.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.StatFs;

/**
 * sd卡的工具类
 * @author 樊成秀
 *
 */
public class SdCardHelper {
	
	private static final String PATH = Environment.getExternalStorageDirectory()+"/1701/img/";
	
	/**
	 * 判断sd卡的状态是否可用
	 * @return
	 */
	public static boolean isMounted()
	{
		//1, 获取当前sd的状态
		String state = Environment.getExternalStorageState();
		
		return state.equals(Environment.MEDIA_MOUNTED);
	}
	
	
	/**
	 * 判断sd卡的可用空间
	 * @return
	 */
	public static boolean isAvailable()
	{
		//1, 判断sd卡是否可用
		if(isMounted())
		{
			//2, 实例话文件管理的系统对象
			StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
		
			long size = 0;
			
//			if(Build.VERSION.SDK_INT>18)
//			{
//				size = statFs.getFreeBytes();
//			}
//			else
			{
				//可用数据块 * 每个数据块的大小
				size = statFs.getFreeBlocks() * statFs.getBlockSize();
			}
			
			//可用的空间大小必须要大于10MB, 则表示sd空间是可以使用
			//1KB = 1024B
			//1MB =  1024KB;
			if(size>10*1024*1024)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 向sd卡的公共目录中, 存入byte[] 类型的数据
	 * @param fileName  文件名称
	 * @param result	文件内容
	 * @return
	 */
	public static boolean saveBytePublicDir(String fileName,byte[] result)
	{
		try {
			
			//1, 判断sd卡是否可用
			if(isMounted() && isAvailable())
			{
				//2, 判断当前存储的路径是否存在
				File dir = new File(PATH);
				if(!dir.exists())
				{
					//级联创建
					dir.mkdirs();
				}
				
				//3, 将byte类型的数据, 写入到文件名称为fileName的文件中
				FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
				
				fos.write(result);
				
				fos.close();
				
				return true;
				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		
		
		return false;
	}

	/**
	 * 向sd卡公共目录中, 存入Bitmap类型的数据
	 * @param fileName
	 * @param bitmap
	 * @return
	 */
	public static boolean savaBitmapPublicDir(String fileName,Bitmap bitmap)
	{
		BufferedOutputStream bos  = null;
		try {
			//判断sd卡的状态
			if(isMounted())
			{
				//得到路径
				File dir = new File(PATH);
				if(!dir.exists())
				{
					dir.mkdirs();
				}
				
				FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
				
				 bos = new BufferedOutputStream(fos);
				
				//将Bitmap类型的图片, 存入sd卡中(图片必须要做压缩处理)
				if(fileName!=null && (fileName.contains(".png") || fileName.contains(".PNG")))
				{
					/**
					 * format		存储图片的格式
					 * quality		图片的质量
					 * stream 		输出流
					 */
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
				}
				
				else
				{
					
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				}
				
				fos.close();
				return true;
			}
				
				
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally
		{
			if(bos!=null)
			{
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 向sd卡的公共目录中, 获取byte[] 类型的数据
	 * @param fileName  文件名称
	 * @return
	 */
	public static byte[] getBytePublicDir(String fileName) {
		
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		try {
			//1, 判断sd是否可用
			if(isMounted())
			{
				//得到图片的路径
				//storage/sdcard/1701/img/fileName
				File file = new File(PATH, fileName);
				
				//判断: 路径(图片)是否存在
				if(file.exists())
				{
					FileInputStream fis = new FileInputStream(file);
					
					bis = new BufferedInputStream(fis);
					
					baos = new ByteArrayOutputStream();
					
					byte[] buffer = new byte[1024];
					int len = 0;
					
					while ((len = bis.read(buffer))!=-1) {
						
						baos.write(buffer, 0, len);
						baos.flush();
						
					}
					
					fis.close();
					
					return baos.toByteArray();
					
				}
			}
				
				
		} catch (Exception e) {
			// TODO: handle exception
		}finally
		{
			if (bis!=null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(baos!=null)
			{
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		return null;
	}
	
	/**
	 * 向sd卡私有目录中存入byte类型的数据
	 * @param fileName	文件名称
	 * @param result	文件内容
	 * @return
	 */
	public static boolean saveBytePrivateDir(Context context,String fileName, byte[] result)
	{
		try {
			
			//1, 判断sd是否存在
			if(isMounted())
			{
				//2, 得到路径
				//context.getExternalFilesDir(null);sd卡私有目录的根目录
				//context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)sd卡私有目录的下载目录
				File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
				
				//3, 存入数据
				FileOutputStream fos = new FileOutputStream(file);
				
				fos.write(result);
				
				fos.close();
				
				return true;
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return false;
	}
	
	
	/**
	 * 根据文件的名称, 从sd卡的私有目录中获取byte[] 类型的数据
	 * @param context 
	 * @param fileName
	 * @return
	 */
	public static byte[] getBytePrivateDir(Context context,String fileName)
	{
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		
		try {
			
			//1, 判断sd卡是否可用
			
			if(isMounted())
			{
				//得到文件的路径
				File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
				
				FileInputStream fis = new FileInputStream(file);
				
				bis = new BufferedInputStream(fis);
				
				baos = new ByteArrayOutputStream();
				
				byte[] buffer = new byte[1024];
				int len = 0;
				
				while ((len = bis.read(buffer))!=-1) {
					
					baos.write(buffer, 0, len);
					baos.flush();
				}
				
				fis.close();
				
				return baos.toByteArray();
				
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally
		{
			if (bis!=null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(baos!=null)
			{
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	
	/**
	 * 向sd卡的缓存目录中存入byte[] 数据
	 * @param context
	 * @param fileName
	 * @param result
	 * @return
	 */
	public static boolean saveBytePrivateCache(Context context,String fileName,byte[] result)
	{
		try {
			//1,判断sd卡的状态
			if(isMounted())
			{
				//2,得到路径
				//context.getExternalCacheDir() sd卡的缓存路径
				File file = new File(context.getExternalCacheDir(), fileName);
				
				//3,写入数据
				FileOutputStream fos = new FileOutputStream(file);
				
				fos.write(result);
				
				fos.close();
				
				return true;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	
	public static boolean saveBitmapPrivateCache(Context context,String fileName,Bitmap bitmap)
	{
		BufferedOutputStream bos = null;
		try {
			//1,判断sd卡的状态
			if(isMounted())
			{
				//2,得到路径
				//context.getExternalCacheDir() sd卡的缓存路径
				File file = new File(context.getExternalCacheDir(), fileName);
					
				bos = new BufferedOutputStream(new FileOutputStream(file));
				
				if(fileName!=null && (fileName.contains(".png") || fileName.contains(".PNG")))
				{
					bitmap.compress(CompressFormat.PNG, 100, bos);
				}else
				{
					bitmap.compress(CompressFormat.JPEG, 100, bos);
				}
			
				
				return true;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally
		{
			if(bos!=null)
			{
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * 向sd的缓存目录中读取byte类型的数据
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static byte[] getBytePrivateCache(Context context,String fileName)
	{
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		
		try {
			
			//1, 判断sd卡是否可用
			
			if(isMounted())
			{
				//得到文件的路径
				File file = new File(context.getExternalCacheDir(), fileName);
				
				FileInputStream fis = new FileInputStream(file);
				
				bis = new BufferedInputStream(fis);
				
				baos = new ByteArrayOutputStream();
				
				byte[] buffer = new byte[1024];
				int len = 0;
				
				while ((len = bis.read(buffer))!=-1) {
					
					baos.write(buffer, 0, len);
					baos.flush();
				}
				
				fis.close();
				
				return baos.toByteArray();
				
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally
		{
			if (bis!=null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(baos!=null)
			{
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 清除公共目录中所有的缓存的文件
	 */
	public  static void clearPublicDir()
	{
		if(isMounted())
		{
			//得到sd卡的路径
			File file = new File(PATH);
			
			if(file.exists())
			{
				File[] files = file.listFiles();//列出指定目录中的所有文件
				
				for(File f:files)
				{
					f.delete();//删除文件
				}
			}
		}
	}

	/**
	 * 返回文件的路径+文件的名称 == 文件的全路径
	 * @param fileName
	 * @return
	 */
	public static File getFile(String fileName)
	{
		File file = new File(PATH, fileName);
		
		return file;
	}
}
