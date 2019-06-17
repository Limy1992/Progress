package com.lmy.progress

/**
 * 拼接进度参数
 * CreateDate:2019/6/17
 * Author:lmy
 */
data class MergeAppendProgress(
    /**进度值*/
    var progressValue: Long = 0,
    /**每个音乐的进度条颜色*/
    var progressColor: Int = 0
)