# DS KUSHWAHA GROUP Android App — Starter Project

## इस ZIP में क्या है
- Android Studio / Kotlin / Jetpack Compose प्रोजेक्ट
- 7 business sections
- Hindi / English toggle
- Product enquiry cart
- Customer name, mobile और delivery address
- WhatsApp order/enquiry to **+91 89339 79022**
- GitHub Actions workflow जो APK build करके downloadable artifact बनाता है

## शामिल business units
1. DS PROPERTIES
2. DS BUILDING MATERIALS
3. DS SOLAR TECHNOLOGY
4. DS SMART GADGETS
5. DS LIFESTYLE BRANDS
6. DS POULTRY FARM
7. DS AQUA

## फोन से APK कैसे बनाएं (GitHub)
1. ZIP extract करके GitHub में नया repository बनाएं।
2. इस project की सभी files repository में upload करें; `.github/workflows/build-apk.yml` भी upload होना चाहिए।
3. GitHub repository खोलें → **Actions** tab → **Build Android APK** → **Run workflow**।
4. Build पूरा होने पर workflow run के नीचे **Artifacts** में `DS-KUSHWAHA-GROUP-debug-apk` मिलेगा। उसे download करके ZIP खोलें; उसमें `app-debug.apk` होगा।
5. APK install करने के लिए Android में उस file को खोलें और जरूरत पड़ने पर “Install unknown apps” की permission दें।

## जरूरी सीमाएं — public launch से पहले
- यह V1 enquiry/order prototype है, Play Store-ready production commerce system नहीं।
- Product names demo categories हैं; वास्तविक product catalogue, images, stock और prices अभी भरने होंगे।
- PhonePe का वास्तविक payment gateway अभी connected नहीं है। इसके लिए PhonePe merchant onboarding और API credentials / approved integration चाहिए। अभी ग्राहक WhatsApp पर order/enquiry भेजता है और आप price/payment confirm कर सकते हैं।
- Single-admin dashboard, cloud database, inventory, order status, shipping integration, OTP login और secure backend अभी शामिल नहीं हैं।
- App package `com.dskushwahagroup.shop`, version `1.0` है।
- Original company logo बाद में app assets में जोड़ा जा सकता है।

## Build
JDK 17 और Gradle 8.7 के साथ:
`gradle assembleDebug`
APK path:
`app/build/outputs/apk/debug/app-debug.apk`
