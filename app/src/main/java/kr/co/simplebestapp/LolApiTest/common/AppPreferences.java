package kr.co.simplebestapp.LolApiTest.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.co.simplebestapp.LolApiTest.DefaultApplication;
import kr.co.simplebestapp.LolApiTest.models.sharedpreferences.History;

public class AppPreferences {

    private static final String APP_SHARED_PREFS = "kr.co.simplebestapp.defaultpack.preference"; //  Name of the file -.xml

    private static Context getContext() {
        return DefaultApplication.mInstance;
    }

    private static SharedPreferences getAppPref() {
        return getContext().getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
    }

    /**
     * 첫실행
     **/
    public static boolean isFirstRun() {
        return getAppPref().getBoolean("FirstRun", true);
    }

    public static void setFirstRun(boolean FirstRun) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putBoolean("FirstRun", FirstRun);
        //editor.commit();
        editor.apply();
    }
    /** 첫실행 끝 **/

    /**
     * 추천회원
     */
    public static String getRecommendMember() {
        return getAppPref().getString("RecommendMember", "");
    }

    public static void setRecommendMember(String RecommendMember) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("RecommendMember", RecommendMember + "");
        editor.apply();
    }
    /** 추천회원 끝 **/

    /**
     * 추천채널
     */
    public static String getRecommendChannel() {
        return getAppPref().getString("RecommendChannel", "");
    }

    public static void setRecommendChannel(String RecommendChannel) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("RecommendChannel", RecommendChannel + "");
        editor.apply();
    }
    /** 추천채널 끝 **/

    /**
     * FCM Token
     */
    public static String getFcmToken() {
        return getAppPref().getString("FcmToken", "");
    }

    public static void setFcmToken(String FcmToken) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("FcmToken", FcmToken + "");
        editor.apply();
    }

    public static String getNewFcmToken() {
        return getAppPref().getString("NewFcmToken", "");
    }

    public static void setNewFcmToken(String NewFcmToken) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("NewFcmToken", NewFcmToken + "");
        editor.apply();
    }
    /** FCM Token 끝 **/

    /**
     * 튜토리얼
     **/
    public static boolean isTutorialYn() {
        return getAppPref().getBoolean("TutorialYn", false);
    }

    public static void setTutorialYn(boolean TutorialYn) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putBoolean("TutorialYn", TutorialYn);
        //editor.commit();
        editor.apply();
    }
    /** 튜토리얼 끝 **/

    /**
     * 버전
     **/
    public static String getVersion() {
        return getAppPref().getString("Version", "");
    }

    public static void setVersion(String Version) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("Version", Version + "");
        //editor.commit();
        editor.apply();
    }
    /** 버전 끝 **/

    /**
     * 광고아이디
     */
    public static String getAdIdNo() {
        return getAppPref().getString("AdIdNo", "");
    }

    public static void setAdIdNo(String AdIdNo) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("AdIdNo", AdIdNo + "");
        editor.apply();
    }
    /** 광고아이디 끝 **/

    /**
     * 카카오톡 링크
     **/
    public static String getKakaoTitle() {
        return getAppPref().getString("KakaoTitle", "우리, 친구해요~");
    }

    public static void setKakaoTitle(String KakaoTitle) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("KakaoTitle", KakaoTitle);
        //editor.commit();
        editor.apply();
    }
    public static String getKakaoDesc() {
        return getAppPref().getString("KakaoDesc", "광고 NO! 무제한 다운 받기 팁!\n" +
                "슬기로운 찐친구 인지 공유하기~♥\n");
    }

    public static void setKakaoDesc(String KakaoDesc) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("KakaoDesc", KakaoDesc);
        //editor.commit();
        editor.apply();
    }
    public static String getKakaoImage() {
        return getAppPref().getString("KakaoImage", Constants.HOST_URL + "tutorial/img/share.jpg");
    }

    public static void setKakaoImage(String KakaoImage) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("KakaoImage", KakaoImage);
        //editor.commit();
        editor.apply();
    }
    /** 카카오톡 링크 끝 **/

    /**
     * 공유유도 내용
     **/
    public static String getShareDesc() {
        return getAppPref().getString("ShareDesc", "무제한 이용하기 응답하라! 친구야! 공유하기~♥");
    }

    public static void setShareDesc(String ShareDesc) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("ShareDesc", ShareDesc);
        //editor.commit();
        editor.apply();
    }
    /** 공유유도 내용 끝 **/

    /**
     * 노티바사용
     **/
    public static boolean isNotiYn() {
        return getAppPref().getBoolean("NotiYn", true);
    }

    public static void setNotiYn(boolean NotiYn) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putBoolean("NotiYn", NotiYn);
        //editor.commit();
        editor.apply();
    }
    /** 노티바사용 끝 **/

    /**
     * 테스트 디바이스
     **/
    public static String getTestDevices() {
        return getAppPref().getString("TestDevices", "");
    }

    public static void setTestDevices(String TestDevices) {
        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("TestDevices", TestDevices);
        //editor.commit();
        editor.apply();
    }
    /** 테스트 디바이스 끝 **/

    public static void setHistoryInfo(History history){
        ArrayList<History> historyInfo = getHistoryInfo();
        historyInfo.add(history);
        Gson gson = new Gson();

        String jsonString = gson.toJson(historyInfo);

        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("HistoryInfo", jsonString);
        editor.apply();
    }

    public static ArrayList<History> getHistoryInfo(){
        String jsonString = getAppPref().getString("HistoryInfo", "");
        if(TextUtils.isEmpty(jsonString)) return new ArrayList<History>();

        Gson gson = new Gson();
        ArrayList<History> historyInfo = gson.fromJson(jsonString, new TypeToken<ArrayList<History>>() {
        }.getType());

        return historyInfo;
    }

    public static void removeHistoryInfo(int index){
        List<History> historyInfo = getHistoryInfo();
        historyInfo.remove(index);

        Gson gson = new Gson();
        String jsonString = gson.toJson(historyInfo);

        SharedPreferences.Editor editor = getAppPref().edit();
        editor.putString("HistoryInfo", jsonString);
        editor.apply();
    }
}
