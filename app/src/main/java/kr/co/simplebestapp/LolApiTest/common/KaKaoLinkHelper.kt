package kr.co.simplebestapp.skipjump.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.widget.Toast
import androidx.annotation.Nullable
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import kr.co.simplebestapp.LolApiTest.common.AppPreferences
import kr.co.simplebestapp.LolApiTest.common.Constants
import kr.co.simplebestapp.LolApiTest.interfaces.RecommendResult

class KaKaoLinkHelper {

    companion object {

        val LOGTAG : String = "KaKaoLinkHelper";

        @JvmStatic
        fun kakaoLink(c:Context, @Nullable resultListener: RecommendResult, gb:String) {

            val marketLink = Constants.HOST_URL + "market.jsp?id=" + AppPreferences.getAdIdNo() + "&channel=" + gb + "&package_name=" + c.packageName

            val defaultFeed = FeedTemplate(
                    content = Content(
                            title = AppPreferences.getKakaoTitle(),
                            description = AppPreferences.getKakaoDesc(),
                            imageUrl = AppPreferences.getKakaoImage(),
                            link = Link(
                                    webUrl = marketLink,
                                    mobileWebUrl = marketLink
                            )
                    ),
                    buttons = listOf(
                            Button(
                                    "다운로드",
                                    Link(
                                            //androidExecParams = mapOf("key1" to "value1", "key2" to "value2"),
                                            //iosExecParams = mapOf("key1" to "value1", "key2" to "value2")
                                            webUrl = marketLink,
                                            mobileWebUrl = marketLink
                                    )
                            )
                    )
            )

            // 피드 메시지 보내기
            // 콜백 파라미터 설정
            val serverCallbackArgs = mapOf("ad_id" to AppPreferences.getAdIdNo(), "from_page" to gb)
            //Log.e(LOGTAG, "serverCallbackArgs = " + serverCallbackArgs);

            LinkClient.instance.defaultTemplate(c, defaultFeed, serverCallbackArgs) { linkResult, error ->
                if (error != null) {

                    //Log.e(LOGTAG, "카카오링크 보내기 실패", error)

                    resultListener.onFail(error.message);

                } else if (linkResult != null) {

                    resultListener.onSuccess();

                    //Log.e(LOGTAG, "카카오링크 보내기 성공 ${linkResult.intent}")
                    c.startActivity(linkResult.intent)

                    // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    //Log.w(LOGTAG, "Warning Msg: ${linkResult.warningMsg}")
                    //Log.w(LOGTAG, "Argument Msg: ${linkResult.argumentMsg}")
                }
            }

        }

        @JvmStatic
        fun kakaoChannel(c:Context) {

            val url = TalkApiClient.instance.channelChatUrl("_kPAxeb")

            try {
                // CustomTabs 로 열기
                KakaoCustomTabsClient.openWithDefault(c, url)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(c, "디바이스에 설치된 인터넷 브라우저가 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

}