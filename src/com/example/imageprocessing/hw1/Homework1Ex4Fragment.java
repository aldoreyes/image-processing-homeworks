package com.example.imageprocessing.hw1;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.imageprocessing.BinaryImage;
import com.example.imageprocessing.R;
import com.example.imageprocessing.util.BitmapUtils;

public class Homework1Ex4Fragment extends SherlockFragment implements
		OnItemSelectedListener {

	private BinaryImage image = new BinaryImage(new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
					{ 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
					{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }});

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.homework1ex4, container,
				false);
		getSherlockActivity().getSupportActionBar().setTitle("Exercise 4");
		
		image.calculateAcumulativeHistogram();
		
		int w = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 300, getResources()
						.getDisplayMetrics());

		ImageView iv = ((ImageView) toReturn.findViewById(R.id.image));
		iv.setImageBitmap(BitmapUtils.createBinaryImage(image.data, w, w));
		return toReturn;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
