package kr.co.simplebestapp.LolApiTest.service;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TransferService {

    @FormUrlEncoded
    @POST("/m/access.do")
    Call<HashMap> setAccess(@FieldMap HashMap<String, String> param);

    @FormUrlEncoded
    @POST("/m/recommend.do")
    Call<HashMap> setRecommend(@FieldMap HashMap<String, String> param);

    @FormUrlEncoded
    @POST("/m/ad.do")
    Call<HashMap> getAd(@FieldMap HashMap<String, String> param);

    @FormUrlEncoded
    @POST("/m/adClick.do")
    Call<HashMap> setAdClick(@FieldMap HashMap<String, String> param);
}
