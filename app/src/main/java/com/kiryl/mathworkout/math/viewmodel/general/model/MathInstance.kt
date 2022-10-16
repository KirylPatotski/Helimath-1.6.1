package com.kiryl.mathworkout.math.viewmodel.general.model

class MathInstance(
    private var mScore: Int,
    private var mHearts: Int,
    private val mRandomShuffle: Boolean,
    private val mInfinitePlay:Boolean,
    private var mTimeUsed: Long = 0,

    private var mTotalAnswers: Int = 0,
    private var mAdditionUsed: Int = 0,
    private var mSubtractionUsed: Int = 0,
    private var mMultiplicationUsed: Int = 0,
    private var mDivisionUsed: Int = 0,
    private var mPowerUsed: Int = 0,
    private var mLogarithmUsed: Int = 0,
) {

    var score: Int
        get() = this.mScore
        set(value) {
            this.mScore = value
        }

    var hearts: Int
        get() = this.mHearts
        set(value) {
            this.mHearts = value
        }

    val randomShuffle: Boolean
        get() = this.mRandomShuffle


    val infinitePlay: Boolean
        get() = this.mInfinitePlay

    var additionUsed: Int
        get() = this.mAdditionUsed
        set(value) {
            this.mAdditionUsed = value
        }

    var subtractionUsed: Int
        get() = this.mSubtractionUsed
        set(value) {
            this.mSubtractionUsed = value
        }

    var multiplicationUsed: Int
        get() = this.mMultiplicationUsed
        set(value) {
            this.mMultiplicationUsed = value
        }

    var divisionUsed: Int
        get() = this.mDivisionUsed
        set(value) {
            this.mDivisionUsed = value
        }

    var powerUsed: Int
        get() = this.mPowerUsed
        set(value) {
            this.mPowerUsed = value
        }

    var logarithmUsed: Int
        get() = this.mLogarithmUsed
        set(value) {
            this.mLogarithmUsed = value
        }

    var totalAnswers:Int
    get() = this.mTotalAnswers
        set(value) {
            this.mTotalAnswers = value
        }


    var startTime:Long
        get() = this.mTimeUsed
        set(value) {
            this.mTimeUsed = value
        }


}