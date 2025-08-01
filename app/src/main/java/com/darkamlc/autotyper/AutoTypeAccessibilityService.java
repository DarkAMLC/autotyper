package com.darkamlc.autotyper;
import android.os.Handler;
import android.os.Looper;
import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.LinkedList;
import java.util.Queue;

public class AutoTypeAccessibilityService extends AccessibilityService {

    private static Queue<String> textQueue = new LinkedList<>();
    private static AutoTypeAccessibilityService instance;

    public static void queueInput(String text) {
        textQueue.add(text);
    }

    public static AutoTypeAccessibilityService getInstance() {
        return instance;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Ya no procesamos texto aquí
    }

    public void processNextLine() {
        if (!textQueue.isEmpty()) {
            String line = textQueue.poll();
            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
            if (nodeInfo != null) {
                AccessibilityNodeInfo focusNode = nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
                if (focusNode != null && focusNode.isEditable()) {
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, line
                    );
                    focusNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                }
            }
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public void onInterrupt() {
    }
	public void startTypingFromQueue() {
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
        @Override
        public void run() {
            processNextLine();
            if (!textQueue.isEmpty()) {
                handler.postDelayed(this, 1000);
            }
        }
    });
}

}
