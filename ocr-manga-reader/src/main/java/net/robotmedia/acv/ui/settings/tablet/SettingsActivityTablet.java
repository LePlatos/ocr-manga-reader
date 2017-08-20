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
package net.robotmedia.acv.ui.settings.tablet;

import java.util.List;

import android.annotation.SuppressLint;
import android.preference.PreferenceActivity;

import com.cb4960.ocrmr.R;

public class SettingsActivityTablet extends PreferenceActivity
{
  private List<Header> headers;


  @SuppressLint("NewApi")
  @Override
  public void onBuildHeaders(List<Header> target)
  {
    loadHeadersFromResource(R.xml.preference_headers, target);
    headers = target;
  }


  @Override
  public Header onGetNewHeader()
  {
    return bug22430Workaround();
  }


  /**
   * @see <a
   *      href="http://code.google.com/p/android/issues/detail?id=22430">http://code.google.com/p/android/issues/detail?id=22430</a>
   * @see {@link PremiumSettingsFragment#onPremiumPurchased()}
   * @return
   */
  private Header bug22430Workaround()
  {
    return headers.get(0);
  }


  // Android 4.4 now needs to override this to prevent exception.
  @Override
  protected boolean isValidFragment(String fragmentName)
  {
    return true;
  }

}
