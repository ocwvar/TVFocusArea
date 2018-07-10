package com.ocwvar.tvfocusarea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public final class Test1Activity extends BaseActivity {

	@Override
	protected void onCreate( @Nullable Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_focus_1 );

		RecyclerView recyclerView = findViewById( R.id.recycleViewV );
		recyclerView.setLayoutManager( new LinearLayoutManager( Test1Activity.this, LinearLayoutManager.VERTICAL, false ) );
		recyclerView.setAdapter( getLVAdapter() );
		recyclerView.setHasFixedSize( true );

		RecyclerView recyclerView2 = findViewById( R.id.recycleViewG );
		recyclerView2.setLayoutManager( new GridLayoutManager( Test1Activity.this, 2, GridLayoutManager.HORIZONTAL, false ) );
		recyclerView2.setAdapter( getGAdapter() );
		recyclerView2.setHasFixedSize( true );

		RecyclerView recyclerView3 = findViewById( R.id.recycleViewH );
		recyclerView3.setLayoutManager( new LinearLayoutManager( Test1Activity.this, LinearLayoutManager.HORIZONTAL, false ) );
		recyclerView3.setAdapter( getLHAdapter() );
		recyclerView3.setHasFixedSize( true );

		findViewById( R.id.jump2 ).setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startActivity( new Intent( Test1Activity.this, Test2Activity.class ) );
			}
		} );
	}

}
