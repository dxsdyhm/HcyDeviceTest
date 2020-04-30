package com.hcy.devicetest.testcase;

import com.hcy.devicetest.model.TestResult;

public interface IHandlerCallback {

	public void onMessageHandled(BaseTestCase testcase, TestResult result);
	
}
