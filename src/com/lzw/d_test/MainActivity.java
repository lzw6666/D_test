package com.lzw.d_test;

import com.lzw.d_test.fragment.CollectFragment;
import com.lzw.d_test.fragment.HotFragment;
import com.lzw.d_test.fragment.RecreationFragment;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity implements OnCheckedChangeListener{

	private RadioGroup rg;
	
	private FragmentManager manager ;
	
	private FragmentTransaction transaction;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        rg = (RadioGroup) findViewById(R.id.rg);
        rg.check(R.id.rb1);
        manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.fl_id, new HotFragment()).commit();
        rg.setOnCheckedChangeListener(this);
    }

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
		transaction = manager.beginTransaction();
		
		switch (checkedId) {
		case R.id.rb1:
			transaction.replace(R.id.fl_id, new HotFragment());
			break;
		case R.id.rb2:
			transaction.replace(R.id.fl_id, new RecreationFragment());
			break;
		case R.id.rb3:
			transaction.replace(R.id.fl_id, new CollectFragment());
			break;

		default:
			break;
		}
		transaction.commit();
	}

    
  
}
