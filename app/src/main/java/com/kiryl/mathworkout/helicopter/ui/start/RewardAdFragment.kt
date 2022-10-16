package com.kiryl.mathworkout.helicopter.ui.start

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.kiryl.mathworkout.MainActivity
import com.kiryl.mathworkout.R
import com.kiryl.mathworkout.helicopter.mechanics.GameData
import com.kiryl.mathworkout.services.data.pref.AppData


class RewardAdFragment : Fragment() {

    companion object {
        private const val TAG = "reward_Ad"
        const val AD_UNIT_ID = "ca-app-pub-5382505031931002/3594093368"
        const val TOKENS_TO_ADD = 1
    }

    private var mRewardedAd: RewardedAd? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_ad, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adRequest = AdRequest.Builder().build()
        loadAd(adRequest)
    }

    private fun loadAd(adRequest: AdRequest){
        RewardedAd.load(requireContext(), AD_UNIT_ID, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                mRewardedAd = null
                resume()
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                mRewardedAd = rewardedAd
                showAd()
            }
        })
    }

    private fun showAd() {
        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mRewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mRewardedAd = null
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }

        if (mRewardedAd != null) {
            mRewardedAd?.show((activity as MainActivity)) {

                "You can also write comments like this"
                val saveData = AppData(requireContext())
                var a = saveData.getAnyInt(GameData.REVIVE_TOKENS_PREF_KEY)
                a += TOKENS_TO_ADD
                saveData.setAnyInt(GameData.REVIVE_TOKENS_PREF_KEY,a)
                resume()
                Toast.makeText(requireContext(), "+${TOKENS_TO_ADD}💎", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "User earned the reward.")
            }
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }
    }

    private fun resume(){
        (activity as MainActivity).openFragment(HelicopterStartFragment(),true)
    }
}