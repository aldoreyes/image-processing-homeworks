package com.example.imageprocessing;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.imageprocessing.hw1.HW1ListFragment;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class MainActivity extends SherlockFragmentActivity implements IIPNavigation{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main, new MainListFragment());
        ft.commit();
        
    }

	@Override
	public void gotoFragment(Class<? extends Fragment> fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        try {
			ft.replace(R.id.main, fragment.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        ft.addToBackStack(null);
        ft.commit();
		
	}
    
    

}
