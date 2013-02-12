package com.aldoreyes.imageprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.aldoreyes.imageprocessing.Border.BorderPoint;

import android.os.Handler;

public class BinaryImage {

	public static final int CONTAINS_SINGLE_CONNECTED = 1;
	public static final int CONTAINS_MULTI_CONNECTED = 2;
	public static final int CONTAINS_MIXED_CONNECTED = 3;

	public int[][] data;
	public ArrayList<Border> borders;
	public int objectsContained;
	private IExpandBordersListener mListener;
	private List<Integer> mHistogram;

	public BinaryImage(int[][] data) {
		this.data = data;
		borders = new ArrayList<Border>();
		objectsContained = 0;
	}

	public IExpandBordersListener getListener() {
		return mListener;
	}

	public void setListener(IExpandBordersListener mListener) {
		this.mListener = mListener;
	}

	public void addBorder(Border b) {

		borders.add(b);
	}
	
	public void calculateAcumulativeHistogram(){
		//calculate horizontal
		int lenH = data[0].length-1, lenV = data.length-1;
		for(int y=0; y<lenV;y++){
			int sum = 0;
			for(int x=0; x<lenH; x++){
				if(data[y][x] == 1){
					sum++;
				}
			}
			data[y][lenH] = sum;
		}
		
		//calculate vertical
		for(int x=0; x<lenH;x++){
			int sum = 0;
			for(int y=0; y<lenV; y++){
				if(data[y][x] == 1){
					sum++;
				}
			}
			data[lenV][x] = sum;
		}
	}

	public void getBorders() {
		Border border = null;
		while ((border = Border.getBorder(data)) != null) {
			Border.markBorderOnSource(data, border);
			addBorder(border);
			if (!border.isExternal) {
				// fill segment with internal border value (3)
				Border.spreadInternalBorder(data, border, handler);
				objectsContained = BinaryImage.CONTAINS_MULTI_CONNECTED;
			}
		}

		if (objectsContained == 0) {
			
			if(expandExternalBorders()){
				objectsContained = CONTAINS_SINGLE_CONNECTED;
			}
		}
	}
	
	public int dilate(int[][] se){
		int hlen = data[0].length, vlen = data.length;
		int toReturn = 0;
		for (int i = vlen-1; i >= 0; i--) {
			for (int j = hlen-1; j >= 0; j--) {
				if(data[i][j] == 1){
					kernelDilate(j, i, hlen, vlen, se);
					toReturn++;
				}
			}
		}
		
		return toReturn;
	}
	
	public void erosion(int[][] se){
		int hlen = data[0].length, vlen = data.length;
		
		for (int i = 0; i < vlen; i++) {
			for (int j = 0; j < hlen; j++) {
			 data[i][j] = kernelMatch(j, i, hlen, vlen, se)?1:0;
			}
		}
	}
	
	public int getPixelsCount(){
		int hlen = data[0].length, vlen = data.length;
		int toReturn = 0;
		for (int i = 0; i < vlen; i++) {
			for (int j = 0; j < hlen; j++) {
			 if(data[i][j] > 0){
				 toReturn++;
			 }
			}
		}
		
		return toReturn;
	}
	
	public int getContactCount(){
		int toReturn = 0;
		int hlen = data[0].length, vlen = data.length;
		for (int i = 0; i < vlen; i++) {
			for (int j = 0; j < hlen; j++) {
			 if(data[i][j] == 1){
				 //check  right border
				 if(j+1 < hlen && data[i][j+1] == 1){
					 toReturn++;
				 }
				//check  bottom border
				 if(i+1 < vlen && data[i+1][j] == 1){
					 toReturn++;
				 }
			 }
			}
		}
		
		return toReturn;
	}
	
	private void kernelDilate(int x, int y, int w, int h, int[][] se){
		int lenh = se[0].length, lenv = se.length;
		for (int i = 0; i < lenv; i++) {
			for(int j = 0; j < lenh; j++) {
				if(i+y < h && j+x < w &&  se[i][j] == 1){
					data[y+i][x+j] = 1;
				}
			}
		}
	}
	
	private boolean kernelMatch(int x, int y, int w, int h, int[][] se){
		int lenh = se[0].length, lenv = se.length;
		for (int i = 0; i < lenv; i++) {
			for(int j = 0; j < lenh; j++) {
				if(y+i >= w || x+j >=h || se[i][j] != data[y+i][x+j]){
					return false;
				}
			}
		}
		
		
		return true;
	}
	
	private boolean expandExternalBorders(){
		boolean toReturn = false;
		expandingInternals = false;
		Iterator<Border> iterator = borders.iterator();
		while (iterator.hasNext()) {
			Border border = iterator.next();
			if (border.isExternal) {
				BorderPoint point = border.points.get(0);
				if (data[point.y][point.x] == 2) {
					toReturn = true;
					Border.spreadExternalBorder(data, border, handler);
				}
			}
		}
		return toReturn;
	}
	
	private boolean expandingInternals = true;
	private int count = 0;
	
	private void handleMessage(int what, boolean hasMessages){
		BinaryImage.this.count += what;
		if (BinaryImage.this.count == 0 && !hasMessages) {
			if (BinaryImage.this.expandingInternals) {
				BinaryImage.this.expandingInternals = false;
				
				//expand borders for simple connected objects
				if (!BinaryImage.this.expandExternalBorders()) {
					BinaryImage.this.mListener.onExpandBorderFinished();
				}else{
					objectsContained = CONTAINS_MIXED_CONNECTED;
				}

			} else {
				if (BinaryImage.this.mListener != null) {
					mListener.onExpandBorderFinished();
				}
			}
		}
	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			BinaryImage.this.handleMessage(msg.what, hasMessages(1) || hasMessages(-1));
		};
	};
	
	public List<Integer> getHistogram(int l){
		if(mHistogram == null){
			Integer[] mArray= new Integer[l];
			for (int x = 0; x < mArray.length; x++) {
				mArray[x] = Integer.valueOf(0);
			}
			
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++){
					mArray[data[i][j]]++;
				}
			}
			
			mHistogram = Arrays.asList(mArray);
		}
		
		
		
		return mHistogram;
	}

	public interface IExpandBordersListener {
		void onExpandBorderFinished();
	}
}
