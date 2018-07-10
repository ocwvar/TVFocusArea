package com.ocwvar.tvfocusarea;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public final class Test5Activity extends BaseActivity {

	@Override
	protected void onCreate( @Nullable Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_focus_5 );

		RecyclerView recyclerView = findViewById( R.id.recycleView );
		recyclerView.setLayoutManager( new LinearLayoutManager( Test5Activity.this, LinearLayoutManager.VERTICAL, false ) );
		recyclerView.setAdapter( getLVAdapter() );
		recyclerView.setHasFixedSize( true );
	}

}