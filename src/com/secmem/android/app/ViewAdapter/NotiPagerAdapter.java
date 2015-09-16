package com.secmem.android.app.ViewAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.secmem.android.app.Data.coverappData;
import com.secmem.android.app.coverappb.CommonInfo;
import com.secmem.android.app.coverappb.R;

public class NotiPagerAdapter extends PagerAdapter {

	Context mContext;
	View containview;
	final RelativeLayout inLayout;
	final LayoutInflater inflater;
	final RelativeLayout relate;
	TextView messageCotent;
	TextView messageSender;
	TextView messageTime;
	ImageView notification_icon;

	public NotiPagerAdapter(Context context, View view) {
		super();
		mContext = context;
		containview = view;
		inLayout = (RelativeLayout) containview.findViewById(R.id.detail_noti_box); //vertical Viewpager 적용 View
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		relate = (RelativeLayout) inflater.inflate(R.layout.detailnoti_info, null);//notificaiton 에서 감지된 내용 View
		messageCotent = ((TextView) relate.findViewById(R.id.messageContent4));
		messageSender = ((TextView) relate.findViewById(R.id.messageSender2));
		messageTime = ((TextView) relate.findViewById(R.id.messageTime3));

		notification_icon = ((ImageView) relate
				.findViewById(R.id.notification_icon1));
		inLayout.addView(relate);
		inLayout.setVisibility(containview.GONE);
		// TODO Auto-generated constructor stub
	}

	public RelativeLayout returnmessagelistView() {
		return relate;
	}

	//notification 감지시, 감지된 Noti_icon viewPager 생성
	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		View v = new View(mContext);
 
		final int Position = position;
		inLayout.setVisibility(View.GONE);

		v = inflater.inflate(R.layout.noti_info,  null);

		ImageView img = ((ImageView) v.findViewById(R.id.notification_icon));
		scaleImage(img, 150);//Icon 크기 조정
		img.setImageDrawable(coverappData.viewNotificationList.get(position)
				.getImgDrawable());

		img.setOnClickListener(new OnClickListener() {//이미지를 클릭했을 경우, 해당 Notificaiton의 발신자, 발신시간, 발신내용이 팝업창으로 뜸

			@Override
			public void onClick(View v) {
				Log.d(CommonInfo.TAG, "clickgood");
				messageCotent.setText(coverappData.viewNotificationList.get(
						Position).getMessage());
				messageSender.setText(coverappData.viewNotificationList.get(
						Position).getName());
				messageTime.setText(coverappData.viewNotificationList.get(
						Position).getTime());
				scaleImage(notification_icon, 150);
				notification_icon
						.setImageDrawable(coverappData.viewNotificationList
								.get(Position).getImgDrawable());
				inLayout.setVisibility(View.VISIBLE);
				relate.setVisibility(View.VISIBLE);
				relate.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						relate.setVisibility(View.GONE);//팝업창을 클릭시 사라짐.
						inLayout.invalidate();//View 화면 갱신

					}
				});
				inLayout.invalidate();//View 화면 갱신
			}
		});
		Log.d(CommonInfo.TAG, "instantiateItem : " + position);


		container.addView(v);//Vertical View Pager에 추가

		return v;
	}

	//Image크기 조정 메소드
	private void scaleImage(ImageView view, int boundBoxInDp) {
		// Get the ImageView and its bitmap
		Drawable drawing = view.getDrawable();
		Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

		// Get current dimensions
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		// Determine how much to scale: the dimension requiring less scaling is
		// closer to the its side. This way the image always stays inside your
		// bounding box AND either x/y axis touches it.
		float xScale = ((float) boundBoxInDp) / width;
		float yScale = ((float) boundBoxInDp) / height;
		float scale = (xScale <= yScale) ? xScale : yScale;

		// Create a matrix for the scaling and add the scaling data
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		// Create a new bitmap and convert it to a format understood by the
		// ImageView
		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		BitmapDrawable result = new BitmapDrawable(scaledBitmap);
		width = scaledBitmap.getWidth();
		height = scaledBitmap.getHeight();

		// Apply the scaled bitmap
		view.setImageDrawable(result);

		// Now change ImageView's dimensions to match the scaled image
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		params.width = width;
		params.height = height;
		view.setLayoutParams(params);
	}

	private int dpToPx(int dp) {
		float density = mContext.getResources().getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
		Log.d(CommonInfo.TAG, "destroyItem : " + position);
	}

	@Override
	public int getCount() {
		return coverappData.viewNotificationList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (view == object);
	}
}