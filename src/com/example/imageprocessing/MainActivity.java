package com.example.imageprocessing;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.Bundle;
import android.app.Activity;
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
	public void gotoFragment(Class<? extends SherlockFragment> fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        try {
			ft.replace(R.id.main, fragment.newInstance());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ft.addToBackStack(null);
        ft.commit();
		
	}
    
    

}
