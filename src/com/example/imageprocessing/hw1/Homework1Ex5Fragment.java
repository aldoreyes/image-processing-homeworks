package com.example.imageprocessing.hw1;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.imageprocessing.BinaryImage;
import com.example.imageprocessing.R;
import com.example.imageprocessing.util.BitmapUtils;

public class Homework1Ex5Fragment extends SherlockFragment implements android.widget.RadioGroup.OnCheckedChangeListener {
	private BinaryImage image = new BinaryImage(new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }});
	
	private int se[][] = new int[][]{
			{1,1},
			{1,1}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.homework1ex5, container,
				false);
		getSherlockActivity().getSupportActionBar().setTitle("Exercise 5");
		
		int w = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 300, getResources()
						.getDisplayMetrics());

		ImageView iv = ((ImageView) toReturn.findViewById(R.id.image));
		iv.setImageBitmap(BitmapUtils.createBinaryImage(image.data, w, w));

		image.erosion(se);
		
		iv = ((ImageView) toReturn.findViewById(R.id.image2));
		iv.setImageBitmap(BitmapUtils.createBinaryImage(image.data, w, w));
		
		int m = image.dilate(se);
		
		iv = ((ImageView) toReturn.findViewById(R.id.image3));
		iv.setImageBitmap(BitmapUtils.createBinaryImage(image.data, w, w));
		
		int c = image.getPixelsCount();
		int pc = image.getContactCount();
		
		//Segments Formula
		int r = c + m -pc;
		
		((TextView)toReturn.findViewById(R.id.tv_segments)).setText("Segments: "+r+" = "+c+" + "+m+" - "+pc);
		
		return toReturn;
	}
	
	public void onResume() {
		super.onResume();
		
		RadioGroup rb = (RadioGroup)getView().findViewById(R.id.radioGroup1);
		rb.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		ImageView iv;
		
		iv = ((ImageView) getView().findViewById(R.id.image));
		iv.setVisibility(View.GONE);
		iv = ((ImageView) getView().findViewById(R.id.image2));
		iv.setVisibility(View.GONE);
		iv = ((ImageView) getView().findViewById(R.id.image3));
		iv.setVisibility(View.GONE);
		
		switch(checkedId){
		case R.id.radio_original:
			iv = ((ImageView) getView().findViewById(R.id.image));
			break;
		case R.id.radio_erosion:
			iv = ((ImageView) getView().findViewById(R.id.image2));
			break;
		case R.id.radio_dilation:
			iv = ((ImageView) getView().findViewById(R.id.image3));
			break;
		}
		
		iv.setVisibility(View.VISIBLE);
		
	}
}
