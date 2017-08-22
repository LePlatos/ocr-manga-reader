/*******************************************************************************
 * Copyright 2009 Robot Media SL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.robotmedia.acv.ui;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.cb4960.ocrmr.R;
import net.robotmedia.acv.utils.BuildUtils;

import java.util.HashSet;

public class ExtendedActivity extends Activity
{

  public void setCanBeKilledByChild(boolean value)
  {
    this.canBeKilledByChild = value;
  }

  private final static int RESULT_DIE = 777;
  private final static int RESULT_KAMIKAZE = 888;

  private boolean canBeKilledByChild = true;

  private HashSet<AsyncTask<?, ?, ?>> mTasks = new HashSet<AsyncTask<?, ?, ?>>();

  protected ActionBar mActionBar;
  protected Runnable mHideActionBarRunnable = new Runnable()
  {
    @Override
    public void run()
    {
      if (isHoneyComb())
      {
        new ActionBarHelper().hide();
      }
    }
  };
  protected Handler mHandler;


  @Override
  public void onStart()
  {
    super.onStart();
  }


  @Override
  public void onStop()
  {
    super.onStop();
  }


  @Override
  protected void onDestroy()
  {
    super.onDestroy();

    for (AsyncTask<?, ?, ?> task : mTasks)
    {
      if (task.getStatus() != AsyncTask.Status.FINISHED)
      {
        task.cancel(true);
      }
    }

  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    
    if (canBeKilledByChild)
    {
      if (resultCode == RESULT_DIE)
      {
        finish();
      }
      else if (resultCode == RESULT_KAMIKAZE)
      {
        setResult(RESULT_KAMIKAZE);
        finish();
      }
    }
  }


  @Override
  protected void onPostCreate(Bundle savedInstanceState)
  {
    super.onPostCreate(savedInstanceState);

    Looper looper = Looper.getMainLooper();
    mHandler = new Handler(looper);

    if (isHoneyComb())
    {
      setupActionBar();
      hideActionBar();
    }
  }


  protected void setupActionBar()
  {
    if (isHoneyComb())
    {
      new ActionBarHelper().setup();
    }
  }


  protected void showActionBar()
  {
    if (isHoneyComb() && !isKitKat())
    {
      new ActionBarHelper().show();
    }
  }


  protected void hideActionBar()
  {
    if (isHoneyComb() && !isKitKat())
    {
      new ActionBarHelper().hide();
    }
  }


  protected void hideActionBarDelayed()
  {
    if (isHoneyComb() && !isKitKat())
    {
      mHandler.removeCallbacks(mHideActionBarRunnable);
      mHandler.postDelayed(mHideActionBarRunnable, 7000);
    }
  }


  // Return true if the action bar ends up being shown
  protected boolean toggleControls()
  {
    if (isHoneyComb())
    {
      mHandler.removeCallbacks(mHideActionBarRunnable);
      if (new ActionBarHelper().isShowing())
      {
        new ActionBarHelper().hide();
      }
      else
      {
        new ActionBarHelper().show();
        return true;
      }
    }
    return false;
  }

  protected boolean isIcecream()
  {
    return BuildUtils.isIceCreamSandwichOrLater();
  }

  protected boolean isHoneyComb()
  {
    return BuildUtils.isHoneycombOrLater();
  }
  
  protected boolean isKitKat()
  {
    return BuildUtils.isKitKatOrLater();
  }

  protected void setImmersiveMode(boolean b)
  {
    if (!isKitKat()) return;
    if(b) {
      getWindow().getDecorView().setSystemUiVisibility(
          View.SYSTEM_UI_FLAG_LAYOUT_STABLE 
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_FULLSCREEN 
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    } else {
      int flags = getWindow().getDecorView().getSystemUiVisibility();

      flags &= (~View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
      flags &= (~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
      flags &= (~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
      flags &= (~View.SYSTEM_UI_FLAG_FULLSCREEN);
      getWindow().getDecorView().setSystemUiVisibility(flags);
    }
  }
  
  protected class ActionBarHelper
  {

    @SuppressLint("NewApi")
    public void setup()
    {
      ActionBar actionBar = getActionBar();

      if (actionBar != null)
      {
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(false);
      }
    }


    @SuppressLint("NewApi")
    public void hide()
    {
      if (getActionBar() != null)
      {
        getActionBar().hide();
      }
    }


    @SuppressLint("NewApi")
    public void show()
    {
      if (getActionBar() != null)
          getActionBar().show();
    }


    @SuppressLint("NewApi")
    public boolean isShowing()
    {
      if (getActionBar() == null)
      {
        return false;
      }
      return getActionBar().isShowing();
    }
  }

  protected class MenuHelper
  {
    @SuppressLint("NewApi")
    public void invalidateOptionsMenu()
    {
      ExtendedActivity.this.invalidateOptionsMenu();
    }
  }
}
