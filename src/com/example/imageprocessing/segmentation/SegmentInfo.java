package com.example.imageprocessing.segmentation;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.SparseIntArray;

public class SegmentInfo {
	private Point mStartPoint;
	private Point mEndPoint;
	private int mLabel;
	private Bitmap mSource;
	private Point mCenter;
	
	private SparseIntArray mCache;
	private SparseIntArray mCentralMomentCache;
	
	public Point getStartPoint() {
		return mStartPoint;
	}
	public void setStartPoint(Point mStartPoint) {
		this.mStartPoint = mStartPoint;
	}
	public Point getEndPoint() {
		return mEndPoint;
	}
	public void setEndPoint(Point mEndPoint) {
		this.mEndPoint = mEndPoint;
	}
	
	public SegmentInfo(int label, Bitmap source){
		mLabel = label;
		mSource = source;
		mCache = new SparseIntArray();
		mCentralMomentCache = new SparseIntArray();
		init();
	}
	
	private void init(){
		int minX=mSource.getWidth(), minY=mSource.getHeight(), maxX=-1, maxY=-1;
		
		//get min point Y
		for(int y=0; y<minY; y++){
			for(int x=0; x<minX; x++){
				
				if((mSource.getPixel(x, y) & 0xffffff) == mLabel){
					minY = y;
					break;
				}
			}
		}
		
		//get min point X
		for(int y=0; y<mSource.getHeight(); y++){
			for(int x=0; x<minX; x++){
				if((mSource.getPixel(x, y) & 0xffffff) == mLabel){
					minX = x;
					break;
				}
			}
		}
		setStartPoint(new Point(minX, minY));
		
		//get max point Y
		for(int y=mSource.getHeight()-1; y>maxY; y--){
			for(int x=0; x<mSource.getWidth(); x++){
				if((mSource.getPixel(x, y) & 0xffffff) == mLabel){
					maxY = y;
					break;
				}
			}
		}
		//get max point X
		for(int y=0; y<mSource.getHeight(); y++){
			for(int x=mSource.getWidth()-1; x>maxX; x--){
				if((mSource.getPixel(x, y) & 0xffffff) == mLabel){
					maxX = x;
					break;
				}
			}
		}
		setEndPoint(new Point(maxX, maxY));
	}
	
	public int getM(int p, int q){
		if(mCache.indexOfKey(p*10+q) >=0){
			return mCache.get(p*10+q);
		}
		
		int m = 0;
		
		for(int y=mStartPoint.y; y<mEndPoint.y; y++){
			for(int x=mStartPoint.x; x<mEndPoint.x; x++){
				if((mSource.getPixel(x, y) & 0xffffff) == mLabel){
					m+=Math.pow(x, p)*Math.pow(y, q);
				}
			}
		}
		
		mCache.put(p*10+q, m);
		
		return m;
		
	}
	
	public int getCentralM(int p, int q){
		if(mCentralMomentCache.indexOfKey(p*10+q) >=0){
			return mCentralMomentCache.get(p*10+q);
		}
		Point centroid = getCenter();
		int m = 0;
		for(int y=mStartPoint.y; y<mEndPoint.y; y++){
			for(int x=mStartPoint.x; x<mEndPoint.x; x++){
				if((mSource.getPixel(x, y) & 0xffffff) == mLabel){
					m+=Math.pow(x - centroid.x, p)*Math.pow(y - centroid.y, q);
				}
			}
		}
		
		mCentralMomentCache.put(p*10+q, m);
		
		return m;
	}
	
	public Point getCenter(){
		if(mCenter != null){
			return mCenter;
		}
		
		return mCenter = new Point(getM(1,0)/getM(0,0), getM(0,1)/getM(0,0));
	}
}
