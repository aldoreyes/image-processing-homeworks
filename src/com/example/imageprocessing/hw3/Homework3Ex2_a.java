package com.example.imageprocessing.hw3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.imageprocessing.HomeworkBase;
import com.example.imageprocessing.R;
import com.example.imageprocessing.neuralnetwork.spiking.SpikingNeuron;

public class Homework3Ex2_a extends HomeworkBase implements OnClickListener {
	
	private static final double[][][] A_CLASSES = new double[][][]{
		{{1,1}, {2,1}, {1,2}, {2,2}},
		{{5,5}, {6,5}, {5,6}, {6,6}},
		{{10,6}, {11,6}, {10,7}, {11,7}}};
	
	
	private static final double [][] TEST_1 = new double[][]{
			{1,1}, {2,2}
		};
	
	private static final double [][] TEST_2 = new double[][]{
		{4,5}, {5,5}, {7,4}
	};
	
	private static final double [][] TEST_3 = new double[][]{
		{10,10}, {11, 7}, {11, 10}, {10,7}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.homework3ex2, container,
				false);
		getSherlockActivity().getSupportActionBar().setTitle("HW3 Exercise 2 - a");

		return toReturn;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		this.getView().findViewById(R.id.button1).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		SpikingNeuron neuron = new SpikingNeuron();
		neuron.train(A_CLASSES);
		
		int result1  = neuron.classify(TEST_1);
		int result2  = neuron.classify(TEST_2);
		int result3  = neuron.classify(TEST_3);
		
		String results = TEST_1.toString()+ " Result: " +result1+"\n";
		results+= TEST_2.toString()+ " Result: " +result2+"\n";
		results+= TEST_3.toString()+ " Result: " +result3+"\n";
		((TextView)getView().findViewById(R.id.textView1)).setText(results);
		

	}
	
}
