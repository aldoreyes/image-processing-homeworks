package com.example.imageprocessing.hw1;

import java.util.Set;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.imageprocessing.R;

import com.example.imageprocessing.segmentation.IBitmapResult;
import com.example.imageprocessing.segmentation.IIntegerResult;
import com.example.imageprocessing.segmentation.OtsuAsyncTask;
import com.example.imageprocessing.segmentation.SeqSegmAsyncTask;

public class Homework1Ex7Fragment extends SherlockFragment implements
		OnClickListener, IBitmapResult {

	private static final int ACTIVITY_SELECT_IMAGE = 0x1000;
	
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.homework1ex7, container,
				false);
		getSherlockActivity().getSupportActionBar().setTitle("Exercise 7");

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
				//new SeqSegmAsyncTask(segmentationHandler).execute(bm.copy(bm.getConfig(), true));
				
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
		
		new SeqSegmAsyncTask(segmentationHandler).execute(bitmap);
	}
	
	private IIntegerResult segmentationHandler = new IIntegerResult() {

		@Override
		public void onResult(int result, Bitmap tagBM,
				Set<Integer> eqTable) {
			((TextView)getView().findViewById(R.id.textView1)).setText("Segments: "+ result);
			//ImageView iv = ((ImageView) getView().findViewById(R.id.image2));
			//iv.setImageBitmap(tagBM);
			//iv.invalidate();
		}
	}; 
}
