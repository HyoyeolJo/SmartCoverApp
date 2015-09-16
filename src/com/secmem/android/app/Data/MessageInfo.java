package com.secmem.android.app.Data;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class MessageInfo {

	private String Message;
	private String phone;
	private String name;
	private String time;
	private String type;
	private Bitmap img;
	private Drawable imgDrawable;
	
	public Drawable getImgDrawable() {
		return imgDrawable;
	}
	public void setImgDrawable(Drawable imgDrawable) {
		this.imgDrawable = imgDrawable;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Bitmap getImg() {
		return img;
	}
	public void setImg(Bitmap img) {
		this.img = img;
	}

}
