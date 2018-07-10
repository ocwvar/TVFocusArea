package com.ocwvar.tvfocusarea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public final class Test2Activity extends BaseActivity {

	@Override
	protected void onCreate( @Nullable Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_focus_2 );

		RecyclerView recyclerView = findViewById( R.id.recycleView2 );
		recyclerView.setLayoutManager( new LinearLayoutManager( Test2Activity.this, LinearLayoutManager.VERTICAL, false ) );
		recyclerView.setAdapter( getLVAdapter() );
		recyclerView.setHasFixedSize( true );

		RecyclerView recyclerView2 = findViewById( R.id.recycleView );
		recyclerView2.setLayoutManager( new GridLayoutManager( Test2Activity.this, 2, GridLayoutManager.HORIZONTAL, false ) );
		recyclerView2.setAdapter( getLHAdapter() );
		recyclerView2.setHasFixedSize( true );

		findViewById( R.id.jump3 ).setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startActivity( new Intent( Test2Activity.this, Test3Activity.class ) );
			}
		} );
	}

}
