package com.example.imageprocessing.hw2;

import java.util.Iterator;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.imageprocessing.HomeworkBase;
import com.example.imageprocessing.R;
import com.example.imageprocessing.segmentation.IBitmapResult;
import com.example.imageprocessing.segmentation.IIntegerResult;
import com.example.imageprocessing.segmentation.OtsuAsyncTask;
import com.example.imageprocessing.segmentation.SegmentInfo;
import com.example.imageprocessing.segmentation.SeqSegmAsyncTask;
import com.example.imageprocessing.util.BitmapUtils;

public class Homework2Ex1_a extends HomeworkBase implements OnClickListener, IIntegerResult, IBitmapResult {
	
	private Bitmap mBitmap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.homework2ex1_a, container,
				false);
		getSherlockActivity().getSupportActionBar().setTitle("Exercise 1 - a");

		return toReturn;
	}

	@Override
	public void onResume() {
		((Button) getView().findViewById(R.id.button1))
				.setOnClickListener(this);
		
		super.onResume();
	}

	@Override
	public void onPause() {
		((Button) getView().findViewById(R.id.button1))
				.setOnClickListener(null);
		super.onPause();
	}
	
	@Override
	protected void onImageSelect(Bitmap bm) {
		super.onImageSelect(bm);
		mBitmap = bm;
		
		new OtsuAsyncTask(this).execute(bm);
		
		
		ImageView iv = ((ImageView) getView().findViewById(R.id.image));
		iv.setImageBitmap(mBitmap);
		iv.invalidate();
		
		iv = ((ImageView) getView().findViewById(R.id.image2));
		iv.setImageBitmap(mBitmap);
		iv.invalidate();
	}

	@Override
	public void onClick(View v) {
		startImageSelect();
		
	}
	
	@Override
	public void onResult(Bitmap bitmap) {
		new SeqSegmAsyncTask(this).execute(bitmap);
		
		ImageView iv = ((ImageView) getView().findViewById(R.id.image));
		iv.setImageBitmap(bitmap);
		iv.invalidate();
	}

	@Override
	public void onResult(int result, Bitmap tagBM, Set<Integer> eqTable) {
		
		Iterator<Integer> iterator = eqTable.iterator();
		while(iterator.hasNext()){
			SegmentInfo si = new SegmentInfo(iterator.next(), tagBM);
			Point p = si.getCenter();
			BitmapUtils.drawCross(mBitmap, p.x, p.y);
		}
		
		ImageView iv = ((ImageView) getView().findViewById(R.id.image2));
		iv.invalidate();
	}

	
}
