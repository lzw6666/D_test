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
 * sd���Ĺ�����
 * @author ������
 *
 */
public class SdCardHelper {
	
	private static final String PATH = Environment.getExternalStorageDirectory()+"/1701/img/";
	
	/**
	 * �ж�sd����״̬�Ƿ����
	 * @return
	 */
	public static boolean isMounted()
	{
		//1, ��ȡ��ǰsd��״̬
		String state = Environment.getExternalStorageState();
		
		return state.equals(Environment.MEDIA_MOUNTED);
	}
	
	
	/**
	 * �ж�sd���Ŀ��ÿռ�
	 * @return
	 */
	public static boolean isAvailable()
	{
		//1, �ж�sd���Ƿ����
		if(isMounted())
		{
			//2, ʵ�����ļ������ϵͳ����
			StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
		
			long size = 0;
			
//			if(Build.VERSION.SDK_INT>18)
//			{
//				size = statFs.getFreeBytes();
//			}
//			else
			{
				//�������ݿ� * ÿ�����ݿ�Ĵ�С
				size = statFs.getFreeBlocks() * statFs.getBlockSize();
			}
			
			//���õĿռ��С����Ҫ����10MB, ���ʾsd�ռ��ǿ���ʹ��
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
	 * ��sd���Ĺ���Ŀ¼��, ����byte[] ���͵�����
	 * @param fileName  �ļ�����
	 * @param result	�ļ�����
	 * @return
	 */
	public static boolean saveBytePublicDir(String fileName,byte[] result)
	{
		try {
			
			//1, �ж�sd���Ƿ����
			if(isMounted() && isAvailable())
			{
				//2, �жϵ�ǰ�洢��·���Ƿ����
				File dir = new File(PATH);
				if(!dir.exists())
				{
					//��������
					dir.mkdirs();
				}
				
				//3, ��byte���͵�����, д�뵽�ļ�����ΪfileName���ļ���
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
	 * ��sd������Ŀ¼��, ����Bitmap���͵�����
	 * @param fileName
	 * @param bitmap
	 * @return
	 */
	public static boolean savaBitmapPublicDir(String fileName,Bitmap bitmap)
	{
		BufferedOutputStream bos  = null;
		try {
			//�ж�sd����״̬
			if(isMounted())
			{
				//�õ�·��
				File dir = new File(PATH);
				if(!dir.exists())
				{
					dir.mkdirs();
				}
				
				FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
				
				 bos = new BufferedOutputStream(fos);
				
				//��Bitmap���͵�ͼƬ, ����sd����(ͼƬ����Ҫ��ѹ������)
				if(fileName!=null && (fileName.contains(".png") || fileName.contains(".PNG")))
				{
					/**
					 * format		�洢ͼƬ�ĸ�ʽ
					 * quality		ͼƬ������
					 * stream 		�����
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
	 * ��sd���Ĺ���Ŀ¼��, ��ȡbyte[] ���͵�����
	 * @param fileName  �ļ�����
	 * @return
	 */
	public static byte[] getBytePublicDir(String fileName) {
		
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		try {
			//1, �ж�sd�Ƿ����
			if(isMounted())
			{
				//�õ�ͼƬ��·��
				//storage/sdcard/1701/img/fileName
				File file = new File(PATH, fileName);
				
				//�ж�: ·��(ͼƬ)�Ƿ����
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
	 * ��sd��˽��Ŀ¼�д���byte���͵�����
	 * @param fileName	�ļ�����
	 * @param result	�ļ�����
	 * @return
	 */
	public static boolean saveBytePrivateDir(Context context,String fileName, byte[] result)
	{
		try {
			
			//1, �ж�sd�Ƿ����
			if(isMounted())
			{
				//2, �õ�·��
				//context.getExternalFilesDir(null);sd��˽��Ŀ¼�ĸ�Ŀ¼
				//context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)sd��˽��Ŀ¼������Ŀ¼
				File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
				
				//3, ��������
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
	 * �����ļ�������, ��sd����˽��Ŀ¼�л�ȡbyte[] ���͵�����
	 * @param context 
	 * @param fileName
	 * @return
	 */
	public static byte[] getBytePrivateDir(Context context,String fileName)
	{
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		
		try {
			
			//1, �ж�sd���Ƿ����
			
			if(isMounted())
			{
				//�õ��ļ���·��
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
	 * ��sd���Ļ���Ŀ¼�д���byte[] ����
	 * @param context
	 * @param fileName
	 * @param result
	 * @return
	 */
	public static boolean saveBytePrivateCache(Context context,String fileName,byte[] result)
	{
		try {
			//1,�ж�sd����״̬
			if(isMounted())
			{
				//2,�õ�·��
				//context.getExternalCacheDir() sd���Ļ���·��
				File file = new File(context.getExternalCacheDir(), fileName);
				
				//3,д������
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
			//1,�ж�sd����״̬
			if(isMounted())
			{
				//2,�õ�·��
				//context.getExternalCacheDir() sd���Ļ���·��
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
	 * ��sd�Ļ���Ŀ¼�ж�ȡbyte���͵�����
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static byte[] getBytePrivateCache(Context context,String fileName)
	{
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		
		try {
			
			//1, �ж�sd���Ƿ����
			
			if(isMounted())
			{
				//�õ��ļ���·��
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
	 * �������Ŀ¼�����еĻ�����ļ�
	 */
	public  static void clearPublicDir()
	{
		if(isMounted())
		{
			//�õ�sd����·��
			File file = new File(PATH);
			
			if(file.exists())
			{
				File[] files = file.listFiles();//�г�ָ��Ŀ¼�е������ļ�
				
				for(File f:files)
				{
					f.delete();//ɾ���ļ�
				}
			}
		}
	}

	/**
	 * �����ļ���·��+�ļ������� == �ļ���ȫ·��
	 * @param fileName
	 * @return
	 */
	public static File getFile(String fileName)
	{
		File file = new File(PATH, fileName);
		
		return file;
	}
}
