package com.example.imageprocessing.segmentation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.example.imageprocessing.util.BitmapUtils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class SeqSegmAsyncTask extends AsyncTask<Bitmap, Void, Integer> {
	
	private WeakReference<IIntegerResult> mHandler;



	public SeqSegmAsyncTask(IIntegerResult handler) {
		mHandler = new WeakReference<IIntegerResult>(handler);
	}

	private HashMap<Integer, ArrayList<Integer>> getEqTable(Bitmap data){
		HashMap<Integer, ArrayList<Integer>> table = new HashMap<Integer, ArrayList<Integer>>(); 
		int lenh = data.getWidth(), lenv = data.getHeight();
		int currentTag = 0;
		int prevTop, prevLeft;
		int min;
		for (int y = 0; y < lenv; y++) {
			for (int x = 0; x < lenh; x++) {
				if((data.getPixel(x, y) & 0xff) == 0xff){
					//check above
					prevTop = y>0?data.getPixel(x, y-1) & 0xff:0;
					prevLeft = x>0?data.getPixel(x-1, y) & 0xff:0;
					if(prevTop + prevLeft > 0){
						if(prevTop > 0){
							if(prevLeft>0){
								//both have a tag
								if(prevTop == prevLeft){
									data.setPixel(x, y, 0xff000000 + prevTop);
								}else{
									data.setPixel(x, y, 0xff000000 + (min = Math.min(prevLeft, prevTop)));
									//add equivalence
									table.get(min).add(Math.max(prevLeft, prevTop));
								}
							}else{
								//prevTop > 0
								data.setPixel(x, y, 0xff000000 + prevTop);
							}
							
						}else{
							//prev left > 0
							data.setPixel(x, y, 0xff000000 + prevLeft);
						}
					}else{
						currentTag++;
						data.setPixel(x, y, 0xff000000 + currentTag);
						table.put(currentTag, new ArrayList<Integer>());
					}
				}
			}
		}
		
		//filter table
		HashMap<Integer, ArrayList<Integer>> filteredTable = (HashMap<Integer, ArrayList<Integer>>) table.clone();
		Iterator<Integer> iterator = table.keySet().iterator();
		ArrayList<Integer> links;
		while(iterator.hasNext()){
			links = table.get(iterator.next());
			Iterator<Integer> linksIterator = links.iterator();
			while(linksIterator.hasNext()){
				filteredTable.remove(linksIterator.next());
			}
		}
		return filteredTable;
	}
	
	
	
	@Override
	protected Integer doInBackground(Bitmap... params) {
		
		Bitmap bitmap = params[0];
		
		int[][] seErosion = new int[][]{
				{1,1},
				{1,1}
		};
		
		int[][] se = new int[][]{
				{1,1,1},
				{1,1,1},
				{1,1,1}
		};
		//BitmapUtils.erosion(bitmap, seErosion);
		BitmapUtils.dilate(bitmap, se);
		
		return getEqTable(bitmap.copy(bitmap.getConfig(), true)).size();
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(mHandler.get() != null){
			mHandler.get().onResult(result);
		}
	}

}
