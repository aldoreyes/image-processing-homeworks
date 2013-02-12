package com.aldoreyes.imageprocessing.hw3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aldoreyes.imageprocessing.HomeworkBase;
import com.aldoreyes.imageprocessing.neuralnetwork.spiking.FreqRangeClassifier;
import com.aldoreyes.imageprocessing.neuralnetwork.spiking.SpikingNeuron;
import com.example.imageprocessing.R;

public class Homework3Ex2_b extends HomeworkBase implements OnClickListener {
	
	private static final double[][][] B_CLASSES = new double[][][]{
		{{1,1}, {6,1}, {1,6}, {6,6}},
		{{3,3}, {4,3}, {3,4}, {4,4}}};
	
	
	private static final double [][] TEST_1 = new double[][]{
			{1.1,1.2},{1.1,1},{5.5,5.5}
		};
	
	private static final double [][] TEST_2 = new double[][]{
		{6,6},{5.8,5.5}, {1.2,1}
	};
	
	private static final double [][] TEST_3 = new double[][]{
		{4,3},{3.4,3.9},{3.5,4}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.homework3ex2, container,
				false);
		getSherlockActivity().getSupportActionBar().setTitle("HW3 Exercise 2 - b");

		return toReturn;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		this.getView().findViewById(R.id.button1).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		SpikingNeuron neuron = new SpikingNeuron(new double[]{80, 80}, new FreqRangeClassifier(75, 92));
		neuron.train(B_CLASSES);
		
		int result1  = neuron.classify(TEST_1);
		int result2  = neuron.classify(TEST_2);
		int result3  = neuron.classify(TEST_3);
		
		String results = " Result1: " +result1+"\n";
		results+= " Result2: " +result2+"\n";
		results+= " Result3: " +result3+"\n";
		((TextView)getView().findViewById(R.id.textView1)).setText(results);
		

	}
	
}
