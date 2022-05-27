package kr.co.bootpay.pgtestkotlin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kr.co.bootpay.android.Bootpay
import kr.co.bootpay.android.BootpayAnalytics
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootExtra
import kr.co.bootpay.android.models.BootItem
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.android.models.Payload
import kr.co.bootpay.android.models.statistics.BootStatItem

class MainActivity : AppCompatActivity() {
    private val application_id = "5b8f6a4d396fa665fdc2b5e8" //production
//    private String application_id = "5b9f51264457636ab9a07cdc"; //development

    var context: Context? = null
    var spinner_pg: Spinner? = null
    var spinner_method: Spinner? = null
    var edit_price: EditText? = null
    var edit_non_tax: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        spinner_pg = findViewById(R.id.spinner_pg)
        spinner_method = findViewById(R.id.spinner_method)
        edit_price = findViewById(R.id.edit_price)
        edit_non_tax = findViewById(R.id.edit_non_tax)

        bootpayInit()
    }


    fun bootpayInit() {
        BootpayAnalytics.init(this, application_id) //this는 context
    }

    fun goTraceUser(v: View?) {
        BootpayAnalytics.userTrace(
            "user_1234",  //user_id
            "test1234@gmail.com",  //email
            "홍길동",  //user name
            1,  //성별 남자:1, 여자:0
            "19941014",  //생년월일
            "01012345678",  //고객 전화번호
            "서울" //ex) 서울|인천|대구|광주|부산|울산|경기|강원|충청북도|충북|충청남도|충남|전라북도|전북|전라남도|전남|경상북도|경북|경상남도|경남|제주|세종|대전 중 1
        )
    }

    fun goTracePage(v: View?) {
        //통계용 데이터 추가
        val items: MutableList<BootStatItem> = ArrayList()
        val item1 = BootStatItem().setItemName("마우's 스").setUnique("ITEM_CODE_MOUSE").setPrice(500.0)
        val item2 = BootStatItem().setItemName("키보드").setUnique("ITEM_KEYBOARD_MOUSE").setPrice(500.0)
        items.add(item1)
        items.add(item2)
        BootpayAnalytics.pageTrace(
            "url",  // 앱 페이지 url 또는 화면이름
            "abcde",  // 분류에 해당하지만 아직 기능 미지원
            items //상품정보
        )
    }

    fun goRequest(v: View?) {
        val user = BootUser().setPhone("010-1234-5678") // 구매자 정보
        val extra = BootExtra()
            .setCardQuota("0,2,3") // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
            .setOpenType("popup")
        var price = 1000.0
        try {
            price = edit_price?.text.toString().toDouble()
        } catch (e: Exception) {
        }

        val pg: String = BootpayValueHelper.pgToString(spinner_pg?.selectedItem.toString()) ?: "nicepay"
        val method: String = BootpayValueHelper.methodToString(spinner_method?.selectedItem.toString()) ?: "card"
        val items: MutableList<BootItem> = ArrayList()
        val item1 = BootItem().setName("마우's 스").setId("ITEM_CODE_MOUSE").setQty(1).setPrice(500.0)
        val item2 = BootItem().setName("키보드").setId("ITEM_KEYBOARD_MOUSE").setQty(1).setPrice(500.0)
        items.add(item1)
        items.add(item2)

        val payload = Payload()
        payload.setApplicationId(application_id)
            .setOrderName("부트페이 결제테스트")
            .setPg(pg)
            .setOrderId("1234")
            .setMethod(method)
            .setPrice(price)
            .setUser(user)
            .setExtra(extra).items = items
        val map: MutableMap<String, Any> = HashMap()
        map["1"] = "abcdef"
        map["2"] = "abcdef55"
        map["3"] = 1234
        payload.metadata = map

        Bootpay.init(supportFragmentManager, applicationContext)
            .setPayload(payload)
            .setEventListener(object : BootpayEventListener {
                override fun onCancel(data: String) {
                    Log.d("bootpay", "cancel: $data")
                }

                override fun onError(data: String) {
                    Log.d("bootpay", "error: $data")
                }

                override fun onClose(data: String) {
                    Log.d("bootpay", "close: $data")
                    Bootpay.removePaymentWindow()
                }

                override fun onIssued(data: String) {
                    Log.d("bootpay", "issued: $data")
                }

                override fun onConfirm(data: String): Boolean {
                    Log.d("bootpay", "confirm: $data")
                    //                        Bootpay.transactionConfirm(data); //재고가 있어서 결제를 진행하려 할때 true (방법 1)
                    return true //재고가 있어서 결제를 진행하려 할때 true (방법 2)
                    //                        return false; //결제를 진행하지 않을때 false
                }

                override fun onDone(data: String) {
                    Log.d("done", data)
                }
            }).requestPayment()
    }

    fun goTotalRequest(v: View?) {
        val user = BootUser().setPhone("010-1234-5678") // 구매자 정보
        val extra =
            BootExtra().setCardQuota("0,2,3") // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
        val price = 1000.0
        val items: MutableList<BootItem> = ArrayList()
        val item1 = BootItem().setName("마우's 스").setId("ITEM_CODE_MOUSE").setQty(1).setPrice(500.0)
        val item2 = BootItem().setName("키보드").setId("ITEM_KEYBOARD_MOUSE").setQty(1).setPrice(500.0)
        items.add(item1)
        items.add(item2)
        val payload = Payload()
        val map: MutableMap<String, Any> = HashMap()
        map["1"] = "abcdef"
        map["2"] = "abcdef55"
        map["3"] = 1234
        payload.metadata = map

        payload.setApplicationId(application_id)
            .setOrderName("맥\"북프로's 임다")
            .setOrderId("1234")
            .setPrice(price)
            .setUser(user)
            .setExtra(extra).items = items
        Bootpay.init(supportFragmentManager, applicationContext)
            .setPayload(payload)
            .setEventListener(object : BootpayEventListener {
                override fun onCancel(data: String) {
                    Log.d("cancel", data)
                }

                override fun onError(data: String) {
                    Log.d("error", data)
                }

                override fun onClose(data: String) {
                    Log.d("close", data)
                    Bootpay.dismissWindow()
                }

                override fun onIssued(data: String) {
                    Log.d("issued", data)
                }

                override fun onConfirm(data: String): Boolean {
                    Log.d("confirm", data)
                    //Bootpay.transactionConfirm(data); //재고가 있어서 결제를 진행하려 할때 true (방법 1)
                    return true //재고가 있어서 결제를 진행하려 할때 true (방법 2)
                    //                        return false; //결제를 진행하지 않을때 false
                }

                override fun onDone(data: String) {
                    Log.d("done", data)
                }
            }).requestPayment()
    }


    fun goSubscriptionRequest(v: View?) {
        val user = BootUser().setPhone("010-1234-5678") // 구매자 정보
        val extra = BootExtra()
            .setCardQuota("0,2,3") // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
            .setOpenType("popup")
        val price = 1000.0
        val pg = "nicepay"
        val method = "card_rebill"
        val items: MutableList<BootItem> = ArrayList()
        val item1 = BootItem().setName("마우's 스").setId("ITEM_CODE_MOUSE").setQty(1).setPrice(500.0)
        val item2 = BootItem().setName("키보드").setId("ITEM_KEYBOARD_MOUSE").setQty(1).setPrice(500.0)
        items.add(item1)
        items.add(item2)
        val payload = Payload()
        payload.setApplicationId(application_id)
            .setOrderName("부트페이 결제테스트")
            .setPg(pg)
            .setSubscriptionId("1234") //정기결제용 orderId
            .setMethod(method)
            .setPrice(price)
            .setUser(user)
            .setExtra(extra).items = items
        val map: MutableMap<String, Any> = HashMap()
        map["1"] = "abcdef"
        map["2"] = "abcdef55"
        map["3"] = 1234
        payload.metadata = map

        Bootpay.init(supportFragmentManager, applicationContext)
            .setPayload(payload)
            .setEventListener(object : BootpayEventListener {
                override fun onCancel(data: String) {
                    Log.d("bootpay", "cancel: $data")
                }

                override fun onError(data: String) {
                    Log.d("bootpay", "error: $data")
                }

                override fun onClose(data: String) {
                    Log.d("bootpay", "close: $data")
                    Bootpay.removePaymentWindow()
                }

                override fun onIssued(data: String) {
                    Log.d("bootpay", "issued: $data")
                }

                override fun onConfirm(data: String): Boolean {
                    Log.d("bootpay", "confirm: $data")
                    //                        Bootpay.transactionConfirm(data); //재고가 있어서 결제를 진행하려 할때 true (방법 1)
                    return true //재고가 있어서 결제를 진행하려 할때 true (방법 2)
                    //                        return false; //결제를 진행하지 않을때 false
                }

                override fun onDone(data: String) {
                    Log.d("done", data)
                }
            }).requestSubscription() //정기결제 실행함수
    }


    fun goAuthenticationRequest(v: View?) {
        val user = BootUser().setPhone("010-1234-5678") // 구매자 정보
        val extra = BootExtra()
            .setCardQuota("0,2,3") // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
        val price = 1000.0
        val pg = "danal"
        val method = "auth"
        val items: MutableList<BootItem> = ArrayList()
        val item1 = BootItem().setName("마우's 스").setId("ITEM_CODE_MOUSE").setQty(1).setPrice(500.0)
        val item2 = BootItem().setName("키보드").setId("ITEM_KEYBOARD_MOUSE").setQty(1).setPrice(500.0)
        items.add(item1)
        items.add(item2)

        val payload = Payload()
        payload.setApplicationId(application_id)
            .setOrderName("부트페이 결제테스트")
            .setPg(pg)
            .setAuthenticationId("1234")
            .setMethod(method)
            .setPrice(price)
            .setUser(user)
            .setExtra(extra).items = items

        val map: MutableMap<String, Any> = HashMap()
        map["1"] = "abcdef"
        map["2"] = "abcdef55"
        map["3"] = 1234
        payload.metadata = map

        Bootpay.init(supportFragmentManager, applicationContext)
            .setPayload(payload)
            .setEventListener(object : BootpayEventListener {
                override fun onCancel(data: String) {
                    Log.d("bootpay", "cancel: $data")
                }

                override fun onError(data: String) {
                    Log.d("bootpay", "error: $data")
                }

                override fun onClose(data: String) {
                    Log.d("bootpay", "close: $data")
                    Bootpay.removePaymentWindow()
                }

                //본인인증에선 호출되지 않음
                override fun onIssued(data: String) {
                    Log.d("bootpay", "issued: $data")
                }

                override fun onConfirm(data: String): Boolean {
                    return true //본인인증에선 호출되지 않음
                }

                override fun onDone(data: String) {
                    Log.d("done", data)
                }
            }).requestAuthentication() //본인인증 실행함수
    }
}