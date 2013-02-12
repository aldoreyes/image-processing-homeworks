package com.aldoreyes.imageprocessing.neuralnetwork.spiking;

public class FreqDistanceClassifier implements ISpikingClassifier {

	@Override
	public int classify(double[] freqMap, double freq) {
		int l = freqMap.length;
		double currDistance = Double.MAX_VALUE, diff;
		int toReturn = -1;
		for (int i = 0; i < l; i++) {
			diff = Math.abs(freqMap[i] - freq);
			if(diff < currDistance){
				toReturn = i;
				currDistance = diff;
			}
		}
		
		return toReturn;

	}

}
