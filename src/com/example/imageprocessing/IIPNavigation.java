package com.example.imageprocessing;

import com.actionbarsherlock.app.SherlockFragment;

public interface IIPNavigation {
	void gotoFragment(Class<? extends SherlockFragment> fragment);
}
