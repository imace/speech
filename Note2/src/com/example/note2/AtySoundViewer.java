package com.example.note2;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class AtySoundViewer extends Activity {
	/** Called when the activity is first created. */

	private SeekBar skb_audio = null;
	private Button btn_start_audio = null;
	private Button btn_stop_audio = null;

	// private boolean ifPlay = false;

	private MediaPlayer m = null;

	public static final String EXTRA_PATH = "path";

	// private String mp3Path =
	// "/storage/emulated/0/kgmusic/download/libai.mp3";

	private boolean ifPlay = false;
	private boolean ifFirst = false; // 设置暂停布尔值

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.aty_sound_viewer);

		// ----------Media控件设置---------//
		m = new MediaPlayer();

		// 播放结束后释放MediaPlayer
		m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				// Toast.makeText(Mplayer.this, "结束", Toast.LENGTH_LONG).show();
				m.release();
			}
		});

		btn_start_audio = (Button) this.findViewById(R.id.Button01);
		btn_stop_audio = (Button) this.findViewById(R.id.Button02);
		btn_start_audio.setOnClickListener(new ClickEvent());
		btn_stop_audio.setOnClickListener(new ClickEvent());
		skb_audio = (SeekBar) this.findViewById(R.id.SeekBar01);
		skb_audio.setOnSeekBarChangeListener(new SeekBarChangeEvent());

	}

	/*
	 * 按键事件处理
	 */
	class ClickEvent implements View.OnClickListener {
		public void onClick(View v) {
			String path = getIntent().getStringExtra(EXTRA_PATH);
			switch (v.getId()) {
			
			
			case R.id.Button01:
				if (m != null && !ifPlay) {
					btn_start_audio.setText("暂停");

					// 设置audio或video标记，以便控制各自的进度条变化

					if (!ifFirst) {

						// 恢复到未初始化的状态
						m.reset();

						// 有两种方式获取资源文件：从工程的resource目录，或是指定路径；鉴于文件比较大，所以本示例均是从SD卡获取
						// m=MediaPlayer.create(TestPlayer.this,
						// R.raw.big);//读取音频
						// m = MediaPlayer.create(AtySoundViewer.this,
						// Uri.parse("file://" + mp3Path));
						m = new MediaPlayer();
						try {
							m.setDataSource(path);
						} catch (IllegalArgumentException | SecurityException
								| IllegalStateException | IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						try {
							m.prepare();
						} catch (IllegalStateException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// 设置SeekBar的长度
						skb_audio.setMax(m.getDuration());

						// 每次播放都将进度条重置为0
						skb_audio.setProgress(0);

						ifFirst = true;
					}
					m.start(); // 播放

					// 启动一个新线程用于更新音频的进度条
					aseekth as = new aseekth();
					as.start();

					ifPlay = true;

				} else if (ifPlay) {
					btn_start_audio.setText("继续");
					m.pause();
					ifPlay = false;

			}/*
				break;
			case R.id.Button02:
			
				 if (ifPlay) {  
                     m.seekTo(0);  
                 } else {  
                     m.reset();  
                     try {  
                         m.setDataSource(path);  
                         m.prepare();// 准备  
                         m.start();// 开始  
                     } catch (IllegalArgumentException e) {  
                         e.printStackTrace();  
                     } catch (IllegalStateException e) {  
                         e.printStackTrace();  
                     } catch (IOException e) {  
                         e.printStackTrace();  
                     }  
                 }  
                 break;  
			
			default:
				break;*/

		}
		}
	}

	/*
	 * SeekBar进度改变事件
	 */
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			m.seekTo(seekBar.getProgress());
		}
	}

	/*
	 * 音频进度条线程处理
	 */
	private class aseekth extends Thread {
		public void run() {
			try {
				while (ifPlay) {
					if (skb_audio.getProgress() < m.getCurrentPosition()) {
						skb_audio.setProgress(m.getCurrentPosition());
						sleep(10);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	// 来电处理
	protected void onDestroy() {
		if (m != null) {
			if (m.isPlaying()) {
				m.stop();
			}
			m.release();
		}
		super.onDestroy();
	}

	protected void onPause() {
		if (m != null) {
			if (m.isPlaying()) {
				m.pause();
			}
		}
		super.onPause();
	}
}
