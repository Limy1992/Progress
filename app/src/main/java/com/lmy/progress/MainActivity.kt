package com.lmy.progress

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mergeAppendProgressList = ArrayList<MergeAppendProgress>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mergeAppendProgressList.add(MergeAppendProgress(3000, Color.BLUE))
        mergeAppendProgressList.add(MergeAppendProgress(6000, Color.YELLOW))
        seekBarProgress.setProgressMaxValue(9000L)
        seekBarProgress.setProgressModel(mergeAppendProgressList)
        seekBarProgress.setOnSeekBarMoreProgressListener(object : SeekBarMoreProgress.OnSeekBarMoreProgressListener{
            override fun onProgressChanged(seekBar: SeekBarMoreProgress, progress: Long) {
                progressText.text = progress.toString()
            }
        })
    }
}
