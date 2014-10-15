package com.example.intent;

import java.io.ByteArrayOutputStream;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Base64;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

/**
 * This class echoes a string called from JavaScript.
 */
public class Intent extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("browse")) {
            String message = args.getString(0); 
            this.browse(message, callbackContext);
            return true;
        } else if (action.equals("phonenumber")) {
        	this.getPhone(callbackContext);
        	return true;
        } else if (action.equals("imageconvert")) {
        	String message = args.getString(0);
        	this.imageConvert(message,callbackContext);
        	return true;
        }
        return false;
    }

    private void imageConvert(String message, CallbackContext callbackContext) {
        if ((message != null) && (message.length() > 0)) { 
        	byte[] baInput = Base64.decode(message,  Base64.DEFAULT);
        	Bitmap img = BitmapFactory.decodeByteArray(baInput, 0, baInput.length); 
        	
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        	img.compress(Bitmap.CompressFormat.JPEG, 75, baos);   
        	byte[] baOutput = baos.toByteArray(); 
        	
        	String encodedImage = Base64.encodeToString(baOutput, Base64.DEFAULT);
        	
        	callbackContext.success(encodedImage);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
        
    private void getPhone(CallbackContext callbackContext) {    	
    	TelephonyManager telephonyManager = (TelephonyManager)this.cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);        
    	String phoneNumber = telephonyManager.getLine1Number();
    	
    	callbackContext.success(phoneNumber);
    }
    
    private void browse(String message, CallbackContext callbackContext) {
        if ((message != null) && (message.length() > 0)) { 
        	
        	android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
        	
        	intent.setData(Uri.parse(message));
        	
        	this.cordova.getActivity().startActivity(intent);
        	            
        	callbackContext.success("Successful");
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}

