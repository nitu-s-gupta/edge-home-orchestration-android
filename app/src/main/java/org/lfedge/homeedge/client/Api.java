/****************************************************************************
 Copyright 2022 Samsung Electronics All Rights Reserved.

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

package org.lfedge.homeedge.client;

import com.google.gson.JsonObject;
import org.lfedge.homeedge.server.ServiceInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {
    @GET
    Call<Response.OrchestrationResponse> getOrchestrationInfo(@Url String url,@Query("id") String deviceID);
    @POST
    Call<Response.ScoreResponse> getScoreInfo(@Url String url,@Body JsonObject device, @Query("id") String deviceId);
    @POST
    Call<Response.ServiceResponse> ExecuteService(@Url String url,@Body JsonObject Servicedata, @Query("Object")ServiceInfo serviceInfo);
}