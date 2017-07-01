package com.lzw.d_test.tools;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 开启网络, 下载数据
 * @author Administrator
 *
 */
public class HttpUtils {

	/**
	 * 网络加载String类型的数据
	 * @param string
	 * @return
	 */
	public static String getStringResult(String path) {
		
		HttpURLConnection conn = null;
		try {
			
			URL url = new URL(path);
			
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			
			conn.connect();
			
			if(conn.getResponseCode() == 200)
			{
				InputStream is = conn.getInputStream();
				
				StringBuilder sBuilder = new StringBuilder();
				
				byte[] buffer = new byte[1024];
				int len = 0;
				
				while ((len = is.read(buffer))!=-1) {
					
					sBuilder.append(new String(buffer, 0, len));
					
				}
				
				return sBuilder.toString();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (conn!=null) {
				
				conn.disconnect();//断开连接
				conn = null;
			}
		}
		
		return null;
	}

	/**
	 * 获取网络图片
	 * @param path
	 * @return
	 */
	public static Bitmap getBitmapResult(String path) {
		
		HttpURLConnection conn = null;
		BufferedInputStream bis = null;
		try {
			
			URL url = new URL(path);
			
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			
			conn.connect();
			
			if(conn.getResponseCode() == 200)
			{
				InputStream is = conn.getInputStream();
				
				bis = new BufferedInputStream(is);
				
				return BitmapFactory.decodeStream(bis);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally
		{
			if(conn!=null)
			{
				conn.disconnect();
				conn = null;
			}
			if(bis!=null)
			{
				try {
					bis.close();
					bis = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return null;
	}

	/**
	 * 获取网络数据
	 * @param imagePath
	 * @return
	 */
	public static byte[] getByteResult(String path)
	{
		HttpURLConnection conn = null;
		ByteArrayOutputStream baos = null;
		
		try {
			
			URL url = new URL(path);
			
			conn = (HttpURLConnection)url.openConnection();
			
			conn.setRequestMethod("GET");
			
			conn.connect();
			
			if(conn.getResponseCode() == 200)
			{
				InputStream is = conn.getInputStream();
				
				baos = new ByteArrayOutputStream();
				
				byte[] buffer = new byte[1024];
				int len = 0;
				
				while ((len = is.read(buffer))!=-1) {
				
					baos.write(buffer, 0, len);
					baos.flush();
				}
				
				return baos.toByteArray();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally
		{
			if(conn !=null)
			{
				conn.disconnect();
				conn = null;
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
}
