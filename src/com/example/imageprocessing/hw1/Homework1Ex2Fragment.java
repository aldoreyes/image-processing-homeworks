package com.example.imageprocessing.hw1;

import android.graphics.Bitmap;
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
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.imageprocessing.BinaryImage;
import com.example.imageprocessing.R;
import com.example.imageprocessing.BinaryImage.IExpandBordersListener;
import com.example.imageprocessing.R.array;
import com.example.imageprocessing.R.id;
import com.example.imageprocessing.R.layout;
import com.example.imageprocessing.util.BitmapUtils;

public class Homework1Ex2Fragment extends SherlockFragment implements
		OnItemSelectedListener, IExpandBordersListener {
	private BinaryImage[] images = new BinaryImage[] {
			new BinaryImage(new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
					{ 0, 1, 1, 0, 0, 0, 1, 1, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 0 },
					{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 0 },
					{ 0, 0, 1, 0, 1, 1, 1, 1, 1, 0 },
					{ 0, 1, 1, 0, 1, 1, 1, 1, 1, 0 },
					{ 0, 1, 1, 0, 0, 1, 1, 1, 1, 0 },
					{ 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } }),
			new BinaryImage(new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
					{ 0, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
					{ 0, 1, 1, 0, 1, 1, 0, 0, 0, 0 },
					{ 0, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
					{ 0, 1, 1, 1, 1, 1, 0, 0, 1, 0 },
					{ 0, 1, 1, 1, 0, 0, 0, 1, 1, 0 },
					{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 0 },
					{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }

			}) };
	private int imagesReady = 0;
	private Bitmap[] sourceImages;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.homework1ex2, container,
				false);
		getSherlockActivity().getSupportActionBar().setTitle("Exercise 2");
		Spinner spinner = (Spinner) toReturn.findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this.getSherlockActivity(), R.array.hw1_ex2_images,
				android.R.layout.simple_spinner_item);
		spinner.setAdapter(adapter);
		return toReturn;
	}

	@Override
	public void onStart() {
		super.onStart();

		for (int x = 0; x < images.length; x++) {
			images[x].setListener(this);
			images[x].getBorders();
		}

	};

	@Override
	public void onResume() {
		((Spinner) getView().findViewById(R.id.spinner))
				.setOnItemSelectedListener(this);
		super.onResume();
	}

	@Override
	public void onPause() {
		((Spinner) getView().findViewById(R.id.spinner))
				.setOnItemSelectedListener(null);
		super.onPause();
	}

	@Override
	public void onItemSelected(AdapterView<?> adapter, View parent,
			int position, long id) {
		// check if images ready
		if (sourceImages != null) {
			((ImageView) getView().findViewById(R.id.image))
					.setImageBitmap(sourceImages[position]);

			((TextView) getView().findViewById(R.id.tv_image_type))
					.setText(getResources().getStringArray(R.array.image_type)[images[position].objectsContained]);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExpandBorderFinished() {
		imagesReady++;
		if (imagesReady == images.length) {
			int w = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 300, getResources()
							.getDisplayMetrics());

			sourceImages = new Bitmap[images.length];
			for (int x = 0; x < images.length; x++) {
				sourceImages[x] = BitmapUtils.createBinaryImage(images[x].data,
						w, w);
			}

			ImageView iv = ((ImageView) getView().findViewById(R.id.image));
			iv.setImageBitmap(sourceImages[0]);
			iv.invalidate();
			
			((TextView) getView().findViewById(R.id.tv_image_type))
			.setText(getResources().getStringArray(R.array.image_type)[images[0].objectsContained]);
		}

	}
}
