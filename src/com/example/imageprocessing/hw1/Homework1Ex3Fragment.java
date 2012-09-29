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
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import com.example.imageprocessing.util.BitmapUtils;

public class Homework1Ex3Fragment extends SherlockFragment implements
		OnItemSelectedListener, IOnHistogramResult {

	private static final int ACTIVITY_SELECT_IMAGE = 0x1000;

	private BinaryImage[] images = new BinaryImage[] {
			new BinaryImage(new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
					{ 0, 1, 2, 2, 2, 2, 2, 2, 1, 0 },
					{ 0, 1, 2, 3, 3, 3, 3, 3, 1, 0 },
					{ 0, 1, 2, 3, 6, 5, 4, 3, 1, 0 },
					{ 0, 1, 2, 3, 6, 5, 4, 3, 1, 0 },
					{ 0, 1, 2, 3, 3, 3, 3, 3, 1, 0 },
					{ 0, 1, 2, 2, 2, 2, 2, 2, 1, 0 },
					{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } }),
			new BinaryImage(new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 3, 4, 3, 3, 4, 3, 4, 0 },
					{ 0, 1, 3, 3, 4, 5, 4, 4, 3, 0 },
					{ 0, 1, 4, 4, 4, 5, 5, 3, 4, 0 },
					{ 0, 0, 0, 0, 3, 6, 5, 0, 0, 0 },
					{ 0, 0, 1, 0, 5, 6, 4, 1, 0, 0 },
					{ 0, 0, 1, 0, 5, 6, 3, 1, 0, 0 },
					{ 0, 1, 0, 1, 5, 5, 4, 1, 0, 0 },
					{ 0, 0, 0, 0, 4, 3, 3, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }

			}) };

	private Bitmap[] binaryImages;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.homework1ex3, container,
				false);
		getSherlockActivity().getSupportActionBar().setTitle("Exercise 3");
		Spinner spinner = (Spinner) toReturn.findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this.getSherlockActivity(), R.array.hw1_ex3_images,
				android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		XYPlot xyplot = (XYPlot) toReturn.findViewById(R.id.histogram);
		xyplot.getLegendWidget().setVisible(false);
		xyplot.setRangeLabel("");
		xyplot.setDomainLabel("");
		xyplot.setBorderStyle(XYPlot.BorderStyle.SQUARE, null, null);
		xyplot.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		xyplot.disableAllMarkup();
		binaryImages = new Bitmap[images.length];
		for (int x = 0; x < images.length; x++) {
			int w = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 300, getResources()
							.getDisplayMetrics());
			binaryImages[x] = BitmapUtils.createBinaryImage(images[x].data, w,
					w);
		}

		return toReturn;
	}

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
		if (position < 3 && position > 0) {
			if (binaryImages != null) {
				((ImageView) getView().findViewById(R.id.image))
						.setImageBitmap(binaryImages[position-1]);
			}
			XYSeries series = new SimpleXYSeries(
					images[position-1].getHistogram(8),
					SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Histogram");

			XYPlot xyplot = (XYPlot) getView().findViewById(R.id.histogram);
			xyplot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
			xyplot.clear();
			xyplot.addSeries(
					series,
					new LineAndPointFormatter(Color.rgb(0, 0, 0), null, Color
							.rgb(0, 0, 0)));
			xyplot.invalidate();
		} else if(position >= 3){
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != 0) {
			if (requestCode == ACTIVITY_SELECT_IMAGE) {
				XYPlot xyplot = (XYPlot) getView().findViewById(R.id.histogram);
				xyplot.setVisibility(View.INVISIBLE);
				
				Uri selectedImage = data.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getSherlockActivity().getContentResolver().query(
	                               selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();

	            Spinner spinner = (Spinner) getView().findViewById(R.id.spinner);
	            spinner.setSelection(0);
	            
	            Bitmap bm = BitmapFactory.decodeFile(filePath);
	            new HistogramAsyncTask(this).execute(bm);
				ImageView iv = ((ImageView) getView().findViewById(R.id.image));
				iv.setImageBitmap(bm);
				iv.invalidate();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResult(List<Integer> list) {
		XYSeries series = new SimpleXYSeries(
				list,
				SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Histogram");

		XYPlot xyplot = (XYPlot) getView().findViewById(R.id.histogram);
		xyplot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 16);
		xyplot.clear();
		xyplot.addSeries(
				series,
				new LineAndPointFormatter(Color.rgb(0, 0, 0), null, Color
						.rgb(0, 0, 0)));
		xyplot.setVisibility(View.VISIBLE);
		xyplot.invalidate();
		
	}
}
