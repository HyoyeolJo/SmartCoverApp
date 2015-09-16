package com.secmem.android.app.view;

/**
 * 
 * 서비스로 구동 중일때 View 화면을 캡쳐하여 파일로 저장하기 위한 유틸리티 코드
 * 
 */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;

public class CaptureView {
	public static void captureView(Bitmap v, String tag)
	{
		
		
		if(v == null) 
		{
			System.out.println("bmp is null");
		}
		FileOutputStream fos = null;
		try {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + tag + "_capture.png";
			System.out.println("capture path : " + path);
			fos = new FileOutputStream(path);
			
			v.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
