package com.aldoreyes.imageprocessing;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.aldoreyes.imageprocessing.hw1.HW1ListFragment;
import com.aldoreyes.imageprocessing.hw2.HW2ListFragment;
import com.aldoreyes.imageprocessing.hw3.HW3ListFragment;
import com.example.imageprocessing.R;

public class MainListFragment extends SherlockListFragment {
	private IIPNavigation mListener;

	@Override
	public void onAttach(Activity activity) {
		if (activity instanceof IIPNavigation) {
			mListener = (IIPNavigation) activity;
		}
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.activity_main, container,
				false);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getSherlockActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, new String[] { "Homework 1", "Homework 2", "Homework 3"});
		setListAdapter(adapter);
		return toReturn;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case 0:
			mListener.gotoFragment(HW1ListFragment.class);			
			break;
		case 1:
			mListener.gotoFragment(HW2ListFragment.class);			
			break;
		case 2:
			mListener.gotoFragment(HW3ListFragment.class);			
			break;
		}
	}
}
