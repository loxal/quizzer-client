# Quizzer Android Client

**Support this project**
<!-- BADGES/ -->
<span class="badge-paypal">
<a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&amp;hosted_button_id=MA847TR65D4N2" title="Donate to this project using PayPal">
<img src="https://img.shields.io/badge/paypal-donate-yellow.svg" alt="PayPal Donate"/>
</a></span>
<span class="badge-flattr">
<a href="https://flattr.com/submit/auto?fid=o6ok7n&url=https%3A%2F%2Fgithub.com%2Floxal" title="Donate to this project using Flattr">
<img src="https://img.shields.io/badge/flattr-donate-yellow.svg" alt="Flattr Donate" />
</a></span>
<span class="badge-gratipay"><a href="https://gratipay.com/~loxal" title="Donate weekly to this project using Gratipay">
<img src="https://img.shields.io/badge/gratipay-donate-yellow.svg" alt="Gratipay Donate" />
</a></span>
<!-- /BADGES -->

[Support this work with crypto-currencies like BitCoin, Ethereum, Ardor, and Komodo!](http://me.loxal.net/coin-support.html)


# Create an *A*ndroid Application *P*ac*k*age and upload to Google Play Store

    1. `echo "android_key_password=myPassword" >> ~/.gradle/gradle.properties`
    1. `echo "basic_auth_base64=base64EncodedUsernameAndPasswordForBasicAuthSeparatedViaColon" >> ~/.gradle/gradle.properties`
    1. Create a signature keystore under `../keystore.jks`
    1. ./gradlew clean build
    1. Publish `app/build/outputs/apk/app-release.apk` on [Google Play Store](https://play.google.com/apps/publish)
