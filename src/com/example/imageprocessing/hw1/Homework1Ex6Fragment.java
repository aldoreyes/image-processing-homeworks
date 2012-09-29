package com.example.imageprocessing.hw1;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidplot.series.XYSeries;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.example.imageprocessing.BinaryImage;
import com.example.imageprocessing.R;
import com.example.imageprocessing.R.array;
import com.example.imageprocessing.R.id;
import com.example.imageprocessing.R.layout;
import com.example.imageprocessing.async.HistogramAsyncTask;
import com.example.imageprocessing.async.HistogramAsyncTask.IOnHistogramResult;
import com.example.imageprocessing.segmentation.IBitmapResult;
import com.example.imageprocessing.segmentation.KittlerAsyncTask;
import com.example.imageprocessing.segmentation.OtsuAsyncTask;
import com.example.imageprocessing.segmentation.SahooAsyncTask;
import com.example.imageprocessing.util.BitmapUtils;

public class Homework1Ex6Fragment extends SherlockFragment implements
		OnClickListener, IBitmapResult, OnCheckedChangeListener {

	private static final int ACTIVITY_SELECT_IMAGE = 0x1000;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.homework1ex6, container,
				false);
		getSherlockActivity().getSupportActionBar().setTitle("Exercise 6");

		return toReturn;
	}

	@Override
	public void onResume() {
		((Button) getView().findViewById(R.id.button1))
				.setOnClickListener(this);
		
		RadioGroup rb = (RadioGroup)getView().findViewById(R.id.radioGroup1);
		rb.setOnCheckedChangeListener(this);
		
		super.onResume();
	}

	@Override
	public void onPause() {
		((Button) getView().findViewById(R.id.button1))
				.setOnClickListener(null);
		
		((RadioGroup)getView().findViewById(R.id.radioGroup1)).setOnCheckedChangeListener(null);
		super.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != 0) {
			if (requestCode == ACTIVITY_SELECT_IMAGE) {

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getSherlockActivity().getContentResolver()
						.query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();

				Bitmap bm = BitmapFactory.decodeFile(filePath);
				
				new OtsuAsyncTask(this).execute(bm);
				new KittlerAsyncTask(kittlerHandler).execute(bm);
				new SahooAsyncTask(sahooHandler).execute(bm);
				
				ImageView iv = ((ImageView) getView().findViewById(R.id.image));
				iv.setImageBitmap(bm);
				iv.invalidate();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, ACTIVITY_SELECT_IMAGE);

	}

	@Override
	public void onResult(Bitmap bitmap) {
		ImageView iv = ((ImageView) getView().findViewById(R.id.image2));
		iv.setImageBitmap(bitmap);
		iv.invalidate();
	}
	
	private IBitmapResult kittlerHandler = new IBitmapResult() {
		
		@Override
		public void onResult(Bitmap bitmap) {
			ImageView iv = ((ImageView) getView().findViewById(R.id.image3));
			iv.setImageBitmap(bitmap);
			iv.invalidate();
			
		}
	};
	
	private IBitmapResult sahooHandler = new IBitmapResult() {
		
		@Override
		public void onResult(Bitmap bitmap) {
			ImageView iv = ((ImageView) getView().findViewById(R.id.image4));
			iv.setImageBitmap(bitmap);
			iv.invalidate();
			
		}
	};


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		ImageView iv = ((ImageView) getView().findViewById(R.id.image2));
		if(iv.getVisibility() == View.VISIBLE){
			iv.setVisibility(View.GONE);
		}
		ImageView ivKittler = ((ImageView) getView().findViewById(R.id.image3));
		if(ivKittler.getVisibility() == View.VISIBLE){
		ivKittler.setVisibility(View.GONE);
		}
		ImageView ivSahoo = ((ImageView) getView().findViewById(R.id.image4));
		if(ivSahoo.getVisibility() == View.VISIBLE){
			ivSahoo.setVisibility(View.GONE);
		}
		
		switch(checkedId){
		case R.id.radio_otsu:
			iv.setVisibility(View.VISIBLE);
			iv.invalidate();
			break;
		case R.id.radio_kittler:
			ivKittler.setVisibility(View.VISIBLE);
			ivKittler.invalidate();
			break;
		case R.id.radio_sahoo:
			ivSahoo.setVisibility(View.VISIBLE);
			ivSahoo.invalidate();
			break;
		}
		
	}
}
