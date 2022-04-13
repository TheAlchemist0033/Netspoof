/*
 * This file is part of Network Spoofer for Android.
 * Network Spoofer lets you change websites on other people’s computers
 * from an Android phone.
 * Copyright (C) 2014 Will Shackleton <will@digitalsquid.co.uk>
 *
 * Network Spoofer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Network Spoofer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Network Spoofer, in the file COPYING.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package uk.digitalsquid.netspoofer.spoofs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import uk.digitalsquid.netspoofer.R;
import uk.digitalsquid.netspoofer.YoutubeSelector;
import uk.digitalsquid.netspoofer.proxy.HttpRequest;
import uk.digitalsquid.netspoofer.proxy.HttpResponse;

/**
 * A custom version of the Google spoof which allows the user to enter their own google search query.
 * @author Will Shackleton <will@digitalsquid.co.uk>
 *
 */
public class VideoChange extends Spoof {
    private static final long serialVersionUID = 8490503138296852028L;
    
    private final boolean custom;

    public VideoChange(Context context, boolean custom) {
        super(context.getResources().getString(
                custom ? R.string.spoof_video_custom : R.string.spoof_video),
                context.getResources().getString(
                        custom ? R.string.spoof_video_custom_description :
                            R.string.spoof_video_description));
        this.custom = custom;
    }
    
    private String videoId = "dQw4w9WgXcQ";
    
    @Override
    public Intent activityForResult(Context context) {
        if(!custom) return null;
        return new Intent(context, YoutubeSelector.class);
    }
    
    @Override
    public boolean activityFinished(Context context, Intent result) {
        videoId = result.getStringExtra(YoutubeSelector.CODE);
        if(videoId == null) return false; // No video selected
        else return true;
    }

    @Override
    public Dialog displayExtraDialog(Context context,
            OnExtraDialogDoneListener onDone) {
        return null;
    }

    public void modifyRequest( HttpRequest request) {
        if(!request.getHost().toLowerCase().contains("youtube.com")) return;
        if(!request.getPath().toLowerCase().startsWith("/watch")) return;
        
        Uri uri = request.getUri();
        Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter("v", videoId);
        request.setUri(builder.build());
    }

    @Override
    public void modifyResponse(HttpResponse response, HttpRequest request) {
    }
}
