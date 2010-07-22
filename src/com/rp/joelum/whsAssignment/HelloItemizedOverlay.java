package com.rp.joelum.whsAssignment;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	//Define marker picture and which class
	public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
	  final OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  
      dialog.setPositiveButton("Feedback", new DialogInterface.OnClickListener() {
    	  
          //Do something when the button is clicked
          public void onClick(DialogInterface arg0, int arg1) {
          	Intent intentNewAct = new Intent(mContext, feedbackActivity.class);
        	
        	intentNewAct.putExtra("whsName", item.getTitle());
        	
        	mContext.startActivity(intentNewAct);
          }
      });

      //Set a negative/no button and create a listener
      dialog.setNegativeButton("Cancel", null);
	  
	  dialog.show();
	  
	  return true;
	}

}
