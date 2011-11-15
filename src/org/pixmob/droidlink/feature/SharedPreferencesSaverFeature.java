/*
 * Copyright (C) 2011 Pixmob (http://github.com/pixmob)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pixmob.droidlink.feature;

import android.content.SharedPreferences;

/**
 * Compatibility interface for saving a {@link SharedPreferences.Editor}
 * instance.
 * @author Pixmob
 */
public interface SharedPreferencesSaverFeature {
    /**
     * Save a {@link SharedPreferences.Editor} instance.
     * @param prefsEditor
     */
    void save(SharedPreferences.Editor prefsEditor);
}