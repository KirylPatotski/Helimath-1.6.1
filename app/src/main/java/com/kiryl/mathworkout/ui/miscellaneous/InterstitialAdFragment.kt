package com.kiryl.mathworkout.ui.miscellaneous

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.ui.start.StartFragment


class InterstitialAdFragment : Fragment() {

    companion object {
        private const val TAG = "non_reward_ad"
        const val AD_UNIT_ID = "ca-app-pub-5382505031931002/1653776996"
    }


    private lateinit var progressBar: ProgressBar

    private var mInterstitialAd: InterstitialAd? = null
    private val adRequest = AdRequest.Builder().build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_ad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progress)
        loadAd(adRequest)
        showAd()
    }


    private fun showAd(){
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }

        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity as MainActivity)
            resume()
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.")
        }
    }

    private fun resume(){
        (activity as MainActivity).openFragment(StartFragment())
    }


    private fun loadAd(adRequest: AdRequest){
        MobileAds.initialize(requireContext()) { }
        InterstitialAd.load(requireContext(), AD_UNIT_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(@NonNull interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    Log.i(TAG, "onAdLoaded")
                    showAd()
                }

                override fun onAdFailedToLoad(@NonNull loadAdError: LoadAdError) {
                    Log.d(TAG, loadAdError.toString())
                    mInterstitialAd = null
                    resume()
                }
            })
    }
}