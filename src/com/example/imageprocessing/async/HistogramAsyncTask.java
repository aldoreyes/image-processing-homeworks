package com.example.imageprocessing.async;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class HistogramAsyncTask extends
		AsyncTask<Bitmap, Integer, List<Integer>> {

	private WeakReference<IOnHistogramResult> mListener;

	public HistogramAsyncTask(IOnHistogramResult listener) {
		mListener = new WeakReference<HistogramAsyncTask.IOnHistogramResult>(
				listener);
	}

	@Override
	protected List<Integer> doInBackground(Bitmap... params) {
		Bitmap bm = params[0];
		Integer[] histogram = new Integer[255];
		
		int ptr = 0;
		while (ptr < histogram.length) histogram[ptr++] = 0;
		
		int hlen = bm.getWidth(), vlen = bm.getHeight();
		int pixel;
		for (int y = 0; y < vlen; y++) {
			for (int x = 0; x < hlen; x++) {
				pixel = bm.getPixel(x, y);
				histogram[pixel & 0xff]++;
				//histogram[(int)(((pixel & 0xff) + (pixel & 0xff00 >> 8) + (pixel & 0xff0000 >> 16))/3)]++;
			}
		}
		
		return Arrays.asList(histogram);
	}

	@Override
	protected void onPostExecute(List<Integer> result) {
		super.onPostExecute(result);
		if (mListener.get() != null) {
			mListener.get().onResult(result);
		}
	}

	public interface IOnHistogramResult {
		void onResult(List<Integer> list);
	}

}
