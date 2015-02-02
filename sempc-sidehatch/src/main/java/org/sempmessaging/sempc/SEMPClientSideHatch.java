package org.sempmessaging.sempc;

import org.sempmessaging.sempc.sidehatch.HttpSideHatch;

public class SEMPClientSideHatch {
	public static void main(String[] args) {
		HttpSideHatch httpSideHatch = new HttpSideHatch();
		SEMPClient.launchWithSideHatch(httpSideHatch, args);
	}
}