package com.aldoreyes.imageprocessing.hw2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aldoreyes.imageprocessing.HomeworkBase;
import com.aldoreyes.imageprocessing.segmentation.IBitmapResult;
import com.aldoreyes.imageprocessing.segmentation.IIntegerResult;
import com.aldoreyes.imageprocessing.segmentation.OtsuAsyncTask;
import com.aldoreyes.imageprocessing.segmentation.SegmentInfo;
import com.aldoreyes.imageprocessing.segmentation.SeqSegmAsyncTask;
import com.aldoreyes.imageprocessing.util.BitmapUtils;
import com.example.imageprocessing.R;

public class Homework2Ex1_a extends HomeworkBase implements OnClickListener, IIntegerResult, IBitmapResult {
	
	private Bitmap mBitmap;
	private File[] files;
	private int filesInd;
	private StringBuffer resultBuffer;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.homework2ex1_a, container,
				false);
		getSherlockActivity().getSupportActionBar().setTitle("Exercise 1 - a");

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
	protected void onImageSelect(Bitmap bm) {
		super.onImageSelect(bm);
		mBitmap = bm;
		
		new OtsuAsyncTask(this).execute(bm);
		
		
		ImageView iv = ((ImageView) getView().findViewById(R.id.image));
		iv.setImageBitmap(mBitmap);
		iv.invalidate();
		
		iv = ((ImageView) getView().findViewById(R.id.image2));
		iv.setImageBitmap(mBitmap);
		iv.invalidate();
	}

	@Override
	public void onClick(View v) {
		//startImageSelect();
		resultBuffer = new StringBuffer();
		resultBuffer.append("Name, Hu1, Hu2, Flusser1, Flusser2, Flusser3, Flusser4, N00, N12, N21, N03, N30\n");
		File dir = new File("/mnt/sdcard/Pictures/markers0/");
		files = dir.listFiles();
		
		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(File lhs, File rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		});
		
		filesInd = 0;
		analizeImage();
	}
	
	private void analizeImage(){
		if(filesInd <  files.length){
			Log.d("ip", files[filesInd].getAbsolutePath());
			resultBuffer.append(files[filesInd].getName()+",");
			Bitmap bm = BitmapFactory.decodeFile(files[filesInd].getAbsolutePath());
			onImageSelect(bm.copy(bm.getConfig(), true));
		}else{
			//save result file
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
			File fResult = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/results.csv");
			if(fResult.exists()){
				fResult.delete();
			}
			try {
				fResult.createNewFile();
				FileOutputStream fOut = new FileOutputStream(fResult);
				OutputStreamWriter fOutWritter = new OutputStreamWriter(fOut);
				fOutWritter.append(resultBuffer.toString());
				fOutWritter.flush();
				fOutWritter.close();
				fOut.flush();
				fOut.close();
				Toast.makeText(getSherlockActivity(), "Results saved on "+fResult.getAbsolutePath(), Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d("ip", fResult.getAbsolutePath());
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onResult(Bitmap bitmap) {
		new SeqSegmAsyncTask(this).execute(bitmap);
		
		ImageView iv = ((ImageView) getView().findViewById(R.id.image));
		iv.setImageBitmap(bitmap);
		iv.invalidate();
	}

	@Override
	public void onResult(int result, Bitmap tagBM, Set<Integer> eqTable) {
		Iterator<Integer> iterator = eqTable.iterator();
		while(iterator.hasNext()){
			SegmentInfo si = new SegmentInfo(iterator.next(), tagBM);
			Point p = si.getCenter();
			BitmapUtils.drawCross(mBitmap, p.x, p.y);
			resultBuffer.append(","+si.getHuInvariant1()+",");
			resultBuffer.append(si.getHuInvariant2()+",");
			resultBuffer.append(si.getFlusser1()+",");
			resultBuffer.append(si.getFlusser2()+",");
			resultBuffer.append(si.getFlusser3()+",");
			resultBuffer.append(si.getFlusser4()+",");
			resultBuffer.append(si.getN(0, 0)+",");
			resultBuffer.append(si.getN(1, 2)+",");
			resultBuffer.append(si.getN(2, 1)+",");
			resultBuffer.append(si.getN(0, 3)+",");
			resultBuffer.append(si.getN(3, 0)+"\n");
			//t1.append(si.getHuInvariant1()+"\n");
			//t2.append(si.getHuInvariant2()+"\n");
		}
		
		try {
		       FileOutputStream out = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+files[filesInd].getName());
		       mBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		} catch (Exception e) {
		       e.printStackTrace();
		}
		
		ImageView iv = ((ImageView) getView().findViewById(R.id.image2));
		iv.invalidate();
		
		filesInd++;
		analizeImage();
	}

	
}
