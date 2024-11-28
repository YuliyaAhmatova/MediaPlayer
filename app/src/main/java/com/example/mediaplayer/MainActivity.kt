package com.example.mediaplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediaplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private var songList: MutableList<SongModal> = mutableListOf(
        SongModal(R.raw.one, R.drawable.one),
        SongModal(R.raw.two, R.drawable.two),
        SongModal(R.raw.three, R.drawable.three),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var index = 0

        startPlayer(index)

        binding.previousFAB.setOnClickListener {
            if (index > 0) {
                index -= 1
            } else if (index == 0) {
                index = songList.size - 1
            }
            if (mediaPlayer != null) {
                mediaPlayer?.stop()
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
            }
            startPlayer(index)
        }

        binding.nextFAB.setOnClickListener {
            if (index < songList.size - 1) {
                index += 1
            } else if (index == songList.size - 1) {
                index = 0
            }
            if (mediaPlayer != null) {
                mediaPlayer?.stop()
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
            }
            startPlayer(index)
        }

        binding.volumeSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mediaPlayer?.setVolume(progress.toFloat() / 100, progress.toFloat() / 100)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun startPlayer(index: Int) {
        binding.imageView.setImageResource(songList[index].image)
        playSound(songList[index])
    }

    private fun playSound(song: SongModal) {
        binding.playFAB.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, song.song)
                initializeSeekbar()
            }
            mediaPlayer?.start()
        }
        binding.pauseFAB.setOnClickListener {
            if (mediaPlayer != null) mediaPlayer?.pause()
        }
        binding.stopFAB.setOnClickListener {
            if (mediaPlayer != null) {
                mediaPlayer?.stop()
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }
        binding.seekbarSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun initializeSeekbar() {
        binding.seekbarSB.max = mediaPlayer!!.duration
        val handler = android.os.Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekbarSB.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    binding.seekbarSB.progress = 0
                }
            }
        }, 0)
    }
}


