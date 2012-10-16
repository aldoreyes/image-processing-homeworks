package com.example.imageprocessing.hw1;

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

public class HW1ListFragment extends SherlockListFragment {

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
				android.R.id.text1, new String[] { "HW1 - Ex 1", "HW1 - Ex 2", "HW1 - Ex 3", "HW1 - Ex 4", "HW1 - Ex 5", "HW1 - Ex 6", "HW1 - Ex 7", "HW1 - Ex 8" });
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
			mListener.gotoFragment(Homework1Fragment.class);
			break;
		case 1:
			mListener.gotoFragment(Homework1Ex2Fragment.class);
			break;
		case 2:
			mListener.gotoFragment(Homework1Ex3Fragment.class);
			break;
		case 3:
			mListener.gotoFragment(Homework1Ex4Fragment.class);
			break;
		case 4:
			mListener.gotoFragment(Homework1Ex5Fragment.class);
			break;
		case 5:
			mListener.gotoFragment(Homework1Ex6Fragment.class);
			break;
		case 6:
			mListener.gotoFragment(Homework1Ex7Fragment.class);
			break;
		case 7:
			mListener.gotoFragment(Homework1Ex8Fragment.class);
			break;
		}
	}

}
