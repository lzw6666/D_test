package com.lzw.d_test.tools;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lzw.d_test.fragment.bean.News;


public class PaseJson {

	public static List<News> pase(String jsonStr) {
		// TODO Auto-generated method stub
		List<News> data =  null;
		try {
			if (jsonStr!=null) {
				data =  new  ArrayList<News>();
			
			JSONObject object = new JSONObject(jsonStr);
			JSONArray array = object.getJSONArray("T1348647853363");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				String title = object2.getString("title");
				String digest = object2.getString("digest");
				String imgsrc = object2.getString("imgsrc");
				data.add(new News(title, digest, imgsrc));
			}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return data;
	}
	
	public static List<News> pase2(String jsonStr) {
		// TODO Auto-generated method stub
		List<News> data =  null;
		try {
			if (jsonStr!=null) {
				data =  new  ArrayList<News>();
				JSONObject object = new JSONObject(jsonStr);
				JSONArray array = object.getJSONArray("T1348648517839");
				for (int i = 0; i < array.length(); i++) {
					JSONObject object2 = array.getJSONObject(i);
					String title = object2.getString("title");
					String digest = object2.getString("digest");
					String imgsrc = object2.getString("imgsrc");
					data.add(new News(title, digest, imgsrc));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return data;
	}

}
