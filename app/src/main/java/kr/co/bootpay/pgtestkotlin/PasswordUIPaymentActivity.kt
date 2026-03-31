package kr.co.bootpay.pgtestkotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootExtra
import kr.co.bootpay.android.models.BootItem
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.bio.BootpayBio
import kr.co.bootpay.bio.models.BioExtra
import kr.co.bootpay.bio.models.BioPayload
import kr.co.bootpay.bio.models.BioPrice
import kr.co.bootpay.pgtestkotlin.deprecated.BootpayRest
import kr.co.bootpay.pgtestkotlin.deprecated.BootpayRestImplement
import kr.co.bootpay.pgtestkotlin.deprecated.EasyPayUserTokenData
import kr.co.bootpay.pgtestkotlin.deprecated.TokenData
import java.util.*

class PasswordUIPaymentActivity: AppCompatActivity(), BootpayRestImplement {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_ui_payment)
    }

    fun PaymentTest(v: View?) {
        BootpayRest.getRestToken(this, this, BootpayConfig.restApplicationId, BootpayConfig.privateKey)
    }

    //1: 남자, 0: 여자
    val bootUser: BootUser
    get() {
        val userId = "123411aaaaaaaaaaaabd4ss121"
        val user = BootUser()
        user.id = userId
        user.area = "서울"
        user.gender = 1 //1: 남자, 0: 여자
        user.email = "test1234@gmail.com"
        user.phone = "010-1234-4567"
        user.birth = "1988-06-10"
        user.username = "홍길동"
        return user
    }

    fun BootpayTest(userToken: String?) {
        val user = BootUser().setPhone("010-1234-5678") // 구매자 정보
        val extra = BioExtra()
        val items: MutableList<BootItem> = ArrayList()
        val item1 = BootItem().setName("마우's 스").setId("ITEM_CODE_MOUSE").setQty(1).setPrice(500.0)
        val item2 = BootItem().setName("키보드").setId("ITEM_KEYBOARD_MOUSE").setQty(1).setPrice(500.0)
        items.add(item1)
        items.add(item2)
        val payload = BioPayload()
        // client_key가 설정되면 application_id 대신 client_key로 인증됩니다
        payload.setApplicationId(BootpayConfig.applicationId)
            .setClientKey(BootpayConfig.clientKey)
            .setOrderName("부트페이 결제테스트")
            .setPg("나이스페이")
            .setOrderId("1234")
            .setOrderName("플리츠레이어 카라숏원피스")
            .setUserToken(userToken)
            .setPrice(1000.0)
            .setUser(user)
            .setExtra(extra)
            .setItems(items)
            .setNames(Arrays.asList("블랙 (COLOR)", "55 (SIZE)")).prices =
            Arrays.asList(
                BioPrice("상품가격", 89000.0),  //결제창에 나타날 가격목록
                BioPrice("쿠폰적용", -25000.0),
                BioPrice("배송비", 2500.0)
            )

//        Map<String, Object> map = new HashMap<>();
//        map.put("1", "abcdef");
//        map.put("2", "abcdef55");
//        map.put("3", 1234);
//        payload.setMetadata(map);
//        payload.setMetadata(new Gson().toJson(map));
        BootpayBio.init(applicationContext)
            .setBioPayload(payload)
            .setEventListener(object : BootpayEventListener {
                override fun onCancel(data: String) {
                    Log.d("bootpay", "cancel: $data")
                }

                override fun onError(data: String) {
                    Log.d("bootpay", "error: $data")
                }

                override fun onClose() {
                    BootpayBio.removePaymentWindow()
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
            }).requestPassword()
    }

    override fun callbackRestToken(token: TokenData?) {
        BootpayRest.getEasyPayUserToken(this, this, token?.access_token, bootUser)
    }

    override fun callbackEasyPayUserToken(userToken: EasyPayUserTokenData?) {
        BootpayTest(userToken?.user_token)
    }
}
