package com.aldoreyes.imageprocessing.hw3;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.aldoreyes.imageprocessing.IIPNavigation;
import com.example.imageprocessing.R;

public class HW3ListFragment extends SherlockListFragment {

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
				android.R.id.text1, new String[] { "HW3 - Ex 2 a",  "HW3 - Ex 2 b"});
		setListAdapter(adapter);
		return toReturn;
	}

	@Override
	public void onDetach() {
		mListener = null;
		super.onDetach();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case 0:
			mListener.gotoFragment(Homework3Ex2_a.class);
			break;
		case 1:
			mListener.gotoFragment(Homework3Ex2_b.class);
			break;
		}
	}

}
