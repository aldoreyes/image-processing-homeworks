package com.aldoreyes.imageprocessing.hw1;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.aldoreyes.imageprocessing.Border;
import com.aldoreyes.imageprocessing.util.ArrayUtils;
import com.aldoreyes.imageprocessing.util.BitmapUtils;
import com.example.imageprocessing.R;
import com.example.imageprocessing.R.id;
import com.example.imageprocessing.R.layout;

public class Homework1Fragment extends SherlockFragment implements OnClickListener, OnTouchListener {

	private int[][] source = new int[][] { 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 }, 
			{ 0, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
			{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 },
			{ 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 },
			{ 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 },
			{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 }, 
			{ 0, 0, 0, 0, 1, 1, 1, 1, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.homework1, container, false);
		getSherlockActivity().getSupportActionBar().setTitle("Exercise 1");
		return toReturn;
	}

	@Override
	public void onStart() {
		int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				300, getResources().getDisplayMetrics());
		((ImageView) getView().findViewById(R.id.image))
				.setImageBitmap(BitmapUtils.createBinaryImage(source, w, w));
		
		reset();
		super.onStart();
	}
	
	@Override
	public void onResume() {
		((Button)getView().findViewById(R.id.btn_count)).setOnClickListener(this);
		((Button)getView().findViewById(R.id.btn_border)).setOnClickListener(this);
		((ImageView)getView().findViewById(R.id.image)).setOnTouchListener(this);
		super.onResume();
	}
	
	@Override
	public void onPause() {
		((Button)getView().findViewById(R.id.btn_count)).setOnClickListener(null);
		((Button)getView().findViewById(R.id.btn_border)).setOnClickListener(null);
		((ImageView)getView().findViewById(R.id.image)).setOnTouchListener(null);
		super.onPause();
	}

	private void reset() {
		int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				300, getResources().getDisplayMetrics());
		
		((ImageView) getView().findViewById(R.id.image_over)).setImageBitmap(Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888));
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_count:
			int vlen = source.length;
			int hlen = source[0].length;
			int counter = 0;
			for(int y = 0; y<vlen; y++){
				for(int x = 0; x<hlen; x++){
					if(source[y][x] == 1){
						counter++;
					}
				}
			}
			((TextView)getView().findViewById(R.id.tv_count)).setText(counter+"");
			break;
		case R.id.btn_border:
			ImageView iv =(ImageView)getView().findViewById(R.id.image_over);
			Bitmap bm = ((BitmapDrawable)iv.getDrawable()).getBitmap();
			BitmapUtils.drawBorder(bm, Border.getBorder(source), iv.getWidth()/source[0].length, iv.getHeight()/source.length);
			iv.invalidate();
			break;
		}
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int x = (int) Math.floor((event.getX()/v.getWidth())* source[0].length);
		int y = (int) Math.floor((event.getY()/v.getHeight())* source.length);
		
		((TextView)getView().findViewById(R.id.tv_neighbors)).setText(ArrayUtils.neighbors(source, x, y)+"");
		ImageView iv =(ImageView)getView().findViewById(R.id.image_over);
		Bitmap bm = ((BitmapDrawable)iv.getDrawable()).getBitmap();
		BitmapUtils.drawSelect(bm, x, y, v.getWidth()/source[0].length, v.getHeight()/source.length);
		iv.invalidate();
		return false;
	}
}
