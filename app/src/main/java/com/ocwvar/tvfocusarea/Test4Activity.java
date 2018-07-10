package com.ocwvar.tvfocusarea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yan.tvprojectutils.FocusRecyclerView;

public final class Test4Activity extends BaseActivity {

	@Override
	protected void onCreate( @Nullable Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_focus_4 );

		FocusRecyclerView recyclerView = findViewById( R.id.recycleView );
		recyclerView.setLayoutManager( new LinearLayoutManager( Test4Activity.this, LinearLayoutManager.HORIZONTAL, false ) );
		recyclerView.setAdapter( getLHAdapter() );
		recyclerView.setHasFixedSize( true );

		findViewById( R.id.jump5 ).setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startActivity( new Intent( Test4Activity.this, Test5Activity.class ) );
			}
		} );
	}

}