package com.lzw.d_test.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lzw.d_test.R;
import com.lzw.d_test.db.DbService;
import com.lzw.d_test.fragment.adapter.NewsAdapter;
import com.lzw.d_test.fragment.bean.News;
import com.lzw.d_test.tools.HttpUtils;
import com.lzw.d_test.tools.PaseJson;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class HotFragment extends Fragment {
	
	private  ListView lv;
	
	private  List<News> total = new  ArrayList<News>();
	
	private  NewsAdapter adapter;
	
	private  String path ="http://c.m.163.com/nc/article/headline/T1348647853363/0-20.html";
	
	private  ProgressDialog pd;
	
	private  DbService dbService ;
	
	private  int  position;
	private  Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				pd = new  ProgressDialog(getActivity());
				pd.setIcon(R.drawable.ic_launcher);
				pd.setTitle("提示");
				pd.setMessage("正在加载,请稍后...");
				pd.show();
				break;
				
			case 2:
				
				total.addAll((List<News>)msg.obj);
				adapter.notifyDataSetChanged();
				
				break;
				
			case 3:
				pd.dismiss();
				break;

			default:
				break;
			}
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_item, container, false);
		
		lv = (ListView) view.findViewById(R.id.lv_id);
		
		adapter = new NewsAdapter(getActivity(),total);
		
		lv.setAdapter(adapter);
		new MyThread().start();
		
		dbService = new DbService(getActivity());
		
		registerForContextMenu(lv);
		
		return view;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.main, menu);
		
		position=((AdapterContextMenuInfo)menuInfo).position;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_collect:
			News news = total.get(position);
			boolean flag = dbService.insert(news.getTitle(), news.getDigest(), news.getImgsrc());
			if (flag) {
				Toast.makeText(getActivity(), "收藏ok", Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(getActivity(), "收藏夹中已存在!!", Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
		
		return super.onContextItemSelected(item);
	}
	
	public class MyThread extends Thread{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(1);
			
			String jsonStr = HttpUtils.getStringResult(path);
			List<News> data = PaseJson.pase(jsonStr);
			Message message = Message.obtain();
			message.obj=data;
			message.what=2;
			handler.sendMessage(message);
			
			handler.sendEmptyMessage(3);
		}
	}
}
