package com.some.studychats.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GroupApiService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA1jDNN4E:APA91bFi1PVgcEtDR2Qe6fiHlm3Ol7MFntuuEyKpzHwl6gT25vm_cUX9ZH9jOf16oxvNKsQhwsSwvPw0HEGh47qlrjQ6cBfOO1Wv9MN-5YJgPMxaw7FAtvffYei-RuIbVYi1H1-nuuC3"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendGroupNotification(@Body Sender body);

}
