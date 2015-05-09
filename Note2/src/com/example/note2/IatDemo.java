package com.example.note2;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.note2.util.JsonParser;
import com.example.note2.util.Pcm2Wav;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

public class IatDemo extends Activity implements OnClickListener{
	private static String TAG = "IatDemo";
	// ������д����
	private SpeechRecognizer mIat;
	// ������дUI
	private RecognizerDialog iatDialog;
	// ��д�������
	private EditText mResultText;

	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	// ��������
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	//�ļ�·��
	File pcmPath;
	File wavPath;
	private String currentPath = null;
	

	
	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.iat_demo);
		
		SpeechUtility.createUtility(this, SpeechConstant.APPID +"=548d4e52");
		
		initLayout();
		
		

		// ��ʼ��ʶ�����
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		// ��ʼ����дDialog,���ֻʹ����UI��д����,���贴��SpeechRecognizer
		iatDialog = new RecognizerDialog(this,mInitListener);
		
		mSharedPreferences = getSharedPreferences("com.example.note2", Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);	
		mResultText = ((EditText)findViewById(R.id.iat_text));
		
	}

	/**
	 * ��ʼ��Layout��
	 */
	private void initLayout(){
		findViewById(R.id.iat_Recognize).setOnClickListener(this);
		//findViewById(R.id.btnPcm2Wav).setOnClickListener(this);
	}

	int ret = 0;// �������÷���ֵ
	@Override
	public void onClick(View view) {	
		switch (view.getId()) {
		
			// ��ʼ��д
		case R.id.iat_Recognize:
			//mResultText.setText(null);// �����ʾ����
			// ���ò���
			setParam();
			boolean isShowDialog = mSharedPreferences.getBoolean("iat_show", true);
			if (isShowDialog) {
				// ��ʾ��д�Ի���
				iatDialog.setListener(recognizerDialogListener);
				iatDialog.show();
				showTip("�뿪ʼ˵��");
			} else {
				// ����ʾ��д�Ի���
				ret = mIat.startListening(recognizerListener);
				if(ret != ErrorCode.SUCCESS){
					showTip("��дʧ��,�����룺" + ret);
				}else {
					showTip("�뿪ʼ˵��");
				}
			}
			break;
		/*case R.id.btnPcm2Wav:
			pcm2wav();
			
	
			break;
			*/
		
		default:
			break;
		}
	}


	private void pcm2wav() {
		// TODO Auto-generated method stub
		   Pcm2Wav tool = new Pcm2Wav();
	        try
	        {
	           //tool.convertAudioFiles(pcmFilePath, wavFilePath);
	            tool.convertAudioFiles(currentPath.toString(), wavPath.toString());
	        }
	        catch (Exception e)
	        {
	            Log.e(TAG, "pcm failed to convert into wav File:" + e.getMessage());
	        }
		
	}

	/**
	 * ��ʼ����������
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		showTip("��ʼ��ʧ��,�����룺"+code);
        	}
		}
	};


	
	/**
	 * ��д��������
	 */
	private RecognizerListener recognizerListener=new RecognizerListener(){

		@Override
		public void onBeginOfSpeech() {	
			showTip("��ʼ˵��");
		}


		@Override
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			showTip("����˵��");
		}



		@Override
		public void onResult(RecognizerResult results, boolean isLast) {		
			Log.d(TAG, results.getResultString());
			String text = JsonParser.parseIatResult(results.getResultString());
			mResultText.append(text);
			mResultText.setSelection(mResultText.length());
			//showTip(text);
			if(isLast) {
				//TODO ���Ľ��
			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			showTip("��ǰ����˵����������С��" + volume);
		}


		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	/**
	 * ��дUI������
	 */
	private RecognizerDialogListener recognizerDialogListener=new RecognizerDialogListener(){
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			mResultText.append(text);
			mResultText.setSelection(mResultText.length());
			//showTip(text);
		}

		/**
		 * ʶ��ص�����.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};

	private void showTip(final String str)
	{
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * ��������
	 * @param param
	 * @return 
	 */
	public void setParam(){
		// ��ղ���
		mIat.setParameter(SpeechConstant.PARAMS, null);
		
		// ������д����
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// ���÷��ؽ����ʽ
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
		
		String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
		if (lag.equals("en_us")) {
			// ��������
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		}else {
			// ��������
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// ������������
			mIat.setParameter(SpeechConstant.ACCENT,lag);
		}
		// ��������ǰ�˵�
		mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "1000"));
		// ����������˵�
		mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		// ���ñ�����
		mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
		// ������Ƶ����·��
		
		

		pcmPath = new File(getMediaDir(), System.currentTimeMillis() + ".pcm");
		wavPath = new File(getMediaDir(), System.currentTimeMillis() + ".wav");
		

		try {
			pcmPath.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		currentPath = pcmPath.getAbsolutePath();
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, currentPath);
	}
	
	public File getMediaDir() {
		File dir = new File(Environment.getExternalStorageDirectory(),
				"NotesMedia");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// �˳�ʱ�ͷ�����
		mIat.cancel();
		mIat.destroy();
	}
	
	@Override
	protected void onResume() {
		//�ƶ�����ͳ�Ʒ���
		FlowerCollector.onResume(this);
		FlowerCollector.onPageStart("IatDemo");
		super.onResume();
	}
	@Override
	protected void onPause() {
		//�ƶ�����ͳ�Ʒ���
		FlowerCollector.onPageEnd("IatDemo");
		FlowerCollector.onPause(this);
		super.onPause();
	}



	public void onBackPressed() { 
        new AlertDialog.Builder(this).setTitle("�Ƿ񱣴浱ǰ��Ƶ��") 
            .setIcon(android.R.drawable.ic_dialog_info) 
            .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
         
                @Override 
                public void onClick(DialogInterface dialog, int which) { 
                // �����ȷ�ϡ���Ĳ��� 
                   Toast.makeText(IatDemo.this, "��Ƶ����ɹ�", Toast.LENGTH_LONG).show();
                   pcm2wav();
                   //�ѵ�ַ���͵�ActivityForResult
                   Intent intent = new Intent();
                   intent.putExtra("wavPath", wavPath.toString());
                   setResult(3, intent);
                   finish();
         
                } 
            }) 
            .setNegativeButton("����", new DialogInterface.OnClickListener() { 
         
                @Override 
                public void onClick(DialogInterface dialog, int which) { 
                	finish();
                // ��������ء���Ĳ���,���ﲻ����û���κβ��� 
                } 
            }).show(); 
           }  
}
