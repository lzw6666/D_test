package com.lzw.d_test.fragment.bean;

public class News {
	private  String  title;
	private  String  digest;
	private  String  imgsrc;
	
	
	public News(String title, String digest, String imgsrc) {
		super();
		this.title = title;
		this.digest = digest;
		this.imgsrc = imgsrc;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getImgsrc() {
		return imgsrc;
	}
	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}
	
	
}
