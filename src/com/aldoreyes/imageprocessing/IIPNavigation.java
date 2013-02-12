package com.aldoreyes.imageprocessing;

import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragment;

public interface IIPNavigation {
	void gotoFragment(Class<? extends Fragment> fragment);
}
