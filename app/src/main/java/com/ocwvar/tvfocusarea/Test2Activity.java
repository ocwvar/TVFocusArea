package com.ocwvar.tvfocusarea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public final class Test2Activity extends AppCompatActivity {

	@Override
	protected void onCreate( @Nullable Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_focus_2 );

		RecyclerView recyclerView = findViewById( R.id.recycleView2 );
		recyclerView.setLayoutManager( new LinearLayoutManager( Test2Activity.this, LinearLayoutManager.VERTICAL, false ) );
		recyclerView.setAdapter( new LVTestAdapter() );
		recyclerView.setHasFixedSize( true );

		RecyclerView recyclerView2 = findViewById( R.id.recycleView );
		recyclerView2.setLayoutManager( new GridLayoutManager( Test2Activity.this, 2, GridLayoutManager.HORIZONTAL, false ) );
		recyclerView2.setAdapter( new LHTestAdapter() );
		recyclerView2.setHasFixedSize( true );

		findViewById( R.id.jump3 ).setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				startActivity( new Intent( Test2Activity.this, Test3Activity.class ) );
			}
		} );
	}

	//横向线性列表适配器
	private class LHTestAdapter extends RecyclerView.Adapter {

		@NonNull
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
			RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams( 300, RecyclerView.LayoutParams.MATCH_PARENT );
			layoutParams.leftMargin = 20;

			TextView textView = new TextView( parent.getContext() );
			textView.setBackground( getResources().getDrawable( R.drawable.selector_background ) );
			textView.setGravity( Gravity.CENTER );
			textView.setTextSize( 25.0f );
			textView.setClickable( true );
			textView.setFocusable( true );
			textView.setFocusableInTouchMode( true );
			textView.setLayoutParams( layoutParams );

			return new ViewHolder( textView );
		}

		@Override
		public void onBindViewHolder( @NonNull RecyclerView.ViewHolder holder, int position ) {
			ViewHolder viewHolder = (ViewHolder) holder;
			viewHolder.item.setText( String.valueOf( position + 1 ) );
		}

		@Override
		public int getItemCount() {
			return 50;
		}

		private class ViewHolder extends RecyclerView.ViewHolder {

			TextView item;

			ViewHolder( View itemView ) {
				super( itemView );
				item = (TextView) itemView;
			}
		}

	}

	//竖向线性列表适配器
	private class LVTestAdapter extends RecyclerView.Adapter {

		@NonNull
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
			RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams( RecyclerView.LayoutParams.MATCH_PARENT, 300 );
			layoutParams.topMargin = 20;

			TextView textView = new TextView( parent.getContext() );
			textView.setBackground( getResources().getDrawable( R.drawable.selector_background ) );
			textView.setGravity( Gravity.CENTER );
			textView.setTextSize( 25.0f );
			textView.setClickable( true );
			textView.setFocusable( true );
			textView.setFocusableInTouchMode( true );
			textView.setLayoutParams( layoutParams );

			return new ViewHolder( textView );
		}

		@Override
		public void onBindViewHolder( @NonNull RecyclerView.ViewHolder holder, int position ) {
			ViewHolder viewHolder = (ViewHolder) holder;
			viewHolder.item.setText( String.valueOf( position + 1 ) );
		}

		@Override
		public int getItemCount() {
			return 50;
		}

		private class ViewHolder extends RecyclerView.ViewHolder {

			TextView item;

			ViewHolder( View itemView ) {
				super( itemView );
				item = (TextView) itemView;
			}
		}

	}

}
