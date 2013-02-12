package com.aldoreyes.imageprocessing.neuralnetwork.spiking;

public interface ISpikingClassifier {
	int classify(double[] freqMap, double freq);
}
