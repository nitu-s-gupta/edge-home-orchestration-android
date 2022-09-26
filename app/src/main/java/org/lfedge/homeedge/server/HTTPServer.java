/****************************************************************************
 Copyright 2022-2023 Samsung Electronics All Rights Reserved.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */

package org.lfedge.homeedge.server;

import android.util.Log;

import org.lfedge.homeedge.common.Utils;
import org.lfedge.homeedge.orchestrationapi.ExternalHandler;
import org.lfedge.homeedge.orchestrationapi.InternalHandler;

import fi.iki.elonen.router.RouterNanoHTTPD;

public class HTTPServer extends RouterNanoHTTPD {
    public static final String TAG=HTTPServer.class.getSimpleName();
    private String deviceIP;
    int mListeningPort;

    //set the IP and port for Server Creation
    public HTTPServer(String hostname, int port) {
        super(hostname, port);
        mListeningPort = port;
        setDeviceIP(hostname);
        addMappings();
        Log.d(TAG,"Setting the hostname as " +hostname + "port to : "+port);
    }
    //set the IP and port for Server Creation
    public HTTPServer(int port) {
        super(port);
        setDeviceIP(null);
        addMappings();
    }
    @Override
    public void addMappings() {
        Log.d(TAG,"Routes are added");
        if(mListeningPort == Utils.API.INTERNAL_PORT) {
            addRoute(Utils.API.ROOT_PATH + Utils.API.PING_REQUEST, InternalHandler.PingHandler.class);
            addRoute(Utils.API.ROOT_PATH + Utils.API.NOTIFICATION_REQUEST, InternalHandler.NotifyHandler.class);
        }
        else if (mListeningPort == Utils.API.EXTERNAL_PORT)
            addRoute(Utils.API.ROOT_PATH+Utils.API.SERVICE_REQUEST, ExternalHandler.PostServiceHandler.class);
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public String getDeviceIP() {
        return deviceIP;
    }
}
