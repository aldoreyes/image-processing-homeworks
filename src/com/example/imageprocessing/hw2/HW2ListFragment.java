package com.example.imageprocessing.hw2;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.example.imageprocessing.IIPNavigation;
import com.example.imageprocessing.R;
import com.example.imageprocessing.R.layout;

public class HW2ListFragment extends SherlockListFragment {

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
				android.R.id.text1, new String[] { "HW2 - Ex 1"});
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
			mListener.gotoFragment(Homework2Ex1_a.class);
			break;
		}
	}

}
