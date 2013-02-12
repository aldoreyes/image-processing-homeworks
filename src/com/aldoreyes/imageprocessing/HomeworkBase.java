package com.aldoreyes.imageprocessing;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.MediaStore;

import com.actionbarsherlock.app.SherlockFragment;


public class HomeworkBase extends SherlockFragment {
	
	private static final int ACTIVITY_SELECT_IMAGE = 0x1000;
	
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
				onImageSelect(bm.copy(bm.getConfig(), true));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected void onImageSelect(Bitmap bm){
		
	}
	
	protected void startImageSelect(){
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
	}
}
