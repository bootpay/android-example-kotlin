package kr.co.bootpay.pgtestkotlin

import kr.co.bootpay.android.constants.BootpayBuildConfig

/**
 * Bootpay 환경 설정
 *
 * 환경 전환: IS_DEBUG를 true/false로 변경하거나 BootpayBuildConfig.DEBUG 사용
 */
object BootpayConfig {
    // BootpayBuildConfig.DEBUG 기반 자동 전환
    val IS_DEBUG: Boolean get() = BootpayBuildConfig.DEBUG

    // ===== PG API 키 =====
    val applicationId: String
        get() = if (IS_DEBUG) "5b9f51264457636ab9a07cdc" else "5b8f6a4d396fa665fdc2b5e8"

    // ===== REST API 키 (deprecated - EasyPay 용) =====
    val restApplicationId: String
        get() = if (IS_DEBUG) "59b731f084382614ebf72215" else "5b8f6a4d396fa665fdc2b5ea"

    val privateKey: String
        get() = if (IS_DEBUG) "WwDv0UjfwFa04wYG0LJZZv1xwraQnlhnHE375n52X0U=" else "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw="

    // ===== Commerce API 키 =====
    val clientKey: String
        get() = if (IS_DEBUG) "hxS-Up--5RvT6oU6QJE0JA" else "sEN72kYZBiyMNytA8nUGxQ"
}
