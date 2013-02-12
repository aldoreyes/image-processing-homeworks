package com.aldoreyes.imageprocessing.neuralnetwork.spiking;

import com.aldoreyes.imageprocessing.util.ArrayUtils;

import android.util.Log;

public class SpikingNeuron {
	private final double C = 100.0;
    private final double VR = -60.0;
    private final double VT = -40.0;
    private final double V_PEAK = 35.0;
    private final double K = 0.7;
    private final double A = 0.03;
    private final double B = -2;
    private final double Cc = -50.0;
    private final double D = 100.0;
    private final double T = 1000;
    private final double theta = 50;
    private final double TAU = 1;
    private final int N = (int) Math.round(T/TAU);
    
    private double[] _weights;
    private int _classesCount;
    private double[] _classesFreq;
	private ISpikingClassifier _classifier;
    
    public SpikingNeuron(){
    	this(new double[]{50,200}, new FreqDistanceClassifier());
    }
    
    public SpikingNeuron(double[] weights){
    	this(new double[]{50,200}, new FreqDistanceClassifier());
    }
    
    public SpikingNeuron(double[] weights, ISpikingClassifier classifier){
    	_weights = weights;
    	_classifier = classifier;

    	
    	
    }
    
    private double calculateCurrent(double[] inVector) {
		double toReturn = 0;
		
		toReturn = ArrayUtils.dotProd(inVector, _weights);
		toReturn += theta;
		return toReturn;
	}
    
    private double calculateFrequency(double current) {
    	double toReturn = 0;
		double[] v = new double[N];
		double[] u = new double[N];
		
		//initialize vectors
		for (int i = 0; i < N; i++) {
			v[i] = VR;
			u[i] = 0;
		}
		
		double vi, ui;
		for (int i = 0; i < N-1; i++) {
			vi = v[i];
			ui = u[i];
			v[i+1] = vi + TAU * (K * (vi-VR) * (vi-VT) - u[i] + current) / C;
			u[i+1] = ui + TAU * A * (B * (vi-VR) -ui);
			if(v[i+1] >= V_PEAK){
				toReturn++;
				v[i] = V_PEAK;
				v[i+1] = Cc;
				u[i+1] += D;
			}
			
		}
		
		return toReturn;
	}
    
    public void train(double[][][] classes){
    	_classesCount = classes.length;
    	_classesFreq = new double[_classesCount];
    	for(int i = 0; i<_classesCount; i++){
    		double[][] classIn = classes[i];
    		double frequency = 0;
    		int classLength = classIn.length;
    		double current, itemFreq;
    		for (int j = 0; j < classLength; j++) {
				current = calculateCurrent(classIn[j]);
    			itemFreq = calculateFrequency(current);
				frequency += itemFreq;
				Log.d("ip", "itemFreq["+i+","+j+"]="+itemFreq);
			}
    		
    		//use average for frequency
    		_classesFreq[i] = frequency/classLength;
    		Log.d("ip", i+": "+(frequency/classLength)+"");
    	}
    	
    }

	public int classify(double[][] input){
		int toReturn = -1;
		
		int inLength = input.length;
		double frequency = 0;
		double current;
		for (int j = 0; j < inLength; j++) {
			current = calculateCurrent(input[j]);
			frequency += calculateFrequency(current);
		}
		
		//use average for frequency
		frequency = frequency/inLength;
		toReturn = _classifier.classify(_classesFreq, frequency);
		return toReturn;
	}

	
}
