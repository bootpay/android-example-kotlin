package kr.co.bootpay.pgtestkotlin

class BootpayValueHelper {

    companion object {
        fun pgToString(key: String?): String? {
            when (key) {
                "KCP" -> return "kcp"
                "다날" -> return "danal"
                "LGU+" -> return "lgup"
                "이니시스" -> return "inicis"
                "유디페이" -> return "udpay"
                "나이스페이" -> return "nicepay"
                "네이버페이" -> return "npay"
                "페이앱" -> return "payapp"
                "카카오페이" -> return "kakao"
                "TPAY" -> return "tpay"
                "페이레터" -> return "payletter"
                "KICC" -> return "easypay"
                "웰컴페이먼츠" -> return "welcome"
            }
            return ""
        }

        fun methodToString(key: String?): String? {
            when (key) {
                "카드결제" -> return "card"
                "휴대폰소액결제" -> return "phone"
                "가상계좌" -> return "vbank"
                "계좌이체" -> return "bank"
                "카드정기결제" -> return "card_rebill"
                "간편결제" -> return ""
                "주문결제" -> return ""
                "부트페이 간편결제" -> return ""
                "본인인증" -> return "auth"
                "카카오페이" -> return "kakao"
                "네이버페이" -> return "npay"
                "페이코" -> return "payco"
            }
            return ""
        }
    }
}