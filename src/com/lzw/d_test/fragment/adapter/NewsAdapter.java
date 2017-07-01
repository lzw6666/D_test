package com.lzw.d_test.fragment.adapter;

import java.util.List;

import com.lzw.d_test.R;
import com.lzw.d_test.fragment.bean.News;
import com.lzw.d_test.tools.HttpUtils;
import com.lzw.d_test.tools.SdCardHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {

	private  Context context;
	
	private  List<News> data;
	
	private  Handler handler = new Handler();
	
	public NewsAdapter(Context context, List<News> data) {
		super();
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size()>0?data.size():0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (convertView==null) {
			convertView =LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		News news = data.get(position);
		viewHolder.title_tv.setText(news.getTitle());
		viewHolder.digest_tv.setText(news.getDigest());
		viewHolder.iv.setImageResource(R.drawable.ic_launcher);
		final String imgsrc = news.getImgsrc();
		viewHolder.iv.setTag(imgsrc);
		final String fileName = imgsrc.substring(imgsrc.lastIndexOf("/")+1);
		byte[] bs = SdCardHelper.getBytePrivateCache(context, fileName);
		if (bs!=null&&bs.length>0) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(bs, 0, bs.length);
			viewHolder.iv.setImageBitmap(bitmap);
		}else {
			new Thread(){
				public void run() {
					
					final Bitmap bitmap = HttpUtils.getBitmapResult(imgsrc);
					
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							if (bitmap!=null) {
								SdCardHelper.saveBitmapPrivateCache(context, fileName, bitmap);
								if (viewHolder.iv.getTag().equals(imgsrc)) {
									
									viewHolder.iv.setImageBitmap(bitmap);
								}
							}
						}
					});
				};
			}.start();
		}
		return convertView;
	}
	
	class ViewHolder{
		
		private ImageView iv;
		private TextView title_tv,digest_tv;
		
		public ViewHolder(View view){
			iv = (ImageView) view .findViewById(R.id.iv);
			title_tv = (TextView) view.findViewById(R.id.title_id);
			digest_tv = (TextView) view.findViewById(R.id.digest_id);
		}
	}
}
