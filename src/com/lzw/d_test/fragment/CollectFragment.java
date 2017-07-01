package com.lzw.d_test.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lzw.d_test.R;
import com.lzw.d_test.db.DbService;
import com.lzw.d_test.fragment.adapter.NewsAdapter;
import com.lzw.d_test.fragment.bean.News;

public class CollectFragment extends Fragment implements OnItemClickListener {
	
	private ListView lv;
	
	private Cursor cursor ;
	
	private List<News> total = new ArrayList<News>();
	
	private NewsAdapter adapter ;
	
	private DbService dbService;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_item, container, false);
		
		lv = (ListView) view.findViewById(R.id.lv_id);
		
		adapter = new NewsAdapter(getActivity(), total);
		
		lv.setAdapter(adapter);
		
		dbService = new DbService(getActivity());
		
		dataSelect();
		
		lv.setOnItemClickListener(this);
		
		return view;
	}

	private void dataSelect() {
		cursor = dbService.select();
		total.clear();
		while (cursor.moveToNext()) {
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String digest = cursor.getString(cursor.getColumnIndex("digest"));
			String imgsrc = cursor.getString(cursor.getColumnIndex("imgsrc"));
			
			total.add(new News(title, digest, imgsrc));
			
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("提示");
		builder.setMessage("是否确认删除?");
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (cursor.moveToPosition(arg2)) {
					int id = cursor.getInt(cursor.getColumnIndex("_id"));
					dbService.delete(id);
					dataSelect();
				}
			}
		});
		builder.show();
		
	}
	
	
}
