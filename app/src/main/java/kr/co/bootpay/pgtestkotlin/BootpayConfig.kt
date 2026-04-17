package kr.co.bootpay.pgtestkotlin

/**
 * Bootpay 환경 설정
 *
 * 우선순위: local.properties → BuildConfig 주입 → production fallback
 *
 * 환경 전환 (로컬 테스트):
 *   local.properties 에 `BOOTPAY_ENV=development` 추가 후 build
 *
 * 키 오버라이드 (로컬 테스트):
 *   local.properties 에 `BOOTPAY_ANDROID_APPLICATION_ID_DEV=...` 등 추가
 *
 * 배포 기본값은 항상 production. local.properties는 .gitignore 처리됨.
 */
object BootpayConfig {
    val IS_DEBUG: Boolean get() = BuildConfig.BOOTPAY_ENV == "development"

    // ===== PG API 키 =====
    val applicationId: String get() = BuildConfig.BOOTPAY_APPLICATION_ID

    // ===== REST API 키 (deprecated - EasyPay 용) =====
    val restApplicationId: String get() = BuildConfig.BOOTPAY_REST_APPLICATION_ID
    val privateKey: String get() = BuildConfig.BOOTPAY_PRIVATE_KEY

    // ===== Commerce API 키 =====
    val clientKey: String get() = BuildConfig.BOOTPAY_CLIENT_KEY
}
