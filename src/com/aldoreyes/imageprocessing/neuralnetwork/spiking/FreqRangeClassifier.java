package com.aldoreyes.imageprocessing.neuralnetwork.spiking;

import android.util.Log;

public class FreqRangeClassifier implements ISpikingClassifier {

	private double _maxLimit;
	private double _minLimit;


	public FreqRangeClassifier(double minLimit, double maxLimit){
		_minLimit = minLimit;
		_maxLimit = maxLimit;
	}
	
	
	@Override
	public int classify(double[] freqMap, double freq) {
		Log.d("ip", "resFreq: "+freq);
		return freq > _minLimit && freq < _maxLimit?1:0;

	}

}
