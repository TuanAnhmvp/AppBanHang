package com.tashop.appbanhang.retrofit;

import com.tashop.appbanhang.model.NotiResponse;
import com.tashop.appbanhang.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNofication {
    @Headers(
            {
                    "content-type: application/json",
                    "authorization: key=AAAAYMoCILQ:APA91bFem4vOgSYs-Sl0weW-M6hD80XSYFB5dSkUGrn_aQ2c9TeCEUlAwTjGQY9gwS4cf94AX4t4bzTZKOt-SWxWkuK8IGEpX-hIaq964-g_e28FxFRo33IIX42-28PKKDoR7s0ndAIp"
            }
    )
    @POST("fcm/send")
    Observable<NotiResponse> sendNofitication(@Body NotiSendData data);
}
