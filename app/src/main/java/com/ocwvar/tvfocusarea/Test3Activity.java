package com.ocwvar.tvfocusarea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public final class Test3Activity extends AppCompatActivity {

	@Override
	protected void onCreate( @Nullable Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_focus_3 );

		findViewById( R.id.jump4 ).setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startActivity( new Intent( Test3Activity.this, Test4Activity.class ) );
			}
		} );
	}
}
