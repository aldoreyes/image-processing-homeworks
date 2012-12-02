package com.example.imageprocessing.neuralnetwork.spiking;

public interface ISpikingClassifier {
	int classify(double[] freqMap, double freq);
}
